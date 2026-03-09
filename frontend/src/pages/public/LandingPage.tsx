import { Link } from 'react-router-dom';

const LandingPage = () => {
    return (
        <div className="bg-gradient-to-br from-primary-50 to-primary-100">
            {/* Hero Section */}
            <section className="container mx-auto px-4 py-16 md:py-24">
                <div className="max-w-4xl mx-auto text-center">
                    <div className="mb-6">
            <span className="inline-block px-4 py-2 bg-primary-100 text-primary-800 rounded-full text-sm font-semibold mb-4">
              🇿🇼 Government of Zimbabwe
            </span>
                    </div>
                    <h1 className="text-4xl md:text-6xl font-bold text-gray-900 mb-6">
                        Book Your National ID Appointment Online
                    </h1>
                    <p className="text-xl text-gray-700 mb-8 max-w-2xl mx-auto">
                        Skip the queues and book your National ID appointment from anywhere, anytime.
                        Fast, convenient, and secure.
                    </p>
                    <div className="flex flex-col sm:flex-row gap-4 justify-center">
                        <Link to="/booking" className="btn-primary text-lg px-8 py-4">
                            📅 Book Appointment Now
                        </Link>
                        <Link to="/check-booking" className="btn-outline text-lg px-8 py-4">
                            🔍 Check Booking Status
                        </Link>
                    </div>
                </div>
            </section>

            {/* Features Section */}
            <section className="bg-white py-16">
                <div className="container mx-auto px-4">
                    <h2 className="text-3xl font-bold text-center text-gray-900 mb-12">
                        Why Book Online?
                    </h2>
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-8 max-w-5xl mx-auto">
                        <div className="card text-center">
                            <div className="w-16 h-16 bg-primary-100 rounded-full flex items-center justify-center mx-auto mb-4">
                                <span className="text-3xl">⏰</span>
                            </div>
                            <h3 className="text-xl font-semibold text-gray-900 mb-2">Save Time</h3>
                            <p className="text-gray-600">
                                No more long queues. Book your slot in advance and arrive at your scheduled time.
                            </p>
                        </div>

                        <div className="card text-center">
                            <div className="w-16 h-16 bg-primary-100 rounded-full flex items-center justify-center mx-auto mb-4">
                                <span className="text-3xl">📱</span>
                            </div>
                            <h3 className="text-xl font-semibold text-gray-900 mb-2">Book Anywhere</h3>
                            <p className="text-gray-600">
                                Use our website or dial *384*7001# from any phone to book your appointment.
                            </p>
                        </div>

                        <div className="card text-center">
                            <div className="w-16 h-16 bg-primary-100 rounded-full flex items-center justify-center mx-auto mb-4">
                                <span className="text-3xl">✅</span>
                            </div>
                            <h3 className="text-xl font-semibold text-gray-900 mb-2">Instant Confirmation</h3>
                            <p className="text-gray-600">
                                Get SMS and email confirmation immediately after booking your appointment.
                            </p>
                        </div>
                    </div>
                </div>
            </section>

            {/* Services Section */}
            <section className="bg-gray-50 py-16">
                <div className="container mx-auto px-4">
                    <h2 className="text-3xl font-bold text-center text-gray-900 mb-12">
                        Available Services
                    </h2>
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 max-w-6xl mx-auto">
                        <div className="bg-white border-2 border-green-200 rounded-lg p-6 hover:shadow-lg transition-shadow">
                            <div className="text-4xl mb-3">🆕</div>
                            <h3 className="text-lg font-semibold text-gray-900 mb-2">New National ID</h3>
                            <p className="text-gray-600 text-sm mb-3">First time ID application</p>
                            <p className="text-2xl font-bold text-primary-600">$5.00</p>
                        </div>

                        <div className="bg-white border-2 border-orange-200 rounded-lg p-6 hover:shadow-lg transition-shadow">
                            <div className="text-4xl mb-3">🔄</div>
                            <h3 className="text-lg font-semibold text-gray-900 mb-2">ID Replacement</h3>
                            <p className="text-gray-600 text-sm mb-3">Lost or damaged ID</p>
                            <p className="text-2xl font-bold text-orange-600">$10.00</p>
                        </div>

                        <div className="bg-white border-2 border-blue-200 rounded-lg p-6 hover:shadow-lg transition-shadow">
                            <div className="text-4xl mb-3">♻️</div>
                            <h3 className="text-lg font-semibold text-gray-900 mb-2">ID Renewal</h3>
                            <p className="text-gray-600 text-sm mb-3">Update or correct details</p>
                            <p className="text-2xl font-bold text-blue-600">$10.00</p>
                        </div>

                        <div className="bg-white border-2 border-purple-200 rounded-lg p-6 hover:shadow-lg transition-shadow">
                            <div className="text-4xl mb-3">⚡</div>
                            <h3 className="text-lg font-semibold text-gray-900 mb-2">Emergency ID</h3>
                            <p className="text-gray-600 text-sm mb-3">Fast track processing</p>
                            <p className="text-2xl font-bold text-purple-600">$25.00</p>
                        </div>
                    </div>
                </div>
            </section>

            {/* CTA Section */}
            <section className="bg-gradient-to-r from-primary-600 to-primary-800 text-white py-16">
                <div className="container mx-auto px-4 text-center">
                    <h2 className="text-3xl md:text-4xl font-bold mb-6">
                        Ready to Book Your Appointment?
                    </h2>
                    <p className="text-xl mb-8 max-w-2xl mx-auto opacity-90">
                        Join thousands of Zimbabweans who have already booked their ID appointments online.
                    </p>
                    <Link to="/booking" className="inline-block bg-white text-primary-600 font-semibold px-8 py-4 rounded-lg hover:bg-gray-100 transition-colors text-lg">
                        Get Started Now →
                    </Link>
                </div>
            </section>
        </div>
    );
};

export default LandingPage;