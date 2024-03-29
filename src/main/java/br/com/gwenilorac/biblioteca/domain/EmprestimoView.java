package br.com.gwenilorac.biblioteca.domain;

public class EmprestimoView {

	private String nmLivro;
	private String nmAutor;
	private Long numEmprestimos;
	
	public EmprestimoView(String nmLivro, String nmAutor, Long numEmprestimos) {
		this.nmLivro = nmLivro;
		this.nmAutor = nmAutor;
		this.numEmprestimos = numEmprestimos;
	}

	public String getNmLivro() {
		return nmLivro;
	}
	
	public void setNmLivro(String nmLivro) {
		this.nmLivro = nmLivro;
	}

	public String getNmAutor() {
		return nmAutor;
	}
	
	public void setNmAutor(String nmAutor) {
		this.nmAutor = nmAutor;
	}

	public Long getNumEmprestimos() {
		return numEmprestimos;
	}
	
	public void setNumEmprestimos(Long numEmprestimos) {
		this.numEmprestimos = numEmprestimos;
	}

}
