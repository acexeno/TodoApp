import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  Alert,
} from 'react-native';
import Icon from 'react-native-vector-icons/MaterialIcons';

interface TodoItemProps {
  id: number;
  title: string;
  description: string;
  completed: boolean;
  created_at: string;
  onToggle: (id: number) => void;
  onDelete: (id: number) => void;
  onEdit: (id: number) => void;
}

const TodoItem: React.FC<TodoItemProps> = ({
  id,
  title,
  description,
  completed,
  created_at,
  onToggle,
  onDelete,
  onEdit,
}) => {
  const handleDelete = () => {
    Alert.alert(
      'Delete Todo',
      'Are you sure you want to delete this todo?',
      [
        {
          text: 'Cancel',
          style: 'cancel',
        },
        {
          text: 'Delete',
          style: 'destructive',
          onPress: () => onDelete(id),
        },
      ]
    );
  };

  return (
    <View style={[styles.container, completed && styles.completedContainer]}>
      <TouchableOpacity
        style={styles.toggleButton}
        onPress={() => onToggle(id)}
      >
        <Icon
          name={completed ? 'check-circle' : 'radio-button-unchecked'}
          size={24}
          color={completed ? '#4CAF50' : '#757575'}
        />
      </TouchableOpacity>

      <View style={styles.content}>
        <Text style={[styles.title, completed && styles.completedText]}>
          {title}
        </Text>
        <Text style={[styles.description, completed && styles.completedText]}>
          {description}
        </Text>
        <Text style={styles.date}>
          {new Date(created_at).toLocaleDateString()}
        </Text>
      </View>

      <View style={styles.actions}>
        <TouchableOpacity
          style={styles.actionButton}
          onPress={() => onEdit(id)}
        >
          <Icon name="edit" size={24} color="#2196F3" />
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.actionButton}
          onPress={handleDelete}
        >
          <Icon name="delete" size={24} color="#F44336" />
        </TouchableOpacity>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    padding: 16,
    backgroundColor: '#FFFFFF',
    borderRadius: 8,
    marginVertical: 8,
    marginHorizontal: 16,
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.2,
    shadowRadius: 2,
  },
  completedContainer: {
    backgroundColor: '#F5F5F5',
  },
  toggleButton: {
    marginRight: 12,
    justifyContent: 'center',
  },
  content: {
    flex: 1,
  },
  title: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#212121',
    marginBottom: 4,
  },
  description: {
    fontSize: 14,
    color: '#757575',
    marginBottom: 4,
  },
  date: {
    fontSize: 12,
    color: '#9E9E9E',
  },
  completedText: {
    color: '#9E9E9E',
    textDecorationLine: 'line-through',
  },
  actions: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  actionButton: {
    padding: 8,
    marginLeft: 8,
  },
});

export default TodoItem; 