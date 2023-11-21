package br.com.gwenilorac.biblioteca.app.client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.list.SelectionInList;

import br.com.gwenilorac.biblioteca.dao.EmprestimoDao;
import br.com.gwenilorac.biblioteca.dao.LivroDao;
import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.model.Emprestimo;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.servicos.ServicoEmprestimo;
import br.com.gwenilorac.biblioteca.servicos.ServicoUsuario;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

@SuppressWarnings("serial")
public class EmprestimosGUI extends JFrame {

	private EntityManager em = JPAUtil.getEntityManager();
	private LivroDao livroDao;
	private UsuarioDao userDao;
	private EmprestimoDao emprestimoDao;
	private JTextField textFieldPesquisa;
	private JTable tableUsers;
	private JTable tableLivros;
	private JTable tableEmprestimos;
	private Usuario usuarioSelecionado;
	private Livro livroSelecionado;
	private Container contentPane;
	
	private List<Usuario> usuariosEncontrados;
	private List<Livro> livrosEncontrados;

	private SelectionInList<Emprestimo> selectionEmprestimo;

	public EmprestimosGUI() {
		initModel();
		initComponents();
		initLayout();
	}

	private void initModel() {
		selectionEmprestimo = new SelectionInList<Emprestimo>(new ArrayList<>());
	}

	private void initComponents() {

	}

	public void initLayout() {
		contentPane = getContentPane();
		contentPane.setLayout(new GridLayout(3, 1));
		contentPane.add(criarPanelPesquisa());
		contentPane.add(criarPanelEmprestimos());
		contentPane.add(criarPanelBotoes());

		setPreferredSize(new Dimension(900, 900));
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		setExtendedState(MAXIMIZED_BOTH);

	}

	private JPanel criarPanelPesquisa() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Pesquisar"));

		JPanel buscaPanel = new JPanel();
		buscaPanel.setLayout(new FlowLayout());

		JRadioButton btnUser = new JRadioButton("Usuario");
		JRadioButton btnLivro = new JRadioButton("Livro");

		textFieldPesquisa = new JTextField(20);
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (btnUser.isSelected()) {
					userDao = new UsuarioDao(em);
					usuariosEncontrados = userDao.buscarUsuarios(textFieldPesquisa.getText());

					DefaultTableModel userTableModel = (DefaultTableModel) tableUsers.getModel();
					userTableModel.setRowCount(0);

					for (Usuario usuario : usuariosEncontrados) {
						Object[] rowData = { usuario.getNome(), usuario.getEmail() };
						userTableModel.addRow(rowData);

					}
					if (btnLivro.isSelected()) {
						livroDao = new LivroDao(em);
						livrosEncontrados = livroDao.buscarLivrosDiponiveis(textFieldPesquisa.getText());

						DefaultTableModel livroTableModel = (DefaultTableModel) tableLivros.getModel();
						livroTableModel.setRowCount(0);

						for (Livro livro : livrosEncontrados) {
							Object[] rowData = { livro.getTitulo(), livro.getAutor(), livro.getGenero() };
							livroTableModel.addRow(rowData);
						}

					}

					tableUsers.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
						@Override
						public void valueChanged(ListSelectionEvent e) {
							if (!e.getValueIsAdjusting()) {
								int selectedRow = tableUsers.getSelectedRow();
								if (selectedRow != -1) {
									usuarioSelecionado = usuariosEncontrados.get(selectedRow);
									pesquisarEmprestimos(usuarioSelecionado);
								}
							}
						}
					});

					tableLivros.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
						@Override
						public void valueChanged(ListSelectionEvent e) {
							if (!e.getValueIsAdjusting()) {
								int selectedRow = tableLivros.getSelectedRow();
								if (selectedRow != -1) {
									livroSelecionado = livrosEncontrados.get(selectedRow);
								}
							}
						}
					});
				}
			}
		});

		buscaPanel.add(new JLabel("Pesquisar:"));
		buscaPanel.add(textFieldPesquisa);
		buscaPanel.add(btnBuscar);
		buscaPanel.add(btnUser);
		buscaPanel.add(btnLivro);

		String[] userColunas = { "Nome", "Email" };
		DefaultTableModel userTableModel = new DefaultTableModel(userColunas, 0);
		tableUsers = new JTable(userTableModel) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		String[] livrosColunas = { "Titulo", "Autor", "Genero" };
		DefaultTableModel livroTableModel = new DefaultTableModel(livrosColunas, 0);
		tableLivros = new JTable(livroTableModel) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		JScrollPane userScrollPane = new JScrollPane(tableUsers);
		userScrollPane.setPreferredSize(new Dimension(400, 150));

		JScrollPane livrosScrollPane = new JScrollPane(tableLivros);
		livrosScrollPane.setPreferredSize(new Dimension(400, 150));

		JPanel pesquisaPanel = new JPanel(new FlowLayout());
		pesquisaPanel.add(userScrollPane);
		pesquisaPanel.add(livrosScrollPane);

		panel.add(buscaPanel, BorderLayout.PAGE_START);
		panel.add(pesquisaPanel, BorderLayout.CENTER);

		return panel;
	}

	private void pesquisarEmprestimos(Usuario usuario) {
		List<Emprestimo> emprestimosEncontrados = getEmprestimoDAO().buscarEmprestimosUser(usuario.getId());

		if (!selectionEmprestimo.getList().isEmpty()) {
			selectionEmprestimo.getList().clear();
		}

		selectionEmprestimo.getList().addAll(emprestimosEncontrados);
	}

	private EmprestimoDao getEmprestimoDAO() {
		if (emprestimoDao == null) {
			emprestimoDao = new EmprestimoDao(em);
		}
		return emprestimoDao;
	}

	private JPanel criarPanelEmprestimos() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Emprestimos"));

		TableModelEmprestimos tableModelEmprestimos = new TableModelEmprestimos(selectionEmprestimo);
		
		tableEmprestimos = new JTable(tableModelEmprestimos) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		JScrollPane emprestimosScrollPane = new JScrollPane(tableEmprestimos);
		emprestimosScrollPane.setPreferredSize(new Dimension(400, 150));

		panel.add(emprestimosScrollPane, BorderLayout.CENTER);

		return panel;
	}

	private JPanel criarPanelBotoes() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());

		JButton btnPegarEmprestado = new JButton("Pegar Emprestado");
		btnPegarEmprestado.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ServicoEmprestimo.pegarLivroEmprestado(livroSelecionado, usuarioSelecionado);
			}
		});
		
		JButton btnDevolverLivro = new JButton("Devolver Livro");
		btnDevolverLivro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ServicoEmprestimo.devolverLivro(livroSelecionado);
			}
		});

		panel.add(btnPegarEmprestado);
		panel.add(btnDevolverLivro);
		
		return panel;
	}

}
