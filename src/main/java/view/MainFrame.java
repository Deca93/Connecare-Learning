package view;

import javax.swing.*;

/**
 * Created by Andrea De Castri on 09/11/2017.
 *
 */
public class MainFrame extends JFrame {

    public MainFrame(String title){
        super();
        this.setTitle(title);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(600, 500);
        this.setVisible(true);
    }

}
