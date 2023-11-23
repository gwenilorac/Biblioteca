package br.com.gwenilorac.biblioteca.servicos;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.persistence.EntityManager;
import javax.swing.JOptionPane;

import br.com.gwenilorac.biblioteca.dao.EmprestimoDao;
import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.model.Emprestimo;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.StatusEmprestimo;
import br.com.gwenilorac.biblioteca.model.TemMulta;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class ServicoEmprestimo {

	public static void pegarLivroEmprestado(Livro livro, Usuario usuario) {
		EntityManager em = JPAUtil.getEntityManager();
		EmprestimoDao emprestimoDao = new EmprestimoDao(em);
		UsuarioDao usuarioDao = new UsuarioDao(em);
		Emprestimo emprestimoDoLivro = emprestimoDao.buscarSeLivroJaTemEmprestimo(livro);
		List<Livro> livrosEmprestados = usuarioDao.buscarLivrosEmprestados(ServicoLogin.getUsuarioLogado().getId());

		em.getTransaction().begin();

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
					emprestimoDao.atualizar(emprestimoDoLivro);
					JOptionPane.showMessageDialog(null, "LIVRO EMPRESTADO COM SUCESSO!");
				} else {
					JOptionPane.showMessageDialog(null, "O LIVRO NÃO ESTÁ DISPONÍVEL PARA EMPRÉSTIMO.");
				}
			}
		} else {
			JOptionPane.showMessageDialog(null, "SEU LIMITE DE EMPRESTIMOS FOI ATINGIDO");
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
						emprestimoDoLivro.setMultaPaga(false);
						emprestimoDoLivro.setValorMulta(0.0);

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

	private static boolean isMultaValid(Emprestimo emprestimo) {

		EntityManager em = JPAUtil.getEntityManager();
		EmprestimoDao emprestimoDao = new EmprestimoDao(em);

		em.getTransaction().begin();

		if (!emprestimo.isMultaPaga()) {
			LocalDate dataAtual = LocalDate.now();
			LocalDate dataDevolucaoLivro = emprestimo.getDataDevolucao();
			if (dataAtual.isAfter(dataDevolucaoLivro)) {
				long diasAtraso = dataDevolucaoLivro.until(dataAtual).getDays();
				double valorMulta = diasAtraso * emprestimo.getValorMulta();
				emprestimo.setDiasAtrasados(diasAtraso);
				emprestimo.setValorMulta(valorMulta);
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

	public static void pagarMulta(Emprestimo emprestimo) {
		EntityManager em = JPAUtil.getEntityManager();
		EmprestimoDao emprestimoDao = new EmprestimoDao(em);

		em.getTransaction().begin();

		emprestimo.setMultaPaga(true);
		emprestimo.setTemMulta(TemMulta.INEXISTENTE);

		emprestimoDao.atualizar(emprestimo);
		em.getTransaction().commit();
		em.close();

		System.out.println("Multa paga com sucesso. Valor: R$ " + emprestimo.getValorMulta());
	}
}
