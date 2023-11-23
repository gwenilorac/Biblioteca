package br.com.gwenilorac.biblioteca.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.gwenilorac.biblioteca.model.Autor;
import br.com.gwenilorac.biblioteca.model.Genero;

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
	
	public List<Autor> buscarTodosAutores(){
		String jpql = "SELECT a FROM Autor a";
		return em.createQuery(jpql, Autor.class)
				.getResultList();
	}

	public Autor buscarAutorPorNome(String nome) {
		try {
			String jpql = "SELECT a FROM Autor a WHERE a.nome = :nome";
			return em.createQuery(jpql, Autor.class)
					.setParameter("nome", nome)
					.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
}
