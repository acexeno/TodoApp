import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';

const API_URL = 'http://10.0.2.2:8000/api/v1'; // Use 10.0.2.2 for Android emulator to access localhost

// Create axios instance
const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests
api.interceptors.request.use(
  async (config) => {
    const token = await AsyncStorage.getItem('userToken');
    if (token) {
      config.headers.Authorization = `Token ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Todo types
export interface Todo {
  id: number;
  title: string;
  description: string;
  created_date: string;
  due_date: string | null;
  completed: boolean;
  priority: 'low' | 'medium' | 'high';
  is_overdue: boolean;
}

// API functions
export const todoApi = {
  // Auth
  login: async (username: string, password: string) => {
    const response = await api.post('/auth/login/', { username, password });
    return response.data;
  },

  register: async (username: string, email: string, password: string) => {
    const response = await api.post('/auth/register/', { username, email, password });
    return response.data;
  },

  logout: async () => {
    await AsyncStorage.removeItem('userToken');
  },

  // Todos
  getTodos: async () => {
    const response = await api.get('/todos/');
    return response.data;
  },

  getCompletedTodos: async () => {
    const response = await api.get('/todos/completed/');
    return response.data;
  },

  getPendingTodos: async () => {
    const response = await api.get('/todos/pending/');
    return response.data;
  },

  createTodo: async (todo: Partial<Todo>) => {
    const response = await api.post('/todos/', todo);
    return response.data;
  },

  updateTodo: async (id: number, todo: Partial<Todo>) => {
    const response = await api.patch(`/todos/${id}/`, todo);
    return response.data;
  },

  deleteTodo: async (id: number) => {
    await api.delete(`/todos/${id}/`);
  },

  toggleTodoComplete: async (id: number) => {
    const response = await api.post(`/todos/${id}/toggle_complete/`);
    return response.data;
  },
};

export default api; 