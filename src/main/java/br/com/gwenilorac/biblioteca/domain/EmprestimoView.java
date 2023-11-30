package br.com.gwenilorac.biblioteca.domain;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.gwenilorac.biblioteca.dao.EstatisticasDao;
import br.com.gwenilorac.biblioteca.model.Emprestimo;

public class EmprestimoView {
	
	private EntityManager em;
	private EstatisticasDao estatisticasDao = new EstatisticasDao(em);
	
	private Integer idLivro;
	private String nmLivro;
	private String nmAutor;
	private Integer numEmprestimos;
	
	public List<Emprestimo> getIdLivro() {
		return estatisticasDao.buscarIdDosLivrosMaisEmprestados();
	}
	public void setIdLivro(Integer idLivro) {
		this.idLivro = idLivro;
	}
	public List<Emprestimo> getNmLivro() {
		return estatisticasDao.buscarTitulosDosLivrosMaisEmprestados();
	}
	public void setNmLivro(String nmLivro) {
		this.nmLivro = nmLivro;
	}
	public List<Emprestimo> getNmAutor() {
		return estatisticasDao.buscarAutorDosLivrosMaisEmprestados();
	}
	public void setNmAutor(String nmAutor) {
		this.nmAutor = nmAutor;
	}
	public List<Emprestimo> getNumEmprestimos() {
		return estatisticasDao.buscarNumeroDeEmprestimosDosLivrosMaisEmprestados();
	}
	public void setNumEmprestimos(Integer numEmprestimos) {
		this.numEmprestimos = numEmprestimos;
	}
	
}
