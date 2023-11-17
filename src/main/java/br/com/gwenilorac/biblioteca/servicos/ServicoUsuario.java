package br.com.gwenilorac.biblioteca.servicos;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.gwenilorac.biblioteca.dao.EmprestimoDao;
import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.model.Emprestimo;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class ServicoUsuario {

	public static boolean removerUsuario(Usuario usuario) {
	    EntityManager em = JPAUtil.getEntityManager();
	    EmprestimoDao emprestimoDao = new EmprestimoDao(em);
	    UsuarioDao usuarioDao = new UsuarioDao(em);
	    List<Livro> livrosEmprestados = usuarioDao.buscarLivrosEmprestados(usuario.getId());
	    List<Emprestimo> emprestimosUser = emprestimoDao.buscarEmprestimosUser(usuario.getId());

	    if (emprestimosUser.isEmpty() || livrosEmprestados.isEmpty()) {  
	        try {
	        	usuario = em.merge(usuario); 
	            em.getTransaction().begin();
	            usuarioDao.remover(usuario);
	            em.getTransaction().commit();
	            return true;
	        } catch (Exception e) {
	            if (em.getTransaction().isActive()) {
	                em.getTransaction().rollback();
	            }
	            e.printStackTrace();
	            return false;
	        } finally {
	            em.close();
	        }
	    } else {
	        return false;
	    }
	}

}
