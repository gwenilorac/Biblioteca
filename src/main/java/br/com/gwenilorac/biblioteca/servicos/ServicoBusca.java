package br.com.gwenilorac.biblioteca.servicos;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.gwenilorac.biblioteca.dao.LivroDao;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class ServicoBusca {

	public static List<Livro> busca(String termoBusca) {
	    EntityManager em = JPAUtil.getEntityManager();
	    LivroDao livroDao = new LivroDao(em);

	    if (termoBusca == null || termoBusca.isEmpty() || termoBusca.trim().isEmpty()) {
	        return null;
	    } else {
	        return livroDao.buscarLivros(termoBusca);
	    }
	}
}
