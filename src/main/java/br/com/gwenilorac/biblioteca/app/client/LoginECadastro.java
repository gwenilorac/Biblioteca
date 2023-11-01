package br.com.gwenilorac.biblioteca.app.client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

@SuppressWarnings("serial")
public class LoginECadastro extends JDialog {

	private static final int GAP = 0;
	private JLabel lLogin = new JLabel("BEM VINDO DE VOLTA!");
	private JLabel lCadastro = new JLabel("JUNTE-SE A GENTE!");
	private JButton btnLogin;
	private JButton btnCadastro;

	public LoginECadastro() {
		initComponets();
		initLayout();
	}

	private void initComponets() {
		btnLogin = new JButton("Login");
		btnLogin.addActionListener(evt -> actionLogin());

		btnCadastro = new JButton("Cadastro");
		btnCadastro.addActionListener(evt -> actionCadastro());
	}

	private void actionLogin() {
		LoginFrm loginFrm = new LoginFrm();
	}

	private void actionCadastro() {
		CadastroFrm cadastroFrm = new CadastroFrm();
	}

	private void initLayout() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(440, 100));
		setLocationRelativeTo(null);
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		JSeparator separator = new JSeparator(JSeparator.VERTICAL);

		JPanel panele = createLoginForm();
		JPanel paneld = createCadastroForm();
		
		JPanel panelEsquerda = new JPanel();
		panelEsquerda.setLayout(new BorderLayout());
		panelEsquerda.setPreferredSize(new Dimension(210, 100));
		panelEsquerda.add(panele, BorderLayout.CENTER);
		
		JPanel panelDireita = new JPanel();
		panelDireita.setLayout(new BorderLayout());
		panelDireita.setPreferredSize(new Dimension(210, 100));
		panelDireita.add(paneld, BorderLayout.CENTER);
		
		add(panelEsquerda, BorderLayout.LINE_START);
		add(separator, BorderLayout.CENTER);
		add(panelDireita, BorderLayout.LINE_END);
		pack();
		setVisible(true);
	}

//	private Component createButtonPanel() {
//		JPanel panel = new JPanel();
//		panel.setLayout(new FlowLayout());
//		
//		panel.add(btnLogin);
//		panel.add(btnCadastro);
//		return panel;
//	}

	private JPanel createLoginForm() {
		FormLayout layout = new FormLayout("pref, 5px, 70dlu");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		builder.append("BEM VINDO DE VOLTA!");
		builder.nextLine();
		builder.append(btnLogin);

		return builder.getPanel();
	}

	private JPanel createCadastroForm() {
		FormLayout layout = new FormLayout("pref, 5px, 70dlu");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		builder.append("JUNTE-SE A NÓS!");
		builder.nextLine();
		builder.append(btnCadastro);

		return builder.getPanel();
	}

}
