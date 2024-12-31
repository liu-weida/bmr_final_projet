from django.db import models
from django.contrib.auth.models import User

class UserQuery(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    query = models.CharField(max_length=255)
    timestamp = models.DateTimeField(auto_now_add=True)
    results_count = models.IntegerField(default=0)

    def __str__(self):
        return f"{self.user.username} searched for '{self.query}'"

# Create your models here.
class ProduitAllBooks(models.Model):
    created = models.DateTimeField(auto_now_add=True)
    bookID = models.IntegerField(default='-1')

    class Meta:
        ordering = ('bookID',)

class ProduitEnglishBooks(models.Model):
    created = models.DateTimeField(auto_now_add=True)
    bookID = models.IntegerField(default='-1')

    class Meta:
        ordering = ('bookID',)

class ProduitFrenchBooks(models.Model):
    created = models.DateTimeField(auto_now_add=True)
    bookID = models.IntegerField(default='-1')

    class Meta:
        ordering = ('bookID',)

class BookMetadata(models.Model):
    book_id = models.IntegerField(unique=True)
    title = models.CharField(max_length=255, null=True, blank=True)
    author = models.CharField(max_length=255, null=True, blank=True)
    language = models.CharField(max_length=50, null=True, blank=True)
    publication_date = models.CharField(max_length=50, null=True, blank=True)

    def __str__(self):
        return self.title or f"Book {self.book_id}"

class UserQuery(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    query = models.CharField(max_length=255)
    timestamp = models.DateTimeField(auto_now_add=True)
    results_count = models.IntegerField(default=0)

    def __str__(self):
        return f"{self.user.username} searched for '{self.query}'"


