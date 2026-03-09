import api from './axios';
import type {DashboardStats, Booking, PaginatedResponse, AuditLog, AdminUser} from '../types';

export const adminService = {
    // Dashboard
    getDashboardStats: async (): Promise<DashboardStats> => {
        const response = await api.get<DashboardStats>('/admin/dashboard/stats');
        return response.data;
    },

    getDailyReport: async () => {
        const response = await api.get('/admin/dashboard/daily');
        return response.data;
    },

    getWeeklyReport: async () => {
        const response = await api.get('/admin/dashboard/weekly');
        return response.data;
    },

    getMonthlyReport: async () => {
        const response = await api.get('/admin/dashboard/monthly');
        return response.data;
    },

    // Bookings
    searchBookings: async (query: string): Promise<Booking[]> => {
        const response = await api.get<Booking[]>('/admin/bookings/search', {
            params: { query },
        });
        return response.data;
    },

    updateBookingStatus: async (reference: string, status: string): Promise<Booking> => {
        const response = await api.patch<Booking>(`/admin/bookings/${reference}/status`, null, {
            params: { status },
        });
        return response.data;
    },

    cancelBooking: async (reference: string, reason?: string): Promise<void> => {
        await api.delete(`/admin/bookings/${reference}`, {
            params: { reason },
        });
    },

    bulkUpdateStatus: async (references: string[], status: string) => {
        const response = await api.patch('/admin/bookings/bulk/status', {
            references,
            status,
        });
        return response.data;
    },

    // Admin users
    getAllAdminUsers: async (): Promise<AdminUser[]> => {
        const response = await api.get<AdminUser[]>('/admin/users');
        return response.data;
    },

    getCurrentAdminUser: async (): Promise<AdminUser> => {
        const response = await api.get<AdminUser>('/admin/users/me');
        return response.data;
    },

    createAdminUser: async (data: any): Promise<AdminUser> => {
        const response = await api.post<AdminUser>('/admin/users', data);
        return response.data;
    },

    updateAdminUser: async (id: number, data: any): Promise<AdminUser> => {
        const response = await api.put<AdminUser>(`/admin/users/${id}`, data);
        return response.data;
    },

    deactivateAdminUser: async (id: number): Promise<void> => {
        await api.patch(`/admin/users/${id}/deactivate`);
    },

    activateAdminUser: async (id: number): Promise<void> => {
        await api.patch(`/admin/users/${id}/activate`);
    },

    // Audit logs
    getAuditLogs: async (page = 0, size = 20): Promise<PaginatedResponse<AuditLog>> => {
        const response = await api.get<PaginatedResponse<AuditLog>>('/admin/audit-logs', {
            params: { page, size },
        });
        return response.data;
    },

    getAuditLogsByUser: async (userId: number, page = 0, size = 20): Promise<PaginatedResponse<AuditLog>> => {
        const response = await api.get<PaginatedResponse<AuditLog>>(`/admin/audit-logs/user/${userId}`, {
            params: { page, size },
        });
        return response.data;
    },

    getAuditLogsByAction: async (action: string, page = 0, size = 20): Promise<PaginatedResponse<AuditLog>> => {
        const response = await api.get<PaginatedResponse<AuditLog>>(`/admin/audit-logs/action/${action}`, {
            params: { page, size },
        });
        return response.data;
    },

    // Configuration
    getAllProvinces: async () => {
        const response = await api.get('/admin/config/provinces');
        return response.data;
    },

    getAllServices: async () => {
        const response = await api.get('/admin/config/services');
        return response.data;
    },
};
