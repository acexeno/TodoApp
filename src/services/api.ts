import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';

const API_URL = 'http://192.168.1.10:8000'; // Your Django server IP

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests if it exists
api.interceptors.request.use(
  async (config) => {
    const token = await AsyncStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export const todoService = {
  // Get all todos
  getTodos: async () => {
    const response = await api.get('/todo/');
    return response.data;
  },

  // Create a new todo
  createTodo: async (todoData: any) => {
    const response = await api.post('/todo/', todoData);
    return response.data;
  },

  // Update a todo
  updateTodo: async (id: number, todoData: any) => {
    const response = await api.put(`/todo/${id}/`, todoData);
    return response.data;
  },

  // Delete a todo
  deleteTodo: async (id: number) => {
    const response = await api.delete(`/todo/${id}/`);
    return response.data;
  },

  // Toggle todo completion
  toggleTodoComplete: async (id: number) => {
    const response = await api.post(`/todo/${id}/toggle/`);
    return response.data;
  },
};

export const authService = {
  // Login
  login: async (credentials: { username: string; password: string }) => {
    const response = await api.post('/auth/login/', credentials);
    if (response.data.token) {
      await AsyncStorage.setItem('token', response.data.token);
    }
    return response.data;
  },

  // Register
  register: async (userData: { username: string; password: string; email: string }) => {
    const response = await api.post('/auth/register/', userData);
    return response.data;
  },

  // Logout
  logout: async () => {
    await AsyncStorage.removeItem('token');
  },
};

export default api;