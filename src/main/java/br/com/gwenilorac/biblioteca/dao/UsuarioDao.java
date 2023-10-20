package br.com.gwenilorac.biblioteca.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import br.com.gwenilorac.biblioteca.model.Livro;
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
		if(usuario.getLivrosEmprestados().isEmpty()) {
			this.em.remove(usuario);
			System.out.println("Usuario excluido com sucesso!");
		} else {
			System.out.println("Por favor devolver livros emprestados!");
		}
	}

	public List<Usuario> buscarTodos() {
		String jpql = "SELECT u FROM Usuario u";
		return em.createQuery(jpql, Usuario.class)
				.getResultList();
	}
	
	public Usuario buscarPorNome(String nome) {
		try {
			String jpql = "SELECT u FROM Usuario u WHERE u.nome = :nome";
			return em.createQuery(jpql, Usuario.class)
					.setParameter("nome", nome)
					.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public Usuario buscarCredenciais(String nome, String senha) {
		try {
			String jpql = "SELECT u FROM Usuario u WHERE u.nome = :nome AND u.senha = :senha";
			return em.createQuery(jpql, Usuario.class)
					.setParameter("nome", nome)
					.setParameter("senha", senha)
					.getResultList().get(0);
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean buscarEmailCadastrado(String email) {
		try {
		String jpql = "SELECT u FROM Usuario u WHERE u.email = :email";
		return em.createQuery(jpql, Usuario.class)
				.setParameter("email", email)
				.getResultList().get(0) != null;
		} catch (Exception e) {
			return false;
		}
	}
	
	public Usuario buscarUsuarioPorNome(String nome) {
		try {
			String jpql = "SELECT u FROM Usuario u WHERE u.nome = :nome";
			return em.createQuery(jpql, Usuario.class)
					.setParameter("nome", nome)
					.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<Livro> buscarLivrosEmprestadosPorUsuario(Usuario usuario) {
	    String jpql = "SELECT livro FROM Usuario u JOIN u.livrosEmprestados livro WHERE u = :usuario";
	    return em.createQuery(jpql, Livro.class)
	             .setParameter("usuario", usuario)
	             .getResultList();
	}
	
	 public List<Usuario> buscarUsuariosComMaisEmprestimos(int quantidade) {
	        String jpql = "SELECT u FROM Usuario u ORDER BY SIZE(u.livrosEmprestados) DESC";
	        TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);
	        query.setMaxResults(quantidade);
	        return query.getResultList();
	    }
	
}
