from django.core.management.base import BaseCommand
from mygutenberg.models import BookMetadata
from mygutenberg.views import get_data

class Command(BaseCommand):
    help = 'Crawl book data from the source and save it to the database'

    def add_arguments(self, parser):
        parser.add_argument('start_id', type=int, help='Start ID for crawling')
        parser.add_argument('end_id', type=int, help='End ID for crawling')

    def handle(self, *args, **kwargs):
        start_id = kwargs['start_id']
        end_id = kwargs['end_id']

        for book_id in range(start_id, end_id + 1):
            metadata = get_data(book_id)
            if metadata:
                self.stdout.write(f"Book {book_id} crawled and saved successfully.")
            else:
                self.stdout.write(f"Failed to crawl book {book_id}.")
