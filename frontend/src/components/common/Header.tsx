import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext.tsx';

const Header = () => {
    const { isAuthenticated, user, adminUser, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/');
    };

    return (
        <header className="bg-white shadow-sm border-b border-gray-200">
            <nav className="container mx-auto px-4 py-4">
                <div className="flex justify-between items-center">
                    {/* Logo */}
                    <Link to="/" className="flex items-center gap-3">
                        <div className="w-10 h-10 bg-gradient-to-br from-zimbabwe-green to-primary-600 rounded-lg flex items-center justify-center">
                            <span className="text-white font-bold text-xl">Z</span>
                        </div>
                        <div>
                            <h1 className="text-xl font-bold text-gray-900">ZimID Booking</h1>
                            <p className="text-xs text-gray-500">Registrar General</p>
                        </div>
                    </Link>

                    {/* Navigation */}
                    <div className="flex items-center gap-6">
                        {!isAuthenticated && !adminUser && (
                            <>
                                <Link to="/booking" className="text-gray-700 hover:text-primary-600 font-medium transition-colors">
                                    Book Appointment
                                </Link>
                                <Link to="/check-booking" className="text-gray-700 hover:text-primary-600 font-medium transition-colors">
                                    Check Booking
                                </Link>
                                <Link to="/login" className="text-gray-700 hover:text-primary-600 font-medium transition-colors">
                                    Login
                                </Link>
                                <Link to="/register" className="btn-primary">
                                    Register
                                </Link>
                            </>
                        )}

                        {isAuthenticated && user && (
                            <>
                                <Link to="/dashboard" className="text-gray-700 hover:text-primary-600 font-medium transition-colors">
                                    My Bookings
                                </Link>
                                <div className="flex items-center gap-3">
                                    <div className="text-right">
                                        <p className="text-sm font-medium text-gray-900">{user.fullName}</p>
                                        <p className="text-xs text-gray-500">{user.email}</p>
                                    </div>
                                    <button onClick={handleLogout} className="btn-secondary text-sm">
                                        Logout
                                    </button>
                                </div>
                            </>
                        )}

                        {adminUser && (
                            <>
                                <Link to="/admin/dashboard" className="text-gray-700 hover:text-primary-600 font-medium transition-colors">
                                    Dashboard
                                </Link>
                                <div className="flex items-center gap-3">
                                    <div className="text-right">
                                        <p className="text-sm font-medium text-gray-900">{adminUser.fullName}</p>
                                        <p className="text-xs text-gray-500 badge badge-info">{adminUser.role}</p>
                                    </div>
                                    <button onClick={handleLogout} className="btn-secondary text-sm">
                                        Logout
                                    </button>
                                </div>
                            </>
                        )}
                    </div>
                </div>
            </nav>
        </header>
    );
};

export default Header;