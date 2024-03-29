package br.com.gwenilorac.biblioteca.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "autor_id")
	private Autor autor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "genero_id")
	private Genero genero;
	
	@Type(type="org.hibernate.type.BinaryType")
	@Column(name = "capa")
	private byte[] capa;
	
	@Transient
	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
	
	public void addPropertyChangeListener(PropertyChangeListener l) {
		changeSupport.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		changeSupport.removePropertyChangeListener(l);
	}
	
	public Livro() {
	}

	public Livro(String titulo, Autor autor, Genero genero, byte[] capa) {
		this.titulo = titulo;
		this.autor = autor;
		this.genero = genero;
		this.capa = capa;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		Object old = this.titulo;
		this.titulo = titulo;
		changeSupport.firePropertyChange("titulo", old, this.titulo);
	}

	public Autor getAutor() {
		return autor;
	}

	public void setAutor(Autor autor) {
		Object old = this.autor;
		this.autor = autor;
		changeSupport.firePropertyChange("autor", old, this.autor);
		
	}

	public Genero getGenero() {
		return genero;
	}

	public void setGenero(Genero genero) {
		Object old = this.genero;
		this.genero = genero;
		changeSupport.firePropertyChange("genero", old, this.genero);
	}

    public byte[] getCapa() {
		return capa;
	}

	public void setCapa(byte[] capa) {
		Object old = this.capa;
		this.capa = capa;
		changeSupport.firePropertyChange("capa", old, this.capa);
	}
	
	public Object getId() {
		return id;
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
