package br.com.gwenilorac.biblioteca.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
    private String nome;
	
	@Column(nullable = false, unique = true) 
    private String email;
	
	@Column(nullable = false)
    private String senha;

	@OneToMany	
	private List<Livro> livrosEmprestados;
	
	@Transient
	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
	
	public void addPropertyChangeListener(PropertyChangeListener l) {
		changeSupport.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		changeSupport.removePropertyChangeListener(l);
	}
 
	public Usuario() {
	}

	public Usuario(String nome, String email, String senha) {
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.livrosEmprestados = new ArrayList<>();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		Object old = this.nome;
		this.nome = nome;
		changeSupport.firePropertyChange("nome", old, this.nome);
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		Object old = this.senha;
		this.senha = senha;
		changeSupport.firePropertyChange("senha", old, this.senha);
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setLivrosEmprestados(List<Livro> livrosEmprestados) {
		this.livrosEmprestados = livrosEmprestados;
	}
	
	public List<Livro> getLivrosEmprestados() {
		return livrosEmprestados;
	}

	public void adicionarLivroEmprestado(Livro livro) {
		if (livro != null) {
			livrosEmprestados.add(livro);
		} else {
			throw new IllegalArgumentException("Livro não pode ser nulo.");
		}
	}

	public void removerLivroEmprestado(Livro livro) {
		if (livro != null) {
			livrosEmprestados.remove(livro);
		} else {
			throw new IllegalArgumentException("Livro não pode ser nulo.");
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(id, other.id);
	}
	
	
}
