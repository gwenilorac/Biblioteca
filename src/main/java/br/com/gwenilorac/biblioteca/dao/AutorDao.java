package br.com.gwenilorac.biblioteca.dao;

import javax.persistence.EntityManager;
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
		System.out.println("Autor excluido com sucesso!");
	}
	
	public Autor buscarAutorPorNome(String nome) {
	    String jpql = "SELECT a FROM Autor a WHERE a.nome = :nome";
	    return em.createQuery(jpql, Autor.class)
	             .setParameter("nome", nome)
	             .getSingleResult();
	}
}
