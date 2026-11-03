import { Link } from 'react-router-dom';
import { categorias } from '../components/Categorias.jsx';

export default function BemVindo() {
    return (
        <>
            <section className="hero">
                <div className="eyebrow">
                    BEM-VINDA À LÓTUS BEAUTY
                </div>

                <h1>
                    Sua hora de <em>florescer.</em>
                </h1>

                <p>
                    Um espaço para cuidar de você, relaxar e escolher o tratamento perfeito.
                </p>

                <Link className="btn" to="/servicos">
                    Explorar serviços →
                </Link>

                <div className="hero-flower">
                    ✿
                </div>
            </section>

            <section className="container">
                <div className="section-heading">
          <span className="eyebrow">

          </span>

                    <h2>
                        O que você deseja hoje?
                    </h2>

                    <p>
                        Escolha uma categoria para conhecer nossos tratamentos.
                    </p>
                </div>

                <div className="cards">
                    {categorias.map((c) => (
                        <Link
                            className="category-card"
                            to={`/servicos/${c.tipo}`}
                            key={c.tipo}
                        >
              <span className="category-icon">
                {c.icone}
              </span>

                            <h3>
                                {c.nome}
                            </h3>

                            <p>
                                {c.texto}
                            </p>

                            <span className="more">
                Ver serviço →
              </span>
                        </Link>
                    ))}
                </div>
            </section>
        </>
    );
}