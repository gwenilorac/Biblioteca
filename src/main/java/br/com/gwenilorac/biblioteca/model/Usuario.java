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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

@SuppressWarnings("serial")
@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "foto")
    private byte[] foto;

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

    public Usuario(String nome, String email, String senha, byte[] foto) {
        setNome(nome);
        setEmail(email);
        setSenha(senha);
        setFoto(foto);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        Objects.requireNonNull(nome, "Nome não pode ser nulo");
        Object old = this.nome;
        this.nome = nome;
        changeSupport.firePropertyChange("nome", old, this.nome);
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        Objects.requireNonNull(senha, "Senha não pode ser nula");
        Object old = this.senha;
        this.senha = senha;
        changeSupport.firePropertyChange("senha", old, this.senha);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        Objects.requireNonNull(email, "E-mail não pode ser nulo");
        Object old = this.email;
        this.email = email;
        changeSupport.firePropertyChange("email", old, this.email);
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        Object old = this.foto;
        this.foto = foto;
        changeSupport.firePropertyChange("foto", old, this.foto);
    }

    public Long getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario other = (Usuario) obj;
        return Objects.equals(id, other.id);
    }
}
