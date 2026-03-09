import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './contexts/AuthContext.tsx';
import MainLayout from './layouts/MainLayout';

// Public Pages
import LandingPage from './pages/public/LandingPage';
import LoginPage from './pages/public/LoginPage';
import RegisterPage from './pages/public/RegisterPage';
import BookingPage from './pages/public/BookingPage';
import BookingSuccessPage from './pages/public/BookingSuccessPage';
import CheckBookingPage from './pages/public/CheckBookingPage';

// Admin Pages
import AdminLoginPage from './pages/admin/AdminLoginPage';
import AdminDashboardPage from './pages/admin/AdminDashboardPage';

// Protected Route Components
const AdminRoute = ({ children }: { children: React.ReactNode }) => {
    const { isAdmin, loading } = useAuth();

    if (loading) {
        return <div>Loading...</div>;
    }

    return isAdmin ? <>{children}</> : <Navigate to="/admin/login" />;
};

function AppRoutes() {
    return (
        <Router>
            <Routes>
                {/* Public Routes */}
                <Route path="/" element={<MainLayout><LandingPage /></MainLayout>} />
                <Route path="/login" element={<MainLayout><LoginPage /></MainLayout>} />
                <Route path="/register" element={<MainLayout><RegisterPage /></MainLayout>} />
                <Route path="/booking" element={<MainLayout><BookingPage /></MainLayout>} />
                <Route path="/booking-success" element={<MainLayout><BookingSuccessPage /></MainLayout>} />
                <Route path="/check-booking" element={<MainLayout><CheckBookingPage /></MainLayout>} />

                {/* Admin Routes */}
                <Route path="/admin/login" element={<AdminLoginPage />} />
                <Route
                    path="/admin/dashboard"
                    element={
                        <AdminRoute>
                            <AdminDashboardPage />
                        </AdminRoute>
                    }
                />

                {/* Fallback */}
                <Route path="*" element={<Navigate to="/" />} />
            </Routes>
        </Router>
    );
}

function App() {
    return (
        <AuthProvider>
            <AppRoutes />
        </AuthProvider>
    );
}

export default App;