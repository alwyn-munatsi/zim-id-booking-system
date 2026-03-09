import api from './axios';
import type {AuthResponse, AdminAuthResponse} from '../types';

interface LoginRequest {
    email: string;
    password: string;
}

interface RegisterRequest {
    fullName: string;
    email: string;
    phoneNumber: string;
    password: string;
    dateOfBirth: string;
    address?: string;
}

interface AdminLoginRequest {
    username: string;
    password: string;
}

export const authService = {
    // Citizen authentication
    register: async (data: RegisterRequest): Promise<AuthResponse> => {
        const response = await api.post<AuthResponse>('/auth/register', data);
        return response.data;
    },

    login: async (data: LoginRequest): Promise<AuthResponse> => {
        const response = await api.post<AuthResponse>('/auth/login', data);
        return response.data;
    },

    getCurrentUser: async () => {
        const response = await api.get('/users/me');
        return response.data;
    },

    // Admin authentication
    adminLogin: async (data: AdminLoginRequest): Promise<AdminAuthResponse> => {
        const response = await api.post<AdminAuthResponse>('/admin/auth/login', data);
        return response.data;
    },

    validateAdminToken: async () => {
        const response = await api.get('/admin/auth/validate');
        return response.data;
    },
};