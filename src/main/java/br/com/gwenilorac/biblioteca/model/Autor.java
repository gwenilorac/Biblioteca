package br.com.gwenilorac.biblioteca.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "autor")
public class Autor implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
    private String nome;

	@OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Livro> livrosDoAutor = new ArrayList<>();
    
    @Deprecated
    public Autor() {}

    public Autor(String nome) {
        this.nome = nome;
        this.livrosDoAutor = new ArrayList<>();

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }

    public List<Livro> getLivrosDoAutor() {
        return livrosDoAutor;
    }

    public void setLivrosDoAutor(List<Livro> livrosDoAutor) {
        this.livrosDoAutor = livrosDoAutor;
    }

}

