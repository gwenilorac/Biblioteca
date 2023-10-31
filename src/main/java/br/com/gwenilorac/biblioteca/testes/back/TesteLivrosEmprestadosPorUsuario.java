package br.com.gwenilorac.biblioteca.testes.back;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.servicos.ServicoLogin;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class TesteLivrosEmprestadosPorUsuario {

	public static void main(String[] args) {
		
		EntityManager em = JPAUtil.getEntityManager();
		Usuario usuario = ServicoLogin.getUsuarioLogado();
		UsuarioDao usuarioDao = new UsuarioDao(em);
		List<Livro> livrosEmprestadosPorUsuario = usuarioDao.buscarLivrosEmprestadosPorUsuario(usuario);
		for (Livro livro : livrosEmprestadosPorUsuario) {
			System.out.println(livro.getTitulo());
		}
	}
}
