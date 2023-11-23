package br.com.gwenilorac.biblioteca.app.client;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javax.persistence.EntityManager;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

import br.com.gwenilorac.biblioteca.dao.GeneroDao;
import br.com.gwenilorac.biblioteca.model.Genero;
import br.com.gwenilorac.biblioteca.servicos.ServicoLivro;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class AdicionarLivroFrm extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField titleField;
	private JTextField authorField;
	private JComboBox<Genero> generosCb;
	private JButton addButton;
	private JButton selectCoverButton;
	private File selectedCoverFile;

	public AdicionarLivroFrm() {
		initComponents();
		initLayout();
	}

	private void initComponents() {
		titleField = new JTextField(20);
		authorField = new JTextField(20);
		
		addButton = new JButton("Adicionar Livro");
		selectCoverButton = new JButton("Selecionar Capa");

		addButton.addActionListener(e -> adicionarLivro());
		selectCoverButton.addActionListener(e -> selecionarCapa());
		
		generosCb = initCbGenero();
	}

	private void initLayout() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		panel.add(createMainForm());
		panel.add(createButtonPanel());

		add(panel);
		setPreferredSize(new Dimension(350, 150));
	}

	private void adicionarLivro() {
	    String titulo = titleField.getText();
	    String autor = authorField.getText();
	    String genero = generosCb.getSelectedItem().toString();

	    if (titulo != null && autor != null && genero != null && selectedCoverFile != null) {
	        try {
	            byte[] capa = Files.readAllBytes(selectedCoverFile.toPath());

	            System.out.println("Caminho do arquivo selecionado: " + selectedCoverFile.getAbsolutePath());

	            ServicoLivro.adicionarLivro(titulo, autor, genero, capa);

	            limparCampos();

	        } catch (IOException e) {
	            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
	            JOptionPane.showMessageDialog(this, "ERRO AO LER O ARQUIVO");
	        } catch (NullPointerException e) {
	            System.out.println("Faltando um campo. O livro não será adicionado.");
	            JOptionPane.showMessageDialog(this, "FALTANDO ALGUMA INFORMAÇÃO!");
	        }
	    }
	}

	private void limparCampos() {
		titleField.setText("");
		authorField.setText("");
		generosCb.setSelectedItem(null);
		selectedCoverFile = null;
	}

	private void selecionarCapa() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png");
		chooser.setFileFilter(filter);
		int result = chooser.showOpenDialog(this);

		if (result == JFileChooser.APPROVE_OPTION) {
			selectedCoverFile = chooser.getSelectedFile();
			System.out.println(selectedCoverFile.getName());
		}
	}
	
	private Component createMainForm() {
		JPanel addPanel = new JPanel();
		
		FormLayout layout = new FormLayout("pref, 5px, 70dlu");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		builder.append("Título: ", titleField);
		builder.nextLine();

		builder.append("Autor: ", authorField);
		builder.nextLine();
		
		builder.append("Genero: ", generosCb);
		
		JPanel formPanel = builder.getPanel();
		
		addPanel.add(formPanel);

		return addPanel;
	}
	
	private JComboBox<Genero> initCbGenero(){
		EntityManager em = JPAUtil.getEntityManager();
		GeneroDao generoDao = new GeneroDao(em);
		
		generosCb = new JComboBox<Genero>();
		generosCb.setEditable(true);

		List<Genero> generos = generoDao.buscarTodosGeneros();
		
		for (Genero genero : generos) {
			generosCb.addItem(genero);
		}
		
		return generosCb;
	}
	
	private Component createButtonPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.add(selectCoverButton);
		panel.add(addButton);
		return panel;
	}
	
}
