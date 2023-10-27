package br.com.gwenilorac.biblioteca.app.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.persistence.EntityManager;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;

import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.servicos.ServicoLogin;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

@SuppressWarnings("serial")
public class FuncoesUsuario extends JPanel {

	private PresentationModel<Usuario> model;
	private EntityManager em = JPAUtil.getEntityManager();
	private UsuarioDao usuarioDao;
	Usuario usuario = ServicoLogin.getUsuarioLogado();
	private JTextField txtNome;
	private JTextField txtEmail;
	private JLabel lblNome;
	private JLabel lblEmail;
	private JButton btnSalvar;
	private JButton btnRemover;
	private JSeparator separator;
	private String novoNome;
	private String novoEmail;

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

		lblNome = new JLabel("Nome: ");
		txtNome = new JTextField(usuario.getNome());
		txtNome.setPreferredSize(new Dimension(200, 30));

		lblEmail = new JLabel("Email: ");
		txtEmail = new JTextField(usuario.getEmail());
		txtEmail.setPreferredSize(new Dimension(200, 30));

		btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(this::salvarEdicao);
		btnRemover = new JButton("Remover Usuario");
		btnRemover.addActionListener(this::excluirUsuario);

		separator = new JSeparator(JSeparator.HORIZONTAL);

		usuarioDao = new UsuarioDao(em);

		novoNome = txtNome.getText();
		novoEmail = txtEmail.getText();

	}

	public void initLayout() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(400, 250));

		JPanel editarUserPanel = criarEditarUser();
		JPanel livrosEmprestadosPanel = criarLivrosEmprestadosList();

		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(editarUserPanel, BorderLayout.NORTH);
		contentPanel.add(separator, BorderLayout.CENTER);
		contentPanel.add(livrosEmprestadosPanel, BorderLayout.SOUTH);

		add(contentPanel);
	}

	private JPanel criarEditarUser() {
		JPanel editarPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 5, 5, 5);

		editarPanel.add(lblNome, gbc);
		gbc.gridx++;
		editarPanel.add(txtNome, gbc);
		gbc.gridx = 0;
		gbc.gridy++;
		editarPanel.add(lblEmail, gbc);
		gbc.gridx++;
		editarPanel.add(txtEmail, gbc);
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		editarPanel.add(btnSalvar, gbc);
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		editarPanel.add(btnRemover, gbc);

		return editarPanel;
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

	private JPanel criarLivrosEmprestadosList() {
		JPanel panel = new JPanel();
		JLabel label = new JLabel("AINDA NAO IMPLEMENTADO");
		panel.add(label);
		return panel;
	}
}
