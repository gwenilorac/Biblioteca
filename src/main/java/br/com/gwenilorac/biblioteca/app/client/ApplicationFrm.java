package br.com.gwenilorac.biblioteca.app.client;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.persistence.EntityManager;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

@SuppressWarnings("serial")
public class ApplicationFrm extends JFrame{
	
	private PresentationModel<Usuario> model;
	private JDesktopPane jDesktopPane;
	private JTextField textField;
	private JPasswordField passField; 
	private JButton btnSalvar;

	public ApplicationFrm() {
		initModel();
		initComponents();
		initLayout();
	}

	private void initModel() {
		Usuario usuario = new Usuario();
		model = new PresentationModel<Usuario>(usuario);
	}

	private void initComponents() {
		textField = BasicComponentFactory.createTextField(model.getModel("nome"));
		passField = BasicComponentFactory.createPasswordField(model.getModel("senha"));
		
		btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(bs -> actionSalvar());
	}

	private void actionSalvar() {
		
		String text = textField.getText();
		String pass = passField.getText();
		EntityManager em = JPAUtil.getEntityManager();
		UsuarioDao usuarioDao = new UsuarioDao(em);
		if(!(textField == null)) {
			model.getBean().setNome(text);
		} if(!(passField == null)) {
			model.getBean().setSenha(pass);
		}
		usuarioDao.atualizar(model.getBean());
		return;
	}

	private void initLayout() {
		jDesktopPane = new JDesktopPane();
		jDesktopPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

		JMenu menu = new JMenu("Menu");
		JMenuItem menuItem1 = new JMenuItem("Generos");
		menuItem1.addActionListener(evt -> abrirInternalFrame());
		JMenuItem menuItem2 = new JMenuItem("Autores");
		menu.add(menuItem1);
		menu.add(menuItem2);
		
		JMenu busca = new JMenu("Busca");

		JMenu user = new JMenu("User");
		JMenuItem userItem1 = new JMenuItem("Livros Emprestados");
		userItem1.addActionListener(le -> abrirLivrosEmprestados());
		JMenuItem userItem2 = new JMenuItem("Editar Usuario");
		userItem2.addActionListener(eu -> abrirEditarUsuario());
		user.add(userItem1);
		user.add(userItem2);
		
		JMenuBar menubar = new JMenuBar();
		menubar.add(menu);
		menubar.add(busca);
		menubar.add(Box.createHorizontalGlue());
		menubar.add(user);
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(800, 600));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setContentPane(jDesktopPane);
		setJMenuBar(menubar);
		setVisible(true);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		
	}

	private JPanel abrirEditarUsuario() {
		MyInternalFrame editarUsuarioFrm = new MyInternalFrame("EDITAR USUARIO");
		
		FormLayout layout = new FormLayout("pref, 5px, 70dlu");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		builder.append("User:", textField);
		builder.nextLine();
		
		builder.append("Password:", passField);
		
		JPanel jPanel = new JPanel();
		jPanel.setLayout(layout);

		editarUsuarioFrm.add(jPanel);		
		jDesktopPane.add(editarUsuarioFrm);
		return builder.getPanel();
		
	}

	private void abrirLivrosEmprestados() {
		MyInternalFrame livrosEmprestadosFrm = new MyInternalFrame("LIVROS EMPRESTADOS");
		jDesktopPane.add(livrosEmprestadosFrm);
	}

	private void abrirInternalFrame() {
		MyInternalFrame frame = new MyInternalFrame("FRAME TESTE");
		jDesktopPane.add(frame);
	}

}
