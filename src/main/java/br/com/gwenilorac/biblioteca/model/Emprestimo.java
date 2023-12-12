package br.com.gwenilorac.biblioteca.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
    protected LocalDate dataEmprestimo = LocalDate.now();

    @Column(nullable = false)
    private LocalDate dataDevolucaoLivro = LocalDate.now().plusWeeks(4);

    @Column(name = "dias_atrasados")
    private Long diasAtrasados;

    @Column(nullable = false)
    private double valorMulta;

    @Column(nullable = false)
    private boolean multaPaga;

    @Column(nullable = false)
    private boolean temMulta;

    private static final double VALOR_MULTA_POR_DIA = 5.0;

    @Deprecated
    public Emprestimo() {
    }

    public Emprestimo(Livro livro, Usuario usuario) {
        this.livro = livro;
        this.usuario = usuario;
        this.status = StatusEmprestimo.ENCERRADO;
        this.multaPaga = false;
        this.temMulta = false;
        this.diasAtrasados = null;
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

	public boolean getTemMulta() {
		return temMulta;
	}

	public void setTemMulta(boolean temMulta) {
		this.temMulta = temMulta;
	}
	
	public LocalDate getDataDevolucaoLivro() {
		return dataDevolucaoLivro;
	}

	public void setDataDevolucaoLivro(LocalDate dataDevolucaoLivro) {
		this.dataDevolucaoLivro = dataDevolucaoLivro;
	}

	public void setMultaPaga(boolean multaPaga) {
		this.multaPaga = multaPaga;
	}

	public String getDataDevolucaoFormatted() {
		if (dataDevolucaoLivro == null)
			return null;
		return dataDevolucaoLivro.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

	public double getValorMulta() {
	    return valorMulta; 
	}

	public void setValorMulta(double valorMulta) {
		this.valorMulta = valorMulta;
	}
	
	public long getDiasAtrasados() {
		return diasAtrasados;
	}

	public void setDiasAtrasados(long diasAtrasados) {
		this.diasAtrasados = diasAtrasados;
	}

	public double getValormultapordia() {
		return VALOR_MULTA_POR_DIA;
	}
	
}
