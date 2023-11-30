package br.com.gwenilorac.biblioteca.dao;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.gwenilorac.biblioteca.model.Livro;

public class LivroDao {

    private EntityManager em;

    public LivroDao(EntityManager em) {
        this.em = em;
    }

    public void cadastrar(Livro livro) {
        this.em.persist(livro);
    }

    public void atualizar(Livro livro) {
        this.em.merge(livro);
    }

    public void remover(Livro livro) {
        this.em.remove(livro);
    }

    public List<Livro> buscarTodosLivros() {
        String jpql = "SELECT l FROM Livro l";
        return em.createQuery(jpql, Livro.class).getResultList();
    }

    public List<Livro> buscarLivros(String palavraChave) {
        try {
            String jpql = "SELECT l FROM Livro l WHERE LOWER(l.titulo) LIKE :palavraChave";
            TypedQuery<Livro> query = em.createQuery(jpql, Livro.class);
            query.setParameter("palavraChave", "%" + palavraChave.toLowerCase() + "%");
            return query.getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public Livro buscarLivroPorTitulo(String titulo) {
        try {
            String jpql = "SELECT l FROM Livro l WHERE l.titulo = :titulo";
            TypedQuery<Livro> query = em.createQuery(jpql, Livro.class);
            query.setParameter("titulo", titulo);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Livro> buscarPorNomeDoGenero(String nome) {
        String jpql = "SELECT l FROM Livro l WHERE l.genero.nome = :nome";
        return em.createQuery(jpql, Livro.class).setParameter("nome", nome).getResultList();
    }

    public List<Livro> buscarPorNomeAutor(String nome) {
        String jpql = "SELECT l FROM Livro l WHERE l.autor.nome = :nome";
        return em.createQuery(jpql, Livro.class).setParameter("nome", nome).getResultList();
    }

    public Livro buscarPorId(Object id) {
        try {
            String jpql = "SELECT l FROM Livro l WHERE l.id = :livroId";
            TypedQuery<Livro> query = em.createQuery(jpql, Livro.class);
            query.setParameter("livroId", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
