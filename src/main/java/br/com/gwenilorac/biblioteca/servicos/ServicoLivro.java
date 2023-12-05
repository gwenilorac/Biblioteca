package br.com.gwenilorac.biblioteca.servicos;

import javax.persistence.EntityManager;
import br.com.gwenilorac.biblioteca.dao.AutorDao;
import br.com.gwenilorac.biblioteca.dao.EmprestimoDao;
import br.com.gwenilorac.biblioteca.dao.GeneroDao;
import br.com.gwenilorac.biblioteca.dao.LivroDao;
import br.com.gwenilorac.biblioteca.model.Autor;
import br.com.gwenilorac.biblioteca.model.Emprestimo;
import br.com.gwenilorac.biblioteca.model.Genero;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class ServicoLivro {

	public static void adicionarLivro(String titulo, String autor, String genero, byte[] capa) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();

			System.out.println("Iniciando adicionarLivro...");

			if (!titulo.trim().isEmpty() && !autor.trim().isEmpty() && !genero.trim().isEmpty()) {
				AutorDao autorDao = new AutorDao(em);
				GeneroDao generoDao = new GeneroDao(em);
				LivroDao livroDao = new LivroDao(em);

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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean removerLivro(Livro livro) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            livro = em.merge(livro);

            LivroDao livroDao = new LivroDao(em);
            EmprestimoDao emprestimoDao = new EmprestimoDao(em);

            Emprestimo emprestimo = emprestimoDao.buscarSeLivroJaTemEmprestimo(livro);

            if (emprestimo != null) {
                boolean estaAberto = emprestimoDao.EmprestimoEstaAberto(emprestimo);
                if (estaAberto == false) {
                    emprestimoDao.remover(emprestimo);
                    livroDao.remover(livro);
                    em.getTransaction().commit();
                    return true;
                } else {
                    System.out.println("Livro está com empréstimo aberto");
                    em.getTransaction().rollback();
                    return false;
                }
            } else {
                livroDao.remover(livro);
                em.getTransaction().commit();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
	
}
