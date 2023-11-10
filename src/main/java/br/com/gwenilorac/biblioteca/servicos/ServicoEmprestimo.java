package br.com.gwenilorac.biblioteca.servicos;

import java.util.List;
import javax.persistence.EntityManager;
import javax.swing.JOptionPane;

import br.com.gwenilorac.biblioteca.dao.EmprestimoDao;
import br.com.gwenilorac.biblioteca.dao.LivroDao;
import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.model.Emprestimo;
import br.com.gwenilorac.biblioteca.model.Estado;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.StatusEmprestimo;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class ServicoEmprestimo {
	
	private EntityManager em = JPAUtil.getEntityManager();
	private UsuarioDao usuarioDao = new UsuarioDao(em);
	private EmprestimoDao emprestimoDao = new EmprestimoDao(em);
	
	public boolean pegarLivroEmprestado(Emprestimo emprestimo) {
	    List<Livro> livrosEmprestados = usuarioDao.buscarLivrosEmprestados(ServicoLogin.getUsuarioLogado().getId());

	    if (livrosEmprestados.size() < 3) {
	        boolean pegarLivroEmprestado = emprestimo.pegarLivroEmprestado();

	        em.getTransaction().begin();
	        
	        if (pegarLivroEmprestado) {
	        	emprestimoDao.atualizar(emprestimo);
	            em.getTransaction().commit();
	            System.out.println("Livro emprestado: " + emprestimo.getLivro().getTitulo() + " por: " + ServicoLogin.getUsuarioLogado().getNome());
	            return true;
	        } else {
	        	System.out.println("LIVRO NÃO DISPONIVEL PARA EMPRESTIMO");
	            return false;
	        }
	    } else {
	    	System.out.println("LIMITE DE EMPRESTIMOS ATINGIDO");
	    	return false;
	    }
	}

	public boolean devolverLivro(Emprestimo emprestimo) {
	    em.getTransaction().begin();
	    
	    if (emprestimo.getStatus() == StatusEmprestimo.ABERTO) {
	        emprestimo.devolverLivro();
	        emprestimoDao.atualizar(emprestimo);
	        em.getTransaction().commit();
	        System.out.println("Livro devolvido: " + emprestimo.getLivro().getTitulo() + " por: " + ServicoLogin.getUsuarioLogado().getNome());
	        return true;
	    } else {
	    	return false;
	    }
	}

}
