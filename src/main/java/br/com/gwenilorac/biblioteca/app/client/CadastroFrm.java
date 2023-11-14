package br.com.gwenilorac.biblioteca.app.client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
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
import br.com.gwenilorac.biblioteca.servicos.ServicoCadastro;

@SuppressWarnings("serial")
public class CadastroFrm extends JDialog {

	private PresentationModel<Usuario> model;

	private JTextField tfUserName;

	private JTextField tfEmail;

	private JPasswordField passField;

	private JButton btnCadastro;

	private boolean cadastro = false;

	public CadastroFrm() {
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
		tfEmail = BasicComponentFactory.createTextField(model.getModel("email"));
		passField = BasicComponentFactory.createPasswordField(model.getModel("senha"));

		btnCadastro = new JButton("Cadastro");
		getRootPane().setDefaultButton(btnCadastro);
		btnCadastro.setMnemonic(KeyEvent.VK_ENTER);
		btnCadastro.addActionListener(evt -> actionCadastro());
	}

	private void actionCadastro() {
		if (!isValid(model.getBean()))
			return;

		ServicoCadastro.cadastraUsuario(model.getBean());
		cadastro = true;
		dispose();
	}

	private boolean isValid(Usuario bean) {
		if (bean.getNome() == null || bean.getNome().isEmpty() || bean.getNome() == " ") {
			JOptionPane.showMessageDialog(this, "FALTA INFORMAR O NOME");
			return false;
		}
		if (bean.getEmail() == null || bean.getEmail().isEmpty() || bean.getEmail() == " "
				|| (!bean.getEmail().contains("@"))) {
			JOptionPane.showMessageDialog(this, "INSIRA UM EMAIL VALIDO");
			return false;
		}
		if (bean.getSenha() == null || bean.getSenha().isEmpty() || bean.getSenha() == " ") {
			JOptionPane.showMessageDialog(this, "FALTA INFORMAR A SENHA");
			return false;
		}
		if (ServicoCadastro.cadastraUsuario(model.getBean()) == false) {
			JOptionPane.showMessageDialog(this, "EMAIL JA CADASTRADO");
			return false;
		} else {
			return true;
		}
	}

	private void initLayout() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(createMainForm(), BorderLayout.CENTER);
		panel.add(createButtonPanel(), BorderLayout.SOUTH);

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
		panel.add(btnCadastro);
		return panel;
	}

	private Component createMainForm() {
		
		JPanel cadastroPanel = new JPanel();
		
		FormLayout layout = new FormLayout("pref, 5px, 70dlu");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		builder.append("User:", tfUserName);
		builder.nextLine();

		builder.append("Email:", tfEmail);
		builder.nextLine();

		builder.append("Password:", passField);

		JPanel formPanel = builder.getPanel();

		cadastroPanel.add(formPanel);

		return cadastroPanel;
	}

	public boolean isOK() {
		return cadastro;
	}

}
