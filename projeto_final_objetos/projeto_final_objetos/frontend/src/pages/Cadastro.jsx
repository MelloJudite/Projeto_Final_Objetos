
import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';

export default function Cadastro() {
    const navigate = useNavigate();

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
            const response = await fetch('/api/clientes', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    nome: nome.trim(),
                    telefone: telefone.trim(),
                    email: email.trim(),
                    senha: senha
                })
            });

            if (!response.ok) {
                const respostaErro = await response.text();

                console.error('Erro no cadastro:', {
                    status: response.status,
                    resposta: respostaErro
                });

                if (response.status === 409) {
                    throw new Error('Este email já está cadastrado.');
                }

                if (response.status === 400) {
                    throw new Error(
                        'Dados inválidos. Confira nome, telefone, email e senha.'
                    );
                }

                throw new Error('Não foi possível criar sua conta.');
            }

            alert('Conta criada com sucesso!');
            navigate('/login');

        } catch (error) {
            setErro(error.message);
        } finally {
            setCarregando(false);
        }
    }

    return (
        <main className="auth-page">
            <div className="auth-card">
                <h1>Crie sua conta</h1>
                <p>Comece sua experiência de beleza e bem-estar.</p>

                <form onSubmit={cadastrar}>
                    <label htmlFor="nome">Nome completo</label>
                    <input
                        id="nome"
                        value={nome}
                        onChange={(e) => setNome(e.target.value)}
                        required
                    />

                    <label htmlFor="telefone">Telefone</label>
                    <input
                        id="telefone"
                        type="tel"
                        value={telefone}
                        onChange={(e) => setTelefone(e.target.value)}
                        maxLength={20}
                        required
                    />

                    <label htmlFor="email">Email</label>
                    <input
                        id="email"
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />

                    <label htmlFor="senha">Senha</label>
                    <input
                        id="senha"
                        type="password"
                        value={senha}
                        onChange={(e) => setSenha(e.target.value)}
                        required
                    />

                    {erro && <p role="alert" className="auth-error">{erro}</p>}

                    <button type="submit" disabled={carregando}>
                        {carregando ? 'Cadastrando...' : 'Criar conta'}
                    </button>
                </form>

                <p>
                    Já tem uma conta? <Link to="/login">Entrar</Link>
                </p>
            </div>
        </main>
    );
}
