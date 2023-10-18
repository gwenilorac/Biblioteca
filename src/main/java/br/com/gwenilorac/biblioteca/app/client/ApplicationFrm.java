package br.com.gwenilorac.biblioteca.app.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.persistence.EntityManager;
import javax.swing.Box;
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

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.servicos.ServicoBusca;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

@SuppressWarnings("serial")
public class ApplicationFrm extends JFrame {

	private PresentationModel<Usuario> model;
	private JDesktopPane jDesktopPane;
	private JTextField textField;
	private JPasswordField passField;
	private JButton btnSalvar;
	private JTextField titleField = new JTextField(20);
	private JTextField authorField = new JTextField(20);
	private JTextField isbnField = new JTextField(20);
	private JButton addButton = new JButton("Adicionar Livro");

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

		JMenu menu = new JMenu("Menu");
		JMenuItem generos = new JMenuItem("Generos");
		generos.addActionListener(evt -> abrirInternalFrame());
		JMenuItem adicionarLivro = new JMenuItem("Adicionar Livro");
		adicionarLivro.addActionListener(al -> abrirFormularioAdicionarLivro());
		menu.add(generos);
		menu.add(adicionarLivro);

		JTextField busca = new JTextField("Faça sua busca aqui", 30);
		busca.setMaximumSize(textField.getPreferredSize());
		busca.addActionListener(e -> realizarBusca(busca.getText()));

		JMenu user = new JMenu("User");
		JMenuItem userItem1 = new JMenuItem("Livros Emprestados");
		userItem1.addActionListener(le -> abrirLivrosEmprestados());
		JMenuItem userItem2 = new JMenuItem("Editar Usuario");
		userItem2.addActionListener(eu -> abrirEditarUsuario());
		user.add(userItem1);
		user.add(userItem2);

		JMenuBar menubar = new JMenuBar();
		menubar.add(menu);
		menubar.add(Box.createHorizontalGlue());
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

	private void abrirFormularioAdicionarLivro() {
//		MyInternalFrame editarUsuarioFrm = new MyInternalFrame("EDITAR USUARIO");

		AddBookForm addBookForm = new AddBookForm();
        JInternalFrame internalFrame = new JInternalFrame("Adicionar Livro", true, true, true, true);
        internalFrame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        internalFrame.add(addBookForm);
        internalFrame.setSize(400, 300);
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

//	// Método para exibir as capas dos livros em botões
//    private void exibirCapasDosLivros(List<Livro> livros) {
//        JPanel panelCapas = new JPanel();
//        panelCapas.setLayout(new FlowLayout());
//
//        // Adicionar botões para cada livro com sua respectiva capa
//        for (Livro livro : livros) {
//            JButton botaoCapa = new JButton();
//            botaoCapa.setIcon(livro.getCapa());  // Define a imagem da capa como ícone do botão
//            botaoCapa.setPreferredSize(new Dimension(100, 150));  // Ajuste o tamanho conforme necessário
//            botaoCapa.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    abrirDetalhesDoLivro(livro);  // Chama o método para exibir detalhes ao clicar na capa
//                }
//            });
//            panelCapas.add(botaoCapa);
//        }
//
//        // Adicione panelCapas à sua interface gráfica onde você deseja exibir as capas dos livros
//        // Por exemplo, você pode ter um JPanel chamado "panel" onde deseja adicionar as capas
//        panel.add(panelCapas);
//    }
//
//    // Método para abrir uma InternalFrame com detalhes do livro ao clicar na capa
//    private void abrirDetalhesDoLivro(Livro livro) {
//        // Implemente a lógica para abrir uma InternalFrame com detalhes do livro aqui
//        // Use a informação do livro passada como parâmetro para exibir os detalhes na InternalFrame
//        // Você pode criar uma nova InternalFrame, preenchê-la com informações do livro e exibi-la
//        // Exemplo:
//        // DetalhesLivroInternalFrame detalhesFrame = new DetalhesLivroInternalFrame(livro);
//        // desktopPane.add(detalhesFrame);
//        // detalhesFrame.setVisible(true);
//    }

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
