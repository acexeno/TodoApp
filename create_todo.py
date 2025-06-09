import requests
import json

url = "http://127.0.0.1:8000/api/todos/"

# Replace with the actual UID you copied from Firebase
user_uid = "OY7QvyzeIsRMDUMcUzR5tGTlb6V2"

# Todo data (removed 'createdAt' field for testing)
todo_data = {
    "text": "Learn API Testing",
    "completed": False, # Explicitly include completed field
    "userId": user_uid # Explicitly include userId field
}

headers = {
    'Content-Type': 'application/json',
    'X-Firebase-UID': user_uid
}

try:
    response = requests.post(url, data=json.dumps(todo_data), headers=headers)
    response.raise_for_status() # Raise an HTTPError for bad responses (4xx or 5xx)
    print("Status Code:", response.status_code)
    print("Response Body:", response.json())
except requests.exceptions.RequestException as e:
    print(f"An error occurred: {e}") 