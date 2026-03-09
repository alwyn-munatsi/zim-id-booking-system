import { useState } from 'react';
import { bookingService } from '../../api/bookingService';
import type {Booking} from '../../types';
import LoadingSpinner from '../../components/common/LoadingSpinner';

const CheckBookingPage = () => {
    const [reference, setReference] = useState('');
    const [booking, setBooking] = useState<Booking | null>(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleSearch = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setBooking(null);
        setLoading(true);

        try {
            const data = await bookingService.getBookingByReference(reference.toUpperCase());
            setBooking(data);
        } catch (err: any) {
            setError(err.response?.data?.message || 'Booking not found. Please check your reference number.');
        } finally {
            setLoading(false);
        }
    };

    const getStatusBadge = (status: string) => {
        const badges: Record<string, string> = {
            PENDING: 'badge-warning',
            CONFIRMED: 'badge-info',
            IN_PROGRESS: 'badge-info',
            COMPLETED: 'badge-success',
            CANCELLED: 'badge-danger',
            NO_SHOW: 'badge-danger',
        };
        return badges[status] || 'badge-info';
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-primary-50 to-primary-100 py-12 px-4">
            <div className="max-w-3xl mx-auto">
                <div className="card">
                    <div className="text-center mb-8">
                        <h1 className="text-3xl font-bold text-gray-900 mb-2">Check Booking Status</h1>
                        <p className="text-gray-600">Enter your booking reference to view your appointment details</p>
                    </div>

                    <form onSubmit={handleSearch} className="mb-6">
                        <div className="flex gap-3">
                            <input
                                type="text"
                                value={reference}
                                onChange={(e) => setReference(e.target.value)}
                                className="input-field flex-1"
                                placeholder="Enter booking reference (e.g., ZW-2024-1234)"
                                required
                            />
                            <button type="submit" disabled={loading} className="btn-primary px-8">
                                {loading ? 'Searching...' : 'Search'}
                            </button>
                        </div>
                    </form>

                    {error && (
                        <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg mb-6">
                            {error}
                        </div>
                    )}

                    {loading && (
                        <div className="py-12">
                            <LoadingSpinner text="Searching for your booking..." />
                        </div>
                    )}

                    {booking && !loading && (
                        <div className="space-y-4">
                            <div className="bg-primary-50 border-2 border-primary-200 rounded-lg p-6 text-center">
                                <p className="text-sm text-gray-600 mb-2">Booking Reference</p>
                                <p className="text-3xl font-bold text-primary-600 mb-3">{booking.bookingReference}</p>
                                <span className={`badge ${getStatusBadge(booking.status)} text-lg`}>
                  {booking.status}
                </span>
                            </div>

                            <div className="bg-gray-50 p-4 rounded-lg">
                                <h3 className="font-semibold text-gray-700 mb-3">Personal Information</h3>
                                <div className="space-y-2">
                                    <p className="text-gray-900"><strong>Name:</strong> {booking.fullName}</p>
                                    <p className="text-gray-900"><strong>Phone:</strong> {booking.phoneNumber}</p>
                                    <p className="text-gray-900"><strong>Email:</strong> {booking.email}</p>
                                </div>
                            </div>

                            <div className="bg-gray-50 p-4 rounded-lg">
                                <h3 className="font-semibold text-gray-700 mb-3">Appointment Details</h3>
                                <div className="space-y-2">
                                    <p className="text-gray-900"><strong>Office:</strong> {booking.province.name}</p>
                                    <p className="text-gray-600 text-sm">{booking.province.officeName}</p>
                                    <p className="text-gray-600 text-sm">{booking.province.address}</p>
                                    <p className="text-gray-600 text-sm">📞 {booking.province.phone}</p>
                                </div>
                            </div>

                            <div className="bg-gray-50 p-4 rounded-lg">
                                <h3 className="font-semibold text-gray-700 mb-3">Service</h3>
                                <p className="text-gray-900 font-semibold">{booking.service.name}</p>
                                <p className="text-gray-600 text-sm mt-1">{booking.service.description}</p>
                                <p className="text-primary-600 font-bold mt-2">${booking.service.fee}</p>
                            </div>

                            <div className="bg-gray-50 p-4 rounded-lg">
                                <h3 className="font-semibold text-gray-700 mb-3">Date & Time</h3>
                                <p className="text-gray-900">
                                    📅 {new Date(booking.appointmentDate).toLocaleDateString('en-ZW', {
                                    weekday: 'long',
                                    year: 'numeric',
                                    month: 'long',
                                    day: 'numeric'
                                })}
                                </p>
                                <p className="text-gray-900">🕒 {booking.appointmentTime}</p>
                            </div>

                            <div className="bg-gray-50 p-4 rounded-lg">
                                <h3 className="font-semibold text-gray-700 mb-3">Timeline</h3>
                                <div className="space-y-2 text-sm">
                                    <p className="text-gray-600">
                                        <strong>Created:</strong> {new Date(booking.createdAt).toLocaleString()}
                                    </p>
                                    {booking.confirmedAt && (
                                        <p className="text-gray-600">
                                            <strong>Confirmed:</strong> {new Date(booking.confirmedAt).toLocaleString()}
                                        </p>
                                    )}
                                    {booking.completedAt && (
                                        <p className="text-gray-600">
                                            <strong>Completed:</strong> {new Date(booking.completedAt).toLocaleString()}
                                        </p>
                                    )}
                                    {booking.cancelledAt && (
                                        <p className="text-gray-600">
                                            <strong>Cancelled:</strong> {new Date(booking.cancelledAt).toLocaleString()}
                                        </p>
                                    )}
                                </div>
                            </div>

                            {booking.status === 'CONFIRMED' && (
                                <div className="bg-yellow-50 border border-yellow-200 p-4 rounded-lg">
                                    <h4 className="font-semibold text-yellow-900 mb-2">📋 Required Documents:</h4>
                                    <ul className="text-sm text-yellow-800 space-y-1">
                                        {booking.service.requiredDocuments?.map((doc, index) => (
                                            <li key={index}>• {doc}</li>
                                        ))}
                                    </ul>
                                </div>
                            )}
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default CheckBookingPage;