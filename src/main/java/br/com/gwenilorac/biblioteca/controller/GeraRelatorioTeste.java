package br.com.gwenilorac.biblioteca.controller;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import br.com.gwenilorac.biblioteca.domain.EmprestimoView;
import br.com.gwenilorac.biblioteca.util.JPAUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class GeraRelatorioTeste {
	
	public GeraRelatorioTeste() {
		File file  = new File("GeraRelatorio.java");
		String pathAbsoluto = file.getAbsolutePath();
		String pathAbsolutoParcial = pathAbsoluto.substring(0,pathAbsoluto.lastIndexOf('\\'))+"\\relatorios\\teste.jrxml";
		
		try {
			
			List<EmprestimoView> reportList = new ArrayList<>(); //TODO buscar do banco via query 
			
			JasperReport jasperReport = JasperCompileManager.compileReport(pathAbsolutoParcial);
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), new JRBeanCollectionDataSource(reportList));
			
			JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
			
			jasperViewer.setVisible(true);
			
		}  catch (JRException | PersistenceException e) {
            e.printStackTrace();
        }
    }
}
