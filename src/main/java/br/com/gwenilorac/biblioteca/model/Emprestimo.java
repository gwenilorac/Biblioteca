package br.com.gwenilorac.biblioteca.model;

import java.time.LocalDate;
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
import javax.persistence.Table;

import br.com.gwenilorac.biblioteca.dao.LivroDao;
import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

@Entity
@Table(name = "emprestimos")
public class Emprestimo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Livro livro;

	@ManyToOne(fetch = FetchType.LAZY)
	private Usuario usuario;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StatusEmprestimo status = StatusEmprestimo.ENCERRADO;

	protected LocalDate dataEmprestimo;
	private LocalDate dataDevolucao;
	
	@Deprecated
	public Emprestimo() {
	}

	public Emprestimo(Livro livro, Usuario usuario) {
		this.livro = livro;
		this.usuario = usuario;
		this.dataEmprestimo = LocalDate.now();
		this.dataDevolucao = LocalDate.now().plusWeeks(4);
		this.status = StatusEmprestimo.ENCERRADO;
	}

	public Emprestimo(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean pegarLivroEmprestado() {
	    EntityManager em = JPAUtil.getEntityManager();
	    LivroDao livroDao = new LivroDao(em);

	    em.getTransaction().begin();

	    if (livroDao.isLivroDisponivel(livro)) {
	        setStatus(StatusEmprestimo.ABERTO);
	        System.out.println("Livro emprestado com sucesso!");
	        System.out.println("Data da Devolução do Livro: " + dataDevolucao);
	        em.getTransaction().commit();
	        return true;
	    } else {
	        System.out.println("O livro não está disponível para empréstimo.");
	        em.getTransaction().rollback();
	        return false;
	    }
	}


	public void devolverLivro() {
		EntityManager em = JPAUtil.getEntityManager();
		LivroDao livroDao = new LivroDao(em);
		
        em.getTransaction().begin();
		
		if (livroDao.isLivroDisponivel(livro) == false) {
			setStatus(StatusEmprestimo.ENCERRADO);
			System.out.println("Livro devolvido com sucesso!");
			System.out.println("Data devolução: " + LocalDate.now());
			em.getTransaction().commit();
		} else {
			System.out.println("O livro não está emprestado ou já foi devolvido.");
			 em.getTransaction().rollback();
		}
	}

	public void DevolucaoParaExclusaoConta() {
		EntityManager em = JPAUtil.getEntityManager();
		UsuarioDao usuarioDao = new UsuarioDao(em);
		List<Livro> livrosEmprestados = usuarioDao.buscarLivrosEmprestados(usuario.getId());
		for (int i = 0; i < livrosEmprestados.size(); i++) {
			devolverLivro();
		}
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

	public StatusEmprestimo getStatus() {
		return this.status;
	}

	public void setStatus(StatusEmprestimo status) {
		this.status = status;
	}

}
