import firebase_admin
from firebase_admin import credentials
from django.conf import settings

def initialize_firebase():
    if not firebase_admin._apps:
        try:
            cred = credentials.Certificate(settings.FIREBASE_ADMIN_SDK_CONFIG['credential'])
            firebase_admin.initialize_app(cred)
            print("Firebase Admin SDK initialized successfully!")
        except FileNotFoundError:
            print("Error: Firebase Admin SDK service account key file not found.")
            print("Please make sure the path in settings.py is correct and the file exists.")
        except Exception as e:
            print(f"Error initializing Firebase Admin SDK: {e}")

initialize_firebase() 