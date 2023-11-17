package br.com.gwenilorac.biblioteca.servicos;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.persistence.EntityManager;

import br.com.gwenilorac.biblioteca.dao.UsuarioDao;
import br.com.gwenilorac.biblioteca.model.Usuario;
import br.com.gwenilorac.biblioteca.util.JPAUtil;

public class ServicoCadastro {

    private static boolean emailCadastrado;

    public static boolean cadastraUsuario(Usuario usuario) {
        EntityManager em = JPAUtil.getEntityManager();
        UsuarioDao usuarioDao = new UsuarioDao(em);

        String nome = usuario.getNome();
        String email = usuario.getEmail();
        String senha = usuario.getSenha();

        emailCadastrado = usuarioDao.buscarEmailCadastrado(email);

        if (emailCadastrado) {
            return false;
        } else {
            byte[] foto = usuario.getFoto();

            Usuario newUsuario = new Usuario(nome, email, senha, foto);

            em.getTransaction().begin();
            usuarioDao.cadastrar(newUsuario);
            em.getTransaction().commit();
            em.close();
            return true;
        }
    }

    public static byte[] LerFoto(File selectedPhoto) {
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
