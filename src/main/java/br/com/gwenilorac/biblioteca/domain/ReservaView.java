package br.com.gwenilorac.biblioteca.domain;

public class ReservaView {
	
	private String nmLivro;
	private String nmAutor;
	private String nmUsuario;
	
	public ReservaView(String nmLivro, String nmAutor, String nmUsuario) {
		this.nmLivro = nmLivro;
		this.nmAutor = nmAutor;
		this.nmUsuario = nmUsuario;
	}

	public String getNmLivro() {
		return nmLivro;
	}
	
	public void setNmLivro(String nmLivro) {
		this.nmLivro = nmLivro;
	}
	
	public String getNmUsuario() {
		return nmUsuario;
	}
	
	public void setNmUsuario(String nmUsuario) {
		this.nmUsuario = nmUsuario;
	}

	public String getNmAutor() {
		return nmAutor;
	}

	public void setNmAutor(String nmAutor) {
		this.nmAutor = nmAutor;
	}

}
