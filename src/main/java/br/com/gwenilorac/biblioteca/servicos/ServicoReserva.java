package br.com.gwenilorac.biblioteca.servicos;

import javax.persistence.EntityManager;
import javax.swing.JOptionPane;
import br.com.gwenilorac.biblioteca.dao.EmprestimoDao;
import br.com.gwenilorac.biblioteca.dao.ReservaDao;
import br.com.gwenilorac.biblioteca.model.Emprestimo;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.Reserva;
import br.com.gwenilorac.biblioteca.model.StatusEmprestimo;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class ServicoReserva {

	public static void realizarReserva(Usuario usuarioSelecionado, Livro livroSelecionado) {
		EntityManager em = JPAUtil.getEntityManager();
		em.getTransaction().begin();

		EmprestimoDao emprestimoDao = new EmprestimoDao(em);
		ReservaDao reservaDao = new ReservaDao(JPAUtil.getEntityManager());

		try {

			if (livroSelecionado != null && usuarioSelecionado != null) {

				Emprestimo buscarSeLivroJaTemEmprestimo = emprestimoDao.buscarSeLivroJaTemEmprestimo(livroSelecionado);

				if (buscarSeLivroJaTemEmprestimo != null && buscarSeLivroJaTemEmprestimo.getStatus() == StatusEmprestimo.ABERTO) {
					boolean estaEmprestadoPorUser = emprestimoDao.buscarSeLivroEstaEmprestadoPorUser(usuarioSelecionado,
							livroSelecionado);

					if (!estaEmprestadoPorUser) {
						boolean livroTemReserva = reservaDao.buscarSeLivroTemReserva(livroSelecionado);

						if (livroTemReserva == false) {

							Reserva reserva = new Reserva(livroSelecionado, usuarioSelecionado);
							
							reservaDao.cadastrar(reserva);
							em.getTransaction().commit();
							em.close();

							JOptionPane.showMessageDialog(null, "Livro reservado com sucesso!");
						} else {
							JOptionPane.showMessageDialog(null, "O livro selecionado já esta reservado.");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Livro está emprestado para você");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Livro está disponivel para emprestimo!");
				}
			} else {
				JOptionPane.showMessageDialog(null, "SELECIONE UM LIVRO E USUARIO!");
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
