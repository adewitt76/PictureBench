package Utilities;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;

/**
 * Created by aaron on 5/10/15.
 */
public class ImageUtilities {

    public static BufferedImage copy(BufferedImage i) {
        ColorModel cm = i.getColorModel();
        boolean alpha = i.isAlphaPremultiplied();
        WritableRaster r = i.copyData(null);
        return new BufferedImage(cm, r, alpha, null);
    }

    public static File getPictureFile(JFrame parent){
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "PNG, JPG & GIF Images", "jpg", "gif", "png");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(parent);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        else return null;
    }
}
