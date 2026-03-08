import time, re
from datetime import datetime, date
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
from bs4 import BeautifulSoup as Soup


class ReweScrapper:
    """
    A web scraper class for extracting deals from the Rewe supermarket website.

    This class handles the scraping of promotional deals from Rewe's online platform,
    including product information, pricing, and deal validity periods.
    """

    def __init__(self, url: str):
        """
        Initialize the ReweScrapper with the target URL.

        Args:
            url (str): The URL of the Rewe deals page to scrape.
        """
        self.url = url
        self.scrapped_deals = []

    def extract_valid_date(self, soup: str):
        """
        Extract the validity dates from the page's date heading.

        Args:
            soup (BeautifulSoup): The parsed HTML content of the page.
        """
        date_heading = soup.select_one("button.sos-week-tabs__tab--selected")
        date_title = date_heading.select_one(".sos-week-tabs__tab-subtitle")
        date_str = date_title.text.strip()
        year = datetime.now().year
        matches = re.findall(r'(\d{1,2})\.(\d{1,2})\.', date_str)

        if len(matches) != 2:
            return None, None
        
        day1, month1 = map(int, matches[0])
        day2, month2 = map(int, matches[1])

        self.valid_from = date(year, month1, day1)
        self.valid_to = date(year, month2, day2)

    def scroll_page(self, driver):
        """
        Scroll the webpage to load additional content.

        Args:
            driver (webdriver.Chrome): The Selenium WebDriver instance.
        """
        for _ in range(10):
            driver.execute_script("window.scrollBy(0, 4000)")
            time.sleep(1)
            
    def scrap_deals(self, ):
        """
        Scrape all deals from the Rewe website.
        """
        driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()))
        driver.get(self.url)
        time.sleep(5)
        self.scroll_page(driver)

        html = driver.page_source
        soup = Soup(html, "html.parser")
        self.extract_valid_date(soup)

        for item in soup.select("[data-testid='sos-offer-span']"):
            deal = self.extract_deal(item)
            if not deal or deal in self.scrapped_deals:
                continue
            self.scrapped_deals.append(deal)

        driver.quit()

    def extract_deal(self, item):
        """
        Extract deal information from a single deal HTML element.

        Parses the product name, price, and additional information from the deal item.
        """
        name = item.select_one("a.cor-offer-information__title-link")
        price = item.select_one(".cor-offer-price__tag-price")

        if not name or not price:
           return None
    
        name = name.text.strip()
        price = price.text.replace("€","").replace(",", ".") .strip()
        infos = item.select(".cor-offer-information__additional")
        infos = "".join([info.text.strip() for info in infos])
        
        deal = {
            "productName": name,
            "brand": "",
            "infos": infos,
            "price": price,
            "supermarketName": "Rewe",
            "validFrom": self.valid_from.isoformat(),
            "validTo": self.valid_to.isoformat()
        }
        
        return deal
