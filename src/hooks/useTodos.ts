import { useState, useEffect } from 'react';
import { todoService } from '../services/api';

interface Todo {
  id: number;
  title: string;
  description: string;
  completed: boolean;
  created_at: string;
}

interface TodoState {
  todos: Todo[];
  isLoading: boolean;
  error: string | null;
}

export const useTodos = () => {
  const [state, setState] = useState<TodoState>({
    todos: [],
    isLoading: true,
    error: null,
  });

  const fetchTodos = async () => {
    try {
      setState(prev => ({ ...prev, isLoading: true, error: null }));
      const todos = await todoService.getTodos();
      setState({
        todos,
        isLoading: false,
        error: null,
      });
    } catch (error: any) {
      setState({
        todos: [],
        isLoading: false,
        error: error.response?.data?.message || 'Failed to fetch todos',
      });
    }
  };

  useEffect(() => {
    fetchTodos();
  }, []);

  const addTodo = async (title: string, description: string) => {
    try {
      setState(prev => ({ ...prev, isLoading: true, error: null }));
      const newTodo = await todoService.createTodo({ title, description });
      setState(prev => ({
        ...prev,
        todos: [...prev.todos, newTodo],
        isLoading: false,
      }));
      return true;
    } catch (error: any) {
      setState(prev => ({
        ...prev,
        isLoading: false,
        error: error.response?.data?.message || 'Failed to create todo',
      }));
      return false;
    }
  };

  const updateTodo = async (id: number, updates: Partial<Todo>) => {
    try {
      setState(prev => ({ ...prev, isLoading: true, error: null }));
      const updatedTodo = await todoService.updateTodo(id, updates);
      setState(prev => ({
        ...prev,
        todos: prev.todos.map(todo =>
          todo.id === id ? { ...todo, ...updatedTodo } : todo
        ),
        isLoading: false,
      }));
      return true;
    } catch (error: any) {
      setState(prev => ({
        ...prev,
        isLoading: false,
        error: error.response?.data?.message || 'Failed to update todo',
      }));
      return false;
    }
  };

  const deleteTodo = async (id: number) => {
    try {
      setState(prev => ({ ...prev, isLoading: true, error: null }));
      await todoService.deleteTodo(id);
      setState(prev => ({
        ...prev,
        todos: prev.todos.filter(todo => todo.id !== id),
        isLoading: false,
      }));
      return true;
    } catch (error: any) {
      setState(prev => ({
        ...prev,
        isLoading: false,
        error: error.response?.data?.message || 'Failed to delete todo',
      }));
      return false;
    }
  };

  const toggleTodoComplete = async (id: number) => {
    try {
      setState(prev => ({ ...prev, isLoading: true, error: null }));
      const updatedTodo = await todoService.toggleTodoComplete(id);
      setState(prev => ({
        ...prev,
        todos: prev.todos.map(todo =>
          todo.id === id ? { ...todo, completed: updatedTodo.completed } : todo
        ),
        isLoading: false,
      }));
      return true;
    } catch (error: any) {
      setState(prev => ({
        ...prev,
        isLoading: false,
        error: error.response?.data?.message || 'Failed to toggle todo',
      }));
      return false;
    }
  };

  return {
    ...state,
    fetchTodos,
    addTodo,
    updateTodo,
    deleteTodo,
    toggleTodoComplete,
  };
}; 