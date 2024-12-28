import requests
from rest_framework.views import APIView
from rest_framework.response import Response
from bs4 import BeautifulSoup

from mygutenberg.config import baseUrl
from mygutenberg.models import BookMetadata
from mygutenberg.serializer import BookMetadataSerializer

def get_data(bookID):
    response = requests.get(baseUrl + str(bookID))
    html_content = response.text
    soup = BeautifulSoup(html_content, 'html.parser')

    table = soup.find('table', class_='bibrec')
    metadata = {}

    if table:
        rows = table.find_all('tr')
        for row in rows:
            key = row.find('th')
            value = row.find('td')
            if key and value:
                metadata[key.text.strip()] = value.text.strip()

    # 保存到数据库
    if metadata:
        book, created = BookMetadata.objects.get_or_create(
            book_id=bookID,
            defaults={
                'title': metadata.get('Title', None),
                'author': metadata.get('Author', None),
                'language': metadata.get('Language', None),
                'publication_date': metadata.get('Release Date', None)
            }
        )
        if created:
            print(f"Book {bookID} saved successfully.")
        else:
            print(f"Book {bookID} already exists.")

    return metadata

class RedirectionBookList(APIView):
    def get(self, request, format=None):
        books = BookMetadata.objects.all()
        serializer = BookMetadataSerializer(books, many=True)
        return Response(serializer.data)

class RedirectionBookDetail(APIView):
    def get(self, request, pk, format=None):
        try:
            book = BookMetadata.objects.get(book_id=pk)
            serializer = BookMetadataSerializer(book)
            return Response(serializer.data)
        except BookMetadata.DoesNotExist:
            return Response({'error': 'Book not found'}, status=404)

class RedirectionEnglishBookList(APIView):
    def get(self, request, format=None):
        books = BookMetadata.objects.filter(language='English')
        serializer = BookMetadataSerializer(books, many=True)
        return Response(serializer.data)

class RedirectionFrenchBookList(APIView):
    def get(self, request, format=None):
        books = BookMetadata.objects.filter(language='French')
        serializer = BookMetadataSerializer(books, many=True)
        return Response(serializer.data)