import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyJframe extends JFrame implements ActionListener {

    public MyJframe() {
        super();

        this.add(new MyJPanel());
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //  this.setResizable(false);
        this.setVisible(true);
        this.pack();

    }

    @Override
    public void actionPerformed(ActionEvent e) {


    }
}
