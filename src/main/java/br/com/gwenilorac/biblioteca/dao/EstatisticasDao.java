package br.com.gwenilorac.biblioteca.dao;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.Usuario;

public class EstatisticasDao {

	private EntityManager em;

    public EstatisticasDao(EntityManager entityManager) {
        this.em = entityManager;
    }

    public List<String> getTituloDosLivrosMaisEmprestados() {
        String jpql = "SELECT e.livro.titulo FROM Emprestimo e GROUP BY e.livro ORDER BY COUNT(e) DESC";
        return  em.createQuery(jpql, String.class)
        		.getResultList();
    }
    
    public List<String> getAutorDosLivrosMaisEmprestados(){
    	String jpql = "SELECT e.livro.autor FROM Emprestimo e GROUP BY e.livro ORDER BY COUNT(e) DESC";
    	return em.createQuery(jpql, String.class)
    			.getResultList();
    }
    
    public List<Long> getNumDeEmprestimosDosLivrosMaisEmprestados(){
    	String jpql = "SELECT COUNT(e.livro) FROM Emprestimo e GROUP BY e.livro ORDER BY COUNT(e) DESC";
    	return em.createQuery(jpql, Long.class)
    			.getResultList();
    }

	 public List<Usuario> buscarUsuariosComMaisEmprestimos() {
		 String jpql = "SELECT e.usuario FROM Emprestimo e GROUP BY e.usuario ORDER BY COUNT(e) DESC";
		 return em.createQuery(jpql, Usuario.class)
				 .getResultList();
	    }
    
    public List<Usuario> getNomesDosUsuariosComMulta(){
    	String jpql = "SELECT e.usuario.nome FROM Emprestimo e WHERE e.temMulta = :temMulta GROUP BY e.usuario ORDER BY COUNT(e) DESC";
    	return em.createQuery(jpql, Usuario.class)
    			.setParameter("temMulta", true)
    			.getResultList();
    }
    
    public List<Livro> getTitulosDosLivrosComMulta(){
    	String jpql = "SELECT e.livro.titulo FROM Emprestimo e WHERE e.temMulta = :temMulta GROUP BY e.livro ORDER BY COUNT(e) DESC";
    	return em.createQuery(jpql, Livro.class)
    			.setParameter("temMulta", true)
    			.getResultList();
    }
    
    public List<LocalDate> getDiasAtrasadosDosLivrosComMulta(){
    	String jpql = "SELECT e.diasAtrasados FROM Emprestimo e WHERE e.temMulta = :temMulta GROUP BY e.livro ORDER BY COUNT(e) DESC";
    	return em.createQuery(jpql, LocalDate.class)
    			.setParameter("temMulta", true)
    			.getResultList();
    }
    
    public List<Double> getValorMultaDosLivrosComMulta(){
    	String jpql = "SELECT e.valorMulta FROM Emprestimo e WHERE e.temMulta = :temMulta GROUP BY e.livro ORDER BY COUNT(e) DESC";
    	return em.createQuery(jpql, Double.class)
    			.setParameter("temMulta", true)
    			.getResultList();
    }
    
    public List<Usuario> getTotalDeUsuariosComMulta(){
    	String jpql = "SELECT e.usuario FROM Emprestimo e WHERE e.temMulta = :temMulta GROUP BY e.livro ORDER BY COUNT(e) DESC";
    	return em.createQuery(jpql, Usuario.class)
    			.setParameter("temMulta", Usuario.class)
    			.getResultList();
    }
    
    public List<Usuario> getNomesDosUsuarios(){
    	String jpql = "SELECT u.nome FROM Usuario u";
    	return em.createQuery(jpql, Usuario.class)
    			.getResultList();
    }
    
    public List<Usuario> getEmailDosUsuarios(){
    	String jpql = "SELECT u.email FROM Usuario u";
    	return em.createQuery(jpql, Usuario.class)
    			.getResultList();
    }
    
    public int getTotalEmprestimos() {
    	String jpql = "SELECT COUNT(e) FROM Emprestimo e";
    	return em.createQuery(jpql, Long.class)
    			.getSingleResult().intValue();
    }
    
    public int getTotalLivrosNaBiblioteca() {
    	String jpql = "SELECT COUNT(l) FROM Livro l";
    	return em.createQuery(jpql, Long.class)
    			.getSingleResult().intValue();
    }

    public int getTotalUsuariosRegistrados() {
        String jpql = "SELECT COUNT(u) FROM Usuario u";
        return em.createQuery(jpql, Long.class)
        		.getSingleResult().intValue();
    }

}

