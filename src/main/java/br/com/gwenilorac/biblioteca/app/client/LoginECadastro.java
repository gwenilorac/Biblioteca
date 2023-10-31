package br.com.gwenilorac.biblioteca.app.client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

@SuppressWarnings("serial")
public class LoginECadastro extends JDialog {

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
		btnLogin.setMnemonic(KeyEvent.VK_ENTER);
		btnLogin.addActionListener(evt -> actionLogin());
		getRootPane().setDefaultButton(btnLogin);

		btnCadastro = new JButton("Cadastro");
		btnCadastro.setMnemonic(KeyEvent.VK_ENTER);
		btnCadastro.addActionListener(evt -> actionCadastro());
		getRootPane().setDefaultButton(btnCadastro);
	}

	private void actionLogin() {
		LoginFrm loginFrm = new LoginFrm();

		if (loginFrm.isOK()) {
			new ApplicationFrm();
		}
		System.out.println("TESTE");
	}

	private void actionCadastro() {
		CadastroFrm cadastroFrm = new CadastroFrm();
	}

	private void initLayout() {
		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		panel.add(createMainForm());

		add(panel);
		setPreferredSize(new Dimension(300, 200));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
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

	private JPanel createMainForm() {
		FormLayout layout = new FormLayout("pref, 5px, 70dlu");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		
		builder.append("BEM VINDO DE VOLTA!");
		builder.nextLine();
		builder.append(btnLogin);
		
		builder.nextLine();
		
		builder.append("JUNTE-SE A GENTE!");
		builder.append(btnCadastro);
		
		return builder.getPanel();

	}

}
