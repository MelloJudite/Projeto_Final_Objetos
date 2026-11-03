import { requisicao } from './api.js';

export function cadastrarServico(dados) {
    return requisicao('/servicos', {
        method: 'POST',
        body: JSON.stringify(dados)
    });
}

export function listarMeusServicos() {
    return requisicao('/servicos');
}