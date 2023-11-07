package br.com.gwenilorac.biblioteca.servicos;

import java.util.List;
import javax.persistence.EntityManager;
import org.postgresql.core.Oid;
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse.File;
import br.com.gwenilorac.biblioteca.dao.AutorDao;
import br.com.gwenilorac.biblioteca.dao.GeneroDao;
import br.com.gwenilorac.biblioteca.dao.LivroDao;
import br.com.gwenilorac.biblioteca.model.Autor;
import br.com.gwenilorac.biblioteca.model.Emprestimo;
import br.com.gwenilorac.biblioteca.model.Genero;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.StatusEmprestimo;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class ServicoLivro {
	
	private static EntityManager em = JPAUtil.getEntityManager();
	private static LivroDao livroDao = new LivroDao(em);
	private static GeneroDao generoDao = new GeneroDao(em);
	private static AutorDao autorDao = new AutorDao(em);

	public static void adicionarLivro(String titulo, String autor, String genero, byte[] capa) {

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
	}

	public static List<Livro> pegarLivros() {
		List<Livro> todosLivros = livroDao.buscarTodosLivros();
		return todosLivros;
	}
	
	public static boolean removerLivro(Livro livro) {
		if(Emprestimo.getStatus() == StatusEmprestimo.ENCERRADO) {
			em.getTransaction().begin();
			livroDao.remover(livro);
			System.out.println("Livro excluido com sucesso!");
			em.getTransaction().commit();
			return true;
		}  else {
			System.out.println("Por favor devolver livros emprestados!");
			return false;
        }
	}
	
	
}

























