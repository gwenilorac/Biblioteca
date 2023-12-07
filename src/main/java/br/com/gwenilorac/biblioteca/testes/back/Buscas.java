package br.com.gwenilorac.biblioteca.testes.back;

import java.util.List;
import javax.persistence.EntityManager;
import br.com.gwenilorac.biblioteca.dao.LivroDao;
import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class Buscas {

	public static void main(String[] args) {
		
		EntityManager em = JPAUtil.getEntityManager();

		LivroDao livroDao = new LivroDao(em);
		UsuarioDao usuarioDao = new UsuarioDao(em);
		
        Usuario Caroline = usuarioDao.buscarUsuarioPorNome("Caroline");
        
		System.out.println("Todos Usuarios: ");
		List<Usuario> todos = usuarioDao.buscarTodos();
		todos.forEach(u -> System.out.println(u.getNome()));
		System.out.println("-----------------------------");
		
		System.out.println("Livros Emprestados pela Caroline: ");
        List<Livro> livrosEmprestadosC = usuarioDao.buscarLivrosEmprestadosPorUsuario(Caroline);
        livrosEmprestadosC.forEach(lec -> System.out.println(lec.getTitulo()));
		System.out.println("---------------------------------------");

		System.out.println("Todos os livros do Stephen King: ");
		List<Livro> livrosAutor = livroDao.buscarPorNomeAutor("Stephen King");
		livrosAutor.forEach(l -> System.out.println(l.getTitulo()));
		System.out.println("-----------------------------");
		
		System.out.println("Todos os livros da categoria Terror: ");
		List<Livro> livrosCategoria = livroDao.buscarPorNomeDaCategoria("TERROR");
		livrosCategoria.forEach(lc -> System.out.println(lc.getTitulo()));
		System.out.println("-----------------------------");
		
		System.out.println("Livros Disponiveis: ");
		List<Livro> livrosDisponiveis = livroDao.buscarLivrosDisponiveis();
		livrosDisponiveis.forEach(ld -> System.out.println(ld.getTitulo()));
		System.out.println("-----------------------------");
		
		System.out.println("Livros Indisponiveis: ");
		List<Livro> livrosIndisponiveis = livroDao.buscarLivrosIndisponiveis();
		livrosIndisponiveis.forEach(li -> System.out.println(li.getTitulo()));
		System.out.println("-----------------------------");
		
		System.out.println("Usuarios com mais emprestimo: ");
		List<Usuario> usuariosComMaisEmprestimo = usuarioDao.buscarUsuariosComMaisEmprestimos(1);
		usuariosComMaisEmprestimo.forEach(ue -> System.out.println(ue.getNome()));
		System.out.println("-----------------------------");	
		
		System.out.println("Livros mais emprestados: ");
		List<Livro> buscarLivrosMaisEmprestados = livroDao.buscarLivrosMaisEmprestados();
		buscarLivrosMaisEmprestados.forEach(le -> System.out.println(le.getTitulo()));
		System.out.println("-----------------------------");	
	}
	
}
