import { useLocation, Link } from 'react-router-dom';
import type {Booking} from '../../types';

const BookingSuccessPage = () => {
    const location = useLocation();
    const booking = location.state?.booking as Booking;

    if (!booking) {
        return (
            <div className="min-h-screen bg-gradient-to-br from-primary-50 to-primary-100 py-12 px-4">
                <div className="max-w-2xl mx-auto card text-center">
                    <p className="text-gray-600">No booking information found.</p>
                    <Link to="/booking" className="btn-primary mt-4 inline-block">
                        Make a New Booking
                    </Link>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gradient-to-br from-primary-50 to-primary-100 py-12 px-4">
            <div className="max-w-2xl mx-auto">
                <div className="card text-center">
                    <div className="w-20 h-20 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-6">
                        <span className="text-4xl">✅</span>
                    </div>

                    <h1 className="text-3xl font-bold text-gray-900 mb-2">Booking Confirmed!</h1>
                    <p className="text-gray-600 mb-8">
                        Your appointment has been successfully booked. A confirmation has been sent to your email and phone.
                    </p>

                    <div className="bg-primary-50 border-2 border-primary-200 rounded-lg p-6 mb-6">
                        <p className="text-sm text-gray-600 mb-2">Booking Reference</p>
                        <p className="text-3xl font-bold text-primary-600">{booking.bookingReference}</p>
                        <p className="text-sm text-gray-500 mt-2">Save this reference number</p>
                    </div>

                    <div className="text-left space-y-4 mb-6">
                        <div className="bg-gray-50 p-4 rounded-lg">
                            <p className="text-sm text-gray-600">Office</p>
                            <p className="font-semibold text-gray-900">{booking.province.name}</p>
                            <p className="text-sm text-gray-600">{booking.province.address}</p>
                        </div>

                        <div className="bg-gray-50 p-4 rounded-lg">
                            <p className="text-sm text-gray-600">Service</p>
                            <p className="font-semibold text-gray-900">{booking.service.name}</p>
                        </div>

                        <div className="bg-gray-50 p-4 rounded-lg">
                            <p className="text-sm text-gray-600">Appointment</p>
                            <p className="font-semibold text-gray-900">
                                📅 {new Date(booking.appointmentDate).toLocaleDateString('en-ZW', {
                                weekday: 'long',
                                year: 'numeric',
                                month: 'long',
                                day: 'numeric'
                            })}
                            </p>
                            <p className="font-semibold text-gray-900">
                                🕒 {booking.appointmentTime}
                            </p>
                        </div>
                    </div>

                    <div className="bg-yellow-50 border border-yellow-200 p-4 rounded-lg mb-6 text-left">
                        <h4 className="font-semibold text-yellow-900 mb-2">📋 What to Bring:</h4>
                        <ul className="text-sm text-yellow-800 space-y-1">
                            {booking.service.requiredDocuments?.map((doc, index) => (
                                <li key={index}>• {doc}</li>
                            ))}
                        </ul>
                    </div>

                    <div className="flex flex-col sm:flex-row gap-3">
                        <Link to="/" className="btn-primary flex-1">
                            Back to Home
                        </Link>
                        <Link to="/check-booking" className="btn-outline flex-1">
                            Check Booking Status
                        </Link>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default BookingSuccessPage;