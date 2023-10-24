package br.com.gwenilorac.biblioteca.app.client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.persistence.EntityManager;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.servicos.ServicoBusca;
import br.com.gwenilorac.biblioteca.servicos.ServicoLivro;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

@SuppressWarnings("serial")
public class ApplicationFrm extends JFrame {

	private PresentationModel<Usuario> model;
	private JDesktopPane jDesktopPane;
	private JTextField textField;
	private JTextField tfBusca;
	private JPasswordField passField;
	private JButton btnSalvar;
	private JButton btnBusca;
	private JButton btnCapas;

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
		btnBusca = new JButton("Buscar");
		btnBusca.addActionListener(bb -> realizarBusca(tfBusca.getText()));

	}

	private void actionSalvar() {

		String text = textField.getText();
		String pass = passField.getText();
		EntityManager em = JPAUtil.getEntityManager();
		UsuarioDao usuarioDao = new UsuarioDao(em);
		if (!(textField == null)) {
			model.getBean().setNome(text);
		}
		if (!(passField == null)) {
			model.getBean().setSenha(pass);
		}
		usuarioDao.atualizar(model.getBean());
		return;
	}

	private void initLayout() {
		jDesktopPane = new JDesktopPane();
		jDesktopPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
		jDesktopPane.add(exibirCapasDosLivros());

		JMenu menu = new JMenu("Menu");
		JMenuItem generos = new JMenuItem("Generos");
//		generos.addActionListener(evt -> abrirInternalFrame());
		JMenuItem adicionarLivro = new JMenuItem("Adicionar Livro");
		adicionarLivro.addActionListener(al -> abrirFormularioAdicionarLivro());
		menu.add(generos);
		menu.add(adicionarLivro);

		tfBusca = new JTextField("Faça sua busca aqui", 30);
		tfBusca.setMaximumSize(textField.getPreferredSize());

		JMenu user = new JMenu("User");
		JMenuItem userItem1 = new JMenuItem("Livros Emprestados");
//		userItem1.addActionListener(le -> abrirLivrosEmprestados());
		JMenuItem userItem2 = new JMenuItem("Editar Usuario");
		userItem2.addActionListener(eu -> abrirEditarUsuario());
		user.add(userItem1);
		user.add(userItem2);

		JMenuBar menubar = new JMenuBar();
		menubar.add(menu);
		menubar.add(Box.createHorizontalGlue());
		menubar.add(tfBusca);
		menubar.add(btnBusca);
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

	private void abrirFormularioAdicionarLivro() {

		AdicionarLivroFrm addBookForm = new AdicionarLivroFrm();
		JInternalFrame internalFrame = new JInternalFrame("Adicionar Livro", false, true, false, false);
		internalFrame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
		internalFrame.add(addBookForm);
		internalFrame.setSize(400, 300);
		Dimension d = jDesktopPane.getSize();
		internalFrame.setLocation((d.width - internalFrame.getSize().width) / 2, (d.height - internalFrame.getSize().height) / 2);
		internalFrame.setVisible(true);
		internalFrame.pack();

		jDesktopPane.add(internalFrame);

		internalFrame.toFront();
	}

	private Livro realizarBusca(String termoBusca) {
		if (ServicoBusca.busca(termoBusca) == null) {
			JOptionPane.showMessageDialog(this, "NÃO EXISTE!");
			return null;
		} else {
			@SuppressWarnings("unchecked")
			List<Livro> resultados = (List<Livro>) ServicoBusca.busca(termoBusca);
			return (Livro) resultados;
		}
	}

	private Component exibirCapasDosLivros() {
		
		List<Livro> livros = ServicoLivro.pegarLivros();
		
		FlowLayout layout = new FlowLayout();
		
		JInternalFrame internalFrame = new JInternalFrame();
		internalFrame.setLayout(layout);
		internalFrame.getContentPane().setPreferredSize(new Dimension(1285, 645));
		internalFrame.setVisible(true);
		internalFrame.pack();
		
		BasicInternalFrameUI ui = (BasicInternalFrameUI) internalFrame.getUI();
		Component northPane = ui.getNorthPane();
		MouseMotionListener[] motionListeners = (MouseMotionListener[]) northPane
				.getListeners(MouseMotionListener.class);
		for (MouseMotionListener listener : motionListeners)
			northPane.removeMouseMotionListener(listener);

		JPanel panelCapas = new JPanel();
//		panelCapas.setLayout(layout);
		
		for (Livro livro : livros) {
			btnCapas = new JButton();
			btnCapas.setLayout(layout);
			btnCapas.setSize(150, 200);
			
			byte[] imagemIcon = livro.getCapa();
			ImageIcon icon = new ImageIcon(imagemIcon);
			Image img = icon.getImage().getScaledInstance(btnCapas.getWidth(), btnCapas.getHeight(), java.awt.Image.SCALE_SMOOTH);
			ImageIcon newIcon = new ImageIcon(img);
			
			btnCapas.setIcon(newIcon);
			btnCapas.setVisible(true);
			btnCapas.addActionListener(bc -> abrirDetalhesDoLivro(livro));
			panelCapas.add(btnCapas);
		}
		
		btnCapas.setComponentOrientation(
                ComponentOrientation.LEFT_TO_RIGHT);
		
		internalFrame.add(panelCapas);
		internalFrame.add(btnCapas);
		
		jDesktopPane.add(internalFrame);

		return panelCapas;
	}

	private void abrirDetalhesDoLivro(Livro livro) {
		DetalhesLivroInternalFrame detalhesDoLivro = new DetalhesLivroInternalFrame(livro);
		JInternalFrame internalFrame = new JInternalFrame(livro.getTitulo(), false, true, false, false);
		internalFrame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
		internalFrame.add(detalhesDoLivro);
		internalFrame.setSize(400, 300);
		internalFrame.setVisible(true);
		internalFrame.pack();

		jDesktopPane.add(internalFrame);

		internalFrame.toFront();
	}

	private JPanel abrirEditarUsuario() {
		FormLayout layout = new FormLayout("pref, 5px, 70dlu");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		builder.append("User:", textField);
		builder.nextLine();

		builder.append("Password:", passField);

		JPanel jPanel = new JPanel();
		jPanel.setLayout(layout);

//		editarUsuarioFrm.add(jPanel);
//		jDesktopPane.add(editarUsuarioFrm);
		return builder.getPanel();

	}
}

//	private void abrirLivrosEmprestados() {
//		MyInternalFrame livrosEmprestadosFrm = new MyInternalFrame("LIVROS EMPRESTADOS");
//		jDesktopPane.add(livrosEmprestadosFrm);
//	}
//
//	private void abrirInternalFrame() {
//		MyInternalFrame frame = new MyInternalFrame("FRAME TESTE");
//		jDesktopPane.add(frame);
//	}
