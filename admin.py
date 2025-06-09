from django.contrib import admin
from .models import Todo

@admin.register(Todo)
class TodoAdmin(admin.ModelAdmin):
    list_display = ('title', 'created_date', 'due_date', 'completed', 'priority')
    list_filter = ('completed', 'priority', 'created_date', 'due_date')
    search_fields = ('title', 'description')
    date_hierarchy = 'created_date'
    ordering = ('-created_date',)
