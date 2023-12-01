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

   
    
   
    
   

    public List<Object[]> buscarUsuariosComMaisEmprestimos() {
        String jpql = "SELECT e.usuario, COUNT(e) FROM Emprestimo e GROUP BY e.usuario ORDER BY COUNT(e) DESC";
        return em.createQuery(jpql, Object[].class)
                .getResultList();
    }
    
    public List<String> getNomesDosUsuariosComMulta() {
        String jpql = "SELECT e.usuario.nome FROM Emprestimo e WHERE e.temMulta = :temMulta GROUP BY e.usuario.nome ORDER BY COUNT(e) DESC";
        return em.createQuery(jpql, String.class)
                .setParameter("temMulta", true)
                .getResultList();
    }
    
    public List<String> getTitulosDosLivrosComMulta() {
        String jpql = "SELECT e.livro.titulo FROM Emprestimo e WHERE e.temMulta = :temMulta GROUP BY e.livro.titulo ORDER BY COUNT(e) DESC";
        return em.createQuery(jpql, String.class)
                .setParameter("temMulta", true)
                .getResultList();
    }
    
    public List<LocalDate> getDiasAtrasadosDosLivrosComMulta() {
        String jpql = "SELECT e.diasAtrasados FROM Emprestimo e WHERE e.temMulta = :temMulta GROUP BY e.diasAtrasados ORDER BY COUNT(e) DESC";
        return em.createQuery(jpql, LocalDate.class)
                .setParameter("temMulta", true)
                .getResultList();
    }
    
    public List<Double> getValorMultaDosLivrosComMulta() {
        String jpql = "SELECT e.valorMulta FROM Emprestimo e WHERE e.temMulta = :temMulta GROUP BY e.livro.titulo ORDER BY COUNT(e) DESC";
        return em.createQuery(jpql, Double.class)
                .setParameter("temMulta", true)
                .getResultList();
    }
    
    public List<Usuario> getTotalDeUsuariosComMulta() {
        String jpql = "SELECT e.usuario FROM Emprestimo e WHERE e.temMulta = :temMulta GROUP BY e.usuario ORDER BY COUNT(e) DESC";
        return em.createQuery(jpql, Usuario.class)
                .setParameter("temMulta", true)
                .getResultList();
    }
    
    public List<String> getNomesDosUsuarios() {
        String jpql = "SELECT u.nome FROM Usuario u";
        return em.createQuery(jpql, String.class)
                .getResultList();
    }
    
    public List<String> getEmailDosUsuarios() {
        String jpql = "SELECT u.email FROM Usuario u";
        return em.createQuery(jpql, String.class)
                .getResultList();
    }
    
    public int getTotalEmprestimos() {
        String jpql = "SELECT COUNT(e) FROM Emprestimo e";
        Long resultado = em.createQuery(jpql, Long.class)
                          .getSingleResult();

        return resultado != null ? resultado.intValue() : 0;
    }
    
    public int getTotalLivrosNaBiblioteca() {
        String jpql = "SELECT COUNT(l) FROM Livro l";
        Long resultado = em.createQuery(jpql, Long.class)
                          .getSingleResult();

        return resultado != null ? resultado.intValue() : 0;
    }

    public int getTotalUsuariosRegistrados() {
        String jpql = "SELECT COUNT(u) FROM Usuario u";
        Long resultado = em.createQuery(jpql, Long.class)
                          .getSingleResult();

        return resultado != null ? resultado.intValue() : 0;
    }

}

