package br.com.gwenilorac.biblioteca.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.gwenilorac.biblioteca.model.Multa;

public class MultaDao {
	
private EntityManager em;
	
	public MultaDao(EntityManager em) {
		this.em = em;
	}

	public void cadastrar(Multa multa) {
		this.em.persist(multa);
	}

	public void atualizar(Multa multa) {
		this.em.merge(multa);
	}
	
	public void remover(Multa multa) {
			this.em.remove(multa);
	}

	public List<Multa> buscarTodasMultas() {
		String jpql = "SELECT m FROM Multa ";
		return em.createQuery(jpql, Multa.class)
				.getResultList();
	}

}
