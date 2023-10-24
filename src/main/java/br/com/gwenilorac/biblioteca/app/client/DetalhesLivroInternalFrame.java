package br.com.gwenilorac.biblioteca.app.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.persistence.EntityManager;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import br.com.gwenilorac.biblioteca.dao.LivroDao;
import br.com.gwenilorac.biblioteca.model.Emprestimo;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.servicos.ServicoLogin;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

@SuppressWarnings("serial")
public class DetalhesLivroInternalFrame extends JPanel {

	private Livro livro;

	private JButton btnPegarEmprestado;
	private JButton btnRemover;

	public DetalhesLivroInternalFrame(Livro livro) {
		this.livro = livro;
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(400, 250));
		Dimension d = getSize();
		setLocation((d.width - getSize().width) / 2, (d.height - getSize().height) / 2);
		
		byte[] imagemIcon = livro.getCapa();
		ImageIcon icon = new ImageIcon(imagemIcon);
		Image img = icon.getImage().getScaledInstance(150, 200, java.awt.Image.SCALE_SMOOTH);
		ImageIcon newIcon = new ImageIcon(img);
		JLabel capaLabel = new JLabel(newIcon);
		add(capaLabel, BorderLayout.WEST);

		JPanel detalhesPanel = new JPanel();
		detalhesPanel.setPreferredSize(new Dimension(400, 300));
		detalhesPanel.setLayout(new BoxLayout(detalhesPanel, BoxLayout.Y_AXIS));
		detalhesPanel.add(new JLabel("Nome:" + livro.getTitulo()));
		detalhesPanel.add(new JLabel("Autor:" + livro.getAutor()));
		detalhesPanel.add(new JLabel("Gênero:" + livro.getGenero()));
		add(detalhesPanel, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		btnPegarEmprestado = new JButton("Pegar Emprestado");
		btnPegarEmprestado.addActionListener(pe -> pegarLivroEmprestado());
		panel.add(btnPegarEmprestado);
		btnRemover = new JButton("Remover");
		btnRemover.addActionListener(rm -> removerLivro());
		panel.add(btnRemover);
		add(panel, BorderLayout.PAGE_END);
		
	}
	
	private void pegarLivroEmprestado() {
		Usuario usuario = ServicoLogin.getUsuarioLogado();
		Emprestimo emprestimo = new Emprestimo(livro, usuario);
		boolean pegarLivroEmprestado = emprestimo.pegarLivroEmprestado();
		if (pegarLivroEmprestado == true) {
			System.out.println("Livro emprestado: " + livro.getTitulo() + " por: " + usuario.getNome());
			JOptionPane.showMessageDialog(this, "LIVRO EMPRESTADO COM SUCESSO!");
		} else {
			JOptionPane.showMessageDialog(this, "LIVRO NÃO DISPONIVEL PARA EMPRESTIMO!");
		}
	}

	private void removerLivro() {
		EntityManager em = JPAUtil.getEntityManager();
		LivroDao livroDao = new LivroDao(em);
		boolean remover = livroDao.remover(livro);

		if (remover == true) {
			System.out.println("Livro removido: " + livro.getTitulo());
			JOptionPane.showMessageDialog(this, "LIVRO REMOVIDO COM SUCESSO!");
		} else {
			JOptionPane.showMessageDialog(this, "LIVRO INDISPONIVEL PARA REMOÇÃO!");
		}
	}
}



























