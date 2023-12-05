package br.com.gwenilorac.biblioteca.domain;

public class MultasView {
	
	private String nmUsuario;
	private String nmLivro;
	private Long diasAtrasados;
	private Double valorMulta;
	
	public MultasView(String nmUsuario, String nmLivro, Long diasAtrasados, Double valorMulta) {
		this.nmUsuario = nmUsuario;
		this.nmLivro = nmLivro;
		this.diasAtrasados = diasAtrasados;
		this.valorMulta = valorMulta;
	}

	public String getNmUsuario() {
		return nmUsuario;
	}
	
	public void setNmUsuario(String nmUsuario) {
		this.nmUsuario = nmUsuario;
	}
	
	public String getNmLivro() {
		return nmLivro;
	}
	
	public void setNmLivro(String nmLivro) {
		this.nmLivro = nmLivro;
	}
	
	public Long getDiasAtrasados() {
		return diasAtrasados;
	}
	
	public void setDiasAtrasados(Long diasAtrasados) {
		this.diasAtrasados = diasAtrasados;
	}
	
	public Double getValorMulta() {
		return valorMulta;
	}
	
	public void setValorMulta(Double valorMulta) {
		this.valorMulta = valorMulta;
	}
	
}
