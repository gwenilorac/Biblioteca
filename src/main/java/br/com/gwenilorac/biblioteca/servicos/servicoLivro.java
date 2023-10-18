package br.com.gwenilorac.biblioteca.servicos;

import javax.persistence.EntityManager;

import br.com.gwenilorac.biblioteca.dao.AutorDao;
import br.com.gwenilorac.biblioteca.dao.GeneroDao;
import br.com.gwenilorac.biblioteca.dao.LivroDao;
import br.com.gwenilorac.biblioteca.model.Autor;
import br.com.gwenilorac.biblioteca.model.Genero;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class servicoLivro {
	

	public static void adicionarLivro(String titulo, String autor, String genero) {
	    EntityManager em = JPAUtil.getEntityManager();
	    LivroDao livroDao = new LivroDao(em);
	    GeneroDao generoDao = new GeneroDao(em);
	    AutorDao autorDao = new AutorDao(em);
	    
	    em.getTransaction().begin();

	    System.out.println("Iniciando adicionarLivro...");

	    if (titulo != null && !titulo.isEmpty() && autor != null && !autor.isEmpty() && genero != null && !genero.isEmpty()) {

	        Autor buscarAutorPorNome = autorDao.buscarAutorPorNome(autor);
	        Genero buscarGeneroPorNome = generoDao.buscarGeneroPorNome(genero);

	        if (buscarAutorPorNome == null) {
	            Autor newAutor = new Autor(autor);
	            autorDao.cadastrar(newAutor);
	            Livro livro = new Livro(titulo, newAutor, buscarGeneroPorNome);
	            livroDao.cadastrar(livro);
	        }

	        if (buscarGeneroPorNome == null) {
	            Genero newGenero = new Genero(genero);
	            generoDao.cadastrar(newGenero);
	            Livro livro = new Livro(titulo, buscarAutorPorNome, newGenero);
	            livroDao.cadastrar(livro);
	            
	        } if (buscarAutorPorNome != null && buscarGeneroPorNome != null) {
	            Livro livro = new Livro(titulo, buscarAutorPorNome, buscarGeneroPorNome);
	            livroDao.cadastrar(livro);
	        }

	        System.out.println("Livro adicionado com sucesso!");
	    } else {
	        System.out.println("Informações insuficientes para adicionar o livro.");
	    }

	    em.getTransaction().commit();
	    em.close();
	}

}
