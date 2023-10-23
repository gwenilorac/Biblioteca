package br.com.gwenilorac.biblioteca.testes.back;

import java.time.Year;
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

public class populaBD {

	public static void main(String[] args) {

		EntityManager em = JPAUtil.getEntityManager();

		GeneroDao generoDao = new GeneroDao(em);
		AutorDao autorDao = new AutorDao(em);
		LivroDao livroDao = new LivroDao(em);
		UsuarioDao usuarioDao = new UsuarioDao(em);

		Usuario admin = new Usuario("admin", "admin@gmail.com", "admin");
		Usuario Caroline = new Usuario("Caroline", "carolgomes@gmail.com", "1234");
		Usuario Rafaela = new Usuario("Rafaela", "rafarosa@gmail.com", "3456");
		Usuario Pedro = new Usuario("Pedro", "pedrodasilva@gmail.com", "6789");
		
		Genero terror = new Genero("TERROR");
		Genero aventura = new Genero("AVENTURA");
		Genero fabula = new Genero("FABULA");

		Autor NeilGaiman = new Autor("Neil Gaiman");
		Autor StephenKing = new Autor("Stephen King");
		Autor JamesDashner = new Autor("James Dashner");
		Autor SaintExupéry = new Autor("Saint-Exupéry");
		Autor Esopo = new Autor("Esopo");

		Livro Coraline = new Livro("Coraline", NeilGaiman, terror, null);
		Livro Carrie = new Livro("Carrie", StephenKing, terror, null);
		Livro Misery = new Livro("Misery", StephenKing, terror, null);
		Livro ContoDeFadas = new Livro("Conto de fadas", StephenKing, terror, null);
		Livro MazeRunner = new Livro("Maze Runner", JamesDashner, aventura, null);
		Livro OPequenoPríncipe = new Livro("O Pequeno Príncipe", SaintExupéry, fabula, null);
		Livro OLeãoEORato = new Livro("O Leão e o Rato", Esopo, fabula, null);
		Livro ACigarraEAFormiga = new Livro("A Cigarra e a Formiga", Esopo, fabula, null);
		
		Emprestimo emprestimo = new Emprestimo(Misery, Caroline);
		emprestimo.pegarLivroEmprestado();
		emprestimo.devolverLivro();
		Emprestimo emprestimo2 = new Emprestimo(ContoDeFadas, Caroline);
		emprestimo2.pegarLivroEmprestado();

		Emprestimo emprestimo3 = new Emprestimo(Coraline, Rafaela);
		emprestimo3.pegarLivroEmprestado();
		emprestimo3.devolverLivro();
		Emprestimo emprestimo4 = new Emprestimo(Misery, Rafaela);
		emprestimo4.pegarLivroEmprestado();
		
		em.getTransaction().begin();

		usuarioDao.cadastrar(admin);
		usuarioDao.cadastrar(Caroline);
		usuarioDao.cadastrar(Rafaela);
		usuarioDao.cadastrar(Pedro);

		generoDao.cadastrar(terror);
		generoDao.cadastrar(aventura);
		generoDao.cadastrar(fabula);

		autorDao.cadastrar(NeilGaiman);
		autorDao.cadastrar(StephenKing);
		autorDao.cadastrar(JamesDashner);
		autorDao.cadastrar(SaintExupéry);
		autorDao.cadastrar(Esopo);
		
		livroDao.cadastrar(Coraline);
		livroDao.cadastrar(Carrie);
		livroDao.cadastrar(Misery);
		livroDao.cadastrar(ContoDeFadas);
		livroDao.cadastrar(MazeRunner);
		livroDao.cadastrar(OPequenoPríncipe);
		livroDao.cadastrar(OLeãoEORato);
		livroDao.cadastrar(ACigarraEAFormiga);
		
		em.getTransaction().commit();

		em.close();
		
	}

}
