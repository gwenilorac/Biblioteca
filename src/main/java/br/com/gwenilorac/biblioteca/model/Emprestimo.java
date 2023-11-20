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

import org.springframework.format.annotation.DateTimeFormat;

import br.com.gwenilorac.biblioteca.dao.EmprestimoDao;
import br.com.gwenilorac.biblioteca.dao.LivroDao;
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
	private StatusEmprestimo status = StatusEmprestimo.ENCERRADO;

	protected LocalDate dataEmprestimo;
	private LocalDate dataDevolucaoLivro;
	private LocalDate dataAtual;
	private double valorMulta;
	private static final double VALOR_MULTA = 5.0;
	private boolean multaPaga = false;
	private TemMulta temMulta = TemMulta.INEXISTENTE;

	@Deprecated
	public Emprestimo() {
	}

	public Emprestimo(Livro livro, Usuario usuario) {
		this.livro = livro;
		this.usuario = usuario;
		this.dataEmprestimo = LocalDate.now();
		this.dataDevolucaoLivro = LocalDate.now().plusWeeks(4);
		this.status = StatusEmprestimo.ENCERRADO;
	}

	public boolean pegarLivroEmprestado() {
		EntityManager em = JPAUtil.getEntityManager();

		em.getTransaction().begin();

		if (status == StatusEmprestimo.ENCERRADO) {
			this.setStatus(StatusEmprestimo.ABERTO);
			this.livro.setEstado(Estado.INDISPONÍVEL);
			System.out.println("Livro emprestado com sucesso!");
			System.out.println("Data da Devolução do Livro: " + dataDevolucaoLivro);
			em.getTransaction().commit();
			return true;
		} else {
			System.out.println("O livro não está disponível para empréstimo.");
			em.getTransaction().rollback();
			return false;
		}
	}

	public boolean devolverLivro() {
		EntityManager em = JPAUtil.getEntityManager();
		EmprestimoDao emprestimoDao = new EmprestimoDao(em);

		em.getTransaction().begin();

		if (emprestimoDao.isLivroDisponivel(livro) == false) {
			if (isMultaValid() == false) {
				setStatus(StatusEmprestimo.ENCERRADO);
				getLivro().setEstado(Estado.DISPONÍVEL);
				System.out.println("Livro devolvido com sucesso!");
				System.out.println("Data devolução: " + LocalDate.now());
				em.getTransaction().commit();
				return true;
			} else {
				temMulta = TemMulta.PENDENTE;
				return false;
			}
		} else {
			System.out.println("O livro não está emprestado ou já foi devolvido.");
			em.getTransaction().rollback();
			return false;
		}
	}

	public boolean DevolucaoParaExclusaoConta() {
		if (isMultaValid() == false) {
			EntityManager em = JPAUtil.getEntityManager();
			UsuarioDao usuarioDao = new UsuarioDao(em);
			List<Livro> livrosEmprestados = usuarioDao.buscarLivrosEmprestados(usuario.getId());
			for (int i = 0; i < livrosEmprestados.size(); i++) {
				devolverLivro();
			}
			System.out.println("Livros devolvidos com sucesso!");
			return true;
		} else {
			temMulta = TemMulta.PENDENTE;
			return false;
		}
	}

	public boolean isMultaValid() {
		if (!multaPaga) {
			dataAtual = LocalDate.now();
			if (dataAtual.isAfter(dataDevolucaoLivro)) {
				long diasAtraso = dataDevolucaoLivro.until(dataAtual).getDays();
				valorMulta = diasAtraso * VALOR_MULTA;
				if (valorMulta > 0.0) {
					return true;
				} else {
					System.out.println("Não há multa a ser paga.");
					return false;
				}
			} else {
				System.out.println("Livro devolvido antes da data de devolução");
				return false;
			}
		} else {
			System.out.println("Multa já foi paga anteriormente.");
			return false;
		}
	}

	public void pagarMulta() {
		multaPaga = true;
		temMulta = TemMulta.INEXISTENTE;
		System.out.println("Multa paga com sucesso. Valor: R$ " + valorMulta);
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

	public String getDataDevolucaoFormatted() {
		if (dataDevolucaoLivro == null) return null;
		return dataDevolucaoLivro.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}
	

}
