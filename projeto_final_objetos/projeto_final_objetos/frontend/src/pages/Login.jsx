
import { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';

export default function Login() {
    const navigate = useNavigate();
    const location = useLocation();

    const [email, setEmail] = useState('');
    const [senha, setSenha] = useState('');
    const [erro, setErro] = useState('');
    const [carregando, setCarregando] = useState(false);

    async function fazerLogin(event) {
        event.preventDefault();
        setErro('');
        setCarregando(true);

        try {
            const response = await fetch('/api/clientes/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    email: email.trim(),
                    senha: senha
                })
            });

            if (!response.ok) {
                if (response.status === 401) {
                    throw new Error('Email ou senha incorretos.');
                }

                throw new Error('Não foi possível realizar o login.');
            }

            const dados = await response.json();

            if (!dados.token) {
                throw new Error('O servidor não retornou um token.');
            }

            sessionStorage.setItem('token', dados.token);
            sessionStorage.setItem('perfil', dados.perfil);

            const destino = location.state?.from
                ? location.state.from.pathname +
                location.state.from.search
                : '/';

            navigate(destino, { replace: true });

        } catch (error) {
            setErro(error.message);
        } finally {
            setCarregando(false);
        }
    }

    return (
        <main style={{
            minHeight: '100vh',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            backgroundColor: '#F9F6F2',
            padding: '24px'
        }}>
            <div style={{
                backgroundColor: '#FFFFFF',
                padding: '40px',
                borderRadius: '16px',
                width: '100%',
                maxWidth: '420px',
                boxShadow: '0 8px 30px rgba(60, 0, 75, 0.08)'
            }}>
                <h1 style={{
                    color: '#3C004B',
                    textAlign: 'center',
                    marginBottom: '8px'
                }}>
                    Bem-vinda ao SPA!
                </h1>

                <p style={{
                    textAlign: 'center',
                    color: '#775890',
                    marginBottom: '30px'
                }}>
                    Entre na sua conta para agendar seus serviços.
                </p>

                <form onSubmit={fazerLogin}>
                    <label htmlFor="email">Email</label>

                    <input
                        id="email"
                        type="email"
                        placeholder="seuemail@exemplo.com"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                        style={estiloInput}
                    />

                    <label htmlFor="senha">Senha</label>

                    <input
                        id="senha"
                        type="password"
                        placeholder="Digite sua senha"
                        value={senha}
                        onChange={(e) => setSenha(e.target.value)}
                        required
                        style={estiloInput}
                    />

                    {erro && (
                        <p style={{
                            color: '#9C4C53',
                            backgroundColor: '#FFBED1',
                            padding: '12px',
                            borderRadius: '8px'
                        }}>
                            {erro}
                        </p>
                    )}

                    <button
                        type="submit"
                        disabled={carregando}
                        style={{
                            width: '100%',
                            padding: '14px',
                            backgroundColor: '#9C4C53',
                            color: 'white',
                            border: 'none',
                            borderRadius: '8px',
                            cursor: carregando ? 'wait' : 'pointer',
                            fontSize: '16px',
                            marginTop: '12px'
                        }}
                    >
                        {carregando ? 'Entrando...' : 'Entrar'}
                    </button>
                </form>

                <p style={{
                    textAlign: 'center',
                    marginTop: '24px'
                }}>
                    Ainda não tem conta?{' '}
                    <Link
                        to="/cadastro"
                        state={{ from: location.state?.from }}
                        style={{
                            color: '#775890',
                            fontWeight: 'bold'
                        }}
                    >
                        Cadastre-se
                    </Link>
                </p>
            </div>
        </main>
    );
}

const estiloInput = {
    width: '100%',
    padding: '12px',
    marginTop: '8px',
    marginBottom: '20px',
    border: '1px solid #FFBED1',
    borderRadius: '8px',
    fontSize: '15px',
    boxSizing: 'border-box'
};
