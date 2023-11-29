package br.com.gwenilorac.biblioteca.controller;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import br.com.gwenilorac.biblioteca.util.JPAUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class GeraRelatorioTeste {
	
	public GeraRelatorioTeste() {
		EntityManager em = JPAUtil.getEntityManager();
		
		File file  = new File("GeraRelatorio.java");
		String pathAbsoluto = file.getAbsolutePath();
		String pathAbsolutoParcial = pathAbsoluto.substring(0,pathAbsoluto.lastIndexOf('\\'))+"\\relatorios\\teste.jrxml";
		
		try {
			
			Connection connection = em.unwrap(Connection.class);
			
			JasperReport jasperReport = JasperCompileManager.compileReport(pathAbsolutoParcial);
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(),connection);
			
			JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
			
			jasperViewer.setVisible(true);
			
			
			em.close();
			
			
		}  catch (JRException | PersistenceException e) {
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
