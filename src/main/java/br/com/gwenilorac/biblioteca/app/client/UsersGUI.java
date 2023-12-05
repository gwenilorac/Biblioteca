package br.com.gwenilorac.biblioteca.app.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.persistence.EntityManager;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import br.com.gwenilorac.biblioteca.controller.GeraRelatorioUsuarios;
import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.servicos.ServicoUsuario;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

@SuppressWarnings("serial")
public class UsersGUI extends JFrame {

	private EntityManager em = JPAUtil.getEntityManager();
	private UsuarioDao usuarioDao;
	private JTextField textFieldPesquisa;
	private JTable tableUsers;
	private JTextField textFieldNome;
	private JTextField textFieldEmail;
	private JTextField textFieldSenha;
	private Usuario usuarioSelecionado;
	private Container contentPane;
	private JDialog dialog;
	private List<Usuario> usuariosEncontrados;

	public UsersGUI() {
		initLayout();
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
						if (removerUsuario) {
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

		JButton btnRelatorio = new JButton("Relatorio");
		btnRelatorio.addActionListener(br -> new GeraRelatorioUsuarios());
		
		panel.add(btnExcluir);
		panel.add(btnAdicionar);
		panel.add(btnAlterar);
		panel.add(btnRelatorio);

		return panel;
	}
}
