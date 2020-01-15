package start;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.Image;
import java.awt.FlowLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tools.Utility;

public class PanelEndGame extends JPanel {
    private static final long serialVersionUID = 1L;
    private Image winImage, loseImage;
	private JLabel winOrLoseLabel;
    public PanelEndGame(Application app) {
        super();

        try {
            winImage = Utility.resizeImage("win.png", 600, 180);
            loseImage = Utility.resizeImage("lose.png", 600, 180);
        } catch (IOException e1) {
            System.out.println("Win or Lose images are missing.");
        }

        JButton menuButton = new JButton("Menu principal");
		Application.designMenuButton(menuButton);
		
		setPreferredSize(new Dimension(app.windowWidth(), app.windowHeight()));
		setLayout(new BorderLayout(0, 0));
		
		JPanel panelNorth = new JPanel();
		JPanel panelSouth = new JPanel();
		panelNorth.setLayout(new FlowLayout());
		panelSouth.setLayout(new BoxLayout(panelSouth, BoxLayout.PAGE_AXIS));
		panelNorth.setPreferredSize(new Dimension(app.windowWidth(), 300));
		panelSouth.setPreferredSize(new Dimension(app.windowWidth(), 200));
		
		winOrLoseLabel = new JLabel();

		panelNorth.add(winOrLoseLabel);
		panelSouth.add(Box.createRigidArea(new Dimension(0, 40)));
		panelSouth.add(menuButton);
		add(panelNorth, BorderLayout.NORTH);
		add(panelSouth, BorderLayout.SOUTH);
		menuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel contentPane = (JPanel) app.getContentPane();
				contentPane.remove(app.panelEndGame());
				contentPane.add(app.panelMenu());
				app.pack();
				app.revalidate();
				app.repaint();
			}
		});
	}
	
	public void refreshEndImage(boolean victory) {
		if(victory) {
			winOrLoseLabel.setIcon(new ImageIcon(winImage));
		}
		else {
			winOrLoseLabel.setIcon(new ImageIcon(loseImage));
		}
	}
}