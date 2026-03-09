import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { bookingService } from '../../api/bookingService';
import type {Province, ServiceType, BookingRequest} from '../../types';
import LoadingSpinner from '../../components/common/LoadingSpinner';

const BookingPage = () => {
    const [step, setStep] = useState(1);
    const [provinces, setProvinces] = useState<Province[]>([]);
    const [services, setServices] = useState<ServiceType[]>([]);
    const [availableSlots, setAvailableSlots] = useState<string[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const [formData, setFormData] = useState<BookingRequest>({
        fullName: '',
        dateOfBirth: '',
        phoneNumber: '',
        email: '',
        provinceId: 0,
        serviceId: 0,
        appointmentDate: '',
        appointmentTime: '',
        channel: 'WEB',
        notes: '',
    });

    const navigate = useNavigate();

    useEffect(() => {
        fetchProvinces();
        fetchServices();
    }, []);

    const fetchProvinces = async () => {
        try {
            const data = await bookingService.getActiveProvinces();
            setProvinces(data);
        } catch (err) {
            setError('Failed to load provinces');
        }
    };

    const fetchServices = async () => {
        try {
            const data = await bookingService.getActiveServices();
            setServices(data);
        } catch (err) {
            setError('Failed to load services');
        }
    };

    const fetchAvailableSlots = async (provinceId: number, date: string) => {
        try {
            setLoading(true);
            const slots = await bookingService.getAvailableSlots(provinceId, date);
            setAvailableSlots(slots);
        } catch (err) {
            setError('Failed to load available time slots');
        } finally {
            setLoading(false);
        }
    };

    const handleProvinceSelect = (provinceId: number) => {
        setFormData({ ...formData, provinceId });
        setStep(2);
    };

    const handleServiceSelect = (serviceId: number) => {
        setFormData({ ...formData, serviceId });
        setStep(3);
    };

    const handleDateSelect = (date: string) => {
        setFormData({ ...formData, appointmentDate: date });
        fetchAvailableSlots(formData.provinceId, date);
        setStep(4);
    };

    const handleTimeSelect = (time: string) => {
        setFormData({ ...formData, appointmentTime: time });
        setStep(5);
    };

    const handlePersonalInfoSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        setStep(6);
    };

    const handleConfirmBooking = async () => {
        setLoading(true);
        setError('');

        try {
            const booking = await bookingService.createBooking(formData);
            navigate('/booking-success', { state: { booking } });
        } catch (err: any) {
            setError(err.response?.data?.message || 'Failed to create booking. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    const selectedProvince = provinces.find(p => p.id === formData.provinceId);
    const selectedService = services.find(s => s.id === formData.serviceId);

    return (
        <div className="min-h-screen bg-gradient-to-br from-primary-50 to-primary-100 py-12 px-4">
            <div className="max-w-4xl mx-auto">
                {/* Progress Bar */}
                <div className="mb-8">
                    <div className="flex items-center justify-between">
                        {['Province', 'Service', 'Date', 'Time', 'Details', 'Confirm'].map((label, index) => (
                            <div key={label} className="flex items-center">
                                <div
                                    className={`w-10 h-10 rounded-full flex items-center justify-center font-semibold ${
                                        step > index + 1
                                            ? 'bg-primary-600 text-white'
                                            : step === index + 1
                                                ? 'bg-primary-600 text-white'
                                                : 'bg-gray-300 text-gray-600'
                                    }`}
                                >
                                    {index + 1}
                                </div>
                                {index < 5 && (
                                    <div
                                        className={`w-12 h-1 ${
                                            step > index + 1 ? 'bg-primary-600' : 'bg-gray-300'
                                        }`}
                                    />
                                )}
                            </div>
                        ))}
                    </div>
                    <div className="flex justify-between mt-2">
                        {['Province', 'Service', 'Date', 'Time', 'Details', 'Confirm'].map((label) => (
                            <span key={label} className="text-xs text-gray-600">{label}</span>
                        ))}
                    </div>
                </div>

                {error && (
                    <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg mb-6">
                        {error}
                    </div>
                )}

                {/* Step 1: Select Province */}
                {step === 1 && (
                    <div className="card">
                        <h2 className="text-2xl font-bold text-gray-900 mb-6">Select Province</h2>
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            {provinces.map((province) => (
                                <button
                                    key={province.id}
                                    onClick={() => handleProvinceSelect(province.id)}
                                    className="text-left p-4 border-2 border-gray-200 rounded-lg hover:border-primary-600 hover:bg-primary-50 transition-all"
                                >
                                    <h3 className="font-semibold text-gray-900">{province.name}</h3>
                                    <p className="text-sm text-gray-600 mt-1">{province.officeName}</p>
                                    <p className="text-xs text-gray-500 mt-2">📍 {province.address}</p>
                                </button>
                            ))}
                        </div>
                    </div>
                )}

                {/* Step 2: Select Service */}
                {step === 2 && (
                    <div className="card">
                        <button onClick={() => setStep(1)} className="text-primary-600 mb-4">
                            ← Back
                        </button>
                        <h2 className="text-2xl font-bold text-gray-900 mb-6">Select Service</h2>
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            {services.map((service) => (
                                <button
                                    key={service.id}
                                    onClick={() => handleServiceSelect(service.id)}
                                    className="text-left p-6 border-2 border-gray-200 rounded-lg hover:border-primary-600 hover:bg-primary-50 transition-all"
                                >
                                    <h3 className="font-semibold text-gray-900 text-lg">{service.name}</h3>
                                    <p className="text-sm text-gray-600 mt-2">{service.description}</p>
                                    <div className="flex items-center justify-between mt-4">
                    <span className="text-2xl font-bold text-primary-600">
                      ${service.fee}
                    </span>
                                        <span className="text-sm text-gray-500">
                      {service.durationMinutes} minutes
                    </span>
                                    </div>
                                </button>
                            ))}
                        </div>
                    </div>
                )}

                {/* Step 3: Select Date */}
                {step === 3 && (
                    <div className="card">
                        <button onClick={() => setStep(2)} className="text-primary-600 mb-4">
                            ← Back
                        </button>
                        <h2 className="text-2xl font-bold text-gray-900 mb-6">Select Date</h2>
                        <div className="max-w-md mx-auto">
                            <input
                                type="date"
                                className="input-field text-lg"
                                min={new Date().toISOString().split('T')[0]}
                                onChange={(e) => handleDateSelect(e.target.value)}
                            />
                            <p className="text-sm text-gray-600 mt-4">
                                Select a date for your appointment. Bookings can be made up to 90 days in advance.
                            </p>
                        </div>
                    </div>
                )}

                {/* Step 4: Select Time */}
                {step === 4 && (
                    <div className="card">
                        <button onClick={() => setStep(3)} className="text-primary-600 mb-4">
                            ← Back
                        </button>
                        <h2 className="text-2xl font-bold text-gray-900 mb-6">Select Time Slot</h2>
                        {loading ? (
                            <LoadingSpinner text="Loading available slots..." />
                        ) : availableSlots.length > 0 ? (
                            <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
                                {availableSlots.map((slot) => (
                                    <button
                                        key={slot}
                                        onClick={() => handleTimeSelect(slot)}
                                        className="p-4 border-2 border-gray-200 rounded-lg hover:border-primary-600 hover:bg-primary-50 font-semibold transition-all"
                                    >
                                        {slot}
                                    </button>
                                ))}
                            </div>
                        ) : (
                            <p className="text-center text-gray-600">No available slots for this date.</p>
                        )}
                    </div>
                )}

                {/* Step 5: Personal Information */}
                {step === 5 && (
                    <div className="card">
                        <button onClick={() => setStep(4)} className="text-primary-600 mb-4">
                            ← Back
                        </button>
                        <h2 className="text-2xl font-bold text-gray-900 mb-6">Personal Information</h2>
                        <form onSubmit={handlePersonalInfoSubmit} className="space-y-4">
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-2">
                                    Full Name *
                                </label>
                                <input
                                    type="text"
                                    value={formData.fullName}
                                    onChange={(e) => setFormData({ ...formData, fullName: e.target.value })}
                                    className="input-field"
                                    required
                                />
                            </div>

                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-2">
                                    Date of Birth *
                                </label>
                                <input
                                    type="date"
                                    value={formData.dateOfBirth}
                                    onChange={(e) => setFormData({ ...formData, dateOfBirth: e.target.value })}
                                    className="input-field"
                                    required
                                />
                            </div>

                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-2">
                                    Phone Number *
                                </label>
                                <input
                                    type="tel"
                                    value={formData.phoneNumber}
                                    onChange={(e) => setFormData({ ...formData, phoneNumber: e.target.value })}
                                    className="input-field"
                                    placeholder="+263771234567"
                                    required
                                />
                            </div>

                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-2">
                                    Email Address *
                                </label>
                                <input
                                    type="email"
                                    value={formData.email}
                                    onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                                    className="input-field"
                                    required
                                />
                            </div>

                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-2">
                                    Additional Notes (Optional)
                                </label>
                                <textarea
                                    value={formData.notes}
                                    onChange={(e) => setFormData({ ...formData, notes: e.target.value })}
                                    className="input-field"
                                    rows={3}
                                />
                            </div>

                            <button type="submit" className="btn-primary w-full">
                                Continue to Confirmation
                            </button>
                        </form>
                    </div>
                )}

                {/* Step 6: Confirm Booking */}
                {step === 6 && (
                    <div className="card">
                        <button onClick={() => setStep(5)} className="text-primary-600 mb-4">
                            ← Back
                        </button>
                        <h2 className="text-2xl font-bold text-gray-900 mb-6">Confirm Booking</h2>

                        <div className="space-y-4 mb-6">
                            <div className="bg-gray-50 p-4 rounded-lg">
                                <h3 className="font-semibold text-gray-700 mb-2">Appointment Details</h3>
                                <p className="text-gray-900"><strong>Office:</strong> {selectedProvince?.name}</p>
                                <p className="text-gray-600 text-sm">{selectedProvince?.officeName}</p>
                                <p className="text-gray-600 text-sm">{selectedProvince?.address}</p>
                            </div>

                            <div className="bg-gray-50 p-4 rounded-lg">
                                <h3 className="font-semibold text-gray-700 mb-2">Service</h3>
                                <p className="text-gray-900"><strong>{selectedService?.name}</strong></p>
                                <p className="text-gray-600 text-sm">{selectedService?.description}</p>
                                <p className="text-primary-600 font-bold mt-2">${selectedService?.fee}</p>
                            </div>

                            <div className="bg-gray-50 p-4 rounded-lg">
                                <h3 className="font-semibold text-gray-700 mb-2">Date & Time</h3>
                                <p className="text-gray-900">
                                    📅 {new Date(formData.appointmentDate).toLocaleDateString('en-ZW', {
                                    weekday: 'long',
                                    year: 'numeric',
                                    month: 'long',
                                    day: 'numeric'
                                })}
                                </p>
                                <p className="text-gray-900">🕒 {formData.appointmentTime}</p>
                            </div>

                            <div className="bg-gray-50 p-4 rounded-lg">
                                <h3 className="font-semibold text-gray-700 mb-2">Your Information</h3>
                                <p className="text-gray-900"><strong>Name:</strong> {formData.fullName}</p>
                                <p className="text-gray-900"><strong>DOB:</strong> {formData.dateOfBirth}</p>
                                <p className="text-gray-900"><strong>Phone:</strong> {formData.phoneNumber}</p>
                                <p className="text-gray-900"><strong>Email:</strong> {formData.email}</p>
                            </div>
                        </div>

                        <div className="bg-yellow-50 border border-yellow-200 p-4 rounded-lg mb-6">
                            <h4 className="font-semibold text-yellow-900 mb-2">⚠️ Important Reminders:</h4>
                            <ul className="text-sm text-yellow-800 space-y-1">
                                <li>• Arrive 15 minutes before your appointment</li>
                                <li>• Bring all required documents</li>
                                <li>• Payment will be made at the office</li>
                                <li>• Bring this confirmation (SMS/Email)</li>
                            </ul>
                        </div>

                        <button
                            onClick={handleConfirmBooking}
                            disabled={loading}
                            className="btn-primary w-full text-lg"
                        >
                            {loading ? 'Confirming Booking...' : 'Confirm Booking'}
                        </button>
                    </div>
                )}
            </div>
        </div>
    );
};

export default BookingPage;