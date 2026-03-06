import time, re
import requests
from datetime import datetime, date, timedelta
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
from bs4 import BeautifulSoup as Soup


class AldiNordScrapper:
    def __init__(self):
        self.url = "https://www.aldi-nord.de/angebote.html"
        self.scrapped_deals = []
        self.current_year = datetime.now().year

    def extract_valid_date(self, date_text: str):
        match = re.search(r'(\d{1,2})\.(\d{1,2})\.', date_text)

        if not match:
            return
        
        day = int(match.group(1))
        month = int(match.group(2))
        valid_from = date(self.current_year, month, day)

        days_until_weekend = 6 - valid_from.weekday()
        valid_to = valid_from + timedelta.days(days_until_weekend)

        return valid_from, valid_to


    def deal_scrapper(self, ):
        driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()))
        driver.get(self.url)
        time.sleep(5)

        html = driver.page_source
        soup = Soup(html, "html.parser")

        for section in soup.select("h2.heading--h2"):
            date = section.text.strip()
            valid_from, valid_to = self.extract_valid_date(date)
            products = []
            next_node = section.find_next()

            while next_node and next_node.name != "h2":
                if "product-tile" in next_node.get("class", []):
                    products.append(next_node)
    
                next_node = next_node.find_next()




    def form_deals(self, products, valid_from, valid_to):
        for p in products:
            brand = p.select_one(".product-tile__content__upper__brand-name")
            name = p.select_one(".product-tile__content__upper__product-name")
            price = p.select_one(".tag__label--price")
            unit = p.select_one(".tag__marker--salesunit")
            base_price = p.select_one(".tag__marker--base-price")
            deal = {
                "productName": name,
                "brand": brand,
                "price": price,
                "infos": ", ".join([unit, base_price]),
                "validFrom": valid_from

            }

    def post_scrapped_deals(self):
        pass