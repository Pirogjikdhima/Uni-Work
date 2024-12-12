import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KontrolleriLojes {

    private JFrame frame;
    private JFrame frame2;
    private JPanel mainPanel;
    private JPanel topButtonPanel;
    private JPanel bottomButtonPanel;
    private String medal;
    private JLabel treasureCountLabel;
    private Labirint test;
    private Lojtar lojtar;

    public KontrolleriLojes(){
        test = new Labirint();
        lojtar = new Lojtar(test, this , "Ngarkimi");
        frame = new JFrame("Labirint");
        frame.getContentPane().setBackground(Color.GRAY);
        mainPanel = new JPanel(new BorderLayout());
        topButtonPanel = new JPanel(new GridLayout(1, 5, 10, 0));
        bottomButtonPanel = new JPanel(new FlowLayout());
        initFrame();
        thesaret(lojtar.thesare);
    }
    public KontrolleriLojes(int level){
        test = new Labirint(level);
        lojtar = new Lojtar(test, this);
        frame = new JFrame("Labirint");
        mainPanel = new JPanel(new BorderLayout());
        topButtonPanel = new JPanel(new GridLayout(1, 5, 10, 0));
        bottomButtonPanel = new JPanel(new FlowLayout());
        initFrame();
        thesaret(lojtar.thesare);
    }

    private void initFrame()
    {
        mainPanel.add(this.test, BorderLayout.CENTER);
        mainPanel.setFocusable(true);


        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(topButtonPanel, BorderLayout.NORTH);
        frame.add(bottomButtonPanel, BorderLayout.SOUTH);
        frame.setBackground(Color.RED);


        ImageIcon left_icon = new ImageIcon("photos/left.png");
        Image left_image = left_icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        ImageIcon left = new ImageIcon(left_image);

        ImageIcon right_icon = new ImageIcon("photos/right.png");
        Image right_image = right_icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        ImageIcon right = new ImageIcon(right_image);

        ImageIcon up_icon = new ImageIcon("photos/up.png");
        Image up_image = up_icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        ImageIcon up = new ImageIcon(up_image);

        ImageIcon down_icon = new ImageIcon("photos/down.png");
        Image down_image = down_icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        ImageIcon down = new ImageIcon(down_image);

        JPanel drejtimet = new JPanel(new GridBagLayout());
        topButtonPanel.add(drejtimet, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;

        JButton lart = new JButton();
        lart.setIcon(up);
        customizeButton(lart);
        lart.setFocusable(false);
        drejtimet.add(lart, gbc);
        lart.addActionListener(ae -> luajRadhen(test, lojtar, 'l'));

        gbc.gridx = 0;
        gbc.gridy = 1;

        JButton majtas = new JButton();
        majtas.setIcon(left);
        customizeButton(majtas);
        majtas.setFocusable(false);
        drejtimet.add(majtas, gbc);
        majtas.addActionListener(ae -> luajRadhen(test, lojtar, 'm'));

        gbc.gridx = 1;
        gbc.gridy = 1;

        JButton poshte = new JButton();
        poshte.setIcon(down);
        customizeButton(poshte);
        poshte.setFocusable(false);
        drejtimet.add(poshte, gbc);
        poshte.addActionListener(ae -> luajRadhen(test, lojtar, 'p'));

        gbc.gridx = 2;
        gbc.gridy = 1;

        JButton djathtas = new JButton();
        djathtas.setIcon(right);
        customizeButton(djathtas);
        djathtas.setFocusable(false);
        drejtimet.add(djathtas, gbc);
        djathtas.addActionListener(ae -> luajRadhen(test, lojtar, 'd'));

        JButton menu = new JButton("Loje e re");
        menu.setFocusable(false);
        menu.setBackground(Color.green);
        bottomButtonPanel.add(menu);
        menu.addActionListener(ae -> {
            frame.dispose();
            new MainMenu();
        });
        JButton save = new JButton("Ruaj Lojen");
        save.setFocusable(false);
        save.setBackground(Color.lightGray);
        bottomButtonPanel.add(save);
        save.addActionListener(ae -> {
            test.ruajLojen();
            lojtar.ruajThesaret();
            test.ruajthesarecount();
        });

        JButton dalje = new JButton("Dalje");
        dalje.setFocusable(false);
        dalje.setBackground(Color.red);
        bottomButtonPanel.add(dalje);
        dalje.addActionListener(ae -> System.exit(0));


        mainPanel.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (e.getKeyChar() == 'a' || keyCode == KeyEvent.VK_LEFT) luajRadhen(test, lojtar, 'm');
                if (e.getKeyChar() == 'd' || keyCode == KeyEvent.VK_RIGHT) luajRadhen(test, lojtar, 'd');
                if (e.getKeyChar() == 'w' || keyCode == KeyEvent.VK_UP) luajRadhen(test, lojtar, 'l');
                if (e.getKeyChar() == 's' || keyCode == KeyEvent.VK_DOWN) luajRadhen(test, lojtar, 'p');
            }
        });

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setVisible(true);
    }

    private void customizeButton(JButton button) {
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
    }

    public void rregulloNrThesareve(int count) {
        treasureCountLabel.setText("Thesare: "+ count);
    }
    public void thesaret(int thesaret){
        JPanel treasurePanel = new JPanel(new BorderLayout());
        treasureCountLabel = new JLabel("Thesare: " + thesaret, JLabel.CENTER);
        treasureCountLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        treasurePanel.add(treasureCountLabel, BorderLayout.SOUTH);
        mainPanel.add(treasurePanel, BorderLayout.SOUTH);
    }
    public KontrolleriLojes(int thesare, double thesareRatio, boolean epm) {
        perfundoLojen(thesare, thesareRatio, epm);
        frame.dispose();
    }
    
    private void luajRadhen(Labirint lab, Lojtar loj, char ch) {
    	switch(ch) {
    	case 'm', 'd', 'p', 'l': loj.leviz(lab, ch); break;
        }
    }
    private void perfundoLojen(int thesare, double thesareRatio, boolean epm) {
   	SwingUtilities.invokeLater(() -> {
                if (frame!=null){
                    frame.dispose();
                }



            frame2 = new JFrame();

            if(!epm) {
            if (thesareRatio >= 0.7 && thesareRatio < 0.9) {
                medal = "Bronzit";
                frame2.getContentPane().setBackground(Color.ORANGE);
            } else if (thesareRatio >= 0.9 && thesareRatio < 1) {
                medal = "Argjente";
                frame2.getContentPane().setBackground(Color.LIGHT_GRAY);
            } else if (thesareRatio == 1) {
                medal = "Arte";
                frame2.getContentPane().setBackground(Color.YELLOW);
            }
            }else {
            	frame2.getContentPane().setBackground(Color.PINK);
            }

            JPanel medalPanel = new JPanel(new BorderLayout());

            if (!epm && thesareRatio >= 0.7 && thesareRatio <= 1) {
                JLabel textLabelFitore = new JLabel("<html>Urime!<br>Ju mblodhet " + thesare + " thesar(e)!<br>Perqindja e thesareve: " + String.format("%.2f", thesareRatio * 100) + "% </html>", JLabel.CENTER);
                textLabelFitore.setFont(new Font("Verdana", Font.BOLD, 32));
                JLabel textLabelMedalje = new JLabel("<html>Ju fituat medaljen e " + medal + ".</html>", JLabel.CENTER);
                textLabelMedalje.setFont(new Font("Verdana", Font.BOLD, 32));

                medalPanel.add(textLabelFitore, BorderLayout.CENTER);
                medalPanel.add(textLabelMedalje, BorderLayout.SOUTH);
                medalPanel.setBackground(frame2.getContentPane().getBackground());
            } else if(!epm && thesareRatio < 0.7) {
                JLabel textLabelHumbje = new JLabel("<html>Ju humbet!<br>Perqindja e thesareve: " + String.format("%.2f", thesareRatio * 100) + "%</html>", JLabel.CENTER);
                textLabelHumbje.setFont(new Font("Verdana", Font.BOLD, 32));
                medalPanel.add(textLabelHumbje, BorderLayout.CENTER);
                medalPanel.setBackground(frame2.getContentPane().getBackground());
            } else if(epm) {
            	JLabel textLabelHumbje = new JLabel("<html>Ju humbet,preket mur!<br>Perqindja e thesareve: " + String.format("%.2f", thesareRatio * 100) + "%</html>", JLabel.CENTER);
                textLabelHumbje.setFont(new Font("Verdana", Font.BOLD, 32));
                medalPanel.add(textLabelHumbje, BorderLayout.CENTER);
                medalPanel.setBackground(frame2.getContentPane().getBackground());
            }

            JButton back = new JButton("Kthehuni");
            back.addActionListener(ae -> {
                frame2.dispose();
                new MainMenu();
            });
            
            JButton dalje = new JButton("Dalje");
            dalje.setBackground(Color.red);
            dalje.addActionListener(ae -> System.exit(0));

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(back);
            buttonPanel.add(dalje);
            buttonPanel.setBackground(frame2.getContentPane().getBackground());

            frame2.add(medalPanel, BorderLayout.CENTER);
            frame2.add(buttonPanel, BorderLayout.SOUTH);

            frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame2.setSize(500, 500);
            frame2.setLocationRelativeTo(null);
            frame2.setVisible(true);
        });
}
}

    

