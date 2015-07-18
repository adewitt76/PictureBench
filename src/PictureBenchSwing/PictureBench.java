package PictureBenchSwing;

import Utilities.ListenerEvent;
import Utilities.MVC_Listener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 *
 * @author Aaron DeWitt
 * @version 0.1 9-May-2015
 */
public class PictureBench extends JFrame implements MVC_Listener{

    private WorkingImage model;
    private PictureBenchController controller;
    private ImagePanel imagePanel;
    private JMenuBar menuBar;
    private JMenuItem fileSaveItem;
    private JMenuItem fileSaveAsItem;
    private JMenuItem editUndoMenuItem;
    private JMenuItem editRedoMenuItem;
    private JMenuItem viewZoomIn;
    private JMenuItem viewZoomOut;
    private JMenuItem viewNormalZoom;
    private JMenuItem toolsMagicCropItem;
    private JScrollPane scrollPane;

    public PictureBench(){
        super("Picture Bench");

        model = new WorkingImage();
        imagePanel = new ImagePanel();
        controller = new PictureBenchController(this, model, imagePanel);

        model.registerListener(this);

        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        buildMenuBar();

        scrollPane = new JScrollPane(imagePanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        getContentPane().add(menuBar, BorderLayout.PAGE_START);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        pack();
        setSize(1024, 768);
        setVisible(true);
    }

    private void buildMenuBar(){
        menuBar = new JMenuBar();
        // File Menu
        JMenu menu = new JMenu("File");
        menu.addMenuListener(controller);
        menu.setMnemonic(KeyEvent.VK_F);
            JMenuItem menuItem = new JMenuItem("Open...", KeyEvent.VK_O);
            menuItem.addActionListener(controller);
        menu.add(menuItem);
            fileSaveItem = new JMenuItem("Save", KeyEvent.VK_S);
            fileSaveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
            fileSaveItem.addActionListener(controller);
            fileSaveItem.setEnabled(false);
        menu.add(fileSaveItem);
            fileSaveAsItem = new JMenuItem("Save as...");
            fileSaveAsItem.addActionListener(controller);
            fileSaveAsItem.setEnabled(false);
        menu.add(fileSaveAsItem);
        menu.add(new JSeparator());
            menuItem = new JMenuItem("Exit", KeyEvent.VK_X);
            menuItem.addActionListener(controller);
        menu.add(menuItem);
        menuBar.add(menu);
        // Edit Menu
        menu = new JMenu("Edit");
        menu.addMenuListener(controller);
        menu.setMnemonic(KeyEvent.VK_E);
            editUndoMenuItem = new JMenuItem("Undo");
            editUndoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
            editUndoMenuItem.addActionListener(controller);
            editUndoMenuItem.setEnabled(false);
        menu.add(editUndoMenuItem);
            editRedoMenuItem = new JMenuItem("Redo");
            editRedoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
            editRedoMenuItem.addActionListener(controller);
            editRedoMenuItem.setEnabled(false);
        menu.add(editRedoMenuItem);
        menuBar.add(menu);
        // View Menu
        menu = new JMenu("View");
        menu.addMenuListener(controller);
        menu.setMnemonic(KeyEvent.VK_V);
            viewZoomIn = new JMenuItem("Zoom in");
            viewZoomIn.setAccelerator(KeyStroke.getKeyStroke('=', ActionEvent.CTRL_MASK));
            viewZoomIn.addActionListener(controller);
            viewZoomIn.setEnabled(false);
        menu.add(viewZoomIn);
            viewZoomOut = new JMenuItem("Zoom out");
            viewZoomOut.setAccelerator(KeyStroke.getKeyStroke('-', ActionEvent.CTRL_MASK));
            viewZoomOut.addActionListener(controller);
            viewZoomOut.setEnabled(false);
        menu.add(viewZoomOut);
            viewNormalZoom = new JMenuItem("Normal zoom");
            viewNormalZoom.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, ActionEvent.CTRL_MASK));
            viewNormalZoom.addActionListener(controller);
            viewNormalZoom.setEnabled(false);
        menu.add(viewNormalZoom);
        menuBar.add(menu);
        // Tools Menu
        menu = new JMenu("Tools");
        menu.addMenuListener(controller);
        menu.setMnemonic(KeyEvent.VK_T);
            toolsMagicCropItem = new JMenuItem("Magic Crop...");
            toolsMagicCropItem.addActionListener(controller);
            toolsMagicCropItem.setEnabled(false);
        menu.add(toolsMagicCropItem);
        menuBar.add(menu);
        // Help Menu
        menu = new JMenu("Help");
        menu.addMenuListener(controller);
        menu.setMnemonic(KeyEvent.VK_H);
            menuItem = new JMenuItem("About...");
            menuItem.addActionListener(controller);
        menu.add(menuItem);
        menuBar.add(menu);
    }

    @Override
    public void listenerEvent(ListenerEvent event) {
        switch (event){
            case MODEL_CHANGE:
                imagePanel.setImage(model.getImage());
                update();
                break;
        }
    }

    public void update(){
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        if(model.hasRedoInStack()) editRedoMenuItem.setEnabled(true);
        else editRedoMenuItem.setEnabled(false);
        if(model.hasUndoInStack()) editUndoMenuItem.setEnabled(true);
        else editUndoMenuItem.setEnabled(false);
        repaint();
    }

    public void enableMenuItems(String[] list){
        for(String s: list){
            if(s.equals("File.Save")) fileSaveItem.setEnabled(true);
            if(s.equals("File.SaveAs")) fileSaveAsItem.setEnabled(true);
            if(s.equals("Edit.Undo")) editUndoMenuItem.setEnabled(true);
            if(s.equals("Edit.Redo")) editRedoMenuItem.setEnabled(true);
            if(s.equals("View.ZoomIn")) viewZoomIn.setEnabled(true);
            if(s.equals("View.ZoomOut")) viewZoomOut.setEnabled(true);
            if(s.equals("View.NormalZoom")) viewNormalZoom.setEnabled(true);
            if(s.equals("Tools.MagicCrop")) toolsMagicCropItem.setEnabled(true);
        }
    }

    public void disableMenuItems(String[] list){
        for(String s: list){
            if(s.equals("File.Save")) fileSaveItem.setEnabled(false);
            if(s.equals("File.SaveAs")) fileSaveAsItem.setEnabled(false);
            if(s.equals("Edit.Undo")) editUndoMenuItem.setEnabled(false);
            if(s.equals("Edit.Redo")) editRedoMenuItem.setEnabled(false);
            if(s.equals("View.ZoomIn")) viewZoomIn.setEnabled(true);
            if(s.equals("View.ZoomOut")) viewZoomOut.setEnabled(true);
            if(s.equals("View.NormalZoom")) viewNormalZoom.setEnabled(true);
            if(s.equals("Tool.MagicCrop")) toolsMagicCropItem.setEnabled(false);
        }
    }

    public static void main(String[] args) {
        new PictureBench();
    }
}
