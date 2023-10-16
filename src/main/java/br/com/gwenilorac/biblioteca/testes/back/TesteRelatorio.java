package br.com.gwenilorac.biblioteca.testes.back;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.gwenilorac.biblioteca.dao.EstatisticasDao;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class TesteRelatorio {
	
	public static void main(String[] args) {
		
		EntityManager em = JPAUtil.getEntityManager();
		
		EstatisticasDao ed = new EstatisticasDao(em);
		
		
		List<Livro> livrosMaisEmprestados = ed.getLivrosMaisEmprestados();
		livrosMaisEmprestados.forEach(lme -> System.out.println());
		
		int totalUsuariosRegistrados = ed.getTotalUsuariosRegistrados();
		System.out.println(totalUsuariosRegistrados);
		
		List<Usuario> usuariosComMaisEmprestimos = ed.getUsuariosComMaisEmprestimos();
		usuariosComMaisEmprestimos.forEach(ucme -> System.out.println());
		
		int totalLivrosNaBiblioteca = ed.getTotalLivrosNaBiblioteca();
		System.out.println(totalLivrosNaBiblioteca);
	}

}
