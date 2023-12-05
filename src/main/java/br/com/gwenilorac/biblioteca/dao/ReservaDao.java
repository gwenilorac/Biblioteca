package br.com.gwenilorac.biblioteca.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import br.com.gwenilorac.biblioteca.domain.ReservaView;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.Reserva;
import br.com.gwenilorac.biblioteca.model.Usuario;

public class ReservaDao {
    private EntityManager em;

    public ReservaDao(EntityManager em) {
        this.em = em;
    }

    public void cadastrar(Reserva reserva) {
        this.em.persist(reserva);
    }

    public void atualizar(Reserva reserva) {
        this.em.merge(reserva);
    }

    public void remover(Reserva reserva) {
        this.em.remove(reserva);
    }

    public List<Reserva> buscarTodasReservas() {
        String jpql = "SELECT r FROM Reserva r";
        return em.createQuery(jpql, Reserva.class).getResultList();
    }

    public boolean buscarSeLivroTemReserva(Livro livro) {
        try {
            String jpql = "SELECT r FROM Reserva r WHERE r.livro = :livro";
            return !em.createQuery(jpql, Reserva.class)
                      .setParameter("livro", livro)
                      .getResultList().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public Reserva buscarReservaUsuario(Usuario usuario) {
        try {
            String jpql = "SELECT r FROM Reserva r WHERE r.usuario = :usuario";
            return em.createQuery(jpql, Reserva.class)
                     .setParameter("usuario", usuario)
                     .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Reserva livroEstaReservadoPorUsuario(Livro livro, Usuario usuario) {
        try {
            String jpql = "SELECT r FROM Reserva r WHERE r.livro = :livro AND r.usuario = :usuario";
            return em.createQuery(jpql, Reserva.class)
                     .setParameter("livro", livro)
                     .setParameter("usuario", usuario)
                     .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
	public List<ReservaView> findReservasReporList() {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT new br.com.gwenilorac.biblioteca.domain.ReservaView(livr.titulo ");
		hql.append(", livr.autor.nome ");
		hql.append(", user.nome ) ");
		hql.append("FROM Reserva r ");
		hql.append("INNER JOIN r.livro livr ");
		hql.append("INNER JOIN r.usuario user ");
		hql.append("GROUP BY livr.titulo");
		hql.append(", livr.autor.nome ");
		hql.append(", user.nome ");
		hql.append("ORDER BY count(user.nome) DESC");
		TypedQuery<ReservaView> createQuery = em.createQuery(hql.toString(), ReservaView.class);
		return createQuery.getResultList();
	}

}
