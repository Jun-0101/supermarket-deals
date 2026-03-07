import time, re
import requests
from datetime import datetime, date, timedelta
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
from bs4 import BeautifulSoup as Soup


class AldiNordScrapper:
    def __init__(self, url: str):
        self.url = url
        self.scrapped_deals = []

    def scrap_deals(self):
        driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()))
        driver.get(self.url)
        time.sleep(5)
        html = driver.page_source
        soup = Soup(html, "html.parser")

        for section in soup.select("div.offers-tabs-filter-grid-heading"):
            heading = section.select_one("h2.heading--h2")
            date_text = heading.text.strip()
            dates = self.extract_valid_date(date_text)
            if not dates:
                continue
            valid_from, valid_to = dates

            products = section.select(".product-tile")
            section_deals = self.extract_deals(products, valid_from, valid_to)
            self.scrapped_deals.extend(section_deals)

        driver.quit()

    def extract_valid_date(self, date_text: str):
        current_year = datetime.now().year
        match = re.search(r'(\d{1,2})\.(\d{1,2})\.', date_text)

        if not match:
            return
        
        day = int(match.group(1))
        month = int(match.group(2))
        valid_from = date(current_year, month, day)

        days_until_weekend = 6 - valid_from.weekday()
        valid_to = valid_from + timedelta(days=days_until_weekend)

        return valid_from, valid_to

    def extract_deals(self, products, valid_from, valid_to):
        deals = []

        for p in products:
            name = p.select_one(".product-tile__content__upper__product-name")
            price = p.select_one(".tag__label--price")

            if not name or not price:
                continue
            name = name.text.strip()
            price = price.text.replace("*","").strip()
            brand = p.select_one(".product-tile__content__upper__brand-name")
            brand = brand.text.strip() if brand else ""
            unit = p.select_one(".tag__marker--salesunit")
            unit = unit.text.strip() if unit else ""
            base_price = p.select_one(".tag__marker--base-price")
            base_price = base_price.text.strip() if base_price else ""
    
            deal = {
                "productName": name,
                "brand": brand,
                "infos": ", ".join([unit, base_price]),
                "price": price,
                "supermarketName": "aldi nord",
                "validFrom": valid_from.isoformat(),
                "validTo": valid_to.isoformat()
            }
    
            if deal not in self.scrapped_deals:
                deals.append(deal)
        
        return deals
