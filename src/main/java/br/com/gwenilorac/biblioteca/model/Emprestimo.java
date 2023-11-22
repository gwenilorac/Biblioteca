package br.com.gwenilorac.biblioteca.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

@Entity
@Table(name = "emprestimos")
public class Emprestimo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne(fetch = FetchType.LAZY)
	private Livro livro;

	@ManyToOne(fetch = FetchType.LAZY)
	private Usuario usuario;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StatusEmprestimo status;

	@Column(nullable = false)
	protected LocalDate dataEmprestimo;

	@Column(nullable = false)
	private LocalDate dataDevolucaoLivro;

	private LocalDate dataAtual;

	@Column(nullable = false)
	private double valorMulta;

	private static final double VALOR_MULTA = 5.0;

	@Column(nullable = false)
	private boolean multaPaga;

	@Column(nullable = false)
	private TemMulta temMulta;

	@Deprecated
	public Emprestimo() {
	}

	public Emprestimo(Livro livro, Usuario usuario) {
		this.livro = livro;
		this.usuario = usuario;
		this.dataEmprestimo = LocalDate.now();
		this.dataDevolucaoLivro = LocalDate.now().plusWeeks(4);
		this.status = StatusEmprestimo.ENCERRADO;
		this.multaPaga = false;
		this.temMulta = TemMulta.INEXISTENTE;
	}

	public void pegarLivroEmprestado() {
			setStatus(StatusEmprestimo.ABERTO);
			System.out.println("Livro emprestado com sucesso!");
			System.out.println("Data da Devolução do Livro: " + dataDevolucaoLivro);
	}

	public void devolverLivro() {
	    setStatus(StatusEmprestimo.ENCERRADO);
	    System.out.println("Livro devolvido com sucesso!");
	    System.out.println("Data devolução: " + LocalDate.now());
	    temMulta = TemMulta.INEXISTENTE;
	}

	public boolean devolucaoParaExclusaoConta() {
			EntityManager em = JPAUtil.getEntityManager();
			UsuarioDao usuarioDao = new UsuarioDao(em);
			List<Livro> livrosEmprestados = usuarioDao.buscarLivrosEmprestados(usuario.getId());
			for (int i = 0; i < livrosEmprestados.size(); i++) {
				devolverLivro();
			}
			System.out.println("Livros devolvidos com sucesso!");
			return true;
	}

	public Livro getLivro() {
		return livro;
	}

	public void setLivro(Livro livro) {
		this.livro = livro;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public LocalDate getDataEmprestimo() {
		return dataEmprestimo;
	}

	public void setDataEmprestimo(LocalDate dataEmprestimo) {
		this.dataEmprestimo = dataEmprestimo;
	}

	public LocalDate getDataDevolucao() {
		return dataDevolucaoLivro;
	}

	public void setDataDevolucao(LocalDate dataDevolucao) {
		this.dataDevolucaoLivro = dataDevolucao;
	}

	public StatusEmprestimo getStatus() {
		return this.status;
	}

	public void setStatus(StatusEmprestimo status) {
		this.status = status;
	}

	public boolean isMultaPaga() {
		return multaPaga;
	}

	public TemMulta getTemMulta() {
		return temMulta;
	}

	public void setTemMulta(TemMulta temMulta) {
		this.temMulta = temMulta;
	}
	
	public LocalDate getDataDevolucaoLivro() {
		return dataDevolucaoLivro;
	}

	public void setDataDevolucaoLivro(LocalDate dataDevolucaoLivro) {
		this.dataDevolucaoLivro = dataDevolucaoLivro;
	}

	public LocalDate getDataAtual() {
		return dataAtual;
	}

	public void setDataAtual(LocalDate dataAtual) {
		this.dataAtual = dataAtual;
	}

	public void setValorMulta(double valorMulta) {
		this.valorMulta = valorMulta;
	}

	public double getValorMulta() {
		return VALOR_MULTA;
	}

	public void setMultaPaga(boolean multaPaga) {
		this.multaPaga = multaPaga;
	}

	public String getDataDevolucaoFormatted() {
		if (dataDevolucaoLivro == null)
			return null;
		return dataDevolucaoLivro.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}
