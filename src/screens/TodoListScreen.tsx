import React, { useEffect } from 'react';
import {
  View,
  FlatList,
  StyleSheet,
  RefreshControl,
  ActivityIndicator,
  Text,
} from 'react-native';
import { useTodos } from '../hooks/useTodos';
import TodoItem from '../components/TodoItem';
import type { NativeStackScreenProps } from '@react-navigation/native-stack';

type RootStackParamList = {
  TodoList: undefined;
  EditTodo: { id: number };
};

type Props = NativeStackScreenProps<RootStackParamList, 'TodoList'>;

const TodoListScreen: React.FC<Props> = ({ navigation }) => {
  const {
    todos,
    isLoading,
    error,
    fetchTodos,
    deleteTodo,
    toggleTodoComplete,
  } = useTodos();

  useEffect(() => {
    fetchTodos();
  }, []);

  const handleEdit = (id: number) => {
    navigation.navigate('EditTodo', { id });
  };

  const renderItem = ({ item }: { item: any }) => (
    <TodoItem
      id={item.id}
      title={item.title}
      description={item.description}
      completed={item.completed}
      created_at={item.created_at}
      onToggle={toggleTodoComplete}
      onDelete={deleteTodo}
      onEdit={handleEdit}
    />
  );

  if (isLoading && todos.length === 0) {
    return (
      <View style={styles.centered}>
        <ActivityIndicator size="large" color="#2196F3" />
      </View>
    );
  }

  if (error && todos.length === 0) {
    return (
      <View style={styles.centered}>
        <Text style={styles.errorText}>{error}</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <FlatList
        data={todos}
        renderItem={renderItem}
        keyExtractor={(item) => item.id.toString()}
        contentContainerStyle={styles.list}
        refreshControl={
          <RefreshControl
            refreshing={isLoading}
            onRefresh={fetchTodos}
            colors={['#2196F3']}
          />
        }
        ListEmptyComponent={
          <View style={styles.emptyContainer}>
            <Text style={styles.emptyText}>No todos yet</Text>
            <Text style={styles.emptySubText}>
              Tap the + button to add a new todo
            </Text>
          </View>
        }
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F5F5F5',
  },
  list: {
    paddingVertical: 8,
  },
  centered: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5F5F5',
  },
  errorText: {
    color: '#F44336',
    fontSize: 16,
    textAlign: 'center',
    marginHorizontal: 20,
  },
  emptyContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    paddingVertical: 32,
  },
  emptyText: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#757575',
    marginBottom: 8,
  },
  emptySubText: {
    fontSize: 14,
    color: '#9E9E9E',
    textAlign: 'center',
  },
});

export default TodoListScreen; 