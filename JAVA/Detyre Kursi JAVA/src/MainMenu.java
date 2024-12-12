import javax.swing.*;
import java.awt.*;

public class MainMenu {
	JFrame frame = new JFrame("Labirint");
	public MainMenu(){
		
		BoxLayout boxLayout = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS); 

		ImageIcon icon = new ImageIcon("photos/newgame.png");
		Image image = icon.getImage().getScaledInstance(300, 150, Image.SCALE_SMOOTH);
		ImageIcon newgame = new ImageIcon(image);

		ImageIcon icon2 = new ImageIcon("photos/dalje.png");
		Image image2 = icon2.getImage().getScaledInstance(300, 150, Image.SCALE_SMOOTH);
		ImageIcon exit = new ImageIcon(image2);

		ImageIcon icon3 = new ImageIcon("photos/instruksione.png");
		Image image3 = icon3.getImage().getScaledInstance(300, 150, Image.SCALE_SMOOTH);
		ImageIcon inst = new ImageIcon(image3);

		ImageIcon icon4 = new ImageIcon("photos/load.png");
		Image image4 = icon4.getImage().getScaledInstance(300, 150, Image.SCALE_SMOOTH);
		ImageIcon load = new ImageIcon(image4);

		JButton newGame = new JButton();
		newGame.setIcon(newgame);
		newGame.setBorderPainted(false);
		newGame.setContentAreaFilled(false);
		newGame.setFocusPainted(false);
		newGame.setOpaque(false);

		JButton loadGame = new JButton();
		loadGame.setIcon(load);
		loadGame.setBorderPainted(false);
		loadGame.setContentAreaFilled(false);
		loadGame.setFocusPainted(false);
		loadGame.setOpaque(false);
		
		JButton instruction = new JButton();
		instruction.setIcon(inst);
		instruction.setBorderPainted(false);
		instruction.setContentAreaFilled(false);
		instruction.setFocusPainted(false);
		instruction.setOpaque(false);

		JButton exitButton = new JButton();
		exitButton.setIcon(exit);
		exitButton.setBorderPainted(false);
		exitButton.setContentAreaFilled(false);
		exitButton.setFocusPainted(false);
		exitButton.setOpaque(false);

		frame.add(newGame);
		frame.add(loadGame);
		frame.add(instruction);
		frame.add(exitButton);
		frame.getContentPane().setBackground(Color.GRAY);
		frame.setLayout(boxLayout);
		frame.setSize(320,680);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setVisible(true);		

		newGame.addActionListener(ae -> {
            new Veshtiresia();
			frame.dispose();
        });

		loadGame.addActionListener(ae->{
			new KontrolleriLojes();
			frame.dispose();});

		exitButton.addActionListener(ae -> System.exit(0));

		instruction.addActionListener(ae -> {
			frame.dispose();

			JFrame instructionFrame = new JFrame("Instruksionet");
			instructionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


			ImageIcon instructionImage = new ImageIcon("photos/udhezime.png");
			Image instruction_icon = instructionImage.getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH);
			instructionImage = new ImageIcon(instruction_icon);

			JLabel imageLabel = new JLabel(instructionImage);
			instructionFrame.add(imageLabel);

			JButton kthehu = new JButton("KTHEHU");
			kthehu.setForeground(Color.WHITE);
			kthehu.setBackground(Color.GREEN);
			kthehu.setFont(new Font("Verdana", Font.BOLD, 20));
			kthehu.addActionListener(e -> {
				instructionFrame.dispose();
				new MainMenu();
			});

			JPanel buttonPanel = new JPanel();
			buttonPanel.add(kthehu);
			instructionFrame.add(buttonPanel, BorderLayout.SOUTH);

			instructionFrame.setSize(500, 500);
			instructionFrame.setLocationRelativeTo(null);
			instructionFrame.setVisible(true);

		});
	}
}
