package br.com.gwenilorac.biblioteca.app.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import com.jgoodies.binding.list.SelectionInList;

import br.com.gwenilorac.biblioteca.dao.EmprestimoDao;
import br.com.gwenilorac.biblioteca.dao.LivroDao;
import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.model.Emprestimo;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.TemMulta;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.servicos.ServicoEmprestimo;
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
		initLayout();
	}

	private void initModel() {
		selectionEmprestimo = new SelectionInList<Emprestimo>(new ArrayList<>());
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
	                realizarBuscaUsuario();
	            } else if (btnLivro.isSelected()) {
	                realizarBuscaLivro();
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

	private void realizarBuscaUsuario() {
	    userDao = new UsuarioDao(em);
	    usuariosEncontrados = userDao.buscarUsuarios(textFieldPesquisa.getText());

	    DefaultTableModel userTableModel = (DefaultTableModel) tableUsers.getModel();
	    userTableModel.setRowCount(0);

	    for (Usuario usuario : usuariosEncontrados) {
	        Object[] rowData = { usuario.getNome(), usuario.getEmail() };
	        userTableModel.addRow(rowData);
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
	}

	private void realizarBuscaLivro() {
	    livroDao = new LivroDao(em);
	    livrosEncontrados = livroDao.buscarLivros(textFieldPesquisa.getText());

	    DefaultTableModel livroTableModel = (DefaultTableModel) tableLivros.getModel();
	    livroTableModel.setRowCount(0);

	    for (Object livro : livrosEncontrados) {
	        Object[] rowData = { ((Livro) livro).getTitulo(), ((Livro) livro).getAutor(), ((Livro) livro).getGenero() };
	        livroTableModel.addRow(rowData);
	    }

	    tableLivros.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
	        @Override
	        public void valueChanged(ListSelectionEvent e) {
	            if (!e.getValueIsAdjusting()) {
	                int selectedRow = tableLivros.getSelectedRow();
	                if (selectedRow != -1) {
	                    livroSelecionado = (Livro) livrosEncontrados.get(selectedRow);
	                }
	            }
	        }
	    });
	}

	private void pesquisarEmprestimos(Usuario usuario) {
		List<Emprestimo> emprestimosEncontrados = getEmprestimoDAO().buscarEmprestimosUser(usuario.getId());

		if (!selectionEmprestimo.getList().isEmpty()) {
			selectionEmprestimo.getList().clear();
		}

		selectionEmprestimo.getList().addAll(emprestimosEncontrados);

		((AbstractTableModel) tableEmprestimos.getModel()).fireTableDataChanged();
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

		tableEmprestimos.setModel(tableModelEmprestimos);
		tableModelEmprestimos.fireTableDataChanged();

		tableEmprestimos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					int selectedRow = tableEmprestimos.getSelectedRow();
					if (selectedRow != -1) {
						Emprestimo emprestimoSelecionado = selectionEmprestimo.getElementAt(selectedRow);

						if (emprestimoSelecionado.getTemMulta() == TemMulta.PENDENTE) {
							exibirTelaMulta(emprestimoSelecionado);
						}
					}
				}
			}
		});

		JScrollPane emprestimosScrollPane = new JScrollPane(tableEmprestimos);
		emprestimosScrollPane.setPreferredSize(new Dimension(400, 150));

		panel.add(emprestimosScrollPane, BorderLayout.CENTER);

		return panel;
	}

	private void exibirTelaMulta(Emprestimo emprestimo) {

		JDialog multaDialog = new JDialog(this, "Detalhes da Multa", true);
		multaDialog.setSize(400, 250);
		multaDialog.setLayout(new BorderLayout());
		
		JButton btnPagarMulta = new JButton("Pagar Multa");
		btnPagarMulta.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ServicoEmprestimo.pagarMulta(emprestimo);
				
				JOptionPane.showMessageDialog(multaDialog, "Multa paga com sucesso. Data: " + LocalDate.now(),
						"Multa Paga", JOptionPane.INFORMATION_MESSAGE);
				
				((AbstractTableModel) tableEmprestimos.getModel()).fireTableDataChanged();
				
				multaDialog.dispose();
			}
		});

		JPanel panelDadosMulta = new JPanel(new FlowLayout());
		
		JPanel capaPanel = new JPanel();
		byte[] imagemIcon = emprestimo.getLivro().getCapa();
		ImageIcon icon = new ImageIcon(imagemIcon);
		Image img = icon.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH);
		ImageIcon newIcon = new ImageIcon(img);
		JLabel capaLabel = new JLabel(newIcon);
		capaPanel.add(capaLabel);
		
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
		infoPanel.add(new JLabel("Dias Atrasados: " + emprestimo.getDiasAtrasados()));
		infoPanel.add(new JLabel("Valor da Multa: R$ " + emprestimo.getValorMulta()));
		infoPanel.add(btnPagarMulta);

		panelDadosMulta.add(capaPanel);
		panelDadosMulta.add(new JSeparator(JSeparator.VERTICAL));
		panelDadosMulta.add(infoPanel);
		
		multaDialog.add(panelDadosMulta, BorderLayout.CENTER);

		multaDialog.setLocationRelativeTo(this);
		multaDialog.setVisible(true);
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
