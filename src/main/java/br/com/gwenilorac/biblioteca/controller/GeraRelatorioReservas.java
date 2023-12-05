package br.com.gwenilorac.biblioteca.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import javax.persistence.PersistenceException;

import br.com.gwenilorac.biblioteca.domain.EmprestimoView;
import br.com.gwenilorac.biblioteca.domain.ReservaService;
import br.com.gwenilorac.biblioteca.domain.ReservaView;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class GeraRelatorioReservas {
	
	private ReservaService reservasService = new ReservaService();

	public GeraRelatorioReservas() {
		
		String jasperFile = "/home/caroline/workspace/projeto/caroline_project/src/main/java/br/com/gwenilorac/biblioteca/relatorios/reservasRep.jasper";

		try {

			List<ReservaView> reportList = reservasService.getReportList();
			
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperFile);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(),
					new JRBeanCollectionDataSource(reportList));
			

			JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);

			jasperViewer.setVisible(true);

		} catch (JRException | PersistenceException e) {
			e.printStackTrace();
		}
	}
}
