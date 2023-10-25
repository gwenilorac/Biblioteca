package br.com.gwenilorac.biblioteca.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.Table;

import org.hibernate.annotations.Type;


@Entity
@Table(name = "livros")
public class Livro implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String titulo;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "autor_id")
    private Autor autor;
	
	@Enumerated(EnumType.STRING) 
    @Column(nullable = false)
    private Estado estado = Estado.DISPONIVEL;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "genero_id")
    private Genero genero;
	
	@Type(type="org.hibernate.type.BinaryType")
	@Column(name = "capa")
	private byte[] capa;
	
	public Livro() {
	}

	public Livro(String titulo, Autor autor, Genero genero, byte[] capa) {
		this.titulo = titulo;
		this.autor = autor;
		this.genero = genero;
		this.estado = Estado.DISPONIVEL;
		this.capa = capa;
		
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

	public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
    
    public byte[] getCapa() {
		return capa;
	}

	public void setCapa(byte[] capa) {
		this.capa = capa;
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
	
	  @Override
	    public String toString() {
	        return "Livro{" + 
	                "Titulo = " + titulo + '\'' + 
	                "Autor = " + autor + '\'' +
	                "Genero = " + genero + '\'' +
	                '}';
	    }

}
