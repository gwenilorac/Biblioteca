package br.com.gwenilorac.biblioteca.app.client;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.servicos.ServicoLogin;

@SuppressWarnings("serial")
public class LoginFrm extends JDialog {
	private PresentationModel<Usuario> model;
	private JTextField tfUserName;
	private JPasswordField passField;
	private JButton btnLogin;
	private boolean login = false;
	private static final String ERROR_MISSING_NAME = "FALTA INFORMAR O NOME";
	private static final String ERROR_MISSING_PASSWORD = "FALTA INFORMAR A SENHA";

	public LoginFrm() {
		initModel();
		initComponets();
		initLayout();
	}

	private void initModel() {
		Usuario usuario = new Usuario();
		model = new PresentationModel<Usuario>(usuario);
	}

	private void initComponets() {
		tfUserName = BasicComponentFactory.createTextField(model.getModel("nome"));
		passField = BasicComponentFactory.createPasswordField(model.getModel("senha"));

		btnLogin = new JButton("Login");
		btnLogin.setMnemonic(KeyEvent.VK_ENTER);
		btnLogin.addActionListener(evt -> actionLogin());
		getRootPane().setDefaultButton(btnLogin);
	}

	private void actionLogin() {
	    if (!isValid(model.getBean())) return;

	    if (ServicoLogin.validaUsuario(model.getBean())) {
	        login = true;
	        dispose();
	        System.out.println("LOGIN BEM-SUCEDIDO");
	        new ApplicationFrm();
	    } else {
	        JOptionPane.showMessageDialog(this, "USUÁRIO INVÁLIDO!");
	        login = false;
	    }
	}

	private boolean isValid(Usuario bean) {
	    if (bean.getNome() == null || bean.getNome().isEmpty()) {
	        JOptionPane.showMessageDialog(this, ERROR_MISSING_NAME);
	        return false;
	    }
	    if (bean.getSenha() == null || bean.getSenha().isEmpty()) {
	        JOptionPane.showMessageDialog(this, ERROR_MISSING_PASSWORD);
	        return false;
	    }
	    return true;
	}

	private void initLayout() {
	    JPanel panel = new JPanel();
	    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
	    panel.add(createMainForm());
	    panel.add(createButtonPanel());

	    add(panel);
	    setModal(true);
	    setPreferredSize(new Dimension(300, 150));
	    setLocationRelativeTo(null);
	    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	    pack();

	    setVisible(true);
	}

	private Component createButtonPanel() {
		JPanel panel = new JPanel();
		panel.add(btnLogin);
		return panel;
	}

	private Component createMainForm() {
		JPanel loginPanel = new JPanel();
		
		FormLayout layout = new FormLayout("pref, 5px, 70dlu");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		builder.append("User:", tfUserName);
		builder.nextLine();

		builder.append("Password:", passField);
		
		JPanel formPanel = builder.getPanel();
		
		loginPanel.add(formPanel);

		return loginPanel;
	}

	public boolean isOK() {
		return login;
	}

}
