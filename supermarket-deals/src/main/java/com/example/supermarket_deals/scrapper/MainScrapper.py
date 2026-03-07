
import PennyScrapper, KauflandScrapper, ReweScrapper, AldiNordScapper, AldiSuedScrapper

def call_scrapper():
    scrapper_map = {
        "penny": PennyScrapper("https://www.penny.de/angebote.html"),
        "kaufland": KauflandScrapper("https://filiale.kaufland.de/angebote/uebersicht.html"),
        "rewe": ReweScrapper("https://www.rewe.de/angebote.html"),
        "aldi-nord": AldiNordScapper("https://www.aldi-nord.de/angebote.html"),
        "aldi-sued": AldiSuedScrapper("https://www.aldi-sued.de/produkte/wochenangebote?page=")
    }

    for scrapper in scrapper_map.values():
        scrapper.scrap_deals()
        scrapper.post_scrapped_deals()


if __name__ == '__main__':
    call_scrapper()