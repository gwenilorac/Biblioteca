package br.com.gwenilorac.biblioteca.servicos;

import java.util.List;
import javax.persistence.EntityManager;
import br.com.gwenilorac.biblioteca.dao.EmprestimoDao;
import br.com.gwenilorac.biblioteca.dao.ReservaDao;
import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.model.Emprestimo;
import br.com.gwenilorac.biblioteca.model.Reserva;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class ServicoUsuario {

    public static boolean removerUsuario(Usuario usuario) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        EmprestimoDao emprestimoDao = new EmprestimoDao(em);
        UsuarioDao usuarioDao = new UsuarioDao(em);
        ReservaDao reservaDao = new ReservaDao(em);
        List<Emprestimo> emprestimosUser = emprestimoDao.buscarEmprestimosUser(usuario.getId());
        Reserva reservaUsuario = reservaDao.buscarReservaUsuario(usuario);
        em.merge(usuario);
        
        if (emprestimosUser.isEmpty()) {
            try {
            	if (reservaUsuario != null) {
            		reservaDao.remover(reservaUsuario);
            		usuarioDao.atualizar(usuario);
            	}
                usuarioDao.remover(usuario);
                em.getTransaction().commit();
                return true;
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                e.printStackTrace();
                return false;
            } finally {
                if (em != null && em.isOpen()) {
                    em.close();
                }
            }
        } else {
            return false;
        }
    }
}
