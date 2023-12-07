package br.com.gwenilorac.biblioteca.testes.back;

import java.time.Year;
import javax.persistence.EntityManager;
import br.com.gwenilorac.biblioteca.dao.AutorDao;
import br.com.gwenilorac.biblioteca.dao.GeneroDao;
import br.com.gwenilorac.biblioteca.model.Autor;
import br.com.gwenilorac.biblioteca.model.Genero;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class CriacaoDeLivroComElementosExistentes {
	
	public static void main(String[] args) {
		
		EntityManager em = JPAUtil.getEntityManager();

		GeneroDao generoDao = new GeneroDao(em);
		AutorDao autorDao = new AutorDao(em);
		
		Autor StephenKing = autorDao.buscarAutorPorNome("Stephen King");
        Genero terror = generoDao.buscarGeneroPorNome("TERROR");
        
        Livro OLivroDoCemitério = new Livro("O Livro do Cemitério", StephenKing, terror);
		
	}

}
