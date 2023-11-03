package br.com.gwenilorac.biblioteca.app.client;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageUtils {

    private static File selectedCoverFile;

    public static byte[] editarCapa() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png");
        chooser.setFileFilter(filter);
        int result = chooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedCoverFile = chooser.getSelectedFile();
            try {
                BufferedImage originalImage = ImageIO.read(selectedCoverFile);
                Image resizedImage = originalImage.getScaledInstance(150, 200, Image.SCALE_SMOOTH);

                BufferedImage bufferedResizedImage = new BufferedImage(150, 200, BufferedImage.TYPE_INT_RGB);
                bufferedResizedImage.getGraphics().drawImage(resizedImage, 0, 0, null);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bufferedResizedImage, "jpg", baos);

                return baos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
