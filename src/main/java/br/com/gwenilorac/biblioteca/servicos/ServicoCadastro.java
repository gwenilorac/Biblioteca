package br.com.gwenilorac.biblioteca.servicos;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class ServicoCadastro {

    private static boolean emailCadastrado;

    public static boolean cadastraUsuario(Usuario usuario) {
        if (usuario == null || usuario.getEmail() == null || usuario.getSenha() == null) {
            throw new IllegalArgumentException("Usuário, e-mail e senha não podem ser nulos.");
        }

        EntityManager em = JPAUtil.getEntityManager();
        UsuarioDao usuarioDao = new UsuarioDao(em);

        try {
            emailCadastrado = usuarioDao.buscarEmailCadastrado(usuario.getEmail());

            if (emailCadastrado) {
                return false;
            } else {
                byte[] foto = usuario.getFoto();
                Usuario newUsuario = new Usuario(usuario.getNome(), usuario.getEmail(), usuario.getSenha(), foto);

                EntityTransaction transaction = em.getTransaction();
                transaction.begin();
                usuarioDao.cadastrar(newUsuario);
                transaction.commit();
                return true;
            }
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public static byte[] lerFoto(File selectedPhoto) {
        if (selectedPhoto == null || !selectedPhoto.exists() || !selectedPhoto.isFile()) {
            throw new IllegalArgumentException("O arquivo de foto não é válido.");
        }

        try (FileInputStream fis = new FileInputStream(selectedPhoto)) {
            byte[] photoBytes = new byte[(int) selectedPhoto.length()];
            fis.read(photoBytes);
            return photoBytes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
