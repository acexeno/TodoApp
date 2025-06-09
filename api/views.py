import os
import firebase_admin
from firebase_admin import credentials
import logging

logging.basicConfig(
    filename='server_errors.log',
    level=logging.ERROR,
    format='%(asctime)s %(levelname)s:%(message)s'
)

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


db = firestore.client()
todos_ref = db.collection('todos')

@api_view(['POST'])
def signup_view(request):
    email = request.data.get('email')
    password = request.data.get('password')

    if not email or not password:
        return Response({'error': 'Email and password are required'}, status=status.HTTP_400_BAD_REQUEST)

    try:
        user = auth.create_user(email=email, password=password)
        return Response({'message': 'User created successfully', 'uid': user.uid}, status=status.HTTP_201_CREATED)
    except auth.EmailAlreadyExistsError:
        return Response({'error': 'Email already exists'}, status=status.HTTP_400_BAD_REQUEST)
    except Exception as e:
        return Response({'error': str(e)}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

@api_view(['POST'])
def login_view(request):
    id_token = request.data.get('idToken')

    if not id_token:
        return Response({'error': 'Firebase ID token is required'}, status=status.HTTP_400_BAD_REQUEST)

    try:
        decoded_token = auth.verify_id_token(id_token)
        uid = decoded_token['uid']


        return Response({'message': 'User authenticated successfully', 'uid': uid}, status=status.HTTP_200_OK)
    except (InvalidIdTokenError, ExpiredIdTokenError):
        return Response({'error': 'Invalid or expired ID token'}, status=status.HTTP_401_UNAUTHORIZED)
    except Exception as e:
        return Response({'error': str(e)}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

class TodoListCreateView(APIView):
    def get(self, request):
        uid = request.headers.get('X-Firebase-UID')

        if not uid:
             return Response({'error': 'Authentication required'}, status=status.HTTP_401_UNAUTHORIZED)

        try:
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
        uid = request.headers.get('X-Firebase-UID')
        text = request.data.get('text')

        if not uid:
             return Response({'error': 'Authentication required'}, status=status.HTTP_401_UNAUTHORIZED)

        if not text:
            return Response({'error': 'Text is required'}, status=status.HTTP_400_BAD_REQUEST)

        try:
            todo_data = {
                'text': text,
                'completed': False,
                'userId': uid,
                'createdAt': firestore.SERVER_TIMESTAMP
            }
            doc_ref = todos_ref.add(todo_data)[1]
            return Response({'message': 'Todo created successfully', 'id': doc_ref.id}, status=status.HTTP_201_CREATED)
        except Exception as e:
            import traceback
            error_traceback = traceback.format_exc()
            return Response({'error': str(e), 'traceback': error_traceback}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

class TodoDetailView(APIView):
    def get(self, request, todo_id):
        uid = request.headers.get('X-Firebase-UID')

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
        uid = request.headers.get('X-Firebase-UID')
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
        uid = request.headers.get('X-Firebase-UID')

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

