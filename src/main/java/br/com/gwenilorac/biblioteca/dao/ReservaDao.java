package br.com.gwenilorac.biblioteca.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

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
}
