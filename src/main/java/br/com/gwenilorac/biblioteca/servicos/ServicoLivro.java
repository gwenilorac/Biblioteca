package br.com.gwenilorac.biblioteca.servicos;

import java.util.List;

import javax.persistence.EntityManager;

import org.postgresql.core.Oid;

import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse.File;

import br.com.gwenilorac.biblioteca.dao.AutorDao;
import br.com.gwenilorac.biblioteca.dao.GeneroDao;
import br.com.gwenilorac.biblioteca.dao.LivroDao;
import br.com.gwenilorac.biblioteca.model.Autor;
import br.com.gwenilorac.biblioteca.model.Genero;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class ServicoLivro {

	public static void adicionarLivro(String titulo, String autor, String genero, byte[] capa) {

		EntityManager em = JPAUtil.getEntityManager();
		LivroDao livroDao = new LivroDao(em);
		GeneroDao generoDao = new GeneroDao(em);
		AutorDao autorDao = new AutorDao(em);

		em.getTransaction().begin();

		System.out.println("Iniciando adicionarLivro...");

		if (!titulo.trim().isEmpty() && !autor.trim().isEmpty() && !genero.trim().isEmpty()) {

			Autor buscarAutorPorNome = autorDao.buscarAutorPorNome(autor);
			Genero buscarGeneroPorNome = generoDao.buscarGeneroPorNome(genero);

			if (buscarAutorPorNome == null) {
				Autor newAutor = new Autor(autor);
				autorDao.cadastrar(newAutor);
				buscarAutorPorNome = newAutor;
			}

			if (buscarGeneroPorNome == null) {
				Genero newGenero = new Genero(genero);
				generoDao.cadastrar(newGenero);
				buscarGeneroPorNome = newGenero;
			}

			Livro livro = new Livro(titulo, buscarAutorPorNome, buscarGeneroPorNome, capa);
			livroDao.cadastrar(livro);
			System.out.println("Livro adicionado com sucesso!");
		} else {
			System.out.println("Informações insuficientes para adicionar o livro.");
		}

		em.getTransaction().commit();
		em.close();
	}

	public static List<Livro> pegarLivros() {

		EntityManager em = JPAUtil.getEntityManager();
		LivroDao livroDao = new LivroDao(em);
		
		List<Livro> todosLivros = livroDao.buscarTodosLivros();
		return todosLivros;
	}
}

























