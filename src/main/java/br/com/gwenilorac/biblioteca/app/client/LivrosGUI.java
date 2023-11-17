package br.com.gwenilorac.biblioteca.app.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

import br.com.gwenilorac.biblioteca.dao.AutorDao;
import br.com.gwenilorac.biblioteca.dao.GeneroDao;
import br.com.gwenilorac.biblioteca.dao.LivroDao;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.servicos.ServicoLivro;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

@SuppressWarnings("serial")
public class LivrosGUI extends JFrame {

	private EntityManager em = JPAUtil.getEntityManager();
	private PresentationModel<Livro> model;
	private LivroDao livroDao;
	private JTextField textFieldPesquisa;
	private JTable tableLivros;
	private JLabel lblCapa;
	private JTextField textFieldNome;
	private JTextField textFieldAutor;
	private JTextField textFieldGenero;
	private JLabel lblDisponibilidade;
	private byte[] imagemIcon;
	private ImageIcon icon;
	private Image img;
	private ImageIcon newIcon;
	private JLabel capaLabel;
	private Livro livroSelecionado;
	private File selectedCoverFile;
	private Container contentPane;
	private JDialog dialog;
	private boolean isAddBookDialogOpen = false;

	public LivrosGUI() {
		initModel();
		initComponents();
		initLayout();
	}

	private void initModel() {
		Livro livro = new Livro();
		model = new PresentationModel<Livro>(livro);
	}

	private void initComponents() {

	}

	public void initLayout() {
		contentPane = getContentPane();
		contentPane.setLayout(new GridLayout(3, 1));
		contentPane.add(criarPanelPesquisarLivros());
		contentPane.add(criarPanelDadosLivro());
		contentPane.add(criarPanelBotoes());

		setPreferredSize(new Dimension(900, 900));
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		setExtendedState(MAXIMIZED_BOTH);

	}

	private JPanel criarPanelPesquisarLivros() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Pesquisar Livros"));

		JPanel buscaPanel = new JPanel();
		buscaPanel.setLayout(new FlowLayout());

		textFieldPesquisa = new JTextField(20);
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				livroDao = new LivroDao(em);
				java.util.List<Livro> livrosEncontrados = livroDao.buscarLivros(textFieldPesquisa.getText());

				DefaultTableModel tableModel = (DefaultTableModel) tableLivros.getModel();
				tableModel.setRowCount(0);

				for (Livro livro : livrosEncontrados) {
					Object[] rowData = { livro.getTitulo(), livro.getAutor(), livro.getGenero(), livro.getEstado() };
					tableModel.addRow(rowData);
				}

				tableLivros.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						if (!e.getValueIsAdjusting()) {
							int selectedRow = tableLivros.getSelectedRow();
							if (selectedRow != -1) {
								livroSelecionado = livrosEncontrados.get(selectedRow);

								imagemIcon = livroSelecionado.getCapa();
								icon = new ImageIcon(imagemIcon);
								img = icon.getImage().getScaledInstance(150, 190, Image.SCALE_SMOOTH);
								newIcon = new ImageIcon(img);
								capaLabel = new JLabel(newIcon);
								lblCapa.setIcon(newIcon);
								textFieldNome.setText(livroSelecionado.getTitulo());
								textFieldAutor.setText(livroSelecionado.getAutor().toString());
								textFieldGenero.setText(livroSelecionado.getGenero().toString());
								lblDisponibilidade.setText(livroSelecionado.getEstado().toString());
							}
						}
					}
				});
			}
		});

		buscaPanel.add(new JLabel("Pesquisar Livros:"));
		buscaPanel.add(textFieldPesquisa);
		buscaPanel.add(btnBuscar);

		String[] colunas = { "Nome", "Autor", "Gênero", "Disponibilidade" };
		DefaultTableModel tableModel = new DefaultTableModel(colunas, 0);
		tableLivros = new JTable(tableModel) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JScrollPane scrollPane = new JScrollPane(tableLivros);
		scrollPane.setPreferredSize(new Dimension(400, 150));
		panel.add(buscaPanel, BorderLayout.PAGE_START);
		panel.add(scrollPane, BorderLayout.CENTER);

		return panel;
	}

	private JPanel criarPanelDadosLivro() {
		JPanel panelDadosLivros = new JPanel(new FlowLayout());
		panelDadosLivros.setBorder(BorderFactory.createTitledBorder("Detalhes do Livro"));

		JPanel capaPanel = new JPanel();
		lblCapa = new JLabel();
		capaPanel.add(lblCapa);

		FormLayout layout = new FormLayout("pref, 5px, 70dlu");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		textFieldNome = new JTextField();
		textFieldAutor = new JTextField();
		textFieldGenero = new JTextField();
		lblDisponibilidade = new JLabel();

		builder.append("Nome: ", textFieldNome);
		builder.nextLine();
		builder.append("Autor: ", textFieldAutor);
		builder.nextLine();
		builder.append("Gênero: ", textFieldGenero);
		builder.nextLine();
		builder.append("Disponibilidade: ", lblDisponibilidade);

		JPanel formPanel = builder.getPanel();

		panelDadosLivros.add(capaPanel);
		panelDadosLivros.add(new JSeparator(JSeparator.VERTICAL));
		panelDadosLivros.add(formPanel);

		return panelDadosLivros;
	}

	private JPanel criarPanelBotoes() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());

		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			    if (livroSelecionado != null) {
			        int confirmacao = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover o livro?",
			                "Confirmação", JOptionPane.YES_NO_OPTION);

			        if (confirmacao == JOptionPane.YES_OPTION) {
			            livroDao = new LivroDao(em);
			            
			            Livro livroParaRemover = em.merge(livroSelecionado);

			            boolean removerLivro = ServicoLivro.removerLivro(livroParaRemover);
			            
			            if (removerLivro) {
			                System.out.println("Livro removido: " + livroSelecionado.getTitulo());
			                JOptionPane.showMessageDialog(null, "LIVRO REMOVIDO COM SUCESSO!");
			            } else {
			                JOptionPane.showMessageDialog(null,
			                        "ERRO AO REMOVER O LIVRO!" + "\nPOR FAVOR DEVOLVER LIVRO ANTES DE REMOVER");
			            }
			        }
			    } else {
			        JOptionPane.showMessageDialog(null, "SELECIONE UM LIVRO!");
			    }
			}

		});

		JButton btnAdicionar = new JButton("Adicionar Novo Livro");
        btnAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isAddBookDialogOpen) {
                    isAddBookDialogOpen = true;
                    AdicionarLivroFrm addBookForm = new AdicionarLivroFrm();
                    dialog = new JDialog();
                    dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                    dialog.add(addBookForm);
                    dialog.setPreferredSize(new Dimension(400, 200));
                    dialog.pack();
                    dialog.setVisible(true);

                    dialog.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            isAddBookDialogOpen = false;
                        }
                    });

                    Dimension desktopSize = contentPane.getSize();
                    Dimension jInternalFrameSize = dialog.getSize();
                    dialog.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                            (desktopSize.height - jInternalFrameSize.height) / 2);
                } else {
                    JOptionPane.showMessageDialog(null, "A janela de adicionar livro já está aberta.");
                }
            }
        });

		JButton btnEditarCapa = new JButton("Editar Capa");
		btnEditarCapa.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (livroSelecionado != null) {
					JFileChooser chooser = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png");
					chooser.setFileFilter(filter);
					int result = chooser.showOpenDialog(null);

					if (result == JFileChooser.APPROVE_OPTION) {
						selectedCoverFile = chooser.getSelectedFile();
						try {
							BufferedImage originalImage = ImageIO.read(selectedCoverFile);
							Image resizedImage = originalImage.getScaledInstance(150, 200, Image.SCALE_SMOOTH);

							BufferedImage bufferedResizedImage = new BufferedImage(150, 200,
									BufferedImage.TYPE_INT_RGB);
							bufferedResizedImage.getGraphics().drawImage(resizedImage, 0, 0, null);

							ImageIcon novaCapaIcon = new ImageIcon(bufferedResizedImage);

							JOptionPane.showMessageDialog(null, "CAPA EDITADA COM SUCESSO!");
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "SELECIONE UM LIVRO!");
				}
			}
		});

		JButton btnAlterar = new JButton("Salvar Alterações");
		btnAlterar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (livroSelecionado != null) {
					EntityManager em = JPAUtil.getEntityManager();
					GeneroDao generoDao = new GeneroDao(em);
					AutorDao autorDao = new AutorDao(em);
					LivroDao livroDao = new LivroDao(em);

					String novoTitulo = textFieldNome.getText();
					String novoAutor = textFieldAutor.getText();
					String novoGenero = textFieldGenero.getText();

					if (selectedCoverFile != null) {
						try {
							BufferedImage originalImage = ImageIO.read(selectedCoverFile);
							ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
							ImageIO.write(originalImage, "png", outputStream);
							byte[] coverImageBytes = outputStream.toByteArray();
							outputStream.close();
							livroSelecionado.setCapa(coverImageBytes);

						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}

					livroSelecionado.setTitulo(novoTitulo);
					livroSelecionado.getAutor().setNome(novoAutor);
					livroSelecionado.getGenero().setNome(novoGenero);

					em.getTransaction().begin();
					generoDao.atualizar(livroSelecionado.getGenero());
					autorDao.atualizar(livroSelecionado.getAutor());
					livroDao.atualizar(livroSelecionado);
					em.getTransaction().commit();

					JOptionPane.showMessageDialog(null, "ALTERAÇÕES SALVAS COM SUCESSO!");
				} else {
					JOptionPane.showMessageDialog(null, "SELECIONE UM LIVRO!");
				}
			}
		});

		panel.add(btnExcluir);
		panel.add(btnAdicionar);
		panel.add(btnEditarCapa);
		panel.add(btnAlterar);

		return panel;
	}

}
