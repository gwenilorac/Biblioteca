package br.com.gwenilorac.biblioteca.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "emprestimos")
public class Emprestimo {

	private static final int MAX_EMPRESTIMOS_PERMITIDOS = 3;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Livro livro;

	@ManyToOne(fetch = FetchType.LAZY)
	private Usuario usuario;

	protected LocalDate dataEmprestimo;
	private LocalDate dataDevolucao;

	@OneToMany
	private List<Emprestimo> emprestimos = new ArrayList<Emprestimo>();

//	@OneToMany
//	List<Livro> listaDeLivrosMaisEmprestados = obterLivrosMaisEmprestados();

	@Deprecated
	public Emprestimo() {
	}

	public Emprestimo(Livro livro, Usuario usuario) {
		this.livro = livro;
		this.usuario = usuario;
		this.dataEmprestimo = LocalDate.now();
		this.dataDevolucao = LocalDate.now().plusWeeks(4);
	}

	public Emprestimo(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean pegarLivroEmprestado(Livro livro) {
	    if (usuario.getLivrosEmprestados().size() <= MAX_EMPRESTIMOS_PERMITIDOS) {
	        if (livro.getEstado() == Estado.DISPONIVEL) {
	            livro.setEstado(Estado.INDISPONIVEL);
	            usuario.adicionarLivroEmprestado(livro);
	            System.out.println("Livro emprestado com sucesso!");
	            System.out.println("Data da Devolução do Livro: " + dataDevolucao);
	            return true;
	        } else {
	            System.out.println("O livro não está disponível para empréstimo.");
	            return false;
	        }
	    } else {
	        System.out.println("Você já atingiu o limite máximo de livros emprestados.");
	        return false;
	    }
	}

	public void devolverLivro() {
		if (livro.getEstado() == Estado.INDISPONIVEL) {
			livro.setEstado(Estado.DISPONIVEL);
			usuario.removerLivroEmprestado(livro);
			System.out.println("Livro devolvido com sucesso!");
			System.out.println("Data devolução: " + LocalDate.now());
		} else {
			System.out.println("O livro não está emprestado ou já foi devolvido.");
		}
	}

	public void DevolucaoParaExclusaoConta() {
		for (Livro livro : usuario.getLivrosEmprestados()) {
			this.livro = livro;
		}
		devolverLivro();
		System.out.println("Livros devolvidos com sucesso!");
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
		return dataDevolucao;
	}

	public void setDataDevolucao(LocalDate dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}

	public List<Livro> obterLivrosMaisEmprestados() {
		Map<Livro, Long> contagemEmprestimosPorLivro = emprestimos.stream()
				.collect(Collectors.groupingBy(Emprestimo::getLivro, Collectors.counting()));

		List<Livro> livrosMaisEmprestados = contagemEmprestimosPorLivro.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).map(Map.Entry::getKey)
				.collect(Collectors.toList());

		return livrosMaisEmprestados;
	}

}
