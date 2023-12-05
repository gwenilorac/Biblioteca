package br.com.gwenilorac.biblioteca.app.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import br.com.gwenilorac.biblioteca.model.Livro;

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
	private JPanel panelDadosLivros;

	public DetalhesLivroInternalFrame(Livro livro) {
		this.livro = livro;
		initComponents();
		initLayout();
	}

	private void initComponents() {
		panelDadosLivros = new JPanel(new FlowLayout());
		capaPanel = new JPanel();
		infoPanel = new JPanel();

	}

	public void initLayout() {

		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(500, 250));
		
		capaPanel.add(criarCapaPanel());
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

	    infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

	    infoPanel.add(new JLabel("Nome: " + livro.getTitulo()));
	    infoPanel.add(new JLabel("Autor: " + livro.getAutor().getNome()));
	    infoPanel.add(new JLabel("Gênero: " + livro.getGenero().getNome()));

	    return infoPanel;
	}

}
