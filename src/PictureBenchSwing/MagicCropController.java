package PictureBenchSwing;

import Utilities.ImageUtilities;
import Utilities.GeneralUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by aaron on 5/10/15.
 */
public class MagicCropController implements ActionListener {

    private final int HORIZONTAL = 1, VERTICAL = 2, PERCENT = 3, PIXEL = 4;

    private PictureBenchController parent;
    private WorkingImage model;
    private MagicCropView view;
    private MagicCrop carver;

    private int direction;
    private int unitType;

    private SeamRemover vsRemover;

    public MagicCropController(PictureBenchController parent, WorkingImage model){

        this.parent = parent;
        parent.getView().setEnabled(false);

        this.model = model;
        view  = new MagicCropView(this);
        carver = new MagicCrop(ImageUtilities.copy(model.getImage()));
        direction = HORIZONTAL;
        unitType = PIXEL;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getActionCommand().equals("Vertical")) direction = VERTICAL;
        if(e.getActionCommand().equals("Horizontal")) direction = HORIZONTAL;
        if(e.getActionCommand().equals("Pixel")) unitType = PIXEL;
        if(e.getActionCommand().equals("Percent")) unitType = PERCENT;
        if(e.getActionCommand().equals("ok")){
            String text = view.getUnitTextField().getText();
            if(GeneralUtil.isNumerical(text)){
                int value = Integer.parseInt(text);
                magicCrop(value);
            } else JOptionPane.showMessageDialog(view,"Please input a whole number in the text box.");
        }
        if(e.getActionCommand().equals("cancel")) {
            if (vsRemover != null) {
                vsRemover.cancel();
            } else {
                parent.getView().setEnabled(true);
                view.dispose();
            }
        }

        String u, d;
        if (unitType==PIXEL) {
            u = "pixels";
            d = (direction == HORIZONTAL) ? " in width" : " in height";
        } else {
            u = "% ";
            d = (direction == HORIZONTAL) ? " of pictures width" : " of pictures height";
        }
        view.getUnitLabel().setText(String.format("%12s", (u + " " + d)));
    }

    private void magicCrop(int value){
        switch (direction){
            case VERTICAL:
                if(unitType == PIXEL){
                    if(0 < value && value < model.getHeight()) {
                        disableView();
                        vsRemover = new SeamRemover(value, VERTICAL);
                        new Thread(vsRemover).start();
                    } else JOptionPane.showMessageDialog(view,"Please insert a number between 0 and " + model.getHeight());
                }
                if (unitType == PERCENT){
                    if(0 < value && value < 100){
                        double p = value/100.0;
                        value = (int)(model.getHeight() * p);
                        disableView();
                        vsRemover = new SeamRemover(value, VERTICAL);
                        new Thread(vsRemover).start();
                    } else JOptionPane.showMessageDialog(view, "Please insert a number between 0 and 100");
                }
                break;
            case HORIZONTAL:
                if(unitType == PIXEL){
                    if(0 < value && value < model.getWidth()) {
                        disableView();
                        vsRemover = new SeamRemover(value, HORIZONTAL);
                        new Thread(vsRemover).start();
                    } else JOptionPane.showMessageDialog(view, "Please insert a number between 0 and " + model.getWidth());
                }
                if (unitType == PERCENT){
                    if(0 < value && value < 100){
                        double p = value/100.0;
                        value = (int)(model.getWidth() * p);
                        disableView();
                        vsRemover = new SeamRemover(value, HORIZONTAL);
                        new Thread(vsRemover).start();
                    } else JOptionPane.showMessageDialog(view, "Please insert a number between 0 and 100");
                }
                break;
        }
    }

    private void disableView(){
        view.getHorizontalSelect().setEnabled(false);
        view.getVerticalSelect().setEnabled(false);
        view.getPixelSelect().setEnabled(false);
        view.getPercentSelect().setEnabled(false);
        view.getUnitTextField().setEnabled(false);
        view.getOkButton().setEnabled(false);
    }

    class SeamRemover implements Runnable{
        int value;
        int direction;
        boolean running;
        SeamRemover(int value, int direction){
            this.value = value;
            this.direction = direction;
            running = true;
        }
        @Override
        public void run() {
            int pixelsToCut;
            if(direction == HORIZONTAL) pixelsToCut= model.getWidth() - value;
            else pixelsToCut = model.getHeight() - value;
            for (int i = 0; i < pixelsToCut; i++) {
                if (running) {
                    if(direction == HORIZONTAL) carver.removeVerticalSeam(carver.findVerticalSeam());
                    else carver.removeHorizontalSeam(carver.findHorizontalSeam());
                    view.getPercentComplete().setText(String.format("%3d %%", (int) ((i / (double) pixelsToCut) * 100)));
                }
            }
            if(running) model.commitChange(carver.getImage());
            parent.getView().setEnabled(true);
            view.dispose();
        }

        public void cancel(){ running = false; }
    }

    public PictureBenchController getParent() {
        return parent;
    }

    public WorkingImage getModel() {
        return model;
    }
}
