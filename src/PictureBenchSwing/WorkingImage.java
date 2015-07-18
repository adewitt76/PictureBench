package PictureBenchSwing;

import Utilities.MVC_Model;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

/**
 * Created by aaron on 5/9/15.
 */
public class WorkingImage implements MVC_Model {

    private File file; // the file of the image
    private BufferedImage image;
    private Stack<BufferedImage> undoStack;
    private Stack<BufferedImage> redoStack;
    private int width, height;

    public WorkingImage(){
        file = null; image = null; width = 0; height = 0;
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public void loadImage(File file) {
        this.file = file;
        try {
            image = ImageIO.read(file);
            width = image.getWidth(); height = image.getHeight();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading file: " + file.getName());
            file = null; image = null; width = 0; height = 0;
        }
        notifyListeners();
    }

    public void commitChange(BufferedImage i){
        redoStack.clear();
        undoStack.add(image);
        image = i;
        width = image.getWidth(); height = image.getHeight();
        notifyListeners();
    }

    public void undo(){
        if(!undoStack.isEmpty()) {
            redoStack.add(image);
            image = undoStack.pop();
            width = image.getWidth(); height = image.getHeight();
            notifyListeners();
        }
    }

    public void redo(){
        if(!redoStack.isEmpty()){
            undoStack.add(image);
            image = redoStack.pop();
            width = image.getWidth(); height = image.getHeight();
            notifyListeners();
        }
    }
    // TODO: add save methods remember to clear the queues on save
    public BufferedImage getImage() {
        return image;
    }

    public boolean hasImage() { return image != null; }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean hasRedoInStack() { return !redoStack.isEmpty(); }

    public boolean hasUndoInStack() { return ! undoStack.isEmpty(); }
}
