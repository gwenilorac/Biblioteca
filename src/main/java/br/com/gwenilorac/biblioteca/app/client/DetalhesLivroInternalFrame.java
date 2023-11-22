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

	private Livro livro;
	private JPanel capaPanel;
	private JPanel infoPanel;
	private byte[] imagemIcon;
	private ImageIcon icon;
	private Image img;
	private ImageIcon newIcon;
	private JLabel capaLabel;

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
		setPreferredSize(new Dimension(500, 250));
		
		JPanel panelDadosLivros = new JPanel(new FlowLayout());

		JPanel capaPanel = new JPanel();
		capaPanel.add(criarCapaPanel());
		
		JPanel infoPanel = new JPanel();
		infoPanel.add(criarInfoPanel());

		panelDadosLivros.add(capaPanel);
		panelDadosLivros.add(new JSeparator(JSeparator.VERTICAL));
		panelDadosLivros.add(infoPanel);

		add(panelDadosLivros, BorderLayout.CENTER);
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
}
