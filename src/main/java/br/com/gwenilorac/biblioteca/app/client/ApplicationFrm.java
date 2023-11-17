package br.com.gwenilorac.biblioteca.app.client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import com.jgoodies.binding.PresentationModel;

import br.com.gwenilorac.biblioteca.dao.LivroDao;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.servicos.ServicoBusca;
import br.com.gwenilorac.biblioteca.servicos.ServicoLogin;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

@SuppressWarnings("serial")
public class ApplicationFrm extends JFrame {

	private Usuario usuario = ServicoLogin.getUsuarioLogado();
	private PresentationModel<Usuario> model;
	private JDesktopPane jDesktopPane;
	private JTextField tfBusca;
	private JButton btnBusca;
	private JButton btnUser;
	private JButton btnAtualizar;
	private JInternalFrame internalFrame;
	private boolean isDetalhesLivroFrameOpen = false;
	private boolean isLivrosGuiOpen = false;
	private LivrosGUI livrosGui;
	private boolean isUsersGuiOpen = false;
	private UsersGUI usersGUI;



	public ApplicationFrm() {
		initModel();
		initComponents();
		initLayout();
	}

	private void initModel() {
		model = new PresentationModel<Usuario>(usuario);
	}

	private void initComponents() {
		tfBusca = new JTextField("Faça sua busca aqui", 30);
		tfBusca.setMaximumSize(tfBusca.getPreferredSize());
		btnBusca = new JButton("Buscar");
		btnBusca.addActionListener(bb -> realizarBusca(tfBusca.getText()));
		btnUser = new JButton("User");
		btnUser.addActionListener(bu -> abrirInfoUsuario());
		btnAtualizar = new JButton("Recarregar");
		btnAtualizar.addActionListener(ba -> atualizarTela());
	}

	private void initLayout() {
		jDesktopPane = new JDesktopPane();
		jDesktopPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
		jDesktopPane.add(exibirCapasDosLivros());

		JMenu menu = new JMenu("Menu");
		JMenuItem editarLivro = new JMenuItem("Editar Livro");
		editarLivro.addActionListener(al -> livrosPanel());
		menu.add(editarLivro);

		JMenuBar menubar = new JMenuBar();
		menubar.add(menu);
		menubar.add(btnAtualizar);
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

	private JFrame livrosPanel() {
	    if (livrosGui == null || !livrosGui.isVisible()) {
	        livrosGui = new LivrosGUI();

	        livrosGui.addWindowListener(new WindowAdapter() {
	            @Override
	            public void windowClosed(WindowEvent e) {
	                isLivrosGuiOpen = false;
	            }
	        });

	        isLivrosGuiOpen = true;
	        return livrosGui;
	    } else {
	        livrosGui.dispose(); 
	        livrosGui = new LivrosGUI(); 

	        livrosGui.addWindowListener(new WindowAdapter() {
	            @Override
	            public void windowClosed(WindowEvent e) {
	                isLivrosGuiOpen = false;
	            }
	        });

	        isLivrosGuiOpen = true;
	        return livrosGui;
	    }
	}

	private JFrame abrirInfoUsuario() {
	    if (usersGUI != null) {
	        usersGUI.dispose();
	    }

	    usersGUI = new UsersGUI();

	    usersGUI.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosed(WindowEvent e) {
	            isUsersGuiOpen = false;
	        }
	    });

	    isUsersGuiOpen = true;
	    return usersGUI;
	}

	private void atualizarTela() {
		jDesktopPane.removeAll();
		jDesktopPane.add(exibirCapasDosLivros());
        revalidate();
        repaint();
    }


	private Livro realizarBusca(String termoBusca) {
		if (ServicoBusca.busca(termoBusca) == null) {
			JOptionPane.showMessageDialog(this, "NÃO EXISTE!");
			return null;
		} else {
			List<Livro> resultados = (List<Livro>) ServicoBusca.busca(termoBusca);
			return (Livro) resultados;
		}
	}

	private JInternalFrame exibirCapasDosLivros() {
		EntityManager em = JPAUtil.getEntityManager();
		LivroDao livroDao = new LivroDao(em);
		
		List<Livro> livros = livroDao.buscarTodosLivros();
		JInternalFrame internalFrame = new JInternalFrame();
		internalFrame.setLayout(new FlowLayout());

		BasicInternalFrameUI ui = (BasicInternalFrameUI) internalFrame.getUI();
		Component northPane = ui.getNorthPane();
		MouseMotionListener[] motionListeners = (MouseMotionListener[]) northPane
				.getListeners(MouseMotionListener.class);
		for (MouseMotionListener listener : motionListeners)
			northPane.removeMouseMotionListener(listener);

		for (Livro livro : livros) {
			JPanel panel = new JPanel();
			panel.setPreferredSize(new Dimension(200, 225));
			panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
			panel.setBorder(BorderFactory.createRaisedBevelBorder());
			
			JButton btnCapas = criarBotaoComImagem(livro.getCapa());
			btnCapas.addActionListener(e -> abrirDetalhesDoLivro(livro));
			JLabel tituloLivro = new JLabel(livro.getTitulo());
			
			panel.add(btnCapas);
			panel.add(tituloLivro);
			
			internalFrame.add(panel);
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
	    if (!isDetalhesLivroFrameOpen) {
	        DetalhesLivroInternalFrame detalhesDoLivro = new DetalhesLivroInternalFrame(livro);
	        internalFrame = new JInternalFrame(livro.getTitulo(), false, true, false, false);
	        internalFrame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
	        internalFrame.add(detalhesDoLivro);
	        internalFrame.setSize(400, 300);
	        internalFrame.setVisible(true);
	        internalFrame.pack();

	        centralizarPanel();

	        jDesktopPane.add(internalFrame);

	        internalFrame.toFront();
	        isDetalhesLivroFrameOpen = true;

	        internalFrame.addInternalFrameListener(new InternalFrameAdapter() {
	            @Override
	            public void internalFrameClosed(InternalFrameEvent e) {
	                isDetalhesLivroFrameOpen = false;
	            }
	        });
	    } else {
	        JOptionPane.showMessageDialog(this, "Detalhes do Livro já está aberto.");
	    }
	}
	
	private void centralizarPanel() {
		Dimension desktopSize = jDesktopPane.getSize();
		Dimension jInternalFrameSize = internalFrame.getSize();
		internalFrame.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
				(desktopSize.height - jInternalFrameSize.height) / 2);
	}

}