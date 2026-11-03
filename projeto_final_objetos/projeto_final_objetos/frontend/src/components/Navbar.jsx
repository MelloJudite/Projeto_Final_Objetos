
import { Link, useLocation, useNavigate } from 'react-router-dom';

export default function Navbar() {
    const location = useLocation();
    const navigate = useNavigate();

    const token = sessionStorage.getItem('token');
    const perfil = sessionStorage.getItem('perfil');

    function sair() {
        sessionStorage.removeItem('token');
        sessionStorage.removeItem('perfil');
        sessionStorage.removeItem('spa_carrinho');
        navigate('/');
    }

    return (
        <header className="navbar">

            <Link to="/" className="logo">
                <span>✿</span> Nosso Momento
            </Link>

            <nav className="nav-links">

                <Link to="/">Início</Link>

                <Link to="/servicos">Serviços</Link>

                {!token && (
                    <>
                        <Link to="/cadastro">Cadastrar</Link>
                        <Link to="/login" className="login-button">
                            Entrar
                        </Link>
                    </>
                )}

                {token && perfil === 'CLIENTE' && (
                    <>
                        <Link to="/meus-agendamentos">
                            Meus agendamentos
                        </Link>

                        <button onClick={sair} className="nav-sair">
                            Sair
                        </button>
                    </>
                )}

                {token && perfil === 'PROFISSIONAL' && (
                    <>
                        <Link to="/profissional/agenda">
                            Minha agenda
                        </Link>

                        <Link to="/profissional/servicos/novo">
                            Cadastrar serviço
                        </Link>

                        <button onClick={sair} className="nav-sair">
                            Sair
                        </button>
                    </>
                )}

            </nav>
        </header>
    );
}
