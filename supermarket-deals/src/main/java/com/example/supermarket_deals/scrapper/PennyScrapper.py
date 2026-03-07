import time
import requests
from datetime import date, timedelta
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
from bs4 import BeautifulSoup as Soup


class PennyScrapper:
    def __init__(self, url: str):
        self.url = url
        self.scrapped_deals = []

    def scroll_page(self, driver):
        for _ in range(10):
            driver.execute_script("window.scrollBy(0, 4000)")
            time.sleep(1)

    def scrap_deals(self):
        driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()))
        driver.get(self.url)
        time.sleep(5)
        self.scroll_page(driver)
        html = driver.page_source
        soup = Soup(html, "html.parser")

        for section in soup.select("div.js-category-section"):
            section_id = section.get("id", "")
            dates = self.extract_valid_date(section_id)
            if not dates:
                continue
            valid_from, valid_to = dates

            products = section.select("li.tile-list__item, li.tile-list__item--highlight")
            section_deals = self.extract_deals(products, valid_from, valid_to)
            self.scrapped_deals.extend(section_deals)

        driver.quit()

    def extract_valid_date(self, section_id: str):
        day_map = {
            "ab-montag": (0, 6),
            "ab-donnerstag": (3, 6),
            "ab-freitag": (4, 6),
        }
        today = date.today()
        monday = today - timedelta(days=today.weekday())

        if section_id in day_map:
            start_day, end_day = day_map[section_id]
            valid_from = monday + timedelta(days=start_day)
            valid_to = monday + timedelta(days=end_day)
        else:
            return None

        return valid_from, valid_to

    def extract_deals(self, items, valid_from, valid_to):
        deals = []
        for item in items:
            name = item.select_one(".offer-tile__headline")
            price = item.select_one("span.bubble__price-value")
    
            if not name or not price:
                continue
            name = name.text.strip()
            price = price.text.strip()
            infos = item.select_one("offer-tile__unit-price")
            infos = infos.text.strip() if infos else ""

            deal = {
                "productName": name,
                "brand": "",
                "price": price,
                "infos": infos,
                "validFrom": valid_from.isoformat(),
                "validTo": valid_to.isoformat()
            }
            if deal not in self.scrapped_deals:
                deals.append(deal)

        return deals

    def post_scrapped_deals(self):
        requests.post("http://localhost:8080/deal/addMany", json=self.scrapped_deals)
