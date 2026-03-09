import { useState, useEffect } from 'react';
import { adminService } from '../../api/adminService';
import type {DashboardStats} from '../../types';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import { useAuth } from '../../contexts/AuthContext.tsx';
import { Link } from 'react-router-dom';

const AdminDashboardPage = () => {
    const [stats, setStats] = useState<DashboardStats | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const { adminUser } = useAuth();

    useEffect(() => {
        fetchDashboardStats();
    }, []);

    const fetchDashboardStats = async () => {
        try {
            const data = await adminService.getDashboardStats();
            setStats(data);
        } catch (err: any) {
            setError(err.response?.data?.message || 'Failed to load dashboard statistics');
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <LoadingSpinner text="Loading dashboard..." />
            </div>
        );
    }

    if (error) {
        return (
            <div className="min-h-screen bg-gray-50 py-12 px-4">
                <div className="max-w-4xl mx-auto">
                    <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg">
                        {error}
                    </div>
                </div>
            </div>
        );
    }

    if (!stats) return null;

    return (
        <div className="min-h-screen bg-gray-50">
            {/* Header */}
            <div className="bg-white shadow-sm border-b border-gray-200">
                <div className="container mx-auto px-4 py-6">
                    <div className="flex justify-between items-center">
                        <div>
                            <h1 className="text-3xl font-bold text-gray-900">Admin Dashboard</h1>
                            <p className="text-gray-600 mt-1">Welcome back, {adminUser?.fullName}</p>
                        </div>
                        <div className="flex gap-3">
                            <Link to="/admin/bookings" className="btn-primary">
                                Manage Bookings
                            </Link>
                            <Link to="/admin/audit-logs" className="btn-secondary">
                                Audit Logs
                            </Link>
                        </div>
                    </div>
                </div>
            </div>

            <div className="container mx-auto px-4 py-8">
                {/* Stats Grid */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
                    {/* Total Bookings */}
                    <div className="card bg-gradient-to-br from-blue-500 to-blue-600 text-white">
                        <p className="text-blue-100 text-sm mb-1">Total Bookings</p>
                        <p className="text-4xl font-bold">{stats.totalBookings}</p>
                        <p className="text-blue-100 text-sm mt-2">📈 All time</p>
                    </div>

                    {/* Today's Bookings */}
                    <div className="card bg-gradient-to-br from-green-500 to-green-600 text-white">
                        <p className="text-green-100 text-sm mb-1">Today's Bookings</p>
                        <p className="text-4xl font-bold">{stats.todayBookings}</p>
                        <p className="text-green-100 text-sm mt-2">📅 {new Date().toLocaleDateString()}</p>
                    </div>

                    {/* Revenue */}
                    <div className="card bg-gradient-to-br from-purple-500 to-purple-600 text-white">
                        <p className="text-purple-100 text-sm mb-1">Total Revenue</p>
                        <p className="text-4xl font-bold">${stats.totalRevenue.toFixed(2)}</p>
                        <p className="text-purple-100 text-sm mt-2">💰 All time</p>
                    </div>

                    {/* Active Users */}
                    <div className="card bg-gradient-to-br from-orange-500 to-orange-600 text-white">
                        <p className="text-orange-100 text-sm mb-1">Active Users</p>
                        <p className="text-4xl font-bold">{stats.activeUsers}</p>
                        <p className="text-orange-100 text-sm mt-2">👥 Registered</p>
                    </div>
                </div>

                {/* Status Breakdown */}
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
                    <div className="card">
                        <h2 className="text-xl font-bold text-gray-900 mb-4">Booking Status</h2>
                        <div className="space-y-3">
                            <div className="flex justify-between items-center p-3 bg-yellow-50 rounded-lg">
                                <span className="font-medium text-gray-700">Pending</span>
                                <span className="badge badge-warning">{stats.pendingBookings}</span>
                            </div>
                            <div className="flex justify-between items-center p-3 bg-blue-50 rounded-lg">
                                <span className="font-medium text-gray-700">Confirmed</span>
                                <span className="badge badge-info">{stats.confirmedBookings}</span>
                            </div>
                            <div className="flex justify-between items-center p-3 bg-green-50 rounded-lg">
                                <span className="font-medium text-gray-700">Completed</span>
                                <span className="badge badge-success">{stats.completedBookings}</span>
                            </div>
                            <div className="flex justify-between items-center p-3 bg-red-50 rounded-lg">
                                <span className="font-medium text-gray-700">Cancelled</span>
                                <span className="badge badge-danger">{stats.cancelledBookings}</span>
                            </div>
                        </div>
                    </div>

                    <div className="card">
                        <h2 className="text-xl font-bold text-gray-900 mb-4">Performance Metrics</h2>
                        <div className="space-y-4">
                            <div>
                                <div className="flex justify-between mb-1">
                                    <span className="text-sm text-gray-600">Capacity Utilization</span>
                                    <span className="text-sm font-semibold text-gray-900">{stats.capacityUtilization}%</span>
                                </div>
                                <div className="w-full bg-gray-200 rounded-full h-2">
                                    <div
                                        className="bg-primary-600 h-2 rounded-full"
                                        style={{ width: `${stats.capacityUtilization}%` }}
                                    />
                                </div>
                            </div>

                            <div className="p-3 bg-gray-50 rounded-lg">
                                <p className="text-sm text-gray-600">Average Bookings/Day</p>
                                <p className="text-2xl font-bold text-gray-900">{stats.averageBookingsPerDay}</p>
                            </div>

                            <div className="p-3 bg-gray-50 rounded-lg">
                                <p className="text-sm text-gray-600">Peak Hour</p>
                                <p className="text-xl font-bold text-gray-900">🕐 {stats.peakHour}</p>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Top Performers */}
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
                    <div className="card">
                        <h2 className="text-xl font-bold text-gray-900 mb-4">Top Provinces</h2>
                        <div className="space-y-2">
                            {Object.entries(stats.bookingsByProvince)
                                .sort(([, a], [, b]) => b - a)
                                .slice(0, 5)
                                .map(([province, count]) => (
                                    <div key={province} className="flex justify-between items-center p-2 hover:bg-gray-50 rounded">
                                        <span className="text-gray-700">{province}</span>
                                        <span className="font-semibold text-primary-600">{count}</span>
                                    </div>
                                ))}
                        </div>
                    </div>

                    <div className="card">
                        <h2 className="text-xl font-bold text-gray-900 mb-4">Top Services</h2>
                        <div className="space-y-2">
                            {Object.entries(stats.bookingsByService)
                                .sort(([, a], [, b]) => b - a)
                                .map(([service, count]) => (
                                    <div key={service} className="flex justify-between items-center p-2 hover:bg-gray-50 rounded">
                                        <span className="text-gray-700">{service}</span>
                                        <span className="font-semibold text-primary-600">{count}</span>
                                    </div>
                                ))}
                        </div>
                    </div>
                </div>

                {/* Recent Activity */}
                <div className="card">
                    <h2 className="text-xl font-bold text-gray-900 mb-4">Booking Channels</h2>
                    <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                        {Object.entries(stats.bookingsByChannel).map(([channel, count]) => (
                            <div key={channel} className="text-center p-4 bg-gray-50 rounded-lg">
                                <p className="text-sm text-gray-600 mb-1">{channel}</p>
                                <p className="text-3xl font-bold text-gray-900">{count}</p>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AdminDashboardPage;