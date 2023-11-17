package br.com.gwenilorac.biblioteca.model;

import java.time.LocalDate;

public class Multa {
	private static final double VALOR_MULTA = 5.0;
	private Emprestimo emprestimo;
	private boolean multaPaga;

	public Multa(Emprestimo emprestimo) {
		this.emprestimo = emprestimo;
		this.multaPaga = false;
	}

	public double calcularMulta() {
		if (!multaPaga) {
			LocalDate dataAtual = LocalDate.now();
			LocalDate dataDevolucaoLivro = emprestimo.getDataDevolucao();

			if (dataAtual.isAfter(dataDevolucaoLivro)) {
				long diasAtraso = dataDevolucaoLivro.until(dataAtual).getDays();
				return diasAtraso * VALOR_MULTA;
			}
		}
		return 0.0;
	}

	public void pagarMulta() {
		if (!multaPaga) {
			double valorMulta = calcularMulta();
			if (valorMulta > 0.0) {
				System.out.println("Multa paga com sucesso. Valor: R$ " + valorMulta);
				multaPaga = true;
			} else {
				System.out.println("Não há multa a ser paga.");
			}
		} else {
			System.out.println("Multa já foi paga anteriormente.");
		}
	}

	public Emprestimo getEmprestimo() {
		return emprestimo;
	}

	public boolean isMultaPaga() {
		return multaPaga;
	}
}
