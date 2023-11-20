package br.com.gwenilorac.biblioteca.app.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.hibernate.event.spi.RefreshEvent;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import br.com.gwenilorac.biblioteca.dao.AutorDao;
import br.com.gwenilorac.biblioteca.dao.EmprestimoDao;
import br.com.gwenilorac.biblioteca.dao.GeneroDao;
import br.com.gwenilorac.biblioteca.dao.LivroDao;
import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.model.Emprestimo;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.StatusEmprestimo;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.servicos.ServicoLivro;
import br.com.gwenilorac.biblioteca.servicos.ServicoLogin;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

@SuppressWarnings("serial")
public class DetalhesLivroInternalFrame extends JPanel {

	private Usuario usuario = ServicoLogin.getUsuarioLogado();
	private Livro livro;
	private JPanel detalhesPanel;
	private JTabbedPane tabbedPane;
	private JTextField txtTitulo;
	private JTextField txtAutor;
	private JTextField txtGenero;
	private JButton btnPegarEmprestado;
	private JButton btnDevolverLivro;
	private JButton btnAtualizar;
	private JPanel capaPanel;
	private JPanel infoPanel;
	private JPanel buttonPanel;
	private JButton btnEditarCapa;
	private File selectedCoverFile;
	private byte[] imagemIcon;
	private ImageIcon icon;
	private Image img;
	private ImageIcon newIcon;
	private JLabel capaLabel;
	private JSeparator separator;

	public DetalhesLivroInternalFrame(Livro livro) {
		this.livro = livro;
		initModel();
		initComponents();
		initLayout();
	}

	private void initModel() {
	}

	private void initComponents() {
	}

	public void initLayout() {

		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(400, 250));
		
		JPanel panelDadosLivros = new JPanel(new FlowLayout());

		JPanel capaPanel = new JPanel();
		capaPanel.add(criarCapaPanel());
		
		JPanel infoPanel = new JPanel();
		infoPanel.add(criarInfoPanel());

		panelDadosLivros.add(capaPanel);
		panelDadosLivros.add(new JSeparator(JSeparator.VERTICAL));
		panelDadosLivros.add(infoPanel);

		buttonPanel = new JPanel(new FlowLayout());

		btnPegarEmprestado = new JButton("Pegar Emprestado");
		btnPegarEmprestado.addActionListener(e -> pegarLivroEmprestado());
		buttonPanel.add(btnPegarEmprestado);

		btnDevolverLivro = new JButton("Devolver Livro");
		btnDevolverLivro.addActionListener(dl -> devolverLivro());
		buttonPanel.add(btnDevolverLivro);

		add(panelDadosLivros, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	private JPanel criarCapaPanel() {
		capaPanel = new JPanel();

		imagemIcon = livro.getCapa();
		icon = new ImageIcon(imagemIcon);
		img = icon.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH);
		newIcon = new ImageIcon(img);
		capaLabel = new JLabel(newIcon);
		capaPanel.add(capaLabel);

		return capaPanel;
	}

	private JPanel criarInfoPanel() {
		infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
		infoPanel.add(new JLabel("Nome: " + livro.getTitulo()));
		infoPanel.add(new JLabel("Autor: " + livro.getAutor().getNome()));
		infoPanel.add(new JLabel("Gênero: " + livro.getGenero().getNome()));

		return infoPanel;
	}


	private void pegarLivroEmprestado() {
		EntityManager em = JPAUtil.getEntityManager();
		EmprestimoDao emprestimoDao = new EmprestimoDao(em);
		UsuarioDao usuarioDao = new UsuarioDao(em);
		Emprestimo emprestimoDoLivro = emprestimoDao.buscarSeLivroJaTemEmprestimo(livro);
		List<Livro> livrosEmprestados = usuarioDao.buscarLivrosEmprestados(ServicoLogin.getUsuarioLogado().getId());

		if (livrosEmprestados.size() <= 3) {
			if (emprestimoDoLivro == null) {
				em.getTransaction().begin();
				Emprestimo Novoemprestimo = new Emprestimo(livro, usuario);
				emprestimoDao.cadastrar(Novoemprestimo);
				boolean pegarLivroEmprestado = Novoemprestimo.pegarLivroEmprestado();
				if (pegarLivroEmprestado) {
					emprestimoDao.atualizar(Novoemprestimo);
					JOptionPane.showMessageDialog(this, "LIVRO EMPRESTADO COM SUCESSO!");
				} else {
					JOptionPane.showMessageDialog(this, "O LIVRO NÃO ESTÁ DISPONÍVEL PARA EMPRÉSTIMO.|"
							+ "\nOU SEU LIMITE DE EMPRESTIMOS FOI ATINGIDO");
				}
				em.getTransaction().commit();
				return;
			} else {
				boolean pegarLivroEmprestado = emprestimoDoLivro.pegarLivroEmprestado();
				if (pegarLivroEmprestado) {
					emprestimoDao.atualizar(emprestimoDoLivro);
					JOptionPane.showMessageDialog(this, "LIVRO EMPRESTADO COM SUCESSO!");
				} else {
					JOptionPane.showMessageDialog(this, "O LIVRO NÃO ESTÁ DISPONÍVEL PARA EMPRÉSTIMO.|"
							+ "\nOU SEU LIMITE DE EMPRESTIMOS FOI ATINGIDO");
				}
			}
			return;
		}
	}

	private void devolverLivro() {
		EntityManager em = JPAUtil.getEntityManager();
		EmprestimoDao emprestimoDao = new EmprestimoDao(em);
		Emprestimo emprestimoDoLivro = emprestimoDao.buscarSeLivroJaTemEmprestimo(livro);

		if (emprestimoDoLivro != null) {
			em.getTransaction().begin();
			boolean devolverLivro = emprestimoDoLivro.devolverLivro();
			if (devolverLivro) {
				emprestimoDao.atualizar(emprestimoDoLivro);
				em.getTransaction().commit();
				JOptionPane.showMessageDialog(this, "LIVRO DEVOLVIDO COM SUCESSO!");
			} else {
				JOptionPane.showMessageDialog(this, "LIVRO NÃO ESTÁ EMPRESTADO OU JA FOI DEVOLVIDO!");
			}
		} else {
			JOptionPane.showMessageDialog(this, "NÃO EXISTE EMPRESTIMO COM ESSE LIVRO!");
		}
	}

}
