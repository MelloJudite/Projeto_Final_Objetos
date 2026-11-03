
import { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';

import {
    buscarServico,
    buscarHorariosDisponiveis,
    criarAgendamento
} from '../service/api.js';

export default function Agendar() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [servico, setServico] = useState(null);
    const [data, setData] = useState('');
    const [hora, setHora] = useState('');
    const [horarios, setHorarios] = useState([]);
    const [erro, setErro] = useState('');
    const [carregando, setCarregando] = useState(false);
    const [reservando, setReservando] = useState(false);

    const hoje = new Date();
    const dataMinima = [
        hoje.getFullYear(),
        String(hoje.getMonth() + 1).padStart(2, '0'),
        String(hoje.getDate()).padStart(2, '0')
    ].join('-');

    useEffect(() => {
        buscarServico(id)
            .then(setServico)
            .catch(error => setErro(error.message));
    }, [id]);

    useEffect(() => {
        if (!data) {
            setHorarios([]);
            setHora('');
            return;
        }

        let ativo = true;

        setCarregando(true);
        setErro('');
        setHora('');
        setHorarios([]);

        buscarHorariosDisponiveis(id, data)
            .then(dados => {
                if (ativo) setHorarios(dados);
            })
            .catch(error => {
                if (ativo) setErro(error.message);
            })
            .finally(() => {
                if (ativo) setCarregando(false);
            });

        return () => {
            ativo = false;
        };
    }, [id, data]);

    async function reservar(event) {
        event.preventDefault();

        if (!hora || !data) {
            setErro('Selecione uma data e um horário.');
            return;
        }

        setReservando(true);
        setErro('');

        try {
            await criarAgendamento({
                servicoId: Number(id),
                dataHora: `${data}T${hora}:00`
            });

            alert('Solicitação enviada! Aguarde a aprovação de um profissional.');
            navigate('/meus-agendamentos');

        } catch (error) {
            setErro(error.message);
        } finally {
            setReservando(false);
        }
    }

    return (
        <main style={estilos.pagina}>
            <div style={estilos.container}>

                <Link to="/servicos" style={estilos.voltar}>
                    ← Voltar aos serviços
                </Link>

                <span style={estilos.eyebrow}>
                    NOSSO MOMENTO
                </span>

                <h1 style={estilos.titulo}>
                    Escolha seu horário
                </h1>

                <p style={estilos.subtitulo}>
                    Reserve seu atendimento de forma simples.
                </p>

                {servico && (
                    <div style={estilos.servico}>
                        <h2>{servico.nome}</h2>

                        <p>{servico.descricao}</p>

                        <strong>
                            {Number(servico.preco).toLocaleString(
                                'pt-BR',
                                {
                                    style: 'currency',
                                    currency: 'BRL'
                                }
                            )}
                        </strong>
                    </div>
                )}

                <form onSubmit={reservar} style={estilos.card}>

                    <h2 style={estilos.tituloSecao}>
                        1. Escolha uma data
                    </h2>

                    <label style={estilos.label}>
                        Data do atendimento
                    </label>

                    <input
                        type="date"
                        min={dataMinima}
                        value={data}
                        onChange={e => setData(e.target.value)}
                        required
                        style={estilos.input}
                    />

                    <div style={estilos.horariosCabecalho}>
                        <h2 style={estilos.tituloSecao}>
                            2. Escolha um horário
                        </h2>

                        <span style={estilos.horarioInfo}>
                            08h às 18h
                        </span>
                    </div>

                    {!data && (
                        <p style={estilos.aviso}>
                            Selecione uma data para consultar os horários.
                        </p>
                    )}

                    {carregando && (
                        <p style={estilos.aviso}>
                            Consultando disponibilidade...
                        </p>
                    )}

                    {data && !carregando && !erro && (
                        <div style={estilos.grade}>
                            {Array.from(
                                { length: 10 },
                                (_, i) => `${String(i + 8).padStart(2, '0')}:00`
                            ).map(h => {
                                const disponivel = horarios.includes(h);
                                const selecionado = hora === h;

                                return (
                                    <button
                                        key={h}
                                        type="button"
                                        disabled={!disponivel}
                                        onClick={() => setHora(h)}
                                        style={{
                                            ...estilos.horario,
                                            ...(selecionado
                                                ? estilos.selecionado
                                                : {}),
                                            ...(!disponivel
                                                ? estilos.indisponivel
                                                : {})
                                        }}
                                    >
                                        {h}
                                        {!disponivel && (
                                            <small>Ocupado</small>
                                        )}
                                    </button>
                                );
                            })}
                        </div>
                    )}

                    {data && !carregando &&
                        horarios.length === 0 && !erro && (
                            <p style={estilos.aviso}>
                                Não existem horários disponíveis nesta data.
                            </p>
                        )}

                    {erro && (
                        <p role="alert" style={estilos.erro}>
                            {erro}
                        </p>
                    )}

                    {hora && (
                        <div style={estilos.resumo}>
                            <strong>Horário selecionado</strong>
                            <span>
                                {data.split('-').reverse().join('/')} às {hora}
                            </span>
                        </div>
                    )}

                    <button
                        type="submit"
                        disabled={!hora || reservando}
                        style={{
                            ...estilos.botao,
                            opacity: !hora || reservando ? 0.5 : 1
                        }}
                    >
                        {reservando
                            ? 'Confirmando...'
                            : 'Confirmar agendamento'}
                    </button>

                    <p style={estilos.rodape}>
                        Atendimento com duração de 60 minutos.
                    </p>

                </form>
            </div>
        </main>
    );
}

const estilos = {
    pagina: {
        minHeight: '100vh',
        background: '#F9F6F2',
        padding: '45px 20px'
    },
    container: {
        maxWidth: '700px',
        margin: '0 auto'
    },
    voltar: {
        display: 'block',
        marginBottom: '30px',
        color: '#775890',
        textDecoration: 'none'
    },
    eyebrow: {
        color: '#A45B70',
        letterSpacing: '2px',
        fontSize: '12px',
        fontWeight: 'bold'
    },
    titulo: {
        color: '#3C004B',
        fontSize: '34px',
        margin: '12px 0'
    },
    subtitulo: {
        color: '#775890',
        marginBottom: '30px',
        lineHeight: 1.6
    },
    servico: {
        background: '#F1E6EC',
        padding: '22px',
        borderRadius: '14px',
        marginBottom: '24px',
        lineHeight: 1.7
    },
    card: {
        background: '#FFFFFF',
        padding: '30px',
        borderRadius: '16px',
        boxShadow: '0 8px 30px rgba(60,0,75,0.07)'
    },
    tituloSecao: {
        color: '#503451',
        fontSize: '19px',
        margin: '0 0 20px'
    },
    label: {
        display: 'block',
        color: '#503451',
        fontWeight: '600',
        marginBottom: '10px'
    },
    input: {
        width: '100%',
        padding: '13px',
        border: '1px solid #E5C9D5',
        borderRadius: '8px',
        marginBottom: '30px',
        boxSizing: 'border-box',
        fontSize: '16px'
    },
    horariosCabecalho: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        flexWrap: 'wrap',
        gap: '10px'
    },
    horarioInfo: {
        color: '#775890',
        fontSize: '13px'
    },
    grade: {
        display: 'grid',
        gridTemplateColumns: 'repeat(3, minmax(0, 1fr))',
        gap: '12px',
        margin: '20px 0'
    },
    horario: {
        padding: '15px 8px',
        border: '1px solid #E5C9D5',
        borderRadius: '9px',
        background: '#FFFFFF',
        color: '#603B61',
        fontWeight: 'bold',
        cursor: 'pointer',
        display: 'flex',
        flexDirection: 'column',
        gap: '5px'
    },
    selecionado: {
        background: '#A45B70',
        color: '#FFFFFF',
        borderColor: '#A45B70'
    },
    indisponivel: {
        background: '#F0EEEE',
        color: '#999999',
        borderColor: '#E5E0E0',
        cursor: 'not-allowed',
        textDecoration: 'line-through'
    },
    aviso: {
        color: '#806E80',
        margin: '20px 0',
        lineHeight: 1.6
    },
    erro: {
        color: '#9C3030',
        background: '#FFE5E5',
        padding: '13px',
        borderRadius: '8px'
    },
    resumo: {
        display: 'flex',
        justifyContent: 'space-between',
        flexWrap: 'wrap',
        gap: '10px',
        background: '#F5EDF1',
        padding: '16px',
        borderRadius: '9px',
        margin: '20px 0',
        color: '#503451'
    },
    botao: {
        width: '100%',
        padding: '16px',
        border: 'none',
        borderRadius: '9px',
        background: '#9C4C53',
        color: '#FFFFFF',
        fontWeight: 'bold',
        fontSize: '16px',
        cursor: 'pointer'
    },
    rodape: {
        textAlign: 'center',
        color: '#806E80',
        fontSize: '13px',
        marginTop: '20px'
    }
};
