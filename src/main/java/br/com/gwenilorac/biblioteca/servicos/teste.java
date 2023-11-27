package br.com.gwenilorac.biblioteca.servicos;

import javax.persistence.EntityManager;

import br.com.gwenilorac.biblioteca.dao.LivroDao;
import br.com.gwenilorac.biblioteca.dao.ReservaDao;
import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.Reserva;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class teste {

	public static void main(String[] args) {
		EntityManager em = JPAUtil.getEntityManager();

		em.getTransaction().begin();
		
		ReservaDao reservaDao = new ReservaDao(em);
		LivroDao livroDao = new LivroDao(em);
		UsuarioDao usuarioDao = new UsuarioDao(em);
		
		Livro livro = livroDao.buscarLivroPorTitulo("Coraline");
		Usuario usuario = usuarioDao.buscarUsuarioPorNome("usuario");
		
		Reserva reserva = new Reserva(livro, usuario);
		
		reservaDao.cadastrar(reserva);
		
		em.getTransaction().commit();
		em.close();
	}
}
