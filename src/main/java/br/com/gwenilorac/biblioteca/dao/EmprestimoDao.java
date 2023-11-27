package br.com.gwenilorac.biblioteca.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

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

	public boolean isLivroDisponivel(Livro livro) {
		try {
			String jpql = "SELECT e FROM Emprestimo e WHERE e.livro = :livro AND e.status = :status";
			return em.createQuery(jpql, Emprestimo.class)
					.setParameter("livro", livro)
					.setParameter("status", StatusEmprestimo.ENCERRADO)
					.getResultList().get(0) != null;
		} catch (Exception e) {
			return false;
		}
	}

	public Emprestimo buscarSeLivroJaTemEmprestimo(Livro livro) {
	    try {
	        String jpql = "SELECT e FROM Emprestimo e WHERE e.livro = :livro";
	        List<Emprestimo> resultados = em.createQuery(jpql, Emprestimo.class)
	                .setParameter("livro", livro)
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
	    String jpql = "SELECT e FROM Emprestimo e WHERE e.usuario.id = :idUsuario AND e.status = :status";
	    return em.createQuery(jpql, Emprestimo.class)
	             .setParameter("idUsuario", idUsuario)
	             .setParameter("status", StatusEmprestimo.ABERTO)
	             .getResultList();
	}
	
	public boolean EmprestimoEstaAberto(Emprestimo emprestimo) {
		try {
		  String jpql = "SELECT e FROM Emprestimo e WHERE e.status = :status";
		    return em.createQuery(jpql, Emprestimo.class)
		             .setParameter("status", StatusEmprestimo.ABERTO)
		             .getResultList().get(0) != null;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean buscarSeLivroEstaEmprestadoPorUser(Usuario usuario, Livro livro) {
	    try {
	        String jpql = "SELECT COUNT(e) FROM Emprestimo e WHERE e.usuario = :usuario AND e.livro = :livro AND e.status = 'ABERTO'";
	        Long count = em.createQuery(jpql, Long.class)
	                .setParameter("usuario", usuario)
	                .setParameter("livro", livro)
	                .getSingleResult();

	        return count > 0;
	    } catch (Exception e) {
	        return false;
	    }
	}


	
}
