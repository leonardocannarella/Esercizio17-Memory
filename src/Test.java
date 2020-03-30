import java.awt.*;
import javax.swing.*;
import static javax.swing.WindowConstants.*;


public class Test{
    public static void main(String[] args){
        Board b = new Board();
        b.setBounds(50,50,1200,700);
        b.setDefaultCloseOperation(EXIT_ON_CLOSE);
        b.setVisible(true);
    }   
}