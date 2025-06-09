from django.urls import path
from . import views
from django.contrib.auth import views as auth_views

app_name = 'todo'

urlpatterns = [
    path('', views.todo_list, name='todo_list'),
    path('update/<int:pk>/', views.todo_update, name='todo_update'),
    path('delete/<int:pk>/', views.todo_delete, name='todo_delete'),
    path('toggle/<int:pk>/', views.todo_toggle_complete, name='todo_toggle_complete'),
    # Custom registration view
    path('register/', views.register, name='register'),
    # Django's built-in login view (using our custom template)
    # The URL 'accounts/login/' is already included by 'django.contrib.auth.urls'
    # We just need to ensure our template 'registration/login.html' is found
] 