package br.com.gwenilorac.biblioteca.app.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.persistence.EntityManager;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

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
public class FuncoesUsuario extends JPanel {

	private PresentationModel<Usuario> model;
	private EntityManager em = JPAUtil.getEntityManager();
	private UsuarioDao usuarioDao;
	private LivroDao livroDao;
	Usuario usuario = ServicoLogin.getUsuarioLogado();
	private JTextField txtNome;
	private JTextField txtEmail;
	private JButton btnSalvar;
	private JButton btnRemover;
	private JSeparator separator;
	private String novoNome;
	private String novoEmail;
	private JPanel editarUserPanel;
	private JPanel livrosEmprestadosPanel;
	private JList list;
	private DefaultListModel listModel;

	public FuncoesUsuario() {
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

		separator = new JSeparator(JSeparator.HORIZONTAL);

		usuarioDao = new UsuarioDao(em);
		livroDao = new LivroDao(em);

		novoNome = txtNome.getText();
		novoEmail = txtEmail.getText();

	}

	public void initLayout() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(400, 250));

		editarUserPanel = criarEditarUser();
		livrosEmprestadosPanel = criarLivrosEmprestadosList();

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.add(editarUserPanel, BorderLayout.NORTH);
		contentPanel.add(separator, BorderLayout.CENTER);
		contentPanel.add(livrosEmprestadosPanel, BorderLayout.SOUTH);

		add(contentPanel);
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
			if (model.getBean().getLivrosEmprestados() == null || model.getBean().getLivrosEmprestados().isEmpty()) {
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
		}
	}

	@SuppressWarnings("unchecked")
	private JPanel criarLivrosEmprestadosList() {
		JPanel panel = new JPanel();
		
		Usuario usuarioGerenciado = em.merge(usuario);
        em.getTransaction().begin();
		List<Livro> livrosEmprestadosPorUsuario = usuarioDao.buscarLivrosEmprestadosPorUsuario(usuarioGerenciado);

		listModel = new DefaultListModel<String>();
		listModel.addElement("Jane Doe");
		listModel.addElement("John Smith");
		listModel.addElement("Kathy Green");

		list = new JList<String>(listModel); 
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		
		for (Livro livro : livrosEmprestadosPorUsuario) {
			System.out.println(livro.getTitulo());
			String titulo = livro.getTitulo();
			listModel.addElement(titulo);
		}

		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(300, 150));

		panel.add(listScroller);
		
		em.getTransaction().commit();
		em.close();
		
		return panel;

	}
}
