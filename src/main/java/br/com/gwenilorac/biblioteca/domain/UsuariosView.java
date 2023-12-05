package br.com.gwenilorac.biblioteca.domain;

public class UsuariosView {
	
	private String nmUsuario;
	private String userEmail;
	private Long nmEmprestimos;
	
	public UsuariosView(String nmUsuario, String userEmail, Long nmEmprestimos) {
		this.nmUsuario = nmUsuario;
		this.userEmail = userEmail;
		this.nmEmprestimos = nmEmprestimos;
	}

	public String getNmUsuario() {
		return nmUsuario;
	}
	
	public void setNmUsuario(String nmUsuario) {
		this.nmUsuario = nmUsuario;
	}
	
	public String getUserEmail() {
		return userEmail;
	}
	
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	public Long getNmEmprestimos() {
		return nmEmprestimos;
	}
	
	public void setNmEmprestimos(Long nmEmprestimos) {
		this.nmEmprestimos = nmEmprestimos;
	}
	
}
