from django.urls import path
from . import views

urlpatterns = [
    path('signup/', views.signup_view, name='api_signup'),
    path('login/', views.login_view, name='api_login'),

    path('todos/', views.TodoListCreateView.as_view(), name='api_todo_list_create'),
    path('todos/<str:todo_id>/', views.TodoDetailView.as_view(), name='api_todo_detail'),
] 