package br.com.gwenilorac.biblioteca.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import br.com.gwenilorac.biblioteca.model.Autor;

public class AutorDao {

	private EntityManager em;

	public AutorDao(EntityManager em) {
		this.em = em;
	}

	public void cadastrar(Autor autor) {
		this.em.persist(autor);
	}

	public void atualizar(Autor autor) {
		this.em.merge(autor);
	}

	public void remover(Autor autor) {
	    this.em.remove(autor);
	    System.out.println("Autor excluído com sucesso: " + autor.getNome());
	}
	
	public List<Autor> buscarTodosAutores() {
	    try {
	        String jpql = "SELECT a FROM Autor a";
	        return em.createQuery(jpql, Autor.class)
	                .getResultList();
	    } finally {
	        em.close(); 
	    }
	}

	public Autor buscarAutorPorNome(String nome) {
	    try {
	        String jpql = "SELECT a FROM Autor a WHERE a.nome = :nome";
	        return em.createQuery(jpql, Autor.class)
	                .setParameter("nome", nome)
	                .getSingleResult();
	    } catch (NoResultException e) {
	        return null;
	    } catch (NonUniqueResultException e) {
	        return null;
	    } catch (Exception e) {
	        throw new RuntimeException("Erro ao buscar autor por nome", e);
	    }
	}

}
