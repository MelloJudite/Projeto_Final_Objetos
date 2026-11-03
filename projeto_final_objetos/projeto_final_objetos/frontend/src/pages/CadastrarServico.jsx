import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { cadastrarServico } from '../service/servicoService.js';

const categorias = [
    { value: 'CABELO', label: 'Cabelo' },
    { value: 'PELE', label: 'Pele' },
    { value: 'SOBRANCELHA', label: 'Sobrancelha' },
    { value: 'UNHA', label: 'Unhas' },
    { value: 'MASSAGEM', label: 'Massagem' },
    { value: 'MAQUIAGEM', label: 'Maquiagem' }
];

export default function CadastrarServico() {
    const navigate = useNavigate();

    const [nome, setNome] = useState('');
    const [descricao, setDescricao] = useState('');
    const [preco, setPreco] = useState('');
    const [tipo, setTipo] = useState('');
    const [erro, setErro] = useState('');
    const [sucesso, setSucesso] = useState('');
    const [carregando, setCarregando] = useState(false);

    async function cadastrar(event) {
        event.preventDefault();

        setErro('');
        setSucesso('');

        if (Number(preco) <= 0 || !Number.isFinite(Number(preco))) {
            setErro('Informe um preço válido.');
            return;
        }

        setCarregando(true);

        try {
            await cadastrarServico({
                nome: nome.trim(),
                descricao: descricao.trim(),
                preco: Number(preco),
                tipo
            });

            setSucesso('Serviço cadastrado com sucesso!');

            setNome('');
            setDescricao('');
            setPreco('');
            setTipo('');

        } catch (error) {
            setErro(error.message || 'Não foi possível cadastrar o serviço.');
        } finally {
            setCarregando(false);
        }
    }

    return (
        <main style={estilos.pagina}>
            <div style={estilos.container}>

                <Link to="/" style={estilos.voltar}>
                    ← Voltar
                </Link>

                <div style={estilos.cabecalho}>
                    <span style={estilos.eyebrow}>
                        ÁREA DO PROFISSIONAL
                    </span>

                    <h1 style={estilos.titulo}>
                        Cadastre seu serviço
                    </h1>

                    <p style={estilos.subtitulo}>
                        Apresente seu trabalho e disponibilize seus
                        serviços para novos clientes.
                    </p>
                </div>

                <form onSubmit={cadastrar} style={estilos.formulario}>

                    <div style={estilos.grupo}>
                        <label htmlFor="nome">Nome do serviço</label>

                        <input
                            id="nome"
                            type="text"
                            placeholder="Ex.: Hidratação capilar"
                            value={nome}
                            onChange={e => setNome(e.target.value)}
                            maxLength={100}
                            required
                        />
                    </div>

                    <div style={estilos.grupo}>
                        <label htmlFor="descricao">Descrição</label>

                        <textarea
                            id="descricao"
                            placeholder="Descreva como funciona seu serviço..."
                            value={descricao}
                            onChange={e => setDescricao(e.target.value)}
                            rows={5}
                            required
                        />
                    </div>

                    <div style={estilos.grupo}>
                        <label htmlFor="tipo">Categoria</label>

                        <select
                            id="tipo"
                            value={tipo}
                            onChange={e => setTipo(e.target.value)}
                            required
                        >
                            <option value="">
                                Selecione uma categoria
                            </option>

                            {categorias.map(categoria => (
                                <option
                                    key={categoria.value}
                                    value={categoria.value}
                                >
                                    {categoria.label}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div style={estilos.grupo}>
                        <label htmlFor="preco">Preço (R$)</label>

                        <input
                            id="preco"
                            type="number"
                            placeholder="Ex.: 120.00"
                            value={preco}
                            onChange={e => setPreco(e.target.value)}
                            min="0.01"
                            step="0.01"
                            required
                        />
                    </div>

                    {erro && (
                        <div style={estilos.erro} role="alert">
                            {erro}
                        </div>
                    )}

                    {sucesso && (
                        <div style={estilos.sucesso} role="status">
                            {sucesso}
                        </div>
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
                            : 'Cadastrar serviço'}
                    </button>

                </form>

            </div>
        </main>
    );
}

const estilos = {
    pagina: {
        minHeight: '100vh',
        background: '#F9F6F2',
        padding: '40px 20px',
        boxSizing: 'border-box'
    },

    container: {
        maxWidth: '680px',
        margin: '0 auto'
    },

    voltar: {
        color: '#775890',
        textDecoration: 'none',
        fontWeight: '600',
        display: 'inline-block',
        marginBottom: '35px'
    },

    cabecalho: {
        marginBottom: '30px'
    },

    eyebrow: {
        color: '#9C4C53',
        fontSize: '12px',
        letterSpacing: '2px',
        fontWeight: 'bold'
    },

    titulo: {
        color: '#3C004B',
        fontSize: '34px',
        margin: '12px 0'
    },

    subtitulo: {
        color: '#775890',
        lineHeight: '1.6'
    },

    formulario: {
        background: '#FFFFFF',
        padding: '35px',
        borderRadius: '16px',
        boxShadow: '0 8px 30px rgba(60, 0, 75, 0.07)'
    },

    grupo: {
        display: 'flex',
        flexDirection: 'column',
        gap: '9px',
        marginBottom: '23px'
    },

    erro: {
        background: '#FFE5E5',
        color: '#9C3030',
        padding: '13px',
        borderRadius: '8px',
        marginBottom: '18px'
    },

    sucesso: {
        background: '#E2F5E8',
        color: '#28643B',
        padding: '13px',
        borderRadius: '8px',
        marginBottom: '18px'
    },

    botao: {
        width: '100%',
        padding: '15px',
        background: '#9C4C53',
        color: '#FFFFFF',
        border: 'none',
        borderRadius: '9px',
        fontSize: '16px',
        fontWeight: 'bold',
        cursor: 'pointer'
    }
};