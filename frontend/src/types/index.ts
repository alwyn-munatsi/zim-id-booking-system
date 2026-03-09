// API Response Types
export interface ApiResponse<T> {
    data: T;
    message?: string;
    status: number;
}

// User Types
export interface User {
    id: number;
    fullName: string;
    email: string;
    phoneNumber: string;
    role: 'CITIZEN' | 'ADMIN' | 'SUPER_ADMIN' | 'OPERATOR' | 'VIEWER';
    emailVerified: boolean;
    phoneVerified: boolean;
    lastLoginAt?: string;
    createdAt: string;
    updatedAt: string;
}

export interface AuthResponse {
    token: string;
    tokenType: string;
    expiresIn: number;
    user: User;
}

// Province Types
export interface Province {
    id: number;
    code: string;
    name: string;
    officeName: string;
    address: string;
    phone: string;
    dailyCapacity?: number;
    active: boolean;
    latitude?: number;
    longitude?: number;
}

// Service Types
export interface ServiceType {
    id: number;
    code: string;
    name: string;
    description: string;
    durationMinutes: number;
    fee: number;
    currency: string;
    requiredDocuments?: string[];
    active: boolean;
    color?: string;
}

// Booking Types
export type BookingStatus =
    | 'PENDING'
    | 'CONFIRMED'
    | 'IN_PROGRESS'
    | 'COMPLETED'
    | 'CANCELLED'
    | 'NO_SHOW';

export type BookingChannel = 'WEB' | 'MOBILE' | 'USSD' | 'ADMIN';

export interface Booking {
    id: number;
    bookingReference: string;
    fullName: string;
    dateOfBirth: string;
    phoneNumber: string;
    email: string;
    province: Province;
    service: ServiceType;
    appointmentDate: string;
    appointmentTime: string;
    status: BookingStatus;
    channel: BookingChannel;
    notes?: string;
    cancellationReason?: string;
    createdAt: string;
    updatedAt: string;
    confirmedAt?: string;
    completedAt?: string;
    cancelledAt?: string;
}

export interface BookingRequest {
    fullName: string;
    dateOfBirth: string;
    phoneNumber: string;
    email: string;
    provinceId: number;
    serviceId: number;
    appointmentDate: string;
    appointmentTime: string;
    channel?: BookingChannel;
    notes?: string;
}

// Dashboard Stats
export interface DashboardStats {
    totalBookings: number;
    todayBookings: number;
    thisWeekBookings: number;
    thisMonthBookings: number;
    pendingBookings: number;
    confirmedBookings: number;
    completedBookings: number;
    cancelledBookings: number;
    totalRevenue: number;
    todayRevenue: number;
    thisMonthRevenue: number;
    totalUsers: number;
    activeUsers: number;
    newUsersToday: number;
    averageBookingsPerDay: number;
    capacityUtilization: number;
    peakHour: string;
    mostPopularService: string;
    busiestProvince: string;
    last7Days: DailyStats[];
    last30Days: DailyStats[];
    bookingsByProvince: Record<string, number>;
    bookingsByService: Record<string, number>;
    bookingsByStatus: Record<string, number>;
    bookingsByChannel: Record<string, number>;
}

export interface DailyStats {
    date: string;
    bookings: number;
    revenue: number;
    newUsers: number;
}

export interface ProvincePerformance {
    provinceName: string;
    totalBookings: number;
    revenue: number;
    capacity: number;
    utilizationRate: number;
}

export interface ServicePerformance {
    serviceName: string;
    totalBookings: number;
    revenue: number;
    averagePrice: number;
}

export interface AnalyticsReport {
    startDate: string;
    endDate: string;
    reportType: string;
    totalBookings: number;
    totalRevenue: number;
    completedBookings: number;
    cancelledBookings: number;
    completionRate: number;
    cancellationRate: number;
    timeSeriesData: DailyStats[];
    topProvinces: ProvincePerformance[];
    topServices: ServicePerformance[];
    bookingsByHour: Record<string, number>;
    bookingsByDayOfWeek: Record<string, number>;
}

// Admin User Types
export interface AdminUser {
    id: number;
    username: string;
    email: string;
    fullName: string;
    role: 'SUPER_ADMIN' | 'ADMIN' | 'OPERATOR' | 'VIEWER';
    active: boolean;
    createdAt: string;
    updatedAt: string;
    lastLoginAt?: string;
}

export interface AdminAuthResponse {
    token: string;
    tokenType: string;
    expiresIn: number;
    user: AdminUser;
}

// Audit Log Types
export interface AuditLog {
    id: number;
    adminUserId: number;
    adminUsername: string;
    action: string;
    entity: string;
    entityId?: string;
    details: string;
    ipAddress?: string;
    createdAt: string;
}

// Pagination
export interface PaginatedResponse<T> {
    content: T[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
}
