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

		try {
			em.getTransaction().begin();

			String nome = usuario.getNome();
			String senha = usuario.getSenha();
			
			Usuario credenciais = usuarioDao.buscarCredenciais(nome, senha);

			if (credenciais != null) {
				usuarioLogado = credenciais;
				em.getTransaction().commit();
				System.out.println("Usuário válido!");
				return true;
			} else {
				System.out.println("Usuário inválido!");
				return false;
			}
		} catch (Exception e) {
			System.out.println("Erro durante a validação do usuário: " + e.getMessage());
			em.getTransaction().rollback();
			return false;
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
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