package br.com.gwenilorac.biblioteca.servicos;

import javax.persistence.EntityManager;
import javax.swing.JOptionPane;

import br.com.gwenilorac.biblioteca.dao.EmprestimoDao;
import br.com.gwenilorac.biblioteca.dao.ReservaDao;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.Reserva;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class ServicoReserva {

	static EntityManager em = JPAUtil.getEntityManager();
	static EmprestimoDao emprestimoDao = new EmprestimoDao(em);
	static ReservaDao reservaDao = new ReservaDao(JPAUtil.getEntityManager());
	
	public static void realizarReserva(Usuario usuarioSelecionado, Livro livroSelecionado) {
	    try {
	        boolean estaEmprestadoPorUser = emprestimoDao.buscarSeLivroEstaEmprestadoPorUser(usuarioSelecionado, livroSelecionado);

	        if (!estaEmprestadoPorUser) {
	            Reserva livroTemReserva = reservaDao.buscarSeLivroTemReserva(livroSelecionado);

	            if (livroSelecionado != null && usuarioSelecionado != n ull) {
	                if (livroTemReserva == null) {
	                    if (!emprestimoDao.isLivroDisponivel(livroSelecionado)) {
	                        em.getTransaction().begin();

	                        Reserva reserva = new Reserva(livroSelecionado, usuarioSelecionado);

	                        reservaDao.cadastrar(reserva);
	                        em.getTransaction().commit();

	                        JOptionPane.showMessageDialog(null, "Livro reservado com sucesso!");
	                    } else {
	                        JOptionPane.showMessageDialog(null, "O livro selecionado está disponível para empréstimo.");
	                    }
	                } else {
	                    JOptionPane.showMessageDialog(null, "Livro já está reservado");
	                }
	            } else {
	                JOptionPane.showMessageDialog(null, "SELECIONE UM LIVRO E USUÁRIO!");
	            }
	        }
	    } catch (Exception e) {
	        if (em.getTransaction().isActive()) {
	            em.getTransaction().rollback();
	        }
	        JOptionPane.showMessageDialog(null, "Erro ao realizar a reserva: " + e.getMessage());
	    } finally {
	        if (em.getTransaction().isActive()) {
	            em.getTransaction().rollback();
	        }
	    }
	}
}
