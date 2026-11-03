
const BASE = 'https://spa-beleza.onrender.com';

export async function requisicao(caminho, opcoes = {}) {
  const token = sessionStorage.getItem('token');

  const resposta = await fetch(BASE + caminho, {
    ...opcoes,
    headers: {
      ...(opcoes.body
          ? { 'Content-Type': 'application/json' }
          : {}),
      ...(token
          ? { Authorization: `Bearer ${token}` }
          : {}),
      ...opcoes.headers
    }
  });

  const texto = await resposta.text();

  let dados;

  try {
    dados = texto ? JSON.parse(texto) : null;
  } catch {
    dados = texto;
  }

  if (!resposta.ok) {
    const mensagem = dados && typeof dados === 'object'
        ? dados.message || dados.detail || dados.error
        : null;

    const erro = new Error(
        mensagem || `Erro HTTP ${resposta.status}`
    );

    erro.status = resposta.status;
    throw erro;
  }

  return dados;
}

// SERVIÇOS

export function listarServicos(tipo = null) {
  const caminho = tipo
      ? `/servicos?tipo=${encodeURIComponent(tipo)}`
      : '/servicos';

  return requisicao(caminho);
}

export function buscarServico(id) {
  return requisicao(`/servicos/${id}`);
}

// AUTENTICAÇÃO

export function fazerLogin(dados) {
  return requisicao('/clientes/login', {
    method: 'POST',
    body: JSON.stringify(dados)
  });
}

export function cadastrarCliente(dados) {
  return requisicao('/clientes', {
    method: 'POST',
    body: JSON.stringify(dados)
  });
}

// AGENDAMENTOS

export function buscarHorariosDisponiveis(servicoId, data) {
  return requisicao(`/agendamentos/disponibilidade?servicoId=${servicoId}&data=${data}`);
}

export function criarAgendamento(dados) {
  return requisicao('/agendamentos', { method: 'POST', body: JSON.stringify(dados) });
}

export function listarAgendamentos() {
  return requisicao('/agendamentos');
}

export function cancelarAgendamento(id) {
  return requisicao(`/agendamentos/${id}`, { method: 'DELETE' });
}

export function listarSolicitacoesProfissional() {
  return requisicao('/agendamentos/solicitacoes');
}

export function listarMinhaAgendaProfissional() {
  return requisicao('/agendamentos/minha-agenda');
}

export function aceitarSolicitacao(id) {
  return requisicao(`/agendamentos/${id}/aceitar`, { method: 'POST' });
}

export function cancelarAtendimentoProfissional(id) {
  return requisicao(`/agendamentos/${id}/profissional`, { method: 'DELETE' });
}
