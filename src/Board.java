import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Board extends JFrame
{
    private List<Card> cards;
    private Card selectedCard;
    private Card c1;
    private Card c2;
    private Timer t;
    private int scoreboard=0;
    private javax.swing.ImageIcon[] img =new javax.swing.ImageIcon[10];
    private JLabel scorelbl = new JLabel("Punteggio: "+scoreboard, SwingConstants.CENTER);
    
    public Board()
    {
        img[0]=new javax.swing.ImageIcon(getClass().getResource("Pics/cane.jpg"));
        img[1]=new javax.swing.ImageIcon(getClass().getResource("Pics/capra.jpg"));
        img[2]=new javax.swing.ImageIcon(getClass().getResource("Pics/cavallo.jpg"));
        img[3]=new javax.swing.ImageIcon(getClass().getResource("Pics/coniglio.jpg"));
        img[4]=new javax.swing.ImageIcon(getClass().getResource("Pics/gatto.jpg"));
        img[5]=new javax.swing.ImageIcon(getClass().getResource("Pics/maiale.jpg"));
        img[6]=new javax.swing.ImageIcon(getClass().getResource("Pics/mucca.jpg"));
        img[7]=new javax.swing.ImageIcon(getClass().getResource("Pics/muflone.jpg"));
        img[8]=new javax.swing.ImageIcon(getClass().getResource("Pics/oca.jpg"));
        img[9]=new javax.swing.ImageIcon(getClass().getResource("Pics/riccio.jpg"));
        
        int pairs=10;
        int i=0;
        List<Card> cardsList = new ArrayList<Card>();
        List<Integer> cardVals = new ArrayList<Integer>();

        for (i=0;i<pairs;i++)
        {
            cardVals.add(i);
            cardVals.add(i);
        }
        
        Collections.shuffle(cardVals);

        for (int val : cardVals)
        {
            Card c = new Card();
            c.setId(val);
            c.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent ae){
                    selectedCard = c;
                    doTurn();
                }
            });
            cardsList.add(c);
        }
        this.cards = cardsList;
        //set up the timer
        t=new Timer(750, new ActionListener()
        {
            public void actionPerformed(ActionEvent ae){
                checkCards();
            }
        });

        t.setRepeats(false);
        
        JButton resetBtn = new JButton("RESET");
        JButton saveBtn = new JButton("SALVA");
        JButton loadBtn = new JButton("CARICA");
        //set up the board itself
        Container pane = getContentPane();
        pane.setLayout(new GridLayout(5, 5));
        for (Card c : cards){
            pane.add(c);
        }
        pane.add(resetBtn);
        resetBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae){
                setVisible(false);
                Board a = new Board();
                a.setBounds(50,50,1200,700);
                a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                a.setVisible(true);
            }
        });
            
        pane.add(saveBtn);
        saveBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae){
                try
                {
                    saveMatch();
                }
                catch(IOException exception)
                {
                    System.out.println("Errore sul file!");
                }
            }
        });
            
        pane.add(loadBtn);
        loadBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae){
                try
                {
                    loadMatch();
                }
                catch(IOException exception)
                {
                    JOptionPane.showMessageDialog(null, "Nessuna partita salvata!");
                }
                catch(ClassNotFoundException exception)
                {
                    JOptionPane.showMessageDialog(null, "Errore sul file!");
                }
            }
        });
        pane.add(scorelbl);

        setTitle("Memory");
    }

    public void doTurn(){
        if (c1 == null && c2 == null){
            c1 = selectedCard;
            //c1.setText(String.valueOf(c1.getId()));
            c1.setIcon(img[c1.getId()]);
            //c1.setIcon(new javax.swing.ImageIcon(getClass().getResource("Pics/cane.jpg")));
        }

        if (c1 != null && c1 != selectedCard && c2 == null){
            c2 = selectedCard;
            //c2.setIcon(new javax.swing.ImageIcon(getClass().getResource("Pics/cane.jpg")));
            //c2.setText(String.valueOf(c2.getId()));
            c2.setIcon(img[c2.getId()]);
            t.start();
        }
    }

    public void checkCards(){
        if (c1.getId() == c2.getId())
        {//match condition
            c1.setEnabled(false);
            c2.setEnabled(false);
            c1.setMatched(true);
            c2.setMatched(true);
            //c1.setIcon(new javax.swing.ImageIcon(getClass().getResource("Pics/cane.jpg")));
            c1.setIcon(img[c1.getId()]);
            //c2.setIcon(new javax.swing.ImageIcon(getClass().getResource("Pics/cane.jpg")));
            c2.setIcon(img[c2.getId()]);
            scoreboard++;
            refresh();
            if (this.isGameWon()){
                JOptionPane.showMessageDialog(this, "Hai vinto!");
                System.exit(0);
            }
        }

        else{
            c1.setText("");
            c1.setIcon(null);
            c2.setText("");
            c2.setIcon(null);
        }
        c1 = null; //reset c1 and c2
        c2 = null;
    }

    public boolean isGameWon(){
        for(Card c: this.cards){
            if (c.getMatched() == false){
                return false;
            }
        }
        return true;
    }
    
    public void saveMatch() throws IOException
    {
        try {
            File file = new File("match.txt");
            FileWriter fw = new FileWriter(file);
            if(scoreboard==0)
            {
                fw.write("0");
                fw.flush();
                fw.close();
            }
            else
            {
                fw.write(Integer.toString(scoreboard));
                fw.flush();
                fw.close();
            }
            JOptionPane.showMessageDialog(this, "Punteggio salvato correttamente!");
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public void loadMatch() throws IOException,ClassNotFoundException
    {
        BufferedReader reader = new BufferedReader(new FileReader("match.txt"));
        String temp = reader.readLine();
        scoreboard = Integer.parseInt(temp);
        JOptionPane.showMessageDialog(this, "Punteggio caricato correttamente da file di testo!");
        refresh();
    }

    public void refresh()
    {
        scorelbl.setText("Punteggio: "+scoreboard);
    }
        

}