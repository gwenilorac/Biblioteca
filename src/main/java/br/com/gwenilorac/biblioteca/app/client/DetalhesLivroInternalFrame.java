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

import com.jgoodies.binding.PresentationModel;
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
import br.com.gwenilorac.biblioteca.servicos.ServicoEmprestimo;
import br.com.gwenilorac.biblioteca.servicos.ServicoLivro;
import br.com.gwenilorac.biblioteca.servicos.ServicoLogin;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

@SuppressWarnings("serial")
public class DetalhesLivroInternalFrame extends JPanel {

	private Usuario usuario = ServicoLogin.getUsuarioLogado();
	private Livro livro;
	private Emprestimo emprestimo;
	private JPanel detalhesPanel;
	private JTabbedPane tabbedPane;
	private JTextField txtTitulo;
	private JTextField txtAutor;
	private JTextField txtGenero;
	private JButton btnRemover;
	private JButton btnPegarEmprestado;
	private JButton btnDevolverLivro;
	private JButton btnAtualizar;
	private JPanel capaPanel;
	private JPanel infoPanel;
	private JPanel editarPanel;
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
		setPreferredSize(new Dimension(520, 250));

		detalhesPanel = new JPanel(new BorderLayout());

		capaPanel = criarCapaPanel();
		detalhesPanel.add(capaPanel, BorderLayout.LINE_START);

		separator = new JSeparator(JSeparator.VERTICAL);
		detalhesPanel.add(separator);

		infoPanel = criarInfoPanel();
		detalhesPanel.add(infoPanel, BorderLayout.CENTER);

		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Detalhes", detalhesPanel);

		editarPanel = criarEditarPanel();
		tabbedPane.addTab("Editar", editarPanel);

		buttonPanel = new JPanel(new FlowLayout());

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
		removeAll();
		initLayout();
		revalidate();
		repaint();

		JOptionPane.showMessageDialog(this, "INFORMAÇÕES E CAPA ATUALIZADAS COM SUCESSO!");
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

	private JPanel criarEditarPanel() {
		JPanel editarPanel = new JPanel();
		FormLayout layout = new FormLayout("right:max(50dlu;pref), 6dlu, pref, 6dlu, pref",
				"pref, 6dlu, pref, 6dlu, pref, 6dlu, pref, 6dlu, pref");
		CellConstraints cc = new CellConstraints();
		editarPanel.setLayout(layout);

		JLabel lblTitulo = new JLabel("Título: ");
		JLabel lblAutor = new JLabel("Autor: ");
		JLabel lblGenero = new JLabel("Gênero: ");
		JLabel lblCapa = new JLabel("Capa: ");

		txtTitulo = new JTextField(livro.getTitulo());
		txtAutor = new JTextField(livro.getAutor().toString());
		txtGenero = new JTextField(livro.getGenero().toString());

		btnEditarCapa = new JButton("Editar Capa");
		btnEditarCapa.addActionListener(e -> editarCapa());

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
		editarPanel.add(lblCapa, cc.xy(1, 7));
		editarPanel.add(btnEditarCapa, cc.xyw(3, 7, 3));
		editarPanel.add(btnSalvar, cc.xy(3, 9));
		editarPanel.add(btnCancelar, cc.xy(5, 9));

		return editarPanel;
	}

	private ImageIcon editarCapa() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png");
		chooser.setFileFilter(filter);
		int result = chooser.showOpenDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			selectedCoverFile = chooser.getSelectedFile();
			try {
				BufferedImage originalImage = ImageIO.read(selectedCoverFile);
				Image resizedImage = originalImage.getScaledInstance(150, 200, Image.SCALE_SMOOTH);

				BufferedImage bufferedResizedImage = new BufferedImage(150, 200, BufferedImage.TYPE_INT_RGB);
				bufferedResizedImage.getGraphics().drawImage(resizedImage, 0, 0, null);

				ImageIcon novaCapaIcon = new ImageIcon(bufferedResizedImage);
				return novaCapaIcon;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private void salvarEdicao(ActionEvent e) {
		EntityManager em = JPAUtil.getEntityManager();
		GeneroDao generoDao = new GeneroDao(em);
		AutorDao autorDao = new AutorDao(em);
		LivroDao livroDao = new LivroDao(em);

		String novoTitulo = txtTitulo.getText();
		String novoAutor = txtAutor.getText();
		String novoGenero = txtGenero.getText();

		if (selectedCoverFile != null) {
			try {
				BufferedImage originalImage = ImageIO.read(selectedCoverFile);
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				ImageIO.write(originalImage, "png", outputStream);
				byte[] coverImageBytes = outputStream.toByteArray();
				outputStream.close();

				livro.setCapa(coverImageBytes);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

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
		emprestimo = EmprestimoDoLivro();
		ServicoEmprestimo servicoEmprestimo = new ServicoEmprestimo();
		boolean pegarLivroEmprestado = servicoEmprestimo.pegarLivroEmprestado(emprestimo);
		if (pegarLivroEmprestado) {
			JOptionPane.showMessageDialog(this, "LIVRO EMPRESTADO COM SUCESSO!");
		} else {
			JOptionPane.showMessageDialog(this, "O LIVRO NÃO ESTÁ DISPONÍVEL PARA EMPRÉSTIMO.|"
					+ "\nOU SEU LIMITE DE EMPRESTIMOS FOI ATINGIDO");
		}
	}

	private void devolverLivro() {
		ServicoEmprestimo servicoEmprestimo = new ServicoEmprestimo();
		boolean devolverLivro = servicoEmprestimo.devolverLivro(emprestimo);
		if (devolverLivro) {
			JOptionPane.showMessageDialog(this, "LIVRO DEVOLVIDO COM SUCESSO!");
		} else {
			JOptionPane.showMessageDialog(this, "LIVRO NÃO ESTÁ EMPRESTADO OU JA FOI DEVOLVIDO!");
		}
	}

	private void removerLivro() {
		int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja remover o livro?", "Confirmação",
				JOptionPane.YES_NO_OPTION);

		if (confirmacao == JOptionPane.YES_OPTION) {
			if (ServicoLivro.removerLivro(livro) == true) {
				System.out.println("Livro removido: " + livro.getTitulo());
				JOptionPane.showMessageDialog(this, "LIVRO REMOVIDO COM SUCESSO!");
			} else {
				JOptionPane.showMessageDialog(this,
						"ERRO AO REMOVER O LIVRO!" + "\nPOR FAVOR DEVOLVER LIVRO ANTES DE REMOVER");
			}
		}
	}
	
	private Emprestimo EmprestimoDoLivro() {
		EntityManager em = JPAUtil.getEntityManager();
		EmprestimoDao emprestimoDao = new EmprestimoDao(em);
		Emprestimo emprestimoDoLivro = emprestimoDao.buscarSeLivroJaTemEmprestimo(livro); 
		
		if(emprestimoDoLivro == null) {
			em.getTransaction().begin();
			Emprestimo emprestimo = new Emprestimo(livro, usuario);
			emprestimoDao.cadastrar(emprestimo);
			em.getTransaction().commit();
			em.close();
			return emprestimo;
		} else {
			return emprestimoDoLivro;
		}
	}
}
