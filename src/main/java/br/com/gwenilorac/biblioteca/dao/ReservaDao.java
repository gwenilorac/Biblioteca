package br.com.gwenilorac.biblioteca.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.Reserva;

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
		return em.createQuery(jpql, Reserva.class)
				.getResultList();
	}
	
	public Reserva buscarSeLivroTemReserva(Livro livro) {
	    try {
	        String jpql = "SELECT r FROM Reserva r WHERE r.livro = :livro";
	        List<Reserva> resultados = em.createQuery(jpql, Reserva.class)
	                .setParameter("livro", livro)
	                .getResultList();

	        if (!resultados.isEmpty()) {
	            return resultados.get(0);
	        } else {
	            System.out.println("Nenhuma Reserva encontrado para o livro: " + livro.getTitulo());
	            return null;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}


}
