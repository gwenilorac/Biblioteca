package br.com.gwenilorac.biblioteca.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.gwenilorac.biblioteca.model.Genero;

public class GeneroDao {

    private EntityManager em;

    public GeneroDao(EntityManager em) {
        this.em = em;
    }

    public void cadastrar(Genero genero) {
        this.em.persist(genero);
    }

    public void atualizar(Genero genero) {
        this.em.merge(genero);
    }

    public void remover(Genero genero) {
        this.em.remove(genero);
        System.out.println("Gênero excluído com sucesso!");
    }

    public List<Genero> buscarTodosGeneros() {
        String jpql = "SELECT g FROM Genero g";
        return em.createQuery(jpql, Genero.class).getResultList();
    }

    public Genero buscarGeneroPorNome(String nome) {
        try {
            String jpql = "SELECT g FROM Genero g WHERE g.nome = :nome";
            TypedQuery<Genero> query = em.createQuery(jpql, Genero.class);
            query.setParameter("nome", nome);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
