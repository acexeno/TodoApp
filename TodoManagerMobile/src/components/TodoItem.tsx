import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { MaterialIcons } from '@expo/vector-icons';
import { Todo } from '../services/api';

interface TodoItemProps {
  todo: Todo;
  onToggle: (id: number) => void;
  onDelete: (id: number) => void;
  onPress: (todo: Todo) => void;
}

const priorityColors = {
  low: '#4CAF50',
  medium: '#FFC107',
  high: '#F44336',
};

export const TodoItem: React.FC<TodoItemProps> = ({ todo, onToggle, onDelete, onPress }) => {
  const priorityColor = priorityColors[todo.priority];

  return (
    <TouchableOpacity
      style={[styles.container, todo.completed && styles.completed]}
      onPress={() => onPress(todo)}
    >
      <View style={styles.content}>
        <TouchableOpacity
          style={[styles.checkbox, todo.completed && styles.checked]}
          onPress={() => onToggle(todo.id)}
        >
          {todo.completed && <MaterialIcons name="check" size={16} color="#fff" />}
        </TouchableOpacity>
        
        <View style={styles.textContainer}>
          <Text style={[styles.title, todo.completed && styles.completedText]}>
            {todo.title}
          </Text>
          {todo.description && (
            <Text style={[styles.description, todo.completed && styles.completedText]}>
              {todo.description}
            </Text>
          )}
          {todo.due_date && (
            <Text style={[styles.dueDate, todo.is_overdue && styles.overdue]}>
              Due: {new Date(todo.due_date).toLocaleDateString()}
            </Text>
          )}
        </View>

        <View style={[styles.priorityIndicator, { backgroundColor: priorityColor }]} />
      </View>

      <TouchableOpacity
        style={styles.deleteButton}
        onPress={() => onDelete(todo.id)}
      >
        <MaterialIcons name="delete-outline" size={24} color="#FF5252" />
      </TouchableOpacity>
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  container: {
    backgroundColor: '#fff',
    borderRadius: 8,
    padding: 16,
    marginVertical: 8,
    marginHorizontal: 16,
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    flexDirection: 'row',
    alignItems: 'center',
  },
  completed: {
    opacity: 0.7,
  },
  content: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
  },
  checkbox: {
    width: 24,
    height: 24,
    borderRadius: 12,
    borderWidth: 2,
    borderColor: '#2196F3',
    marginRight: 12,
    alignItems: 'center',
    justifyContent: 'center',
  },
  checked: {
    backgroundColor: '#2196F3',
  },
  textContainer: {
    flex: 1,
  },
  title: {
    fontSize: 16,
    fontWeight: '600',
    color: '#333',
    marginBottom: 4,
  },
  description: {
    fontSize: 14,
    color: '#666',
    marginBottom: 4,
  },
  dueDate: {
    fontSize: 12,
    color: '#888',
  },
  overdue: {
    color: '#F44336',
  },
  completedText: {
    textDecorationLine: 'line-through',
    color: '#888',
  },
  priorityIndicator: {
    width: 4,
    height: '100%',
    borderRadius: 2,
    marginLeft: 12,
  },
  deleteButton: {
    padding: 8,
  },
}); 