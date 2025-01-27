import requests
from bs4 import BeautifulSoup
import random
import csv

# Base URL for the book information
baseUrl = 'https://www.gutenberg.org/ebooks/'

# Function to get book data
def get_data(bookID):
    try:
        response = requests.get(baseUrl + str(bookID))
        response.raise_for_status()  # Raise an error for unsuccessful requests
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

        img = soup.find('img', class_='cover-art')
        if img and img.get('src'):
            metadata['img'] = img['src']
        else:
            metadata['img'] = ''

        table = soup.find('table', class_='files')
        if table:
            td = table.find('td', content='text/plain; charset=us-ascii')
            if td:
                metadata['storagePath'] = 'https://www.gutenberg.org' + td.find('a').get('href');

        metadata['Downloads'] = metadata['Downloads'].strip(' ')[0]
        metadata['Title'] = metadata['Title'][:199]
        metadata['Subject'] = metadata['Subject'][:90]
        return metadata
    except requests.exceptions.RequestException as e:
        print(f"Error fetching data for bookID {bookID}: {e}")
        return None

# Function to write data to a CSV file
def write_to_csv(data, filename):
    with open(filename, 'w', newline='', encoding='utf-8') as csvfile:
        fieldnames = ['refID', 'Title', 'storagePath', 'Author', 'Subject', 'Summary', 'Language', 'Downloads', 'img']
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
        writer.writeheader()
        for entry in data:
            row = {field: entry.get(field, '') for field in fieldnames}  # Fill missing fields with empty strings
            writer.writerow(row)



# Main script
if __name__ == "__main__":
    n = 200  # Number of random book IDs to fetch
    min_book_id = 1  # Minimum book ID
    max_book_id = 75000  # Maximum book ID (you can adjust based on your requirement)

    random_book_ids = random.sample(range(min_book_id, max_book_id), n)
    book_data = []

    for bookID in random_book_ids:
        print(f"Fetching data for bookID {bookID}...")
        metadata = get_data(bookID)
        if metadata:
            metadata['refID'] = bookID
            book_data.append(metadata)

    if book_data:
        output_file = 'resources/bookInfo/bookInfo.csv'
        write_to_csv(book_data, output_file)
        print(f"Data successfully written to {output_file}")
    else:
        print("No data fetched.")
