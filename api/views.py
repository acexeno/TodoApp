import os
import firebase_admin
from firebase_admin import credentials
import logging

# Configure logging to write to a file
logging.basicConfig(
    filename='server_errors.log',
    level=logging.ERROR,
    format='%(asctime)s %(levelname)s:%(message)s'
)

# Assume the credentials file is in the project root (adjust if needed)
cred_path = os.path.join(os.path.dirname(os.path.dirname(__file__)), "todo-manager-27c7d-firebase-adminsdk-fbsvc-9fd272ba9f.json")
if not firebase_admin._apps:
    firebase_admin.initialize_app(credential=firebase_admin.credentials.Certificate(cred_path))

from django.shortcuts import render
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework import status
from firebase_admin import auth, firestore
from firebase_admin.auth import InvalidIdTokenError, ExpiredIdTokenError
from rest_framework.views import APIView

# Ensure Firebase Admin SDK is initialized
# This is handled by todo_project/firebase_utils.py when the app starts

db = firestore.client()
todos_ref = db.collection('todos')

@api_view(['POST'])
def signup_view(request):
    email = request.data.get('email')
    password = request.data.get('password')

    if not email or not password:
        return Response({'error': 'Email and password are required'}, status=status.HTTP_400_BAD_REQUEST)

    try:
        # Create the user in Firebase Authentication
        user = auth.create_user(email=email, password=password)
        return Response({'message': 'User created successfully', 'uid': user.uid}, status=status.HTTP_201_CREATED)
    except auth.EmailAlreadyExistsError:
        return Response({'error': 'Email already exists'}, status=status.HTTP_400_BAD_REQUEST)
    except Exception as e:
        return Response({'error': str(e)}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

@api_view(['POST'])
def login_view(request):
    # For login, the frontend should typically send the Firebase ID token
    # after the user signs in on the client side.
    # The backend then verifies the token.
    id_token = request.data.get('idToken')

    if not id_token:
        return Response({'error': 'Firebase ID token is required'}, status=status.HTTP_400_BAD_REQUEST)

    try:
        # Verify the ID token while checking if the token is revoked.
        decoded_token = auth.verify_id_token(id_token)
        uid = decoded_token['uid']

        # You can optionally get the user details from uid if needed:
        # user = auth.get_user(uid)

        # For this example, we'll just return success with uid
        return Response({'message': 'User authenticated successfully', 'uid': uid}, status=status.HTTP_200_OK)
    except (InvalidIdTokenError, ExpiredIdTokenError):
        return Response({'error': 'Invalid or expired ID token'}, status=status.HTTP_401_UNAUTHORIZED)
    except Exception as e:
        return Response({'error': str(e)}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

class TodoListCreateView(APIView):
    def get(self, request):
        # Get the user's UID from the authenticated request
        # This assumes authentication middleware has populated request.user or similar
        # For simplicity here, we'll expect uid in headers for now, or implement auth properly later
        uid = request.headers.get('X-Firebase-UID') # Example: Expect UID in a custom header

        if not uid:
             return Response({'error': 'Authentication required'}, status=status.HTTP_401_UNAUTHORIZED)

        try:
            # Fetch todos from Firestore for the specific user
            docs = todos_ref.where('userId', '==', uid).order_by('createdAt').stream()
            todos = []
            for doc in docs:
                todo_data = doc.to_dict()
                todos.append({
                    'id': doc.id,
                    'text': todo_data.get('text'),
                    'completed': todo_data.get('completed', False),
                    'userId': todo_data.get('userId'),
                    'createdAt': todo_data.get('createdAt').isoformat() if todo_data.get('createdAt') else None,
                })
            return Response(todos, status=status.HTTP_200_OK)
        except Exception as e:
            return Response({'error': str(e)}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

    def post(self, request):
        uid = request.headers.get('X-Firebase-UID') # Example: Expect UID in a custom header
        text = request.data.get('text')

        if not uid:
             return Response({'error': 'Authentication required'}, status=status.HTTP_401_UNAUTHORIZED)

        if not text:
            return Response({'error': 'Text is required'}, status=status.HTTP_400_BAD_REQUEST)

        try:
            # Add a new todo to Firestore
            todo_data = {
                'text': text,
                'completed': False,
                'userId': uid,
                'createdAt': firestore.SERVER_TIMESTAMP # Use server timestamp
            }
            doc_ref = todos_ref.add(todo_data)[1]
            # Fetch the created document to get the server timestamp if needed
            # created_todo = doc_ref.get().to_dict()
            # created_todo['id'] = doc_ref.id
            return Response({'message': 'Todo created successfully', 'id': doc_ref.id}, status=status.HTTP_201_CREATED)
        except Exception as e:
            import traceback
            error_traceback = traceback.format_exc()
            # logging.error("Error creating todo:", exc_info=True)
            return Response({'error': str(e), 'traceback': error_traceback}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

class TodoDetailView(APIView):
    def get(self, request, todo_id):
        uid = request.headers.get('X-Firebase-UID') # Example: Expect UID in a custom header

        if not uid:
             return Response({'error': 'Authentication required'}, status=status.HTTP_401_UNAUTHORIZED)

        try:
            doc = todos_ref.document(todo_id).get()
            if not doc.exists or doc.to_dict().get('userId') != uid:
                return Response({'error': 'Todo not found'}, status=status.HTTP_404_NOT_FOUND)

            todo_data = doc.to_dict()
            todo = {
                 'id': doc.id,
                 'text': todo_data.get('text'),
                 'completed': todo_data.get('completed', False),
                 'userId': todo_data.get('userId'),
                 'createdAt': todo_data.get('createdAt').isoformat() if todo_data.get('createdAt') else None,
             }
            return Response(todo, status=status.HTTP_200_OK)
        except Exception as e:
            return Response({'error': str(e)}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

    def put(self, request, todo_id):
        uid = request.headers.get('X-Firebase-UID') # Example: Expect UID in a custom header
        text = request.data.get('text')
        completed = request.data.get('completed')

        if not uid:
             return Response({'error': 'Authentication required'}, status=status.HTTP_401_UNAUTHORIZED)

        try:
            doc_ref = todos_ref.document(todo_id)
            doc = doc_ref.get()

            if not doc.exists or doc.to_dict().get('userId') != uid:
                return Response({'error': 'Todo not found'}, status=status.HTTP_404_NOT_FOUND)

            update_data = {}
            if text is not None:
                update_data['text'] = text
            if completed is not None:
                update_data['completed'] = completed

            if not update_data:
                 return Response({'error': 'No fields to update'}, status=status.HTTP_400_BAD_REQUEST)

            doc_ref.update(update_data)

            # Fetch the updated document to return the latest data
            updated_doc = doc_ref.get()
            updated_todo_data = updated_doc.to_dict()
            updated_todo = {
                 'id': updated_doc.id,
                 'text': updated_todo_data.get('text'),
                 'completed': updated_todo_data.get('completed', False),
                 'userId': updated_todo_data.get('userId'),
                 'createdAt': updated_todo_data.get('createdAt').isoformat() if updated_todo_data.get('createdAt') else None,
             }

            return Response(updated_todo, status=status.HTTP_200_OK)
        except Exception as e:
            return Response({'error': str(e)}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

    def delete(self, request, todo_id):
        uid = request.headers.get('X-Firebase-UID') # Example: Expect UID in a custom header

        if not uid:
             return Response({'error': 'Authentication required'}, status=status.HTTP_401_UNAUTHORIZED)

        try:
            doc_ref = todos_ref.document(todo_id)
            doc = doc_ref.get()

            if not doc.exists or doc.to_dict().get('userId') != uid:
                return Response({'error': 'Todo not found'}, status=status.HTTP_404_NOT_FOUND)

            doc_ref.delete()
            return Response({'message': 'Todo deleted successfully'}, status=status.HTTP_204_NO_CONTENT)
        except Exception as e:
            return Response({'error': str(e)}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
