package PictureBenchSwing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by aaron on 5/10/15.
 */
public class MagicCropView extends JDialog {

    private MagicCropController controller;

    private ButtonGroup seamOrientationGroup;
    private JRadioButton verticalSelect;
    private JRadioButton horizontalSelect;

    private ButtonGroup unitGroup;
    private JRadioButton pixelSelect;
    private JRadioButton percentSelect;

    private JTextField unitTextField;
    private JLabel unitLabel;

    private JLabel percentComplete;

    private JButton okButton;
    private JButton cancelButton;

    public MagicCropView(MagicCropController controller){
        super(controller.getParent().getView() ,"Magic Crop");
        this.controller = controller;

        setSize(325, 250);
        setResizable(true);
        setVisible(true);

        getContentPane().setLayout(new GridBagLayout());

        seamOrientationGroup = new ButtonGroup();
        verticalSelect = new JRadioButton("Height");
        verticalSelect.setActionCommand("Vertical");
        verticalSelect.addActionListener(controller);
        horizontalSelect = new JRadioButton("Width");
        horizontalSelect.setActionCommand("Horizontal");
        horizontalSelect.addActionListener(controller);
        horizontalSelect.setSelected(true);
        seamOrientationGroup.add(horizontalSelect);
        seamOrientationGroup.add(verticalSelect);

        unitGroup = new ButtonGroup();
        pixelSelect = new JRadioButton("Pixel");
        pixelSelect.addActionListener(controller);
        pixelSelect.setSelected(true);
        percentSelect = new JRadioButton("Percent");
        percentSelect.addActionListener(controller);
        unitGroup.add(pixelSelect);
        unitGroup.add(percentSelect);

        unitTextField = new JTextField();
        unitTextField.addActionListener(controller);
        unitLabel = new JLabel(String.format("%12s","pixels in width"));

        percentComplete = new JLabel("  0 %");

        okButton = new JButton("ok");
        okButton.addActionListener(controller);
        cancelButton = new JButton("cancel");
        cancelButton.addActionListener(controller);


        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 0.5;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        constraints.insets = new Insets(5,10,10,5);
        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = 1;
        getContentPane().add(new JLabel(String.format("Image Dimensions: %4dx%d",controller.getModel().getWidth(),
                controller.getModel().getHeight())),constraints);
        constraints.insets = new Insets(0,0,0,0);
        constraints.gridx = 0;
        constraints.gridy = 2;
        getContentPane().add(new JSeparator(), constraints);
        constraints.insets = new Insets(5,5,5,5);
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 3;
        getContentPane().add(unitTextField,constraints);
        constraints.gridwidth = 2;
        constraints.insets = new Insets(5,0,0,5);
        constraints.gridx = 1;
        constraints.gridy = 3;
        getContentPane().add(unitLabel, constraints);
        constraints.gridwidth = 1;
        constraints.insets = new Insets(0,0,0,0);
        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = 4;
        getContentPane().add(new JSeparator(),constraints);
        constraints.insets = new Insets(0,5,0,0);
        constraints.gridx = 0;
        constraints.gridy = 5;
        getContentPane().add(new JLabel("Select Units:"),constraints);
        constraints.insets = new Insets(0,10,0,0);
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 6;
        getContentPane().add(pixelSelect,constraints);
        constraints.insets = new Insets(0,0,0,0);
        constraints.gridx = 1;
        constraints.gridy = 6;
        getContentPane().add(percentSelect,constraints);
        constraints.insets = new Insets(0,0,0,0);
        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = 7;
        getContentPane().add(new JSeparator(),constraints);
        constraints.gridwidth = 3;
        constraints.insets = new Insets(0,5,0,0);
        constraints.gridx = 0;
        constraints.gridy = 8;
        getContentPane().add(new JLabel("Select Scaling:"),constraints);
        constraints.insets = new Insets(0,10,0,0);
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 9;
        getContentPane().add(horizontalSelect, constraints);
        constraints.insets = new Insets(0,0,0,0);
        constraints.gridx = 1;
        constraints.gridy = 9;
        getContentPane().add(verticalSelect,constraints);
        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = 10;
        getContentPane().add(new JSeparator(),constraints);
        constraints.insets = new Insets(5,5,5,5);
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 11;
        getContentPane().add(new JLabel("Completed: "),constraints);
        constraints.gridx = 1;
        constraints.gridy = 11;
        getContentPane().add(percentComplete, constraints);

        constraints.insets = new Insets(5,5,5,5);
        constraints.gridwidth = 1;
        constraints.gridx = 1;
        constraints.gridy = 12;
        getContentPane().add(okButton,constraints);
        constraints.gridx = 2;
        constraints.gridy = 12;
        getContentPane().add(cancelButton,constraints);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                controller.getParent().getView().setEnabled(true);
            }
        });
    }

    public JTextField getUnitTextField() {
        return unitTextField;
    }

    public JButton getOkButton() {
        return okButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JLabel getUnitLabel() {
        return unitLabel;
    }

    public JLabel getPercentComplete() {
        return percentComplete;
    }

    public JRadioButton getVerticalSelect() {
        return verticalSelect;
    }

    public JRadioButton getHorizontalSelect() {
        return horizontalSelect;
    }

    public JRadioButton getPixelSelect() {
        return pixelSelect;
    }

    public JRadioButton getPercentSelect() {
        return percentSelect;
    }
}
