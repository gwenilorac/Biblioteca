package br.com.gwenilorac.biblioteca.app.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;

import javax.persistence.EntityManager;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import br.com.gwenilorac.biblioteca.dao.AutorDao;
import br.com.gwenilorac.biblioteca.dao.GeneroDao;
import br.com.gwenilorac.biblioteca.dao.LivroDao;
import br.com.gwenilorac.biblioteca.model.Autor;
import br.com.gwenilorac.biblioteca.model.Emprestimo;
import br.com.gwenilorac.biblioteca.model.Genero;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.servicos.ServicoLogin;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

@SuppressWarnings("serial")
public class DetalhesLivroInternalFrame extends JPanel {

	private Livro livro;
	private JTabbedPane tabbedPane;
	private JTextField txtTitulo;
	private JTextField txtAutor;
	private JTextField txtGenero;
	private JButton btnRemover;
	private JButton btnPegarEmprestado;
	private JButton btnAtualizar;
	private JPanel infoPanel;

	public DetalhesLivroInternalFrame(Livro livro) {
		this.livro = livro;

		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(400, 250));

		JPanel detalhesPanel = new JPanel(new BorderLayout());
		byte[] imagemIcon = livro.getCapa();
		ImageIcon icon = new ImageIcon(imagemIcon);
		Image img = icon.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH);
		ImageIcon newIcon = new ImageIcon(img);
		JLabel capaLabel = new JLabel(newIcon);
		detalhesPanel.add(capaLabel, BorderLayout.WEST);
		
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

		add(buttonPanel, BorderLayout.SOUTH);
		add(tabbedPane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
	private void atualizarInformacoes() {
	    EntityManager em = JPAUtil.getEntityManager();
	    
	    LivroDao livroDao = new LivroDao(em); 
	    Livro livroAtualizado = livroDao.buscarLivroPorTitulo(livro.getTitulo());

	    if (livroAtualizado != null) {
	        livro = livroAtualizado;

	        txtTitulo.setText(livro.getTitulo());
	        txtAutor.setText(livro.getAutor().getNome()); 
	        txtGenero.setText(livro.getGenero().getNome()); 
	        
	        ((JLabel)infoPanel.getComponent(0)).setText("Nome: " + livro.getTitulo());
	        ((JLabel)infoPanel.getComponent(1)).setText("Autor: " + livro.getAutor().getNome());
	        ((JLabel)infoPanel.getComponent(2)).setText("Gênero: " + livro.getGenero().getNome());

	        JOptionPane.showMessageDialog(this, "Informações atualizadas com sucesso!");
	    } else {
	        JOptionPane.showMessageDialog(this, "Livro não encontrado no banco de dados.");
	    }
	    em.close(); 
	}

	private JPanel criarInfoPanel() {
	    infoPanel = new JPanel();
	    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
	    infoPanel.add(new JLabel("Nome: " + livro.getTitulo()));
	    infoPanel.add(new JLabel("Autor: " + livro.getAutor().getNome()));
	    infoPanel.add(new JLabel("Gênero: " + livro.getGenero().getNome()));
	    return infoPanel;
	}

	private JPanel criarEditarPanel() {
		JPanel editarPanel = new JPanel();
		FormLayout layout = new FormLayout("right:max(50dlu;pref), 6dlu, pref, 6dlu, pref",
				"pref, 6dlu, pref, 6dlu, pref, 6dlu, pref");
		CellConstraints cc = new CellConstraints();
		editarPanel.setLayout(layout);

		JLabel lblTitulo = new JLabel("Título:");
		JLabel lblAutor = new JLabel("Autor:");
		JLabel lblGenero = new JLabel("Gênero:");

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
		EntityManager em = JPAUtil.getEntityManager();
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
		Usuario usuario = ServicoLogin.getUsuarioLogado();
		Emprestimo emprestimo = new Emprestimo(livro, usuario);
		boolean pegarLivroEmprestado = emprestimo.pegarLivroEmprestado();
		if (pegarLivroEmprestado == true) {
			System.out.println("Livro emprestado: " + livro.getTitulo() + " por: " + usuario.getNome());
			JOptionPane.showMessageDialog(this, "LIVRO EMPRESTADO COM SUCESSO!");
		} else {
			JOptionPane.showMessageDialog(this, "LIVRO NÃO DISPONÍVEL PARA EMPRÉSTIMO!");
		}
	}

	private void removerLivro() {
	    int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja remover o livro?", "Confirmação", JOptionPane.YES_NO_OPTION);
	    
	    if (confirmacao == JOptionPane.YES_OPTION) {
	        EntityManager em = JPAUtil.getEntityManager();
	        LivroDao livroDao = new LivroDao(em);

	        Livro livroGerenciado = em.merge(livro);
	        em.getTransaction().begin();
	        try {
	            livroDao.remover(livroGerenciado);
	            em.getTransaction().commit();
	            System.out.println("Livro removido: " + livro.getTitulo());
	            JOptionPane.showMessageDialog(this, "LIVRO REMOVIDO COM SUCESSO!");
	            this.getParent().remove(this); 
	        } catch (Exception e) {
	            em.getTransaction().rollback();
	            e.printStackTrace(); 
	            JOptionPane.showMessageDialog(this, "ERRO AO REMOVER O LIVRO!");
	        } finally {
	            em.close();
	        }
	    }
	}

}
