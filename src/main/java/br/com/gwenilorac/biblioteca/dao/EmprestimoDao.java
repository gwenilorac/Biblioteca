package br.com.gwenilorac.biblioteca.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import br.com.gwenilorac.biblioteca.domain.EmprestimoView;
import br.com.gwenilorac.biblioteca.domain.MultasView;
import br.com.gwenilorac.biblioteca.domain.UsuariosView;
import br.com.gwenilorac.biblioteca.model.Emprestimo;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.StatusEmprestimo;
import br.com.gwenilorac.biblioteca.model.Usuario;

public class EmprestimoDao {

	private EntityManager em;

	public EmprestimoDao(EntityManager em) {
		this.em = em;
	}

	public void cadastrar(Emprestimo emprestimo) {
		this.em.persist(emprestimo);
	}

	public void atualizar(Emprestimo emprestimo) {
		this.em.merge(emprestimo);
	}

	public void remover(Emprestimo emprestimo) {
		this.em.remove(emprestimo);
	}

	public boolean isLivroEmprestado(Livro livro) {
		try {
			String jpql = "SELECT e FROM Emprestimo e WHERE e.livro = :livro AND e.status = :status";
			return !em.createQuery(jpql, Emprestimo.class).setParameter("livro", livro)
					.setParameter("status", StatusEmprestimo.ABERTO).getResultList().isEmpty();
		} catch (Exception e) {
			return false;
		}
	}

	public Emprestimo buscarSeLivroJaTemEmprestimo(Livro livro) {
		try {
			String jpql = "SELECT e FROM Emprestimo e WHERE e.livro = :livro";
			List<Emprestimo> resultados = em.createQuery(jpql, Emprestimo.class).setParameter("livro", livro)
					.getResultList();

			if (!resultados.isEmpty()) {
				return resultados.get(0);
			} else {
				System.out.println("Nenhum empréstimo encontrado para o livro: " + livro.getTitulo());
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	 public List<Emprestimo> buscarEmprestimosUser(Long idUsuario) {
		    StringBuilder hql = new StringBuilder();
		    hql.append("SELECT empr ");
		    hql.append("FROM Emprestimo empr ");
		    hql.append("WHERE empr.status = :status ");
		    hql.append("AND empr.usuario.id = :idUsuario ");

		    return em.createQuery(hql.toString(), Emprestimo.class)
		            .setParameter("status", StatusEmprestimo.ABERTO)
		            .setParameter("idUsuario", idUsuario)
		            .getResultList();
		}

	public boolean EmprestimoEstaAberto(Emprestimo emprestimo) {
		try {
			String jpql = "SELECT e FROM Emprestimo e WHERE e.status = :status";
			return em.createQuery(jpql, Emprestimo.class).setParameter("status", StatusEmprestimo.ABERTO)
					.getResultList().get(0) != null;
		} catch (Exception e) {
			return false;
		}
	}

	public Emprestimo buscarSeLivroEstaEmprestadoPorUser(Usuario usuario, Livro livro) {
		try {
			String jpql = "SELECT e FROM Emprestimo e WHERE e.usuario = :usuario AND e.livro = :livro AND e.status = :status";
			return em.createQuery(jpql, Emprestimo.class).setParameter("usuario", usuario).setParameter("livro", livro)
					.setParameter("status", StatusEmprestimo.ABERTO).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public boolean esteUsuarioEstaComOEmprestimoDesteLivroEncerrado(Livro livro, Usuario usuario) {
		try {
			String jpql = "SELECT e FROM Emprestimo e WHERE e.livro = :livro AND e.usuario = :usuario AND e.status = :status";
			return em.createQuery(jpql, Emprestimo.class).setParameter("livro", livro).setParameter("usuario", usuario)
					.setParameter("status", StatusEmprestimo.ENCERRADO).getResultList().get(0) != null;
		} catch (Exception e) {
			return false;
		}
	}
	
	public Emprestimo buscarQuemEstaComLivro(Livro livro) {
		String jpql = "SELECT e.usuario FROM Emprestimo e WHERE e.livro = :livro AND e.status :status";
		return em.createQuery(jpql, Emprestimo.class)
				.setParameter("livro", livro)
				.setParameter("status", StatusEmprestimo.ABERTO)
				.getSingleResult();
	}
	
	public Emprestimo buscarSeLivroJaTemEmprestimoRelacionadoAUsuario(Livro livro, Usuario usuario) {
		try {
			String jpql = "SELECT e FROM Emprestimo e WHERE e.livro = :livro AND e.usuario = :usuario";
			TypedQuery<Emprestimo> query = em.createQuery(jpql, Emprestimo.class);
			query.setParameter("livro", livro);
			query.setParameter("usuario", usuario);

			List<Emprestimo> resultados = query.getResultList();

			if (!resultados.isEmpty()) {
				return resultados.get(0);
			} else {
				System.out.println("Nenhum empréstimo encontrado para o livro: " + livro.getTitulo());
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<EmprestimoView> findEmprestimosReporList() {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT new br.com.gwenilorac.biblioteca.domain.EmprestimoView(livr.titulo ");
		hql.append(", auto.nome ");
		hql.append(", count(e.id) ) ");
		hql.append("FROM Emprestimo e ");
		hql.append("INNER JOIN e.livro livr ");
		hql.append("INNER JOIN livr.autor auto ");
		hql.append("GROUP BY livr.titulo");
		hql.append(", auto.nome ");
		hql.append("ORDER BY count(e.id) DESC");
		TypedQuery<EmprestimoView> createQuery = em.createQuery(hql.toString(), EmprestimoView.class);
		return createQuery.getResultList();
	}

	public List<MultasView> findMultasReportList() {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT new br.com.gwenilorac.biblioteca.domain.MultasView(user.nome ");
		hql.append(", livr.titulo ");
		hql.append(", count(e.diasAtrasados) ");
		hql.append(", e.valorMulta) ");
		hql.append("FROM Emprestimo e ");
		hql.append("INNER JOIN e.usuario user ");
		hql.append("INNER JOIN e.livro livr ");
		hql.append("GROUP BY user.nome");
		hql.append(", livr.titulo ");
		hql.append(", e.valorMulta ");
		hql.append("ORDER BY count(e.diasAtrasados) DESC");
		TypedQuery<MultasView> createQuery = em.createQuery(hql.toString(), MultasView.class);
		return createQuery.getResultList();
	}

	public List<UsuariosView> findUsuarioReportList() {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT new br.com.gwenilorac.biblioteca.domain.UsuariosView(user.nome ");
		hql.append(", user.email ");
		hql.append(", count(e.id) ) ");
		hql.append("FROM Emprestimo e ");
		hql.append("INNER JOIN e.usuario user ");
		hql.append("GROUP BY user.nome");
		hql.append(", user.email ");
		hql.append("ORDER BY count(e.id) DESC");
		TypedQuery<UsuariosView> createQuery = em.createQuery(hql.toString(), UsuariosView.class);
		return createQuery.getResultList();
	}

}
