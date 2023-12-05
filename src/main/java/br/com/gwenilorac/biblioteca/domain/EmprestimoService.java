package br.com.gwenilorac.biblioteca.domain;

import java.util.List;

import br.com.gwenilorac.biblioteca.dao.EmprestimoDao;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class EmprestimoService {
	
	private EmprestimoDao emprestimoDao;
	
	public EmprestimoService() {
		this.emprestimoDao = new EmprestimoDao(JPAUtil.getEntityManager());
	}

	public List<EmprestimoView> getReportList() {
		return emprestimoDao.findEmprestimosReporList();
	}

}
