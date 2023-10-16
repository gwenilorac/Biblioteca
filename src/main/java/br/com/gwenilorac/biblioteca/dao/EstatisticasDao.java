package br.com.gwenilorac.biblioteca.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.Usuario;

public class EstatisticasDao {

	private EntityManager em;

    public EstatisticasDao(EntityManager entityManager) {
        this.em = entityManager;
    }

    public List<Livro> getLivrosMaisEmprestados() {
        String jpql = "SELECT e.listaDeLivrosMaisEmprestados FROM Emprestimo e";
        TypedQuery<Livro> query = em.createQuery(jpql, Livro.class);
        return query.getResultList();
    }

    public int getTotalUsuariosRegistrados() {
        String jpql = "SELECT COUNT(u) FROM Usuario u";
        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        return query.getSingleResult().intValue();
    }

    public List<Usuario> getUsuariosComMaisEmprestimos() {
        String jpql = "SELECT u.usuariosComMaisEmprestimos FROM Usuario u";
        TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);
        return query.getResultList();
    }

    public int getTotalLivrosNaBiblioteca() {
        String jpql = "SELECT COUNT(l) FROM Livro l";
        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        return query.getSingleResult().intValue();
    }
}

