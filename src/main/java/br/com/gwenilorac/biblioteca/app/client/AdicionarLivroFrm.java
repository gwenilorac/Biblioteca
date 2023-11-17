package br.com.gwenilorac.biblioteca.app.client;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.CloseAction;

import org.postgresql.core.Oid;

import br.com.gwenilorac.biblioteca.servicos.ServicoLivro;

public class AdicionarLivroFrm extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField titleField;
	private JTextField authorField;
	private JTextField generoField;
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
		generoField = new JTextField(20);
		addButton = new JButton("Adicionar Livro");
		selectCoverButton = new JButton("Selecionar Capa");

		addButton.addActionListener(e -> adicionarLivro());
		selectCoverButton.addActionListener(e -> selecionarCapa());
	}

	private void initLayout() {
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		JLabel titleLabel = new JLabel("Título:");
		JLabel authorLabel = new JLabel("Autor:");
		JLabel generoLabel = new JLabel("Gênero:");

		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(titleLabel)
						.addComponent(authorLabel).addComponent(generoLabel).addComponent(selectCoverButton))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(titleField)
						.addComponent(authorField).addComponent(generoField).addComponent(addButton)));

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(titleLabel)
						.addComponent(titleField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(authorLabel)
						.addComponent(authorField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(generoLabel)
						.addComponent(generoField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(selectCoverButton)
						.addComponent(addButton)));

		setPreferredSize(new Dimension(350, 150));
	}

	private void adicionarLivro() {
	    String titulo = titleField.getText();
	    String autor = authorField.getText();
	    String genero = generoField.getText();

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
		generoField.setText("");
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
}
