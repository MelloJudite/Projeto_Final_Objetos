import { Navigate, Outlet, useLocation } from 'react-router-dom';

export default function RotaProtegidaProfissional() {
    const location = useLocation();

    const token = sessionStorage.getItem('token');
    const perfil = sessionStorage.getItem('perfil');

    if (!token) {
        return (
            <Navigate
                to="/login"
                state={{ from: location }}
                replace
            />
        );
    }

    if (perfil !== 'PROFISSIONAL') {
        return <Navigate to="/" replace />;
    }

    return <Outlet />;
}