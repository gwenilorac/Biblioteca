package br.com.gwenilorac.biblioteca.servicos;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class ServicoCadastro {

	public static boolean cadastraUsuario(Usuario usuario) {
	    if (usuario == null || usuario.getEmail() == null || usuario.getSenha() == null) {
	        throw new IllegalArgumentException("Usuário, e-mail e senha não podem ser nulos.");
	    }

	    EntityManager em = JPAUtil.getEntityManager();
	    UsuarioDao usuarioDao = new UsuarioDao(em);

	    try {
	        EntityTransaction transaction = em.getTransaction();
	        transaction.begin();

	        if (usuarioDao.buscarEmailCadastrado(usuario.getEmail())) {
	            transaction.rollback(); 
	            return false;
	        } else {
	            Usuario newUsuario = new Usuario(usuario.getNome(), usuario.getEmail(), usuario.getSenha());
	            usuarioDao.cadastrar(newUsuario);

	            em.flush();

	            transaction.commit();
	            return true; 
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    } finally {
	        if (em != null && em.isOpen()) {
	            em.close();
	        }
	    }
	}
}
