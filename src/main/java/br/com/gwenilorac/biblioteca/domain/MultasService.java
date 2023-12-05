package br.com.gwenilorac.biblioteca.domain;

import java.util.List;

import br.com.gwenilorac.biblioteca.dao.EmprestimoDao;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class MultasService {
	
	private EmprestimoDao emprestimoDao;
	
	public MultasService() {
		this.emprestimoDao = new EmprestimoDao(JPAUtil.getEntityManager());
	}
	
	public List<MultasView> getReportList(){
		return emprestimoDao.findMultasReportList();
	}

}
