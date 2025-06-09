import requests
import json

# Replace with the actual UID you copied from Firebase
user_uid = "OY7QvyzeIsRMDUMcUzR5tGTlb6V2"

# Replace with the actual ID of the todo you want to delete
todo_id = "RgHqo5aIUlIkVQB8lJPT"

url = f"http://127.0.0.1:8000/api/todos/{todo_id}/"

headers = {
    'X-Firebase-UID': user_uid
}

try:
    response = requests.delete(url, headers=headers)
    response.raise_for_status() # Raise an HTTPError for bad responses (4xx or 5xx)
    print("Status Code:", response.status_code)
    # DELETE requests typically return a 204 No Content on success, with no body
    if response.status_code != 204:
        print("Response Body:", response.json())
except requests.exceptions.RequestException as e:
    print(f"An error occurred: {e}") 