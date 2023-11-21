package br.com.gwenilorac.biblioteca.servicos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.swing.JOptionPane;

import br.com.gwenilorac.biblioteca.dao.EmprestimoDao;
import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.model.Emprestimo;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class ServicoEmprestimo {

	public static void pegarLivroEmprestado(Livro livro, Usuario usuario) {
		EntityManager em = JPAUtil.getEntityManager();
		EmprestimoDao emprestimoDao = new EmprestimoDao(em);
		UsuarioDao usuarioDao = new UsuarioDao(em);
		Emprestimo emprestimoDoLivro = emprestimoDao.buscarSeLivroJaTemEmprestimo(livro);
		List<Livro> livrosEmprestados = usuarioDao.buscarLivrosEmprestados(ServicoLogin.getUsuarioLogado().getId());

		if (livrosEmprestados.size() <= 2) {
			if (emprestimoDoLivro == null) {
				em.getTransaction().begin();
				Emprestimo Novoemprestimo = new Emprestimo(livro, usuario);
				emprestimoDao.cadastrar(Novoemprestimo);
				boolean pegarLivroEmprestado = Novoemprestimo.pegarLivroEmprestado();
				if (pegarLivroEmprestado) {
					emprestimoDao.atualizar(Novoemprestimo);
					JOptionPane.showMessageDialog(null, "LIVRO EMPRESTADO COM SUCESSO!");
				} else {
					JOptionPane.showMessageDialog(null, "O LIVRO NÃO ESTÁ DISPONÍVEL PARA EMPRÉSTIMO.");
				}
				em.getTransaction().commit();
				return;
			} else {
				boolean pegarLivroEmprestado = emprestimoDoLivro.pegarLivroEmprestado();
				if (pegarLivroEmprestado) {
					emprestimoDao.atualizar(emprestimoDoLivro);
					JOptionPane.showMessageDialog(null, "LIVRO EMPRESTADO COM SUCESSO!");
				} else {
					JOptionPane.showMessageDialog(null, "O LIVRO NÃO ESTÁ DISPONÍVEL PARA EMPRÉSTIMO.");
				}
			}
		} else {
			JOptionPane.showMessageDialog(null, "SEU LIMITE DE EMPRESTIMOS FOI ATINGIDO");
		}
	}

	public static void devolverLivro(Livro livro) {

		EntityManager em = JPAUtil.getEntityManager();
		EmprestimoDao emprestimoDao = new EmprestimoDao(em);

		em.getTransaction().begin();

		if (emprestimoDao.isLivroDisponivel(livro) == false) {
			Emprestimo emprestimoDoLivro = emprestimoDao.buscarSeLivroJaTemEmprestimo(livro);
			if (emprestimoDoLivro != null) {
				boolean devolverLivro = emprestimoDoLivro.devolverLivro();
				if (devolverLivro) {
					emprestimoDao.atualizar(emprestimoDoLivro);
					em.getTransaction().commit();
					JOptionPane.showMessageDialog(null, "LIVRO DEVOLVIDO COM SUCESSO!");
				} else {
					JOptionPane.showMessageDialog(null, "LIVRO TEM MULTA PENDENTE!");
				}
			} else {
				JOptionPane.showMessageDialog(null, "NÃO EXISTE EMPRESTIMO COM ESSE LIVRO!");
			}
		} else {
			JOptionPane.showMessageDialog(null, "LIVRO NÃO ESTÁ EMPRESTADO OU JA FOI DEVOLVIDO!");
		}
	}

}
