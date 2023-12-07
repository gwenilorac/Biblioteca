package br.com.gwenilorac.biblioteca.app.client;

import java.awt.Dimension;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.com.gwenilorac.biblioteca.servicos.servicoLivro;

public class AddBookForm extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField generoField;
    private JButton addButton;

    public AddBookForm() {
        initComponents();
        initLayout();
    }

    private void initComponents() {
        titleField = new JTextField(20);
        authorField = new JTextField(20);
        generoField = new JTextField(20);
        addButton = new JButton("Adicionar Livro");

        addButton.addActionListener(e -> adicionarLivro());
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
                        .addComponent(generoLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(titleField)
                        .addComponent(authorField)
                        .addComponent(generoField)
                        .addComponent(addButton)));

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
                .addComponent(addButton));

        setPreferredSize(new Dimension(300, 200));
    }

    private void adicionarLivro() {
        String titulo = titleField.getText();
        String autor = authorField.getText();
        String genero = generoField.getText();

        servicoLivro.adicionarLivro(titulo, autor, genero);

        limparCampos();
    }

    private void limparCampos() {
        titleField.setText("");
        authorField.setText("");
        generoField.setText("");
    }
}