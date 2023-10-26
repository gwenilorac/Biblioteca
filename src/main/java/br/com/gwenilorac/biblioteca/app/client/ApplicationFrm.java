package br.com.gwenilorac.biblioteca.app.client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyVetoException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import br.com.gwenilorac.biblioteca.dao.AutorDao;
import br.com.gwenilorac.biblioteca.dao.GeneroDao;
import br.com.gwenilorac.biblioteca.dao.LivroDao;
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
	private JButton btnUser;

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

		btnBusca = new JButton("Buscar");
		btnBusca.addActionListener(bb -> realizarBusca(tfBusca.getText()));
		btnUser = new JButton("User");
		btnUser.addActionListener(bu -> abrirFuncoesUsuario());

	}

	private void initLayout() {
		jDesktopPane = new JDesktopPane();
		jDesktopPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
		jDesktopPane.add(exibirCapasDosLivros());

		JMenu menu = new JMenu("Menu");
		JMenuItem generos = new JMenuItem("Generos");
		JMenuItem adicionarLivro = new JMenuItem("Adicionar Livro");
		adicionarLivro.addActionListener(al -> abrirFormularioAdicionarLivro());
		menu.add(generos);
		menu.add(adicionarLivro);

		tfBusca = new JTextField("Faça sua busca aqui", 30);
		tfBusca.setMaximumSize(textField.getPreferredSize());

		JMenuBar menubar = new JMenuBar();
		menubar.add(menu);
		menubar.add(Box.createHorizontalGlue());
		menubar.add(tfBusca);
		menubar.add(btnBusca);
		menubar.add(Box.createHorizontalGlue());
		menubar.add(btnUser);

		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(800, 600));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setContentPane(jDesktopPane);
		setJMenuBar(menubar);
		setVisible(true);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	private JInternalFrame abrirFuncoesUsuario() {
		FuncoesUsuario funcoesUser = new FuncoesUsuario();
		JInternalFrame internalFrame = new JInternalFrame("Detalhes do Usuário", true, true, true, true);
		internalFrame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
		internalFrame.add(funcoesUser);
		internalFrame.setPreferredSize(new Dimension(600, 400));
		internalFrame.pack();
		internalFrame.setVisible(true);

		jDesktopPane.add(internalFrame);

		internalFrame.toFront();

		Dimension desktopSize = jDesktopPane.getSize();
		Dimension jInternalFrameSize = internalFrame.getSize();
		internalFrame.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
				(desktopSize.height - jInternalFrameSize.height) / 2);
		
		return internalFrame;
	}

	private void abrirFormularioAdicionarLivro() {
		AdicionarLivroFrm addBookForm = new AdicionarLivroFrm();
		JInternalFrame internalFrame = new JInternalFrame("Adicionar Livro", false, true, false, false);
		internalFrame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
		internalFrame.add(addBookForm);
		internalFrame.setSize(400, 300);
		Dimension d = jDesktopPane.getSize();
		internalFrame.setLocation((d.width - internalFrame.getSize().width) / 2,
				(d.height - internalFrame.getSize().height) / 2);
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

	private JInternalFrame exibirCapasDosLivros() {
		List<Livro> livros = ServicoLivro.pegarLivros();
		JInternalFrame internalFrame = new JInternalFrame();
		internalFrame.setLayout(new FlowLayout());

		BasicInternalFrameUI ui = (BasicInternalFrameUI) internalFrame.getUI();
		Component northPane = ui.getNorthPane();
		MouseMotionListener[] motionListeners = (MouseMotionListener[]) northPane
				.getListeners(MouseMotionListener.class);
		for (MouseMotionListener listener : motionListeners)
			northPane.removeMouseMotionListener(listener);

		for (Livro livro : livros) {
			JButton btnCapas = criarBotaoComImagem(livro.getCapa());
			btnCapas.addActionListener(e -> abrirDetalhesDoLivro(livro));
			internalFrame.add(btnCapas);
		}

		internalFrame.pack();

		internalFrame.setVisible(true);

		try {
			internalFrame.setMaximum(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}

		return internalFrame;
	}

	private JButton criarBotaoComImagem(byte[] imagemDados) {
		ImageIcon icon = new ImageIcon(imagemDados);
		Image img = icon.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH);
		ImageIcon novaIcone = new ImageIcon(img);
		JButton botao = new JButton(novaIcone);
		botao.setPreferredSize(new Dimension(150, 200));
		return botao;
	}

	private void abrirDetalhesDoLivro(Livro livro) {
		DetalhesLivroInternalFrame detalhesDoLivro = new DetalhesLivroInternalFrame(livro);
		JInternalFrame internalFrame = new JInternalFrame(livro.getTitulo(), false, true, false, false);
		internalFrame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
		internalFrame.add(detalhesDoLivro);
		internalFrame.setSize(400, 300);
		internalFrame.setVisible(true);
		internalFrame.pack();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screenSize.width - internalFrame.getWidth()) / 2;
		int y = (screenSize.height - internalFrame.getHeight()) / 2;
		internalFrame.setLocation(x, y);

		jDesktopPane.add(internalFrame);

		internalFrame.toFront();
	}

}