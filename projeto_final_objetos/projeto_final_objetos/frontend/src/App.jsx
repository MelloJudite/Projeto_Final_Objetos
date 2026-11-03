import { Routes, Route, Navigate } from 'react-router-dom';

import Navbar from './components/Navbar.jsx';

import BemVindo from './pages/BemVindo.jsx';
import Login from './pages/Login.jsx';
import Cadastro from './pages/Cadastro.jsx';
import Servicos from './pages/Servicos.jsx';
import Agendar from './pages/Agendar.jsx';
import Carrinho from './pages/Carrinho.jsx';
import MeusAgendamentos from './pages/MeusAgendamentos.jsx';
import PainelProfissional from './pages/PainelProfissional.jsx';
import CadastrarServico from './pages/CadastrarServico.jsx';

import RotaProtegida from './components/RotaProtegida.jsx';
import RotaProtegidaProfissional from './components/RotaProtegidaProfissional.jsx';

export default function App() {
    return (
        <>
            <Navbar />

            <Routes>
                {/* Página inicial */}
                <Route path="/" element={<BemVindo />} />

                {/* Autenticação */}
                <Route path="/login" element={<Login />} />
                <Route path="/cadastro" element={<Cadastro />} />

                {/* Serviços */}
                <Route path="/servicos" element={<Servicos />} />
                <Route path="/servicos/:tipo" element={<Servicos />} />

                {/* Área do cliente */}
                <Route element={<RotaProtegida />}>
                    <Route
                        path="/agendar/:id"
                        element={<Agendar />}
                    />

                    <Route
                        path="/carrinho"
                        element={<Carrinho />}
                    />

                    <Route
                        path="/meus-agendamentos"
                        element={<MeusAgendamentos />}
                    />
                </Route>

                {/* Área do profissional */}
                <Route element={<RotaProtegidaProfissional />}>
                    <Route
                        path="/profissional/servicos/novo"
                        element={<CadastrarServico />}
                    />

                    <Route
                        path="/profissional/agenda"
                        element={<PainelProfissional />}
                    />
                </Route>

                {/* Página não encontrada */}
                <Route
                    path="*"
                    element={<Navigate to="/" replace />}
                />
            </Routes>
        </>
    );
}