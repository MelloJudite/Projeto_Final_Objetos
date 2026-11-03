
import { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';

export default function Login() {
    const navigate = useNavigate();
    const location = useLocation();

    const [perfil, setPerfil] = useState(
        location.state?.perfil || 'CLIENTE'
    );

    const [email, setEmail] = useState('');
    const [senha, setSenha] = useState('');
    const [erro, setErro] = useState('');
    const [carregando, setCarregando] = useState(false);

    async function fazerLogin(event) {
        event.preventDefault();
        setErro('');
        setCarregando(true);

        try {
            const endpoint = perfil === 'PROFISSIONAL'
                ? '/api/profissionais/login'
                : '/api/clientes/login';

            const response = await fetch(endpoint, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    email: email.trim(),
                    senha
                })
            });

            if (!response.ok) {
                if (response.status === 401) {
                    throw new Error('Email ou senha incorretos.');
                }

                throw new Error(
                    `Não foi possível realizar o login. Erro ${response.status}.`
                );
            }

            const dados = await response.json();

            if (!dados.token) {
                throw new Error('O servidor não retornou um token.');
            }

            sessionStorage.setItem('token', dados.token);
            sessionStorage.setItem(
                'perfil',
                dados.perfil || perfil
            );

            const destino = location.state?.from
                ? location.state.from.pathname +
                location.state.from.search
                : perfil === 'PROFISSIONAL'
                    ? '/profissional/cadastrar-servico'
                    : '/';

            navigate(destino, { replace: true });

        } catch (error) {
            setErro(error.message);
        } finally {
            setCarregando(false);
        }
    }

    return (
        <main style={estilos.pagina}>

            <div style={estilos.card}>

                <h1 style={estilos.titulo}>
                    Bem-vinda à Lótus Beauty!
                </h1>

                <p style={estilos.subtitulo}>
                    Entre na sua conta para continuar.
                </p>

                <form onSubmit={fazerLogin}>

                    <label style={estilos.label}>
                        Entrar como
                    </label>

                    <div style={estilos.perfis}>

                        <button
                            type="button"
                            onClick={() => {
                                setPerfil('CLIENTE');
                                setErro('');
                            }}
                            style={{
                                ...estilos.perfil,
                                ...(perfil === 'CLIENTE'
                                    ? estilos.selecionado
                                    : {})
                            }}
                        >
                            <strong>♡ Cliente</strong>
                        </button>

                        <button
                            type="button"
                            onClick={() => {
                                setPerfil('PROFISSIONAL');
                                setErro('');
                            }}
                            style={{
                                ...estilos.perfil,
                                ...(perfil === 'PROFISSIONAL'
                                    ? estilos.selecionado
                                    : {})
                            }}
                        >
                            <strong>✿ Profissional</strong>
                        </button>

                    </div>

                    <label style={estilos.label} htmlFor="email">
                        Email
                    </label>

                    <input
                        id="email"
                        type="email"
                        placeholder="seuemail@exemplo.com"
                        value={email}
                        onChange={e => setEmail(e.target.value)}
                        required
                        style={estilos.input}
                    />

                    <label style={estilos.label} htmlFor="senha">
                        Senha
                    </label>

                    <input
                        id="senha"
                        type="password"
                        placeholder="Digite sua senha"
                        value={senha}
                        onChange={e => setSenha(e.target.value)}
                        required
                        style={estilos.input}
                    />

                    {erro && (
                        <p role="alert" style={estilos.erro}>
                            {erro}
                        </p>
                    )}

                    <button
                        type="submit"
                        disabled={carregando}
                        style={{
                            ...estilos.botao,
                            opacity: carregando ? 0.6 : 1
                        }}
                    >
                        {carregando ? 'Entrando...' : 'Entrar'}
                    </button>

                </form>

                <p style={estilos.rodape}>
                    Ainda não tem conta?{' '}
                    <Link to="/cadastro" style={estilos.link}>
                        Cadastre-se
                    </Link>
                </p>

            </div>

        </main>
    );
}

const estilos = {
    pagina: {
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: '#F9F6F2',
        padding: '30px 20px',
        boxSizing: 'border-box'
    },

    card: {
        backgroundColor: '#FFFFFF',
        padding: '40px',
        borderRadius: '16px',
        width: '100%',
        maxWidth: '450px',
        boxShadow: '0 8px 30px rgba(60, 0, 75, 0.08)',
        boxSizing: 'border-box'
    },

    titulo: {
        color: '#3C004B',
        textAlign: 'center',
        fontSize: '27px',
        marginBottom: '10px'
    },

    subtitulo: {
        textAlign: 'center',
        color: '#775890',
        marginBottom: '30px'
    },

    label: {
        display: 'block',
        color: '#503451',
        fontWeight: '600',
        fontSize: '14px',
        marginBottom: '9px'
    },

    perfis: {
        display: 'grid',
        gridTemplateColumns: '1fr 1fr',
        gap: '12px',
        marginBottom: '25px'
    },

    perfil: {
        padding: '15px 8px',
        border: '1px solid #E5D8E2',
        borderRadius: '9px',
        background: '#FFFFFF',
        color: '#503451',
        cursor: 'pointer'
    },

    selecionado: {
        border: '2px solid #A45B70',
        background: '#F9EEF2',
        color: '#9C4C53'
    },

    input: {
        display: 'block',
        width: '100%',
        padding: '12px',
        marginTop: '8px',
        marginBottom: '20px',
        border: '1px solid #FFBED1',
        borderRadius: '8px',
        fontSize: '15px',
        boxSizing: 'border-box'
    },

    botao: {
        width: '100%',
        padding: '14px',
        backgroundColor: '#9C4C53',
        color: '#FFFFFF',
        border: 'none',
        borderRadius: '8px',
        cursor: 'pointer',
        fontSize: '16px',
        fontWeight: 'bold',
        marginTop: '12px'
    },

    erro: {
        color: '#9C4C53',
        backgroundColor: '#FFBED1',
        padding: '12px',
        borderRadius: '8px',
        fontSize: '14px'
    },

    rodape: {
        textAlign: 'center',
        marginTop: '24px',
        color: '#503451'
    },

    link: {
        color: '#775890',
        fontWeight: 'bold',
        textDecoration: 'none'
    }
};
