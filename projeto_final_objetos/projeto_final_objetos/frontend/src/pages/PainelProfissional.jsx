import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { aceitarSolicitacao, listarSolicitacoesProfissional, listarMinhaAgendaProfissional } from '../service/api.js';

export default function PainelProfissional() {
  const [solicitacoes, setSolicitacoes] = useState([]);
  const [agenda, setAgenda] = useState([]);
  const [aba, setAba] = useState('solicitacoes');
  const [erro, setErro] = useState('');
  const [carregando, setCarregando] = useState(true);
  const [ocupado, setOcupado] = useState(null);

  async function carregar() {
    setCarregando(true); setErro('');
    try {
      const [s, a] = await Promise.all([listarSolicitacoesProfissional(), listarMinhaAgendaProfissional()]);
      setSolicitacoes(Array.isArray(s) ? s : []);
      setAgenda(Array.isArray(a) ? a : []);
    } catch (e) { setErro(e.message || 'Não foi possível carregar sua agenda.'); }
    finally { setCarregando(false); }
  }

  useEffect(() => { carregar(); }, []);

  async function aceitar(id) {
    setOcupado(id); setErro('');
    try { await aceitarSolicitacao(id); await carregar(); setAba('agenda'); }
    catch (e) { setErro(e.message || 'Outro profissional pode ter aceitado primeiro.'); await carregar(); }
    finally { setOcupado(null); }
  }

  function dataHora(valor) {
    const d = new Date(valor);
    return `${d.toLocaleDateString('pt-BR')} às ${d.toLocaleTimeString('pt-BR', {hour:'2-digit', minute:'2-digit'})}`;
  }

  const itens = aba === 'solicitacoes' ? solicitacoes : agenda;

  return <main className="container page">
    <div className="catalog-header">
      <Link to="/" className="back">← Voltar ao início</Link>
      <div className="catalog-title">
        <span className="eyebrow">ÁREA DO PROFISSIONAL</span>
        <h1>Minha agenda</h1>
        <p className="intro">Receba solicitações e escolha os atendimentos que deseja assumir.</p>
        <Link className="btn" to="/profissional/servicos/novo">Cadastrar novo serviço</Link>
      </div>
    </div>

    <div className="professional-tabs">
      <button className={aba === 'solicitacoes' ? 'active' : ''} onClick={() => setAba('solicitacoes')}>Solicitações recebidas ({solicitacoes.length})</button>
      <button className={aba === 'agenda' ? 'active' : ''} onClick={() => setAba('agenda')}>Minha agenda ({agenda.length})</button>
    </div>

    {erro && <div className="notice" role="alert">{erro}</div>}
    {carregando && <div className="notice">Carregando...</div>}
    {!carregando && !erro && itens.length === 0 && <div className="notice">{aba === 'solicitacoes' ? 'Não há solicitações pendentes no momento.' : 'Você ainda não aceitou atendimentos.'}</div>}

    {!carregando && itens.length > 0 && <div className="appointment-list">
      {itens.map(item => <article className="appointment-card" key={item.id}>
        <div className="appointment-info">
          <span className="eyebrow">{aba === 'solicitacoes' ? 'NOVA SOLICITAÇÃO' : 'ATENDIMENTO ACEITO'} #{item.id}</span>
          <h2>{item.servicoNome}</h2>
          <div className="appointment-details">
            <p>Cliente: {item.clienteNome}</p>
            <p>Data e horário: {dataHora(item.dataHora)}</p>
            <p>Valor: {Number(item.valor).toLocaleString('pt-BR',{style:'currency',currency:'BRL'})}</p>
            {item.descricao && <p>{item.descricao}</p>}
          </div>
        </div>
        {aba === 'solicitacoes' && <button className="btn" disabled={ocupado === item.id} onClick={() => aceitar(item.id)}>{ocupado === item.id ? 'Aceitando...' : 'Aceitar atendimento'}</button>}
        {aba === 'agenda' && <span className="appointment-status">Confirmado</span>}
      </article>)}
    </div>}
    <button className="refresh-agenda" onClick={carregar}>Atualizar solicitações</button>
  </main>;
}
