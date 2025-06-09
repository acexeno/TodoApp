import { useState, useEffect } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { authService } from '../services/api';

interface AuthState {
  isAuthenticated: boolean;
  isLoading: boolean;
  user: any | null;
  error: string | null;
}

export function useAuth() {
  const [authState, setAuthState] = useState<AuthState>({
    isAuthenticated: false,
    isLoading: true,
    user: null,
    error: null,
  });

  useEffect(() => {
    checkAuthStatus();
  }, []);

  const checkAuthStatus = async () => {
    try {
      const token = await AsyncStorage.getItem('token');
      if (token) {
        // You might want to validate the token with your backend here
        setAuthState({
          isAuthenticated: true,
          isLoading: false,
          user: null, // You can fetch user data here if needed
          error: null,
        });
      } else {
        setAuthState({
          isAuthenticated: false,
          isLoading: false,
          user: null,
          error: null,
        });
      }
    } catch (error) {
      setAuthState({
        isAuthenticated: false,
        isLoading: false,
        user: null,
        error: 'Failed to check authentication status',
      });
    }
  };

  const login = async (username: string, password: string) => {
    try {
      setAuthState(prev => ({ ...prev, isLoading: true, error: null }));
      const response = await authService.login({ username, password });
      setAuthState({
        isAuthenticated: true,
        isLoading: false,
        user: response.user,
        error: null,
      });
      return true;
    } catch (error: any) {
      setAuthState({
        isAuthenticated: false,
        isLoading: false,
        user: null,
        error: error.response?.data?.message || 'Login failed',
      });
      return false;
    }
  };

  const register = async (username: string, password: string, email: string) => {
    try {
      setAuthState(prev => ({ ...prev, isLoading: true, error: null }));
      await authService.register({ username, password, email });
      return true;
    } catch (error: any) {
      setAuthState(prev => ({
        ...prev,
        isLoading: false,
        error: error.response?.data?.message || 'Registration failed',
      }));
      return false;
    }
  };

  const logout = async () => {
    try {
      await authService.logout();
      setAuthState({
        isAuthenticated: false,
        isLoading: false,
        user: null,
        error: null,
      });
    } catch (error) {
      setAuthState(prev => ({
        ...prev,
        error: 'Logout failed',
      }));
    }
  };

  return {
    ...authState,
    login,
    register,
    logout,
  };
} 