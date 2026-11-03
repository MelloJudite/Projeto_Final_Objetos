
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

import {
    listarAgendamentos,
    cancelarAgendamento
} from '../service/api.js';

export default function MeusAgendamentos() {
    const [lista, setLista] = useState([]);
    const [erro, setErro] = useState('');
    const [carregando, setCarregando] = useState(true);

    async function carregar() {
        try {
            setCarregando(true);
            setErro('');

            const dados = await listarAgendamentos();

            setLista(Array.isArray(dados) ? dados : []);

        } catch (error) {
            setErro(error.message || 'Não foi possível carregar seus agendamentos.');
        } finally {
            setCarregando(false);
        }
    }

    useEffect(() => {
        carregar();
    }, []);

    async function cancelar(id) {
        const confirmar = window.confirm(
            'Tem certeza que deseja cancelar este agendamento?'
        );

        if (!confirmar) return;

        try {
            await cancelarAgendamento(id);
            await carregar();

        } catch (error) {
            setErro(error.message || 'Não foi possível cancelar o agendamento.');
        }
    }

    function formatarData(data) {
        return new Date(data).toLocaleDateString('pt-BR');
    }

    function formatarHora(data) {
        return new Date(data).toLocaleTimeString('pt-BR', {
            hour: '2-digit',
            minute: '2-digit'
        });
    }

    return (
        <main className="container page">

            <div className="catalog-header">

                <Link to="/" className="back">
                    ← Voltar ao início
                </Link>

                <div className="catalog-title">

                    <span className="eyebrow">
                        NOSSO MOMENTO
                    </span>

                    <h1>Meus agendamentos</h1>

                    <p className="intro">
                        Consulte seus horários reservados e acompanhe
                        seus atendimentos.
                    </p>

                </div>

                <Link to="/servicos" className="btn all-services">
                    Explorar serviços
                </Link>

            </div>

            {carregando && (
                <div className="notice">
                    Carregando seus agendamentos...
                </div>
            )}

            {erro && (
                <div className="notice">
                    <strong>Não foi possível carregar os agendamentos.</strong>
                    <p>{erro}</p>

                    <button
                        className="btn"
                        onClick={carregar}
                        style={{ marginTop: '15px', border: 'none' }}
                    >
                        Tentar novamente
                    </button>
                </div>
            )}

            {!carregando && !erro && lista.length === 0 && (
                <div className="notice empty-appointments">

                    <div className="empty-icon">✿</div>

                    <h2>Você ainda não tem agendamentos.</h2>

                    <p>
                        Que tal escolher um tratamento e reservar
                        seu próximo momento de cuidado?
                    </p>

                    <Link to="/servicos" className="btn">
                        Encontrar serviços
                    </Link>

                </div>
            )}

            {!carregando && !erro && lista.length > 0 && (
                <div className="appointment-list">

                    {lista.map(item => (

                        <article
                            className="appointment-card"
                            key={item.id}
                        >

                            <div className="appointment-info">

                                <span className="eyebrow">
                                    AGENDAMENTO #{item.id}
                                </span>

                                <h2>
                                    {item.servicoNome || 'Atendimento'}
                                </h2>

                                <div className="appointment-details">

                                    <p>
                                        <span>📅</span>
                                        Data: {formatarData(item.dataHora)}
                                    </p>

                                    <p>
                                        <span>🕒</span>
                                        Horário: {formatarHora(item.dataHora)}
                                    </p>

                                    <p>
                                        <span>💰</span>
                                        Valor: {Number(item.valor).toLocaleString(
                                        'pt-BR',
                                        {
                                            style: 'currency',
                                            currency: 'BRL'
                                        }
                                    )}
                                    </p>

                                </div>

                            </div>

                            <div className="appointment-footer">

                                <span className={`appointment-status ${item.status === 'PENDENTE' ? 'status-pendente' : ''}`}>
                                    {item.status === 'PENDENTE' ? 'Aguardando profissional' : item.status === 'ACEITO' ? `Aceito por ${item.profissionalNome || 'profissional'}` : item.status}
                                </span>

                                <button
                                    className="cancel-button"
                                    onClick={() => cancelar(item.id)}
                                    disabled={
                                        new Date(item.dataHora) <= new Date()
                                    }
                                >
                                    Cancelar agendamento
                                </button>

                            </div>

                        </article>

                    ))}

                </div>
            )}

        </main>
    );
}
