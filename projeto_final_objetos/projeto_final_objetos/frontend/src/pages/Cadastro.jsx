
import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';

export default function Cadastro() {
    const navigate = useNavigate();

    const [perfil, setPerfil] = useState('CLIENTE');
    const [nome, setNome] = useState('');
    const [telefone, setTelefone] = useState('');
    const [email, setEmail] = useState('');
    const [senha, setSenha] = useState('');
    const [erro, setErro] = useState('');
    const [carregando, setCarregando] = useState(false);

    async function cadastrar(event) {
        event.preventDefault();
        setErro('');

        setCarregando(true);

        try {
            const profissional = perfil === 'PROFISSIONAL';

            const endereco = profissional
                ? 'https://spa-beleza.onrender.com/profissionais'
                : 'https://spa-beleza.onrender.com/clientes';

            const dados = {
                nome: nome.trim(),
                telefone: telefone.trim(),
                email: email.trim(),
                senha
            };


            const response = await fetch(endereco, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(dados)
            });

            if (!response.ok) {
                const mensagem = await response.text();

                if (response.status === 409) {
                    throw new Error('Este email já está cadastrado.');
                }

                throw new Error(
                    mensagem || 'Não foi possível criar sua conta.'
                );
            }

            alert('Conta criada com sucesso! Faça login para continuar.');

            navigate('/login', {
                state: { perfil }
            });

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
                    Crie sua conta
                </h1>

                <p style={estilos.subtitulo}>
                    Comece sua experiência de beleza e bem-estar.
                </p>

                <form onSubmit={cadastrar}>

                    <label style={estilos.label}>
                        Como deseja utilizar a plataforma?
                    </label>

                    <div style={estilos.perfis}>

                        <button
                            type="button"
                            onClick={() => setPerfil('CLIENTE')}
                            style={{
                                ...estilos.perfil,
                                ...(perfil === 'CLIENTE'
                                    ? estilos.perfilSelecionado
                                    : {})
                            }}
                        >
                            <span style={estilos.icone}>♡</span>
                            <strong>Sou cliente</strong>
                            <small>Quero agendar serviços</small>
                        </button>

                        <button
                            type="button"
                            onClick={() => setPerfil('PROFISSIONAL')}
                            style={{
                                ...estilos.perfil,
                                ...(perfil === 'PROFISSIONAL'
                                    ? estilos.perfilSelecionado
                                    : {})
                            }}
                        >
                            <span style={estilos.icone}>✿</span>
                            <strong>Sou profissional</strong>
                            <small>Quero oferecer serviços</small>
                        </button>

                    </div>

                    <label style={estilos.label} htmlFor="nome">
                        Nome completo
                    </label>

                    <input
                        id="nome"
                        type="text"
                        placeholder="Digite seu nome completo"
                        value={nome}
                        onChange={e => setNome(e.target.value)}
                        required
                        style={estilos.input}
                    />

                    <label style={estilos.label} htmlFor="telefone">
                        Telefone
                    </label>

                    <input
                        id="telefone"
                        type="tel"
                        placeholder="(00) 00000-0000"
                        value={telefone}
                        onChange={e => setTelefone(e.target.value)}
                        maxLength={20}
                        required
                        style={estilos.input}
                    />

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
                        placeholder="Crie uma senha"
                        value={senha}
                        onChange={e => setSenha(e.target.value)}
                        minLength={8}
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
                        {carregando
                            ? 'Cadastrando...'
                            : 'Criar conta'}
                    </button>

                </form>

                <p style={estilos.rodape}>
                    Já tem uma conta?{' '}
                    <Link to="/login" style={estilos.link}>
                        Entrar
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
        padding: '40px 20px',
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
        fontSize: '28px',
        marginBottom: '8px'
    },

    subtitulo: {
        textAlign: 'center',
        color: '#775890',
        marginBottom: '30px',
        lineHeight: '1.6'
    },

    label: {
        display: 'block',
        color: '#503451',
        fontWeight: '600',
        fontSize: '14px',
        marginBottom: '10px'
    },

    perfis: {
        display: 'grid',
        gridTemplateColumns: '1fr 1fr',
        gap: '12px',
        marginBottom: '25px'
    },

    perfil: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        gap: '8px',
        padding: '17px 8px',
        background: '#FFFFFF',
        border: '1px solid #E5D8E2',
        borderRadius: '10px',
        color: '#503451',
        cursor: 'pointer'
    },

    perfilSelecionado: {
        background: '#F9EEF2',
        border: '2px solid #A45B70',
        color: '#9C4C53'
    },

    icone: {
        fontSize: '27px'
    },

    input: {
        display: 'block',
        width: '100%',
        padding: '12px',
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
        fontSize: '16px',
        fontWeight: 'bold',
        cursor: 'pointer',
        marginTop: '12px'
    },

    erro: {
        color: '#9C4C53',
        backgroundColor: '#FFBED1',
        padding: '12px',
        borderRadius: '8px'
    },

    rodape: {
        textAlign: 'center',
        marginTop: '24px',
        color: '#503451',
        fontSize: '14px'
    },

    link: {
        color: '#775890',
        fontWeight: 'bold',
        textDecoration: 'none'
    }
};
