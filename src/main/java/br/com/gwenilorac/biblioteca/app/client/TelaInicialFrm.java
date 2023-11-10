package br.com.gwenilorac.biblioteca.app.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.persistence.EntityManager;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import br.com.gwenilorac.biblioteca.util.JPAUtil;

@SuppressWarnings("serial")
public class TelaInicialFrm extends JDialog {

    private JPanel cadastroPanel;
    private JPanel loginPanel;

    public TelaInicialFrm() {
        initComponents();
        initLayout();
    }

    private void initComponents() {
        loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.PAGE_AXIS));
        JLabel labelLogin = new JLabel("BEM VINDO DE VOLTA!");
        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(this::openLoginForm);
        btnLogin.setPreferredSize(new Dimension(80, 30));
        loginPanel.add(labelLogin);
        loginPanel.add(btnLogin);
        loginPanel.setAlignmentX(LEFT_ALIGNMENT);

        cadastroPanel = new JPanel();
        cadastroPanel.setLayout(new BoxLayout(cadastroPanel, BoxLayout.PAGE_AXIS));
        JLabel labelCadastro = new JLabel("JUNTE-SE A NOS!");
        JButton btnCadastro = new JButton("Cadastro");
        btnCadastro.addActionListener(this::openCadastroForm);
        btnCadastro.setPreferredSize(new Dimension(90, 30));
        cadastroPanel.add(labelCadastro);
        cadastroPanel.add(btnCadastro);
        cadastroPanel.setAlignmentX(RIGHT_ALIGNMENT); 

        JSeparator separator = new JSeparator(JSeparator.VERTICAL);
        separator.setPreferredSize(new Dimension(20, 100)); 

        JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        contentPanel.add(loginPanel);
        contentPanel.add(separator);
        contentPanel.add(cadastroPanel);

        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
    }

    private void initLayout() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(400, 150)); 
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    private void openLoginForm(ActionEvent e) {
        dispose();
        new LoginFrm();
    }

    private void openCadastroForm(ActionEvent e) {
        dispose();
        new CadastroFrm();

    }
}
