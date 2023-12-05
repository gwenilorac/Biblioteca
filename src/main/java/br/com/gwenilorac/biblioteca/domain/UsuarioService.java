package br.com.gwenilorac.biblioteca.domain;

import java.util.List;

import br.com.gwenilorac.biblioteca.dao.EmprestimoDao;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class UsuarioService {
	
	private EmprestimoDao emprestimoDao;
	
	public UsuarioService() {
		this.emprestimoDao = new EmprestimoDao(JPAUtil.getEntityManager());
	}
	
	public List<UsuariosView> getReportList(){
		return emprestimoDao.findUsuarioReportList();
	}

}
