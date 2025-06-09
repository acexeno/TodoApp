from django.apps import AppConfig


class ApiConfig(AppConfig):
    default_auto_field = "django.db.models.BigAutoField"
    name = "api"

    def ready(self):
        # Import the initialization function here to ensure apps are ready
        from todo_project.firebase_utils import initialize_firebase
        initialize_firebase()
