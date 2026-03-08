import time, re
from datetime import datetime, date
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
from bs4 import BeautifulSoup as Soup


class AldiSuedScrapper:
    def __init__(self, url: str):
        self.url = url
        self.scrapped_deals = []
        self.valid_from = None
        self.valid_to = None

    def extract_valid_date(self, soup: str):
        """
        Extract validity dates from the first h2 header
        """
                
        date_heading = soup.select_one("h2.product-category-teaser-list__title")
        date_str = date_heading.text.strip()
        year = datetime.now().year
        matches = re.findall(r'(\d{1,2})\.(\d{1,2})\.', date_str)

        if len(matches) != 2:
            return None, None
        
        day1, month1 = map(int, matches[0])
        day2, month2 = map(int, matches[1])

        self.valid_from = date(year, month1, day1)
        self.valid_to = date(year, month2, day2)

    def scrap_deals(self):
        driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()))
        for page in range(1, 5):
            url = self.url + str(page)
            driver.get(url)
            time.sleep(5)
            html = driver.page_source
            page_soup = Soup(html, "html.parser")

            if not self.valid_from and not self.valid_to:
                self.extract_valid_date(page_soup)

            products = page_soup.select("div.product-grid__item")
            section_deals = self.extract_deals(products)
            self.scrapped_deals.extend(section_deals)

        driver.quit()

    def extract_deals(self, products):
        deals = []

        for p in products:
            name = p.select_one(".product-tile__name")
            price = p.select_one(".base-price__discounted")
    
            if not name or not price:
                continue
            name = name.text.strip()
            price = price.text.replace("\xa0€","").replace(",", ".").strip()
            brand = p.select_one(".product-tile__brandname")
            brand = brand.text.strip() if brand else ""
            unit = p.select_one(".product-tile__unit-of-measurement")
            unit = unit.text.replace("\xa0"," ").strip() if unit else ""
            base_price = p.select_one(".product-tile__comparison-price")
            base_price = base_price.text.strip() if base_price else ""
    
            deal = {
                "productName": name,
                "brand": brand,
                "infos": ", ".join([unit, base_price]),
                "price": price,
                "supermarketName": "Aldi Sued",
                "validFrom": self.valid_from.isoformat(),
                "validTo": self.valid_to.isoformat()
            }

            if deal not in self.scrapped_deals:
                deals.append(deal)
        
        return deals