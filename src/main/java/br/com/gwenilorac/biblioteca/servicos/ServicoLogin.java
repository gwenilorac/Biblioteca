package br.com.gwenilorac.biblioteca.servicos;

import javax.persistence.EntityManager;
import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class ServicoLogin {

	private static Usuario usuarioLogado;

	public static boolean validaUsuario(Usuario usuario) {
		EntityManager em = JPAUtil.getEntityManager();
		UsuarioDao usuarioDao = new UsuarioDao(em);

		String nome = usuario.getNome();
		String senha = usuario.getSenha();
		
		em.getTransaction().begin();

		Usuario credenciais = usuarioDao.buscarCredenciais(nome, senha);

		if (credenciais != null) {
			Usuario usuarioAtualizado = credenciais;
			usuario = usuarioAtualizado;
			usuarioLogado = usuario;
			em.getTransaction().commit();
			System.out.println("Usuario valido!");
			return true;
		} else {
			System.out.println("Usuario invalido!");
			em.getTransaction().rollback();
			return false;
		}
	}

	public static void logout() {
		usuarioLogado = null;
	}

	public static boolean isUsuarioLogado() {
		return usuarioLogado != null;
	}

	public static Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

}
