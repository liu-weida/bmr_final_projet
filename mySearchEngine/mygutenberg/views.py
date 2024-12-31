import requests
from django.contrib.auth.models import User
from django.contrib.auth import authenticate, login
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.permissions import AllowAny, IsAuthenticated
from bs4 import BeautifulSoup

from mygutenberg.config import baseUrl
from mygutenberg.models import BookMetadata, UserQuery
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

class RegisterView(APIView):
    permission_classes = [AllowAny]

    def post(self, request, *args, **kwargs):
        username = request.data.get('username')
        password = request.data.get('password')
        email = request.data.get('email')

        if User.objects.filter(username=username).exists():
            return Response({'error': 'Username already exists'}, status=400)

        user = User.objects.create_user(username=username, password=password, email=email)
        return Response({'message': 'User registered successfully'})

class LoginView(APIView):
    permission_classes = [AllowAny]

    def post(self, request, *args, **kwargs):
        username = request.data.get('username')
        password = request.data.get('password')

        user = authenticate(request, username=username, password=password)
        if user is not None:
            login(request, user)
            return Response({'message': 'Login successful'})
        return Response({'error': 'Invalid credentials'}, status=401)

# class RedirectionBookList(APIView):
#     def get(self, request, format=None):
#         books = BookMetadata.objects.all()
#         serializer = BookMetadataSerializer(books, many=True)
#         return Response(serializer.data)
class RedirectionBookList(APIView):
    permission_classes = [IsAuthenticated]

    def get(self, request, format=None):
        user = request.user  # 当前登录用户
        query = request.GET.get('q', '')  # 获取查询参数
        books = []

        # 如果有查询关键词
        if query:
            books = BookMetadata.objects.all() 
            results_count = books.count()

            # 保存用户查询记录
            UserQuery.objects.create(user=user, query=query, results_count=results_count)

        serializer = BookMetadataSerializer(books, many=True)
        return Response({
            'user': user.username,
            'query': query,
            'results_count': len(books),
            'books': serializer.data
        })

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