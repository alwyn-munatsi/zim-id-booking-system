import { createContext, useContext, useState, useEffect, type ReactNode } from 'react';
import { authService } from '../api/authService';
import type {User, AdminUser} from '../types';

interface AuthContextType {
    user: User | null;
    adminUser: AdminUser | null;
    token: string | null;
    isAuthenticated: boolean;
    isAdmin: boolean;
    loading: boolean;
    login: (email: string, password: string) => Promise<void>;
    adminLogin: (username: string, password: string) => Promise<void>;
    register: (data: any) => Promise<void>;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const [user, setUser] = useState<User | null>(null);
    const [adminUser, setAdminUser] = useState<AdminUser | null>(null);
    const [token, setToken] = useState<string | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        // Check for existing token on mount
        const storedToken = localStorage.getItem('token');
        const storedUser = localStorage.getItem('user');
        const storedAdminUser = localStorage.getItem('adminUser');

        if (storedToken) {
            setToken(storedToken);
            if (storedUser) {
                setUser(JSON.parse(storedUser));
            }
            if (storedAdminUser) {
                setAdminUser(JSON.parse(storedAdminUser));
            }
        }
        setLoading(false);
    }, []);

    const login = async (email: string, password: string) => {
        const response = await authService.login({ email, password });
        setToken(response.token);
        setUser(response.user);
        localStorage.setItem('token', response.token);
        localStorage.setItem('user', JSON.stringify(response.user));
    };

    const adminLogin = async (username: string, password: string) => {
        const response = await authService.adminLogin({ username, password });
        setToken(response.token);
        setAdminUser(response.user);
        localStorage.setItem('token', response.token);
        localStorage.setItem('adminUser', JSON.stringify(response.user));
    };

    const register = async (data: any) => {
        const response = await authService.register(data);
        setToken(response.token);
        setUser(response.user);
        localStorage.setItem('token', response.token);
        localStorage.setItem('user', JSON.stringify(response.user));
    };

    const logout = () => {
        setUser(null);
        setAdminUser(null);
        setToken(null);
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        localStorage.removeItem('adminUser');
    };

    const value = {
        user,
        adminUser,
        token,
        isAuthenticated: !!token,
        isAdmin: !!adminUser,
        loading,
        login,
        adminLogin,
        register,
        logout,
    };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};
