
import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';

import { listarServicos } from '../service/api.js';
import { categorias } from '../components/Categorias.jsx';

export default function Servicos() {
    const { tipo } = useParams();

    const [lista, setLista] = useState([]);
    const [erro, setErro] = useState('');
    const [carregando, setCarregando] = useState(true);

    const categoria = categorias.find(
        c => c.tipo === tipo?.toUpperCase()
    );

    useEffect(() => {
        let ativo = true;

        setCarregando(true);
        setErro('');

        listarServicos(tipo)
            .then(dados => {
                if (ativo) {
                    setLista(
                        Array.isArray(dados)
                            ? dados
                            : dados?.content || []
                    );
                }
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
    }, [tipo]);

    return (
        <section className="container page">

            <div className="catalog-header">

                <Link to="/" className="back">
                    ← Voltar ao início
                </Link>

                <div className="catalog-title">
                    <span className="eyebrow">
                        NOSSO CATÁLOGO
                    </span>

                    <h1>
                        {tipo
                            ? categoria?.nome || tipo
                            : 'Todos os serviços'}
                    </h1>

                    <p className="intro">
                        {tipo
                            ? 'Conheça nossos tratamentos disponíveis nesta categoria.'
                            : 'Explore todos os tratamentos disponíveis e escolha o ideal para você.'}
                    </p>
                </div>

                <Link to="/servicos" className="btn all-services">
                    Ver todos os serviços
                </Link>

            </div>

            {carregando && (
                <p className="catalog-message">
                    Carregando serviços...
                </p>
            )}

            {erro && (
                <div className="notice">
                    Não foi possível buscar os serviços: {erro}.
                </div>
            )}

            {!carregando && !erro && lista.length === 0 && (
                <div className="notice">
                    Nenhum serviço cadastrado ainda.
                </div>
            )}

            {!carregando && !erro && lista.length > 0 && (
                <div className="service-list">
                    {lista.map(servico => (
                        <article
                            className="service-card"
                            key={servico.id}
                        >
                            <div>
                                <span className="eyebrow">
                                    {servico.tipo || 'SERVIÇO'}
                                </span>

                                <h3>{servico.nome}</h3>

                                <p>{servico.descricao}</p>
                            </div>

                            <div className="service-action">
                                <strong>
                                    {Number(servico.preco).toLocaleString(
                                        'pt-BR',
                                        {
                                            style: 'currency',
                                            currency: 'BRL'
                                        }
                                    )}
                                </strong>

                                <Link
                                    className="btn"
                                    to={`/agendar/${servico.id}`}
                                >
                                    Agendar
                                </Link>
                            </div>
                        </article>
                    ))}
                </div>
            )}

        </section>
    );
}
