package br.com.gwenilorac.biblioteca.domain;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.gwenilorac.biblioteca.dao.EstatisticasDao;

public class EmprestimoView {

	private EntityManager em;
	private EstatisticasDao estatisticasDao;

	private String nmLivro;
	private String nmAutor;
	private Long numEmprestimos;

	public EmprestimoView() {
		getNmLivro();
		getNmAutor();
		getNumEmprestimos();
	}

	public List<String> getNmLivro() {
		String jpql = "SELECT e.livro.titulo FROM Emprestimo e GROUP BY e.livro.titulo ORDER BY COUNT(e) DESC";
		return em.createQuery(jpql, String.class).getResultList();
	}

	public void setNmLivro(String nmLivro) {
		this.nmLivro = nmLivro;
	}

	public List<String> getNmAutor() {
		String jpql = "SELECT e.livro.autor FROM Emprestimo e GROUP BY e.livro.autor ORDER BY COUNT(e) DESC";
		return em.createQuery(jpql, String.class).getResultList();
	}

	public void setNmAutor(String nmAutor) {
		this.nmAutor = nmAutor;
	}

	public List<Long> getNumEmprestimos() {
		String jpql = "SELECT COUNT(e) FROM Emprestimo e GROUP BY e.livro ORDER BY COUNT(e) DESC";
		return em.createQuery(jpql, Long.class).getResultList();
	}

	public void setNumEmprestimos(Long numEmprestimos) {
		this.numEmprestimos = numEmprestimos;
	}

}
