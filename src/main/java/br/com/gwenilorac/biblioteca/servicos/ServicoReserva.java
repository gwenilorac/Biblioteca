package br.com.gwenilorac.biblioteca.servicos;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.swing.JOptionPane;

import br.com.gwenilorac.biblioteca.dao.EmprestimoDao;
import br.com.gwenilorac.biblioteca.dao.ReservaDao;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.Reserva;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class ServicoReserva {

    public static void realizarReserva(Usuario usuarioSelecionado, Livro livroSelecionado) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            EmprestimoDao emprestimoDao = new EmprestimoDao(em);
            ReservaDao reservaDao = new ReservaDao(em);

            if (livroSelecionado != null && usuarioSelecionado != null) {
                if (emprestimoDao.isLivroEmprestado(livroSelecionado)) {
                    if (!reservaDao.buscarSeLivroTemReserva(livroSelecionado)) {
                        Reserva reserva = new Reserva(livroSelecionado, usuarioSelecionado);
                        reservaDao.cadastrar(reserva);
                        transaction.commit();
                        mostrarMensagem("Livro reservado com sucesso!");
                    } else {
                        mostrarMensagem("O livro selecionado já está reservado.");
                    }
                } else {
                    mostrarMensagem("Livro está disponível para empréstimo.");
                }
            } else {
                mostrarMensagem("SELECIONE UM LIVRO E USUÁRIO!");
            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            lidarComExcecao(e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    private static void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(null, mensagem);
    }

    private static void lidarComExcecao(Exception e) {
        if (e instanceof PersistenceException) {
            mostrarMensagem("Erro ao acessar o banco de dados.");
        } else {
            mostrarMensagem("Erro ao realizar a reserva. Consulte o log para mais detalhes.");
        }
        e.printStackTrace(); 
    }
}

