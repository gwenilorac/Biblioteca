package br.com.gwenilorac.biblioteca.domain;

import java.util.List;
import br.com.gwenilorac.biblioteca.dao.ReservaDao;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class ReservaService {
	
	private ReservaDao reservaDao;
	
	public ReservaService() {
		this.reservaDao = new ReservaDao(JPAUtil.getEntityManager());
	}

	public List<ReservaView> getReportList() {
		return reservaDao.findReservasReporList();
	}

}
