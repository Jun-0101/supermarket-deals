import time, re
import requests
from datetime import datetime, date
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
from bs4 import BeautifulSoup as Soup


class ReweScrapper:
    def __init__(self, url: str):
        self.url = url
        self.current_year = datetime.now().year
        self.scrapped_deals = []

    def extract_valid_date(self, soup: str):
        date_heading = soup.select_one(".sos-week-tabs__tab-subtitle")
        date_str = date_heading.text.strip()
        year = datetime.now().year
        matches = re.findall(r'(\d{1,2})\.(\d{1,2})\.', date_str)

        if len(matches) != 2:
            return None, None
        
        day1, month1 = map(int, matches[0])
        day2, month2 = map(int, matches[1])

        self.valid_from = date(year, month1, day1)
        self.valid_to = date(year, month2, day2)

    def scroll_page(self, driver):
        for _ in range(10):
            driver.execute_script("window.scrollBy(0, 4000)")
            time.sleep(1)
            
    def scrap_deals(self, ):
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
            "price": price,
            "infos": infos,
            "validFrom": self.valid_from.isoformat(),
            "validTo": self.valid_to.isoformat()
        }
        
        return deal

    def post_scrapped_deals(self):
        requests.post("http://localhost:8080/deal/addMany", json=self.scrapped_deals)

