from rest_framework.serializers import ModelSerializer
from mygutenberg.models import BookMetadata

class BookMetadataSerializer(ModelSerializer):
    class Meta:
        model = BookMetadata
        fields = '__all__'
