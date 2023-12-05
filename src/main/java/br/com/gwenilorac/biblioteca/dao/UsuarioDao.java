package br.com.gwenilorac.biblioteca.dao;

import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import br.com.gwenilorac.biblioteca.model.Usuario;

public class UsuarioDao {

	private EntityManager em;

	public UsuarioDao(EntityManager em) {
		this.em = em;
	}

	public void cadastrar(Usuario usuario) {
		this.em.persist(usuario);
	}

	public void atualizar(Usuario usuario) {
		this.em.merge(usuario);
	}

	public void remover(Usuario usuario) {
		em.remove(em.contains(usuario) ? usuario : em.merge(usuario));
	}

	public List<Usuario> buscarTodos() {
		String jpql = "SELECT u FROM Usuario u";
		return em.createQuery(jpql, Usuario.class).getResultList();
	}

	public boolean buscarEmailCadastrado(String email) {
		try {
			String jpql = "SELECT COUNT(u) FROM Usuario u WHERE u.email = :email";
			Long count = em.createQuery(jpql, Long.class).setParameter("email", email).getSingleResult();
			return count > 0;
		} catch (Exception e) {
			return false;
		}
	}

	public Usuario buscarCredenciais(String nome, String senha) {
		try {
			String jpql = "SELECT u FROM Usuario u WHERE u.nome = :nome AND u.senha = :senha";
			return em.createQuery(jpql, Usuario.class).setParameter("nome", nome).setParameter("senha", senha)
					.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public List<Usuario> buscarUsuarios(String palavraChave) {
		try {
			String jpql = "SELECT u FROM Usuario u WHERE LOWER(u.nome) LIKE :palavraChave";
			return em.createQuery(jpql, Usuario.class)
					.setParameter("palavraChave", "%" + palavraChave.toLowerCase() + "%").getResultList();
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}
	
}
