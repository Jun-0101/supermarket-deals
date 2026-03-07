import re
import time
import requests
from datetime import date, datetime, timedelta
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from webdriver_manager.chrome import ChromeDriverManager
from bs4 import BeautifulSoup as Soup


class KauflandScrapper:
    def __init__(self, url: str):
        self.url = url
        self.scrapped_deals = []

    def load_more_deals(self, driver):
        while True:
            buttons = driver.find_elements(By.CSS_SELECTOR, "button.k-icon-button")

            if not buttons:
                break

            for btn in buttons:
                driver.execute_script("arguments[0].click();", btn)
                time.sleep(2)

            driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")

    def scrap_deals(self):
        driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()))
        driver.get(self.url)

        #self.load_more_deals(driver)

        html = driver.page_source
        soup = Soup(html, "html.parser")

        for section in soup.select(".k-product-section"):
            headline = section.select_one(".k-product-section__subheadline")
            if not headline:
                continue
            header = headline.text.strip()

            if "vom" not in header or "bis" not in header:
                continue
            
            dates = self.extract_valid_date(header)
            if not dates:
                continue
            valid_from, valid_to = dates

            products = section.select("div.k-grid__item")
            section_deals = self.extract_deals(products, valid_from, valid_to)
            self.scrapped_deals.extend(section_deals)

        driver.quit()

    def extract_valid_date(self, date_str: str):
        year = datetime.now().year
        matches = re.findall(r'(\d{1,2})\.(\d{1,2})\.', date_str)

        if len(matches) != 2:
            return None, None
        
        day1, month1 = map(int, matches[0])
        day2, month2 = map(int, matches[1])

        valid_from = date(year, month1, day1)
        valid_to = date(year, month2, day2)

        return valid_from, valid_to

    def extract_deals(self, items, valid_from, valid_to):
        deals = []
        for item in items:
            if "display:none" in str(item):
                continue
            title = item.select_one("div.k-product-tile__title")
            price = item.select_one("div.k-price-tag__price")
    
            if not title or not price:
                continue
            
            title = title.text.strip()
            price = price.text.strip()

            subtitle = item.select_one("div.k-product-tile__subtitle")
            subtitle = subtitle.text.strip() if subtitle else ""
            unit = item.select_one("div.k-product-tile__unit-price")
            unit = unit.text.replace("\xa0"," ").strip() if unit else ""
            base_price = item.select_one("div.k-product-tile__base-price")
            base_price = base_price.text.strip() if base_price else ""

            deal = {
                "productName": title if subtitle == "" else subtitle,
                "brand": title if subtitle != "" else "",
                "price": price,
                "infos": " ".join([unit, base_price]),
                "validFrom": valid_from.isoformat(),
                "validTo": valid_to.isoformat()
            }

            if deal not in self.scrapped_deals:
                deals.append(deal)

        return deals

    def post_scrapped_deals(self):
        requests.post("http://localhost:8080/deal/addMany", json=self.scrapped_deals)
