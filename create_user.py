import requests
import json

url = "http://127.0.0.1:8000/api/signup/"

# User credentials
email = "kenniellmart@gmail.com"
password = "velarde112603"

payload = {
    "email": email,
    "password": password
}

headers = {
    'Content-Type': 'application/json'
}

try:
    response = requests.post(url, data=json.dumps(payload), headers=headers)
    response.raise_for_status() # Raise an HTTPError for bad responses (4xx or 5xx)
    print("Status Code:", response.status_code)
    print("Response Body:", response.json())
except requests.exceptions.RequestException as e:
    print(f"An error occurred: {e}") 