package br.com.gwenilorac.biblioteca.app.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.persistence.EntityManager;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import br.com.gwenilorac.biblioteca.dao.AutorDao;
import br.com.gwenilorac.biblioteca.dao.GeneroDao;
import br.com.gwenilorac.biblioteca.dao.LivroDao;
import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.model.Autor;
import br.com.gwenilorac.biblioteca.model.Emprestimo;
import br.com.gwenilorac.biblioteca.model.Estado;
import br.com.gwenilorac.biblioteca.model.Genero;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.servicos.ServicoLivro;
import br.com.gwenilorac.biblioteca.servicos.ServicoLogin;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

@SuppressWarnings("serial")
public class DetalhesLivroInternalFrame extends JPanel {

	private EntityManager em = JPAUtil.getEntityManager();
	private Usuario usuario = ServicoLogin.getUsuarioLogado();
	private Livro livro;
	private JTabbedPane tabbedPane;
	private JTextField txtTitulo;
	private JTextField txtAutor;
	private JTextField txtGenero;
	private JButton btnRemover;
	private JButton btnPegarEmprestado;
	private JButton btnDevolverLivro;
	private JButton btnAtualizar;
	private JPanel infoPanel;

	public DetalhesLivroInternalFrame(Livro livro) {
		this.livro = livro;

		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(520, 250));

		JPanel detalhesPanel = new JPanel(new BorderLayout());
		
		byte[] imagemIcon = livro.getCapa();
		ImageIcon icon = new ImageIcon(imagemIcon);
		Image img = icon.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH);
		ImageIcon newIcon = new ImageIcon(img);
		JLabel capaLabel = new JLabel(newIcon);
		detalhesPanel.add(capaLabel, BorderLayout.LINE_START);
		
		JSeparator separator = new JSeparator(JSeparator.VERTICAL);
		detalhesPanel.add(separator);

		JPanel infoPanel = criarInfoPanel();
		detalhesPanel.add(infoPanel, BorderLayout.CENTER);

		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Detalhes", detalhesPanel);

		JPanel editarPanel = criarEditarPanel();
		tabbedPane.addTab("Editar", editarPanel);

		JPanel buttonPanel = new JPanel(new FlowLayout());

		btnAtualizar = new JButton("Atualizar");
		btnAtualizar.addActionListener(e -> atualizarInformacoes());
		buttonPanel.add(btnAtualizar);

		btnRemover = new JButton("Remover");
		btnRemover.addActionListener(e -> removerLivro());
		buttonPanel.add(btnRemover);

		btnPegarEmprestado = new JButton("Pegar Emprestado");
		btnPegarEmprestado.addActionListener(e -> pegarLivroEmprestado());
		buttonPanel.add(btnPegarEmprestado);
		
		btnDevolverLivro = new JButton("Devolver Livro");
		btnDevolverLivro.addActionListener(dl -> devolverLivro());
		buttonPanel.add(btnDevolverLivro);

		add(tabbedPane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	private void atualizarInformacoes() {
			txtTitulo.setText(livro.getTitulo());
			txtAutor.setText(livro.getAutor().getNome());
			txtGenero.setText(livro.getGenero().getNome());

			((JLabel) infoPanel.getComponent(0)).setText("Nome: " + livro.getTitulo());
			((JLabel) infoPanel.getComponent(1)).setText("Autor: " + livro.getAutor().getNome());
			((JLabel) infoPanel.getComponent(2)).setText("Gênero: " + livro.getGenero().getNome());
			((JLabel) infoPanel.getComponent(3)).setText("Estado: " + livro.getEstado());

			JOptionPane.showMessageDialog(this, "INFORMAÇÕES ATUALIZADAS COM SUCESSO!");
	}

	private JPanel criarInfoPanel() {
		infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
		infoPanel.add(new JLabel("Nome: " + livro.getTitulo()));
		infoPanel.add(new JLabel("Autor: " + livro.getAutor().getNome()));
		infoPanel.add(new JLabel("Gênero: " + livro.getGenero().getNome()));
		infoPanel.add(new JLabel("Estado: " + livro.getEstado()));
		return infoPanel;
	}

	private JPanel criarEditarPanel() {
		JPanel editarPanel = new JPanel();
		FormLayout layout = new FormLayout("right:max(50dlu;pref), 6dlu, pref, 6dlu, pref",
				"pref, 6dlu, pref, 6dlu, pref, 6dlu, pref");
		CellConstraints cc = new CellConstraints();
		editarPanel.setLayout(layout);

		JLabel lblTitulo = new JLabel("Título: ");
		JLabel lblAutor = new JLabel("Autor: ");
		JLabel lblGenero = new JLabel("Gênero: ");

		txtTitulo = new JTextField(livro.getTitulo());
		txtAutor = new JTextField(livro.getAutor().toString());
		txtGenero = new JTextField(livro.getGenero().toString());

		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(this::salvarEdicao);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(e -> tabbedPane.setSelectedIndex(0));

		editarPanel.add(lblTitulo, cc.xy(1, 1));
		editarPanel.add(txtTitulo, cc.xyw(3, 1, 3));
		editarPanel.add(lblAutor, cc.xy(1, 3));
		editarPanel.add(txtAutor, cc.xyw(3, 3, 3));
		editarPanel.add(lblGenero, cc.xy(1, 5));
		editarPanel.add(txtGenero, cc.xyw(3, 5, 3));
		editarPanel.add(btnSalvar, cc.xy(3, 7));
		editarPanel.add(btnCancelar, cc.xy(5, 7));

		return editarPanel;
	}

	private void salvarEdicao(ActionEvent e) {
		GeneroDao generoDao = new GeneroDao(em);
		AutorDao autorDao = new AutorDao(em);
		LivroDao livroDao = new LivroDao(em);

		String novoTitulo = txtTitulo.getText();
		String novoAutor = txtAutor.getText();
		String novoGenero = txtGenero.getText();

		livro.setTitulo(novoTitulo);
		livro.getAutor().setNome(novoAutor);
		livro.getGenero().setNome(novoGenero);

		em.getTransaction().begin();
		generoDao.atualizar(livro.getGenero());
		autorDao.atualizar(livro.getAutor());
		livroDao.atualizar(livro);
		em.getTransaction().commit();
		em.close();

		tabbedPane.setSelectedIndex(0);
	}

	private void pegarLivroEmprestado() {
		LivroDao livroDao = new LivroDao(em);
//		Livro livroGerenciado = em.merge(livro);
		em.getTransaction().begin();
		if (livro.getEstado() == Estado.DISPONIVEL) {
			Emprestimo emprestimo = new Emprestimo(livro, usuario);
			emprestimo.pegarLivroEmprestado();
			livroDao.atualizar(livro);
			em.getTransaction().commit();
			System.out.println("Livro emprestado: " + livro.getTitulo() + " por: " + usuario.getNome());
			JOptionPane.showMessageDialog(this, "LIVRO EMPRESTADO COM SUCESSO!");
		} else {
			JOptionPane.showMessageDialog(this, "LIVRO NÃO ESTÁ DISPONÍVEL PARA EMPRÉSTIMO!");
		} 
		em.close();
	}
	
	private void devolverLivro() {
		EntityManager em = JPAUtil.getEntityManager();
		LivroDao livroDao = new LivroDao(em);
		Livro livroGerenciado = em.merge(livro);
		em.getTransaction().begin();
		if (livro.getEstado() == Estado.INDISPONIVEL) {
			Emprestimo emprestimo = new Emprestimo(livro, usuario);
			emprestimo.devolverLivro();
			livroDao.atualizar(livroGerenciado);
			em.getTransaction().commit();
			System.out.println("Livro devolvido: " + livro.getTitulo() + " por: " + usuario.getNome());
			JOptionPane.showMessageDialog(this, "LIVRO DEVOLVIDO COM SUCESSO!");
		} else {
			JOptionPane.showMessageDialog(this, "LIVRO NÃO ESTÁ EMPRESTADO OU JA FOI DEVOLVIDO!");
		} 
		em.close();
	}

	private void removerLivro() {
		int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja remover o livro?", "Confirmação",
				JOptionPane.YES_NO_OPTION);

		if (confirmacao == JOptionPane.YES_OPTION) {
			if (ServicoLivro.removerLivro(livro) == true) {
				System.out.println("Livro removido: " + livro.getTitulo());
				JOptionPane.showMessageDialog(this, "LIVRO REMOVIDO COM SUCESSO!");
			} else {
				em.getTransaction().rollback();
				JOptionPane.showMessageDialog(this, "ERRO AO REMOVER O LIVRO!"
						+ "\nPOR FAVOR DEVOLVER LIVRO ANTES DE REMOVER");
			}
			em.close();
		} 

	}
}
