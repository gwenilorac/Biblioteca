package br.com.gwenilorac.biblioteca.model;

import java.io.Serializable;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "livros")
public class Livro implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String titulo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "autor_id")
    private Autor autor;
	
	@Column(name = "ano_publicacao")
	private Year anoPublicacao;
	
	@Enumerated(EnumType.STRING) 
    @Column(nullable = false)
    private Estado estado = Estado.DISPONIVEL;

	@ManyToOne
    @JoinColumn(name = "genero_id")
    private Genero genero;
	
	
	@Deprecated
	public Livro() {
	}

	public Livro(String titulo, Autor autor, Genero genero, Year anoPublicacao) {
		this.titulo = titulo;
		this.autor = autor;
		this.genero = genero;
		this.anoPublicacao = anoPublicacao;
		this.estado = Estado.DISPONIVEL;
		
		this.adicionarLivroNaListaDoAutor(this);

	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Autor getAutor() {
		return autor;
	}

	public void setAutor(Autor autor) {
		this.autor = autor;
	}

	public Genero getGenero() {
		return genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
	}

	public Year getAnoPublicacao() {
		return anoPublicacao;
	}

	public void setAnoPublicacao(Year anoPublicacao) {
		this.anoPublicacao = anoPublicacao;
	}
	
	public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void adicionarLivroNaListaDoAutor(Livro livro) {
        if (livro.getAutor() != null) {
            List<Livro> livrosDoAutor = livro.getAutor().getLivrosDoAutor();

            if (livrosDoAutor == null) {
                livrosDoAutor = new ArrayList<>();
                livro.getAutor().setLivrosDoAutor(livrosDoAutor);
            }

            livrosDoAutor.add(livro);
        }
    }
    
}
