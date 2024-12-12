import javax.swing.*;
import java.awt.*;

public class Nivelet {

    public Nivelet(String veshtiresia) {

        final JFrame frame = new JFrame("Nivelet");
        BoxLayout boxLayout = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS);

        ImageIcon l1_icon = new ImageIcon("photos/level1.png");
        Image l1_image = l1_icon.getImage().getScaledInstance(300, 150, Image.SCALE_SMOOTH);
        ImageIcon level1 = new ImageIcon(l1_image);

        ImageIcon l2_icon = new ImageIcon("photos/level2.png");
        Image l2_image = l2_icon.getImage().getScaledInstance(300, 150, Image.SCALE_SMOOTH);
        ImageIcon level2 = new ImageIcon(l2_image);

        ImageIcon l3_icon = new ImageIcon("photos/level3.png");
        Image l3_image = l3_icon.getImage().getScaledInstance(300, 150, Image.SCALE_SMOOTH);
        ImageIcon level3 = new ImageIcon(l3_image);

        ImageIcon back_icon = new ImageIcon("photos/back.png");
        Image back_image = back_icon.getImage().getScaledInstance(300, 150, Image.SCALE_SMOOTH);
        ImageIcon back = new ImageIcon(back_image);

        JButton l1 = new JButton();
        l1.setIcon(level1);
        l1.setBorderPainted(false);
        l1.setContentAreaFilled(false);
        l1.setFocusPainted(false);
        l1.setOpaque(false);

        JButton l2 = new JButton();
        l2.setIcon(level2);
        l2.setBorderPainted(false);
        l2.setContentAreaFilled(false);
        l2.setFocusPainted(false);
        l2.setOpaque(false);

        JButton l3 = new JButton();
        l3.setIcon(level3);
        l3.setBorderPainted(false);
        l3.setContentAreaFilled(false);
        l3.setFocusPainted(false);
        l3.setOpaque(false);

        JButton kthehu = new JButton();
        kthehu.setIcon(back);
        kthehu.setBorderPainted(false);
        kthehu.setContentAreaFilled(false);
        kthehu.setFocusPainted(false);
        kthehu.setOpaque(false);

        frame.add(l1);
        frame.add(l2);
        frame.add(l3);
        frame.add(kthehu);

        frame.setLayout(boxLayout);
        frame.setSize(320, 680);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.GRAY);
        frame.setResizable(false);
        frame.setVisible(true);


            l1.addActionListener(ae -> {
               switch(veshtiresia){
                   case "easy" -> new KontrolleriLojes(9);
                   case "medium" -> new KontrolleriLojes(15);
                   case "hard" -> new KontrolleriLojes(21);
               }
                frame.dispose();
            });

            l2.addActionListener(ae -> {
                switch(veshtiresia){
                    case "easy" -> new KontrolleriLojes(11);
                    case "medium" -> new KontrolleriLojes(17);
                    case "hard" -> new KontrolleriLojes(23);
                }
                frame.dispose();
            });

            l3.addActionListener(ae -> {
                switch(veshtiresia){
                    case "easy" -> new KontrolleriLojes(13);
                    case "medium" -> new KontrolleriLojes(19);
                    case "hard" -> new KontrolleriLojes(25);
                }
                frame.dispose();
            });

            kthehu.addActionListener(ae -> {
                new Veshtiresia();
                frame.dispose();
             });

        frame.revalidate();
    }
}




