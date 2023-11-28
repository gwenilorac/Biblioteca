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
		em.getTransaction().begin();
		
		ReservaDao reservaDao = new ReservaDao(em);
		Reserva livroEstaReservadoPorUsuario = reservaDao.livroEstaReservadoPorUsuario(livro, usuario);
		boolean livroTemReserva = reservaDao.buscarSeLivroTemReserva(livro);

		if (livroTemReserva == false) {
			EmprestarLivro(livro, usuario);
		} else if (livroEstaReservadoPorUsuario != null) {
			reservaDao.remover(livroEstaReservadoPorUsuario);
			EmprestarLivro(livro, usuario);
		} else {
			JOptionPane.showMessageDialog(null, "LIVRO RESERVADO");
		}
		em.getTransaction().commit();
	}

	public static void EmprestarLivro(Livro livro, Usuario usuario) {
		EntityManager em = JPAUtil.getEntityManager();
		UsuarioDao usuarioDao = new UsuarioDao(em);
		EmprestimoDao emprestimoDao = new EmprestimoDao(em);
		Emprestimo emprestimoDoLivro = emprestimoDao.buscarSeLivroJaTemEmprestimoRelacionadoAUsuario(livro, usuario);
		List<Livro> livrosEmprestados = usuarioDao.buscarLivrosEmprestados(usuario.getId());
		boolean esteUsuarioEstaComOEmprestimoDesteLivroEncerrado = emprestimoDao
				.esteUsuarioEstaComOEmprestimoDesteLivroEncerrado(livro, usuario);
		boolean isLivroEmprestado = emprestimoDao.isLivroEmprestado(livro);

		em.getTransaction().begin();

		if (isLivroEmprestado == false) {
			if (livrosEmprestados.size() <= 2) {
				if (emprestimoDoLivro == null) {
					Emprestimo Novoemprestimo = new Emprestimo(livro, usuario);
					emprestimoDao.cadastrar(Novoemprestimo);
					Novoemprestimo.pegarLivroEmprestado();
					emprestimoDao.atualizar(Novoemprestimo);
					JOptionPane.showMessageDialog(null, "LIVRO EMPRESTADO COM SUCESSO!");

				} else if (esteUsuarioEstaComOEmprestimoDesteLivroEncerrado) {
					emprestimoDoLivro.pegarLivroEmprestado();
					emprestimoDoLivro.setDataDevolucao(LocalDate.now().plusWeeks(4));
					emprestimoDoLivro.setMultaPaga(false);
					emprestimoDao.atualizar(emprestimoDoLivro);
					JOptionPane.showMessageDialog(null, "LIVRO EMPRESTADO COM SUCESSO!");
				} else {
					JOptionPane.showMessageDialog(null, "O LIVRO NÃO ESTÁ DISPONÍVEL PARA EMPRÉSTIMO.");
				}
			} else {
				JOptionPane.showMessageDialog(null, "SEU LIMITE DE EMPRESTIMOS FOI ATINGIDO");
			} 
		} else {
			JOptionPane.showMessageDialog(null, "O LIVRO NÃO ESTÁ DISPONÍVEL PARA EMPRÉSTIMO.");
		}
		em.getTransaction().commit();
	}

	public static void devolverLivro(Livro livro, Usuario usuario) {
		EntityManager em = JPAUtil.getEntityManager();
		EmprestimoDao emprestimoDao = new EmprestimoDao(em);
		Emprestimo emprestimoDoLivro = emprestimoDao.buscarSeLivroEstaEmprestadoPorUser(usuario, livro);

		try {
			em.getTransaction().begin();

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
			devolverLivro(livro, usuario);
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
