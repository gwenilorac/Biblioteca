package br.com.gwenilorac.biblioteca.servicos;

import javax.persistence.EntityManager;

import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class ServicoCadastro {

	private static boolean emailCadastrado;

	public static boolean cadastraUsuario(Usuario usuario) {
		EntityManager em = JPAUtil.getEntityManager();
		UsuarioDao usuarioDao = new UsuarioDao(em);

		String nome = usuario.getNome();
		String email = usuario.getEmail();
		String senha = usuario.getSenha();

		emailCadastrado = usuarioDao.buscarEmailCadastrado(email);

		if (emailCadastrado == true) {
			return false;
		} else {
			Usuario NewUsuario = new Usuario(nome, email, senha);
			em.getTransaction().begin();
			usuarioDao.cadastrar(NewUsuario);
			em.getTransaction().commit();
			em.close();
			return true;
		}
	}

}
