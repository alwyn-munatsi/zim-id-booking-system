import api from './axios';
import type {Province, ServiceType, Booking, BookingRequest} from '../types';

export const bookingService = {
    // Get provinces
    getProvinces: async (): Promise<Province[]> => {
        const response = await api.get<Province[]>('/provinces');
        return response.data;
    },

    getActiveProvinces: async (): Promise<Province[]> => {
        const response = await api.get<Province[]>('/provinces/active');
        return response.data;
    },

    // Get services
    getServices: async (): Promise<ServiceType[]> => {
        const response = await api.get<ServiceType[]>('/services');
        return response.data;
    },

    getActiveServices: async (): Promise<ServiceType[]> => {
        const response = await api.get<ServiceType[]>('/services/active');
        return response.data;
    },

    // Get available time slots
    getAvailableSlots: async (provinceId: number, date: string): Promise<string[]> => {
        const response = await api.get<string[]>('/bookings/slots/available', {
            params: { provinceId, date },
        });
        return response.data;
    },

    // Check slot availability
    checkSlotAvailability: async (
        provinceId: number,
        date: string,
        time: string
    ): Promise<{ available: boolean }> => {
        const response = await api.get('/bookings/slots/check', {
            params: { provinceId, date, time },
        });
        return response.data;
    },

    // Create booking
    createBooking: async (data: BookingRequest): Promise<Booking> => {
        const response = await api.post<Booking>('/bookings', data);
        return response.data;
    },

    // Get booking by reference
    getBookingByReference: async (reference: string): Promise<Booking> => {
        const response = await api.get<Booking>(`/bookings/${reference}`);
        return response.data;
    },

    // Search bookings
    searchBookings: async (query: string): Promise<Booking[]> => {
        const response = await api.get<Booking[]>('/bookings/search', {
            params: { query },
        });
        return response.data;
    },

    // Get bookings by phone
    getBookingsByPhone: async (phoneNumber: string): Promise<Booking[]> => {
        const response = await api.get<Booking[]>(`/bookings/phone/${phoneNumber}`);
        return response.data;
    },

    // Cancel booking
    cancelBooking: async (reference: string, reason?: string): Promise<void> => {
        await api.delete(`/bookings/${reference}`, {
            params: { reason },
        });
    },
};