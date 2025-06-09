import requests
import json

user_uid = "OY7QvyzeIsRMDUMcUzR5tGTlb6V2"

todo_data = {
    "title": "Test Todo",
    "description": "This is a test todo.",
    "completed": False,
    "userId": user_uid
}

url = "http://127.0.0.1:8000/api/todos/"
headers = {
    'X-Firebase-UID': user_uid
}

try:
    response = requests.post(url, json=todo_data, headers=headers)
    response.raise_for_status()
    print("Status Code:", response.status_code)
    print("Response Body:", response.json())
except requests.exceptions.RequestException as e:
    print(f"An error occurred: {e}") 