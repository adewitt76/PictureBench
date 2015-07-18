package PictureBenchSwing;

import Utilities.ImageUtilities;

import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by aaron on 5/9/15.
 */
public class PictureBenchController implements ActionListener, MenuListener{

    private PictureBench view;
    private WorkingImage model;
    private ImagePanel imagePanel;

    private String[] openFileMenuSet = {"File.Save","File.SaveAs","View.ZoomIn","View.ZoomOut","View.NormalZoom","Tools.MagicCrop"};

    public PictureBenchController(PictureBench view, WorkingImage model,ImagePanel imagePanel){
        this.view = view; this.model = model; this.imagePanel = imagePanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        // File menu action events
        if(command.equals("Open...")) {
            File file = ImageUtilities.getPictureFile(view);
            if(file != null){
                model.loadImage(file);
                view.enableMenuItems(openFileMenuSet);
            }
        }
        if(command.equals("Save")) {
        }
        if(command.equals("Save as...")) {
        }
        if(command.equals("Exit")) {
            view.dispose();
        }

        // Edit menu action events
        if(command.equals("Undo")) {
            model.undo();
        }
        if(command.equals("Redo")){
            model.redo();
        }

        // View menu action events
        if(command.equals("Zoom in")) {
            if(model.hasImage()){
               imagePanel.zoomIn();
            }
            view.update();
        }
        if(command.equals("Zoom out")) {
            if(model.hasImage()){
                imagePanel.zoomOut();
            }
            view.update();
        }
        if(command.equals("Normal zoom")) {
            if(model.hasImage()){
                imagePanel.resetZoom();
            }
            view.update();
        }

        // Tools menu action events
        if(command.equals("Magic Crop...")){
            if(model.hasImage()){
                new MagicCropController(this, model);
            }
            view.update();
        }

    }

    @Override
    public void menuSelected(MenuEvent e) {}

    @Override
    public void menuDeselected(MenuEvent e) {
        view.repaint();
    }

    @Override
    public void menuCanceled(MenuEvent e) {}

    public PictureBench getView() {
        return view;
    }
}
