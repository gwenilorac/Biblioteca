package br.com.gwenilorac.biblioteca.servicos;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import javax.swing.JOptionPane;

import br.com.gwenilorac.biblioteca.dao.EmprestimoDao;
import br.com.gwenilorac.biblioteca.dao.ReservaDao;
import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.model.Emprestimo;
import br.com.gwenilorac.biblioteca.model.Livro;
import br.com.gwenilorac.biblioteca.model.Reserva;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class ServicoEmprestimo {

    public static void pegarLivroEmprestado(Livro livro, Usuario usuario) {
        EntityManager em = JPAUtil.getEntityManager();
        ReservaDao reservaDao = new ReservaDao(em);

        try {
            em.getTransaction().begin();

            Reserva livroEstaReservadoPorUsuario = reservaDao.livroEstaReservadoPorUsuario(livro, usuario);
            boolean livroTemReserva = reservaDao.buscarSeLivroTemReserva(livro);

            if (!livroTemReserva) {
                EmprestarLivro(livro, usuario, em, reservaDao);
            } else if (livroEstaReservadoPorUsuario != null) {
                reservaDao.remover(livroEstaReservadoPorUsuario);
                EmprestarLivro(livro, usuario, em, reservaDao);
            } else {
                JOptionPane.showMessageDialog(null, "LIVRO RESERVADO");
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private static void EmprestarLivro(Livro livro, Usuario usuario, EntityManager em, ReservaDao reservaDao) {
        UsuarioDao usuarioDao = new UsuarioDao(em);
        EmprestimoDao emprestimoDao = new EmprestimoDao(em);
        Emprestimo emprestimoDoLivro = emprestimoDao.buscarSeLivroJaTemEmprestimoRelacionadoAUsuario(livro, usuario);
        List<Livro> livrosEmprestados = usuarioDao.buscarLivrosEmprestados(usuario.getId());
        boolean esteUsuarioEstaComOEmprestimoDesteLivroEncerrado = emprestimoDao
                .esteUsuarioEstaComOEmprestimoDesteLivroEncerrado(livro, usuario);
        boolean isLivroEmprestado = emprestimoDao.isLivroEmprestado(livro);

        if (!isLivroEmprestado) {
            if (livrosEmprestados.size() <= 2) {
                if (emprestimoDoLivro == null) {
                    Emprestimo Novoemprestimo = new Emprestimo(livro, usuario);
                    emprestimoDao.cadastrar(Novoemprestimo);
                    Novoemprestimo.pegarLivroEmprestado();
                    emprestimoDao.atualizar(Novoemprestimo);
                    JOptionPane.showMessageDialog(null, "LIVRO EMPRESTADO COM SUCESSO!");
                } else if (esteUsuarioEstaComOEmprestimoDesteLivroEncerrado) {
                    emprestimoDoLivro.pegarLivroEmprestado();
                    emprestimoDoLivro.setDataDevolucao(LocalDate.now().plusWeeks(4));
                    emprestimoDoLivro.setMultaPaga(false);
                    emprestimoDao.atualizar(emprestimoDoLivro);
                    JOptionPane.showMessageDialog(null, "LIVRO EMPRESTADO COM SUCESSO!");
                } else {
                    JOptionPane.showMessageDialog(null, "O LIVRO NÃO ESTÁ DISPONÍVEL PARA EMPRÉSTIMO.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "SEU LIMITE DE EMPRESTIMOS FOI ATINGIDO");
            }
        } else {
            JOptionPane.showMessageDialog(null, "O LIVRO NÃO ESTÁ DISPONÍVEL PARA EMPRÉSTIMO.");
        }
    }
    
    public static void devolverLivro(Livro livro, Usuario usuario) {
        EntityManager em = JPAUtil.getEntityManager();
        EmprestimoDao emprestimoDao = new EmprestimoDao(em);
        Emprestimo emprestimoDoLivro = emprestimoDao.buscarSeLivroEstaEmprestadoPorUser(usuario, livro);

        try {
            em.getTransaction().begin();

            if (emprestimoDoLivro != null) {

                if (!isMultaValid(emprestimoDoLivro)) {
                    emprestimoDoLivro.devolverLivro();
                    emprestimoDao.atualizar(emprestimoDoLivro);
                    em.getTransaction().commit();
                    JOptionPane.showMessageDialog(null, "LIVRO DEVOLVIDO COM SUCESSO!");

                } else {
                    JOptionPane.showMessageDialog(null, "LIVRO TEM MULTA PENDENTE!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "LIVRO NÃO ESTÁ EMPRESTADO OU JÁ FOI DEVOLVIDO!");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace(); 
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao devolver o livro. Tente novamente.");
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    
    public static boolean isMultaValid(Emprestimo emprestimo) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            if (!emprestimo.isMultaPaga()) {
                if (LocalDate.now().isAfter(emprestimo.getDataDevolucaoLivro())) {
                    emprestimo.setDiasAtrasados(emprestimo.getDataDevolucao().until(LocalDate.now()).getDays());
                    emprestimo.setValorMulta(emprestimo.getDiasAtrasados() * emprestimo.getValormultapordia());
                    emprestimo.setTemMulta(true);

                    em.getTransaction().commit();
                    return true;
                } else {
                    System.out.println("Livro devolvido antes da data de devolução");
                    return false;
                }
            } else {
                System.out.println("Multa já foi paga anteriormente.");
                return false;
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace(); 
            return false;
        } finally {
            em.close();
        }
    }
    
    public static void pagarMulta(Emprestimo emprestimo) {
        EntityManager em = JPAUtil.getEntityManager();
        EmprestimoDao emprestimoDao = new EmprestimoDao(em);

        try {
            em.getTransaction().begin();

            if (!emprestimo.isMultaPaga()) {
                if (LocalDate.now().isAfter(emprestimo.getDataDevolucaoLivro())) {
                    emprestimo.setDiasAtrasados(emprestimo.getDataDevolucao().until(LocalDate.now()).getDays());
                    emprestimo.setValorMulta(emprestimo.getDiasAtrasados() * emprestimo.getValormultapordia());
                    emprestimo.setTemMulta(true);

                    emprestimoDao.atualizar(emprestimo);
                    em.getTransaction().commit();
                    em.close();

                    JOptionPane.showMessageDialog(null, "Multa paga com sucesso. Valor: R$ " + emprestimo.getValorMulta());

                } else {
                    JOptionPane.showMessageDialog(null, "Não há multa a ser paga.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Multa já foi paga anteriormente.");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            JOptionPane.showMessageDialog(null, "Erro ao processar o pagamento da multa: " + e.getMessage());
        } finally {
            em.close();
        }
    }

}
