package br.com.gwenilorac.biblioteca.controller;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.gwenilorac.biblioteca.vo.RelatorioBibliotecaDTO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class RelatorioBiblioteca {

    public void gerarRelatorio(String[] args) throws JRException {

    	// Compilar o arquivo JRXML para um arquivo de compilação .jasper
            InputStream arq = RelatorioBiblioteca.class.getResourceAsStream("/caminho/para/seu/arquivo.jasper");
            JasperReport report = JasperCompileManager.compileReport(arq);

            // Preparar dados para o relatório (substitua isso com suas próprias lógicas de obtenção de dados)
            List<RelatorioBibliotecaDTO> estatisticasEmprestimo = new ArrayList<>();
            // Preencha estatisticasEmprestimo com dados

            List<RelatorioBibliotecaDTO> estatisticasUsuarios = new ArrayList<>();
            // Preencha estatisticasUsuarios com dados

            List<RelatorioBibliotecaDTO> estadoAcervo = new ArrayList<>();
            // Preencha estadoAcervo com dados

            // Criar uma fonte de dados JRDataSource para cada conjunto de dados
            JRDataSource dataSourceEstatisticasEmprestimo = new JRBeanCollectionDataSource(estatisticasEmprestimo);
            JRDataSource dataSourceEstatisticasUsuarios = new JRBeanCollectionDataSource(estatisticasUsuarios);
            JRDataSource dataSourceEstadoAcervo = new JRBeanCollectionDataSource(estadoAcervo);


            // Preencher o relatório usando os dados e parâmetros
            JasperPrint print = JasperFillManager.fillReport(report, null, new JREmptyDataSource());

            // Visualizar o relatório
            JasperViewer.viewReport(print, false);
    }
}
