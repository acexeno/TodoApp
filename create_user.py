import requests
import json

user_data = {
    "username": "testuser",
    "password": "testpassword",
    "email": "testuser@example.com"
}

url = "http://127.0.0.1:8000/api/register/"

try:
    response = requests.post(url, json=user_data)
    response.raise_for_status()
    print("Status Code:", response.status_code)
    print("Response Body:", response.json())
except requests.exceptions.RequestException as e:
    print(f"An error occurred: {e}") 