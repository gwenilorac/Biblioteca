package br.com.gwenilorac.biblioteca.app.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.persistence.EntityManager;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

import br.com.gwenilorac.biblioteca.dao.LivroDao;
import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.servicos.ServicoLogin;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

@SuppressWarnings("serial")
public class InfoUsuario extends JPanel {

	private PresentationModel<Usuario> model;
	private EntityManager em = JPAUtil.getEntityManager();
	private UsuarioDao usuarioDao;
	private LivroDao livroDao;
	Usuario usuario = ServicoLogin.getUsuarioLogado();
	private JTextField txtNome;
	private JTextField txtEmail;
	private JButton btnSalvar;
	private JButton btnRemover;
	private String novoNome;
	private String novoEmail;
	private JPanel editarUserPanel;
	private JPanel livrosEmprestadosPanel;
	private JList list;
	private JTable table;
	private JList livroList;
	private static DefaultListModel<String> listModel;
	private JButton btnDevolverLivro;
	private JPanel infoPanel;
	private JPanel detalhesPanel;
	private JPanel capaPanel;
	private byte[] imagemIcon;
	private ImageIcon icon;
	private Image img;
	private ImageIcon newIcon;
	private JLabel capaLabel;

	public InfoUsuario() {
		initModel();
		initComponents();
		initLayout();
	}

	private void initModel() {
		model = new PresentationModel<>(usuario);
	}

	private void initComponents() {
		txtNome = BasicComponentFactory.createTextField(model.getModel("nome"));
		txtEmail = BasicComponentFactory.createTextField(model.getModel("email"));

		btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(this::salvarEdicao);
		btnRemover = new JButton("Remover Usuario");
		btnRemover.addActionListener(this::excluirUsuario);

		usuarioDao = new UsuarioDao(em);
		livroDao = new LivroDao(em);

		novoNome = txtNome.getText();
		novoEmail = txtEmail.getText();
		
		infoPanel = new JPanel();
		detalhesPanel = new JPanel();

	}

	public void initLayout() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(400, 250));

		JPanel infoPane = criarEditarUser();
		livrosEmprestadosPanel = criarLivrosEmprestadosPanel(
				usuarioDao.buscarLivrosEmprestados(model.getBean().getId()));

		add(infoPane, BorderLayout.LINE_START);
		add(livrosEmprestadosPanel, BorderLayout.AFTER_LAST_LINE);

		setVisible(true);
	}

	private JPanel criarEditarUser() {
		FormLayout layout = new FormLayout("pref, 5px, 70dlu");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		builder.append("Nome: ", txtNome);
		builder.nextLine();

		builder.append("Email: ", txtEmail);
		builder.nextLine();

		builder.append(btnSalvar);
		builder.append(btnRemover);

		return builder.getPanel();

	}

	private void excluirUsuario(ActionEvent e) {
		try {
			if (usuarioDao.buscarLivrosEmprestados(model.getBean().getId()) == null
					|| usuarioDao.buscarLivrosEmprestados(model.getBean().getId()).isEmpty()) {
				em.getTransaction().begin();
				usuarioDao.remover(model.getBean());
				em.getTransaction().commit();
				JOptionPane.showMessageDialog(this, "Usuário removido com sucesso!");
				System.exit(0);
			} else {
				JOptionPane.showMessageDialog(this,
						"Usuário não pode ser excluído, pois ainda tem livros emprestados. Devolva-os primeiro!");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Erro ao remover o usuário.");
		}
		em.close();
	}

	private void salvarEdicao(ActionEvent e) {
		if (novoNome.isEmpty() || novoEmail.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Nome e e-mail não podem estar vazios.");
		} else if (novoNome.equals(model.getBean().getNome()) && novoEmail.equals(model.getBean().getEmail())) {
			JOptionPane.showMessageDialog(this, "Nenhuma alteração feita.");
		} else {
			try {
				em.getTransaction().begin();
				model.getBean().setNome(novoNome);
				model.getBean().setEmail(novoEmail);
				usuarioDao.atualizar(model.getBean());
				em.getTransaction().commit();

				JOptionPane.showMessageDialog(this, "Informações atualizadas com sucesso!");
			} catch (Exception ex) {
				em.getTransaction().rollback();
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "Erro ao atualizar as informações do usuário.");
			}
			em.close();
		}
	}

	public JPanel criarLivrosEmprestadosPanel(List<Livro> livrosEmprestados) {
	    listModel = new DefaultListModel<>();
	    for (Livro livro : livrosEmprestados) {
	        listModel.addElement(livro.getTitulo());
	    }

	    livroList = new JList<>(listModel);
	    livroList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    livroList.setLayoutOrientation(JList.VERTICAL);
	    livroList.setVisibleRowCount(-1);
	    livroList.addListSelectionListener(new ListSelectionListener() {
	        @Override
	        public void valueChanged(ListSelectionEvent e) {
	            if (!e.getValueIsAdjusting()) {
	                int selectedIndex = livroList.getSelectedIndex();
	                if (selectedIndex != -1) {
	                    String tituloSelecionado = (String) livroList.getSelectedValue();
	                    Livro livroSelecionado = livroDao.buscarLivroPorTitulo(tituloSelecionado);

	                    if (livroSelecionado != null) {
	                    	DetalhesLivro(livroSelecionado);
	                        System.out.println("Livro Selecionado: " + livroSelecionado.getTitulo());
	                    }
	                }
	            }
	        }
	    });

	    JScrollPane scrollPane = new JScrollPane(livroList);
	    scrollPane.setPreferredSize(new Dimension(250, 80));

	    JPanel panel = new JPanel(new BorderLayout());
	    panel.add(scrollPane);

	    return panel;
	}
	
	public JPanel DetalhesLivro(Livro livro) {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(520, 250));
		
		detalhesPanel = new JPanel(new BorderLayout());
		
		capaPanel = new JPanel();
		imagemIcon = livro.getCapa();
		icon = new ImageIcon(imagemIcon);
		img = icon.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH);
		newIcon = new ImageIcon(img);
		capaLabel = new JLabel(newIcon);
		capaPanel.add(capaLabel);
		
		infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
		infoPanel.add(new JLabel("Nome: " + livro.getTitulo()));
		infoPanel.add(new JLabel("Autor: " + livro.getAutor().getNome()));
		infoPanel.add(new JLabel("Gênero: " + livro.getGenero().getNome()));

		detalhesPanel.add(capaPanel);
		detalhesPanel.add(infoPanel);
		
		
		
		return detalhesPanel;

	}
}