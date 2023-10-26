package br.com.gwenilorac.biblioteca.testes.back;


import javax.persistence.EntityManager;

import br.com.gwenilorac.biblioteca.dao.AutorDao;
import br.com.gwenilorac.biblioteca.dao.GeneroDao;
import br.com.gwenilorac.biblioteca.dao.LivroDao;
import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.model.Autor;
import br.com.gwenilorac.biblioteca.model.Emprestimo;
import br.com.gwenilorac.biblioteca.model.Genero;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class Remove {
	
	public static void main(String[] args) {
		
		EntityManager em = JPAUtil.getEntityManager();
		
		LivroDao livroDao = new LivroDao(em);
		UsuarioDao usuarioDao = new UsuarioDao(em);
		AutorDao autorDao = new AutorDao(em);
		GeneroDao generoDao = new GeneroDao(em);

		Usuario rafaela = usuarioDao.buscarUsuarioPorNome("Rafaela");
		Emprestimo emprestimo = new Emprestimo(rafaela);
		emprestimo.DevolucaoParaExclusaoConta();
		usuarioDao.remover(rafaela);
		
		System.out.println("-------------------------------------");
		
		Autor autor = autorDao.buscarAutorPorNome("James Dashner");
		autorDao.remover(autor);
		
		System.out.println("-------------------------------------");
		
		Genero genero = generoDao.buscarGeneroPorNome("FABULA");
		generoDao.remover(genero);
		
		System.out.println("-------------------------------------");
		
		Livro livro = livroDao.buscarLivroPorTitulo("Carrie");
		livroDao.remover(livro);
		
		System.out.println("-------------------------------------");
	}

}
