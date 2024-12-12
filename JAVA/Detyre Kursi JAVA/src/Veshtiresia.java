import javax.swing.*;
import java.awt.*;
public class Veshtiresia {
JFrame frame = new JFrame("Menu");
	public Veshtiresia() {
		
		BoxLayout boxLayout = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS);

		ImageIcon easy_icon = new ImageIcon("photos/easy.png");
		Image easy_image = easy_icon.getImage().getScaledInstance(300, 150, Image.SCALE_SMOOTH);
		ImageIcon lehte = new ImageIcon(easy_image);

		ImageIcon medium_icon = new ImageIcon("photos/medium.png");
		Image medium_image = medium_icon.getImage().getScaledInstance(300, 150, Image.SCALE_SMOOTH);
		ImageIcon mesatar = new ImageIcon(medium_image);

		ImageIcon hard_icon = new ImageIcon("photos/hard.png");
		Image hard_image = hard_icon.getImage().getScaledInstance(300, 150, Image.SCALE_SMOOTH);
		ImageIcon veshtire = new ImageIcon(hard_image);

		ImageIcon back_icon = new ImageIcon("photos/back.png");
		Image back_image = back_icon.getImage().getScaledInstance(300, 150, Image.SCALE_SMOOTH);
		ImageIcon back = new ImageIcon(back_image);

		JButton easy = new JButton();
		easy.setIcon(lehte);
		easy.setBorderPainted(false);
		easy.setContentAreaFilled(false);
		easy.setFocusPainted(false);
		easy.setOpaque(false);

		JButton medium = new JButton();
		medium.setIcon(mesatar);
		medium.setBorderPainted(false);
		medium.setContentAreaFilled(false);
		medium.setFocusPainted(false);
		medium.setOpaque(false);

		JButton hard = new JButton();
		hard.setIcon(veshtire);
		hard.setBorderPainted(false);
		hard.setContentAreaFilled(false);
		hard.setFocusPainted(false);
		hard.setOpaque(false);

		JButton kthehu = new JButton();
		kthehu.setIcon(back);
		kthehu.setBorderPainted(false);
		kthehu.setContentAreaFilled(false);
		kthehu.setFocusPainted(false);
		kthehu.setOpaque(false);

		frame.add(easy);
		frame.add(medium);
		frame.add(hard);
		frame.add(kthehu);

		frame.setLayout(boxLayout);
		frame.setSize(320, 680);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Color.GRAY);
		frame.setResizable(false);
		frame.setVisible(true);

		easy.addActionListener(ae -> {new Nivelet("easy");
		frame.dispose();
		});
		medium.addActionListener(ae -> {new Nivelet("medium");
		frame.dispose();
		});
		hard.addActionListener(ae -> {new Nivelet("hard");
		frame.dispose();
		});
		kthehu.addActionListener(ae -> {
			new MainMenu();
			frame.dispose();
		});

	}
}
