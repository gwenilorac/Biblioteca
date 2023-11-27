package br.com.gwenilorac.biblioteca.servicos;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.swing.JOptionPane;

import br.com.gwenilorac.biblioteca.dao.EmprestimoDao;
import br.com.gwenilorac.biblioteca.dao.ReservaDao;
import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.model.Emprestimo;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.Reserva;
import br.com.gwenilorac.biblioteca.model.StatusEmprestimo;
import br.com.gwenilorac.biblioteca.model.TemMulta;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class ServicoEmprestimo {

	public static void pegarLivroEmprestado(Livro livro, Usuario usuario) {
		EntityManager em = JPAUtil.getEntityManager();
		UsuarioDao usuarioDao = new UsuarioDao(em);
		EmprestimoDao emprestimoDao = new EmprestimoDao(em);
		Emprestimo emprestimoDoLivro = emprestimoDao.buscarSeLivroJaTemEmprestimo(livro);
		List<Livro> livrosEmprestados = usuarioDao.buscarLivrosEmprestados(usuario.getId());
		ReservaDao reservaDao = new ReservaDao(em);
		boolean livroTemReserva = reservaDao.buscarSeLivroTemReserva(livro);
		boolean livroEstaReservadoPorUsuario = reservaDao.livroEstaReservadoPorUsuario(usuario, livro);

		em.getTransaction().begin();

		if (livroTemReserva == false || livroEstaReservadoPorUsuario) {
			if (livrosEmprestados.size() <= 2) {
				if (emprestimoDoLivro == null) {
					Emprestimo Novoemprestimo = new Emprestimo(livro, usuario);
					emprestimoDao.cadastrar(Novoemprestimo);
					if (Novoemprestimo.getStatus() == StatusEmprestimo.ENCERRADO) {
						Novoemprestimo.pegarLivroEmprestado();
						emprestimoDao.atualizar(Novoemprestimo);
						JOptionPane.showMessageDialog(null, "LIVRO EMPRESTADO COM SUCESSO!");
					} else {
						JOptionPane.showMessageDialog(null, "O LIVRO NÃO ESTÁ DISPONÍVEL PARA EMPRÉSTIMO.");
					}
				} else {
					if (emprestimoDoLivro.getStatus() == StatusEmprestimo.ENCERRADO) {
						emprestimoDoLivro.pegarLivroEmprestado();
						emprestimoDoLivro.setDataDevolucao(LocalDate.now().plusWeeks(4));
						emprestimoDoLivro.setMultaPaga(false);
						emprestimoDao.atualizar(emprestimoDoLivro);
						JOptionPane.showMessageDialog(null, "LIVRO EMPRESTADO COM SUCESSO!");
					} else {
						JOptionPane.showMessageDialog(null, "O LIVRO NÃO ESTÁ DISPONÍVEL PARA EMPRÉSTIMO.");
					}
				}
			} else {
				JOptionPane.showMessageDialog(null, "SEU LIMITE DE EMPRESTIMOS FOI ATINGIDO");
			}
		} else {
			JOptionPane.showMessageDialog(null, "LIVRO RESERVADO POR " + usuario.getId() + usuario.getNome() , ", POR FAVOR ESCOLHER OUTRO", 0);
		}
		em.getTransaction().commit();
	}

	public static void devolverLivro(Livro livro) {
		EntityManager em = JPAUtil.getEntityManager();
		EmprestimoDao emprestimoDao = new EmprestimoDao(em);

		try {
			em.getTransaction().begin();

			if (!emprestimoDao.isLivroDisponivel(livro)) {
				Emprestimo emprestimoDoLivro = emprestimoDao.buscarSeLivroJaTemEmprestimo(livro);

				if (emprestimoDoLivro != null) {
					if (!isMultaValid(emprestimoDoLivro)) {

						emprestimoDoLivro.devolverLivro();

						emprestimoDao.atualizar(emprestimoDoLivro);
						em.getTransaction().commit();
						JOptionPane.showMessageDialog(null, "LIVRO DEVOLVIDO COM SUCESSO!");
					} else {
						JOptionPane.showMessageDialog(null, "LIVRO TEM MULTA PENDENTE!");
					}
				} else {
					JOptionPane.showMessageDialog(null, "NÃO EXISTE EMPRÉSTIMO COM ESSE LIVRO!");
				}
			} else {
				JOptionPane.showMessageDialog(null, "LIVRO NÃO ESTÁ EMPRESTADO OU JÁ FOI DEVOLVIDO!");
			}
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			e.printStackTrace();
		} finally {
			em.close();
		}
	}

	public static void pagarMulta(Emprestimo emprestimo) {
		EntityManager em = JPAUtil.getEntityManager();
		EmprestimoDao emprestimoDao = new EmprestimoDao(em);

		em.getTransaction().begin();

		emprestimo.setMultaPaga(true);
		emprestimo.setTemMulta(TemMulta.INEXISTENTE);
		emprestimo.setValorMulta(0);
		emprestimo.setDiasAtrasados(0);

		emprestimoDao.atualizar(emprestimo);
		em.getTransaction().commit();
		em.close();

		System.out.println("Multa paga com sucesso. Valor: R$ " + emprestimo.getValorMulta());
	}

	public boolean devolucaoParaExclusaoConta(Usuario usuario) {
		EntityManager em = JPAUtil.getEntityManager();
		UsuarioDao usuarioDao = new UsuarioDao(em);
		List<Livro> livrosEmprestados = usuarioDao.buscarLivrosEmprestados(usuario.getId());
		for (Livro livro : livrosEmprestados) {
			devolverLivro(livro);
		}
		System.out.println("Livros devolvidos com sucesso!");
		return true;
	}

	public static boolean isMultaValid(Emprestimo emprestimo) {
		EntityManager em = JPAUtil.getEntityManager();
		EmprestimoDao emprestimoDao = new EmprestimoDao(em);
		
		em.getTransaction().begin();
		
		if (!emprestimo.isMultaPaga()) {
			emprestimo.setDataAtual(LocalDate.now());
			if (emprestimo.getDataAtual().isAfter(emprestimo.getDataDevolucaoLivro())) {
				emprestimo.setDiasAtrasados(emprestimo.getDataDevolucao().until(emprestimo.getDataAtual()).getDays());
				emprestimo.setValorMulta(emprestimo.getDiasAtrasados() * emprestimo.getValormultapordia());
				emprestimo.setTemMulta(TemMulta.PENDENTE);
				
				emprestimoDao.atualizar(emprestimo);
				em.getTransaction().commit();
				em.close();
				return true;
			} else {
				System.out.println("Livro devolvido antes da data de devolução");
				return false;
			}
		} else {
			System.out.println("Multa já foi paga anteriormente.");
			return false;
		}
	}
}
