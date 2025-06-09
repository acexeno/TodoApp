from rest_framework import serializers
from todo.models import Todo

class TodoSerializer(serializers.ModelSerializer):
    class Meta:
        model = Todo
        fields = ['id', 'title', 'description', 'created_date', 'due_date', 'completed', 'priority', 'is_overdue']
        read_only_fields = ['created_date', 'is_overdue']
        extra_kwargs = {
            'description': {'required': False, 'allow_blank': True},
            'due_date': {'required': False},
            'priority': {'required': False}
        }

    def validate_priority(self, value):
        if value not in dict(Todo.PRIORITY_CHOICES):
            raise serializers.ValidationError("Invalid priority value")
        return value
