package br.com.gwenilorac.biblioteca.servicos;

import javax.persistence.EntityManager;

import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class ServicoLogin {

	private static Usuario usuarioLogado;
	private static boolean isUsuario;

	public static boolean validaUsuario(Usuario usuario) {
		EntityManager em = JPAUtil.getEntityManager();
		UsuarioDao usuarioDao = new UsuarioDao(em);

		String nome = usuario.getNome();
		String senha = usuario.getSenha();

		Usuario credenciais = usuarioDao.buscarCredenciais(nome, senha);

		if (credenciais == null) {
			System.out.println("Usuario invalido!");
			return false;
		} else {
			usuarioLogado = usuario;
			System.out.println("Usuario valido!");
			return true;
		}
	}

	public static void logout() {
		usuarioLogado = null;
	}

	public static boolean isUsuarioLogado() {
		return usuarioLogado != null;
	}

	public static boolean isUsuario() {
		return isUsuario;
	}

	public static Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

}
