package br.com.gwenilorac.biblioteca.app.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import br.com.gwenilorac.biblioteca.dao.EmprestimoDao;
import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.model.Emprestimo;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.servicos.ServicoUsuario;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

@SuppressWarnings("serial")
public class UsersGUI extends JFrame {

	private EntityManager em = JPAUtil.getEntityManager();
	private PresentationModel<Livro> model;
	private UsuarioDao usuarioDao;
	private EmprestimoDao emprestimoDao;
	private JTextField textFieldPesquisa;
	private JTable tableUsers;
	private JTable tableEmprestimos;
	private JLabel lblFoto;
	private JTextField textFieldNome;
	private JTextField textFieldEmail;
	private JTextField textFieldSenha;
	private Usuario usuarioSelecionado;
	private File selectedCoverFile;
	private Container contentPane;
	private JDialog dialog;
	private java.util.List<Usuario> usuariosEncontrados;
	private java.util.List<Emprestimo> emprestimosUser;

	public UsersGUI() {
		initModel();
		initComponents();
		initLayout();
	}

	private void initModel() {
	}

	private void initComponents() {

	}

	public void initLayout() {
		contentPane = getContentPane();
		contentPane.setLayout(new GridLayout(3, 1));
		contentPane.add(criarPanelPesquisarUsers());
		contentPane.add(criarPanelDadosUser());
		contentPane.add(criarPanelBotoes());

		setPreferredSize(new Dimension(900, 900));
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		setExtendedState(MAXIMIZED_BOTH);
	}

	private JPanel criarPanelPesquisarUsers() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Pesquisar Usuarios"));

		JPanel buscaPanel = new JPanel();
		buscaPanel.setLayout(new FlowLayout());

		textFieldPesquisa = new JTextField(20);
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				usuarioDao = new UsuarioDao(em);
				usuariosEncontrados = usuarioDao.buscarUsuarios(textFieldPesquisa.getText());

				DefaultTableModel tableModel = (DefaultTableModel) tableUsers.getModel();
				tableModel.setRowCount(0);

				for (Usuario usuario : usuariosEncontrados) {
					Object[] rowData = { usuario.getNome(), usuario.getEmail(), usuario.getSenha() };
					tableModel.addRow(rowData);
				}

				tableUsers.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						if (!e.getValueIsAdjusting()) {
							int selectedRow = tableUsers.getSelectedRow();
							if (selectedRow != -1) {
								usuarioSelecionado = usuariosEncontrados.get(selectedRow);

								if (usuarioSelecionado != null) {
									byte[] imagemIcon = usuarioSelecionado.getFoto();
									if (imagemIcon != null) {
										ImageIcon icon = new ImageIcon(imagemIcon);
										Image img = icon.getImage().getScaledInstance(150, 190, Image.SCALE_SMOOTH);
										ImageIcon newIcon = new ImageIcon(img);
										lblFoto.setIcon(newIcon);
									} else {
										JOptionPane.showMessageDialog(null, "Foto Inexistente!");
									}
								}

								textFieldNome.setText(usuarioSelecionado.getNome());
								textFieldEmail.setText(usuarioSelecionado.getEmail());
								textFieldSenha.setText(usuarioSelecionado.getSenha());
							}
						}
					}
				});
			}
		});

		buscaPanel.add(new JLabel("Pesquisar Usuarios:"));
		buscaPanel.add(textFieldPesquisa);
		buscaPanel.add(btnBuscar);

		String[] colunas = { "Nome", "Email", "Senha" };
		DefaultTableModel tableModel = new DefaultTableModel(colunas, 0);
		tableUsers = new JTable(tableModel) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JScrollPane scrollPane = new JScrollPane(tableUsers);
		scrollPane.setPreferredSize(new Dimension(400, 150));
		panel.add(buscaPanel, BorderLayout.PAGE_START);
		panel.add(scrollPane, BorderLayout.CENTER);

		return panel;
	}

	private JPanel criarPanelDadosUser() {
		JPanel panelDadosUsers = new JPanel(new FlowLayout());
		panelDadosUsers.setBorder(BorderFactory.createTitledBorder("Detalhes do Usuario"));

		JPanel fotoPanel = new JPanel();
		lblFoto = new JLabel();
		fotoPanel.add(lblFoto);

		FormLayout layout = new FormLayout("pref, 5px, 70dlu");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		textFieldNome = new JTextField();
		textFieldEmail = new JTextField();
		textFieldSenha = new JTextField();

		builder.append("Nome: ", textFieldNome);
		builder.nextLine();
		builder.append("Email: ", textFieldEmail);
		builder.nextLine();
		builder.append("Senha: ", textFieldSenha);

		JPanel formPanel = builder.getPanel();

		panelDadosUsers.add(fotoPanel);
		panelDadosUsers.add(new JSeparator(JSeparator.VERTICAL));
		panelDadosUsers.add(formPanel);

		return panelDadosUsers;
	}


	private JPanel criarPanelBotoes() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());

		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (usuarioSelecionado != null) {
					int confirmacao = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover o usuario?",
							"Confirmação", JOptionPane.YES_NO_OPTION);

					if (confirmacao == JOptionPane.YES_OPTION) {
						boolean removerUsuario = ServicoUsuario.removerUsuario(usuarioSelecionado);
						if (removerUsuario == true) {
							System.out.println("User removido: " + usuarioSelecionado.getNome());
							JOptionPane.showMessageDialog(null, "USUARIO REMOVIDO COM SUCESSO!");
						} else {
							JOptionPane.showMessageDialog(null, "ERRO AO REMOVER USUARIO!");
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "SELECIONE UM USUARIO!");
				}
			}
		});

		JButton btnAdicionar = new JButton("Adicionar Novo Usuario");
		btnAdicionar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CadastroFrm addUserFrm = new CadastroFrm();
				dialog = new JDialog();
				dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				dialog.add(addUserFrm);
				dialog.setPreferredSize(new Dimension(400, 200));
				dialog.pack();
				dialog.setVisible(true);

				Dimension desktopSize = contentPane.getSize();
				Dimension jInternalFrameSize = dialog.getSize();
				dialog.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
						(desktopSize.height - jInternalFrameSize.height) / 2);

				return;
			}
		});

		JButton btnEditarFoto = new JButton("Editar Foto de Perfil");
		btnEditarFoto.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (usuarioSelecionado != null) {
					JFileChooser chooser = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png");
					chooser.setFileFilter(filter);
					int result = chooser.showOpenDialog(null);

					if (result == JFileChooser.APPROVE_OPTION) {
						selectedCoverFile = chooser.getSelectedFile();
						try {
							BufferedImage originalImage = ImageIO.read(selectedCoverFile);
							Image resizedImage = originalImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH);

							BufferedImage bufferedResizedImage = new BufferedImage(150, 150,
									BufferedImage.TYPE_INT_RGB);
							bufferedResizedImage.getGraphics().drawImage(resizedImage, 0, 0, null);

							ImageIcon novaFotoIcon = new ImageIcon(bufferedResizedImage);
							lblFoto.setIcon(novaFotoIcon);

							ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
							ImageIO.write(originalImage, "png", outputStream);
							byte[] coverImageBytes = outputStream.toByteArray();
							outputStream.close();
							usuarioSelecionado.setFoto(coverImageBytes);

							JOptionPane.showMessageDialog(null, "FOTO EDITADA COM SUCESSO!");
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "SELECIONE UM USUARIO!");
				}
			}
		});

		JButton btnAlterar = new JButton("Salvar Alterações");
		btnAlterar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (usuarioSelecionado != null) {
					EntityManager em = JPAUtil.getEntityManager();
					UsuarioDao usuarioDao = new UsuarioDao(em);

					String novoNome = textFieldNome.getText();
					String novoEmail = textFieldEmail.getText();
					String novoSenha = textFieldSenha.getText();

					if (selectedCoverFile != null) {
						try {
							BufferedImage originalImage = ImageIO.read(selectedCoverFile);
							ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
							ImageIO.write(originalImage, "png", outputStream);
							byte[] coverImageBytes = outputStream.toByteArray();
							outputStream.close();
							usuarioSelecionado.setFoto(coverImageBytes);

						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}

					usuarioSelecionado.setNome(novoNome);
					usuarioSelecionado.setEmail(novoEmail);
					usuarioSelecionado.setSenha(novoSenha);

					em.getTransaction().begin();
					usuarioDao.atualizar(usuarioSelecionado);
					em.getTransaction().commit();

					JOptionPane.showMessageDialog(null, "ALTERAÇÕES SALVAS COM SUCESSO!");
				} else {
					JOptionPane.showMessageDialog(null, "SELECIONE UM USUARIO!");
				}
			}
		});

		panel.add(btnExcluir);
		panel.add(btnAdicionar);
		panel.add(btnEditarFoto);
		panel.add(btnAlterar);

		return panel;
	}

}
