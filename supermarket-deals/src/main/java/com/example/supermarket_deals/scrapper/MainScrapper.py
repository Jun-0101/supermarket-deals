import requests
from PennyScrapper import PennyScrapper
from KauflandScrapper import KauflandScrapper
from ReweScrapper import ReweScrapper
from AldiNordScrapper import AldiNordScrapper
from AldiSuedScrapper import AldiSuedScrapper

def call_scrapper():
    """
    Run all scrapers and collect deals.

    Returns:
        list: All scraped deals.
    """
    all_deals = []
    scrapper_map = {
        "penny": PennyScrapper("https://www.penny.de/angebote.html"),
        "kaufland": KauflandScrapper("https://filiale.kaufland.de/angebote/uebersicht.html"),
        "rewe": ReweScrapper("https://www.rewe.de/angebote.html"),
        "aldi-nord": AldiNordScrapper("https://www.aldi-nord.de/angebote.html"),
        "aldi-sued": AldiSuedScrapper("https://www.aldi-sued.de/produkte/wochenangebote?page=")
    }

    for scrapper in scrapper_map.values():
        scrapper.scrap_deals()
        all_deals.extend(scrapper.scrapped_deals)

    return all_deals

def post_scrapped_deals(scrapped_deals):
    """
    Post deals to the server.

    Args:
        scrapped_deals (list): List of deal dictionaries.
    """
    try:
        requests.post("http://localhost:8080/deals/bulk", json=scrapped_deals)
        print("Post successfully")

    except requests.exceptions.ConnectionError:
        print("Server not running")

    except requests.exceptions.Timeout:
        print("Request timed out")

    except requests.exceptions.HTTPError as e:
        print(f"HTTP error: {e.response.status_code}", e.response.text)


if __name__ == '__main__':
    all_deals = call_scrapper()
    post_scrapped_deals(all_deals)
