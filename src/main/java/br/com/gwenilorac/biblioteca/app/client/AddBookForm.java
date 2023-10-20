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

import br.com.gwenilorac.biblioteca.servicos.ServicoLivro;

public class AddBookForm extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField generoField;
    private JButton addButton;
    private JButton selectCoverButton; 

    private File selectedCoverFile; 

    public AddBookForm() {
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
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(titleLabel)
                        .addComponent(authorLabel)
                        .addComponent(generoLabel)
                        .addComponent(selectCoverButton)) 
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(titleField)
                        .addComponent(authorField)
                        .addComponent(generoField)
                        .addComponent(addButton))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(titleLabel)
                        .addComponent(titleField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(authorLabel)
                        .addComponent(authorField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(generoLabel)
                        .addComponent(generoField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(selectCoverButton) 
                        .addComponent(addButton))
        );

        setPreferredSize(new Dimension(300, 200));
    }

    private void adicionarLivro() {
        String titulo = titleField.getText();
        String autor = authorField.getText();
        String genero = generoField.getText();

        if (selectedCoverFile != null) {
            try {
                Path destinationPath = Paths.get("br.com.gwenilorac.biblioteca.imagens" + selectedCoverFile.getName());

                Files.copy(selectedCoverFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

                byte[] capa = Files.readAllBytes(selectedCoverFile.toPath());
                
                System.out.println("Caminho do arquivo copiado: " + destinationPath.toString());

                ServicoLivro.adicionarLivro(titulo, autor, genero, capa);
                limparCampos();
                
            } catch (FileAlreadyExistsException e) {
                System.out.println("O arquivo já existe");
                JOptionPane.showMessageDialog(this, "O ARQUIVO JÁ EXISTE");
            } catch (IOException e) {
                System.out.println("Erro ao copiar o arquivo: " + e.getMessage());
                JOptionPane.showMessageDialog(this, "ERRO AO COPIAR O ARQUIVO");
            }
        } else {
            System.out.println("Nenhuma capa selecionada. O livro não será adicionado.");
            JOptionPane.showMessageDialog(this, "SELECIONE UMA CAPA PARA O LIVRO");
        }
    }

    private void limparCampos() {
        titleField.setText("");
        authorField.setText("");
        generoField.setText("");
        selectedCoverFile = null; 
    }

    private void selecionarCapa() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedCoverFile = fileChooser.getSelectedFile();
            System.out.println(selectedCoverFile.getName());
        }
    }
}

