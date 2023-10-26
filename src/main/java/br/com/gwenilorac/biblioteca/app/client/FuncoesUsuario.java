package br.com.gwenilorac.biblioteca.app.client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.persistence.EntityManager;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.servicos.ServicoLogin;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

@SuppressWarnings("serial")
public class FuncoesUsuario extends JPanel {

	Usuario usuarioLogado = ServicoLogin.getUsuarioLogado();
	private JTextField txtNome;
	private JTextField txtEmail;
	private JPanel infoPanel;

	public FuncoesUsuario() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(400, 250));

		JPanel infoUsuarioPanel = new JPanel(new BorderLayout());

		JPanel infoPanel = criarInfoPanel();
		JPanel editPanel = criarEditarUser();
		JPanel livrosEmprestadosPanel = criarLivrosEmprestadosList();

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Informações Usuario", infoPanel);
		tabbedPane.addTab("Editar Usuário", editPanel);
		tabbedPane.addTab("Livros Emprestados", livrosEmprestadosPanel);

		infoUsuarioPanel.add(tabbedPane);

		add(infoUsuarioPanel);

	}

	private JPanel criarInfoPanel() {
		EntityManager em = JPAUtil.getEntityManager();
		UsuarioDao usuarioDao = new UsuarioDao(em);
		Usuario usuario = usuarioDao.buscarUsuarioPorNome(usuarioLogado.getNome());

		infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.add(new JLabel("Nome: " + usuario.getNome()));
		infoPanel.add(new JLabel("Email: " + usuario.getEmail()));
		return infoPanel;
	}

	private JPanel criarEditarUser() {
		EntityManager em = JPAUtil.getEntityManager();
		UsuarioDao usuarioDao = new UsuarioDao(em);
		Usuario usuario = usuarioDao.buscarUsuarioPorNome(usuarioLogado.getNome());

		JPanel editarPanel = new JPanel();
		FormLayout layout = new FormLayout("right:max(50dlu;pref), 6dlu, pref, 6dlu, pref",
				"pref, 6dlu, pref, 6dlu, pref, 6dlu, pref");
		CellConstraints cc = new CellConstraints();
		editarPanel.setLayout(layout);

		JLabel lblNome = new JLabel("Nome: ");
		JLabel lblEmail = new JLabel("Email: ");

		txtNome = new JTextField(usuario.getNome());
		txtEmail = new JTextField(usuario.getEmail());

		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(this::salvarEdicao);

		editarPanel.setVisible(true);
		editarPanel.add(lblNome, cc.xy(1, 1));
		editarPanel.add(txtNome, cc.xyw(3, 1, 3));
		editarPanel.add(lblEmail, cc.xy(1, 3));
		editarPanel.add(txtEmail, cc.xyw(3, 3, 3));
		editarPanel.add(btnSalvar, cc.xy(3, 7));

		return editarPanel;
	}

	private JPanel criarLivrosEmprestadosList() {
		JLabel label = new JLabel("AINDA NAO IMPLEMENTADO");
		add(label);
		return null;
	}

	private void atualizarInformacoesTela() {
		if (usuarioLogado != null) {
			String nome = usuarioLogado.getNome();
			String email = usuarioLogado.getEmail();

			txtNome.setText(nome);
			txtEmail.setText(email);

			((JLabel) infoPanel.getComponent(0)).setText("Nome: " + nome);
			((JLabel) infoPanel.getComponent(1)).setText("Email: " + email);

			JOptionPane.showMessageDialog(this, "Informações atualizadas com sucesso!");
		} else {
			JOptionPane.showMessageDialog(this, "Usuario não encontrado no banco de dados.");
		}
	}

	private void salvarEdicao(ActionEvent e) {
		EntityManager em = JPAUtil.getEntityManager();
		UsuarioDao usuarioDao = new UsuarioDao(em);

		String nome = usuarioLogado.getNome();
		String email = usuarioLogado.getEmail();

		String novoNome = txtNome.getText();
		String novoEmail = txtEmail.getText();

		try {
			if (novoNome != null && novoNome != nome) {
				usuarioLogado.setNome(novoNome);
				em.getTransaction().begin();
				usuarioDao.atualizar(usuarioLogado);
				em.getTransaction().commit();
				em.close();
				JOptionPane opitionPane = new JOptionPane("NOME ATUALIZADO COM SUCESSO!");

			}
			if (novoEmail != null && novoEmail != email) {
				usuarioLogado.setEmail(novoEmail);
				em.getTransaction().begin();
				usuarioDao.atualizar(usuarioLogado);
				em.getTransaction().commit();
				em.close();
				JOptionPane opitionPane = new JOptionPane("EMAIL ATUALIZADO COM SUCESSO!");

			} else {
				JOptionPane opitionPane = new JOptionPane("NOME E/OU EMAIL JA EXISTES!");
			}
		} catch (Exception e) {
			em.getTransaction().rollback();
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "ERRO AO REMOVER O LIVRO!");
		} finally {
			em.close();
		}
		return;
	}
}
