package br.com.gwenilorac.biblioteca.app.client;

import com.jgoodies.binding.adapter.AbstractTableAdapter;
import com.jgoodies.binding.list.SelectionInList;

import br.com.gwenilorac.biblioteca.model.Emprestimo;

public class TableModelEmprestimos extends AbstractTableAdapter<Emprestimo>{
	
	public TableModelEmprestimos(SelectionInList<Emprestimo> selection) {
		super(selection, new String[]{
				"T\u00EDtulo"
				, "Data devolu\u00E7\u00E3o"
				, "Multa"
				});
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Emprestimo bean = getRow(rowIndex);
		switch (columnIndex) {
		case 0: return bean.getLivro().getTitulo();
		case 1: return bean.getDataDevolucaoFormatted();
		case 2: return bean.getTemMulta();
		default: return null;
		}
	}

}
