package com.company;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class View {
    private Controller controller;
    private JLabel imageLabel;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setImageLabel(BufferedImage image) {
        imageLabel.setIcon(new ImageIcon(image));
    }

    public void create(int width, int height) {
        JFrame jFrame = new JFrame("MineSweeper");
        jFrame.setUndecorated(true);
        jFrame.setLayout(null);
        jFrame.setSize(width, height);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);


        imageLabel = new JLabel();
        imageLabel.setBounds(0, 0, width, height);
        jFrame.add(imageLabel);

        jFrame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                controller.handleMouseClick(e.getX() , e.getY(), e.getButton());
            }
        });



        jFrame.setVisible(true);
    }
}
