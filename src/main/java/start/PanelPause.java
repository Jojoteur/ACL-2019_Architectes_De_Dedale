package start;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

import org.json.simple.JSONObject;

import tools.Utility;

public class PanelPause extends JPanel {
    private static final long serialVersionUID = 1L;

    public PanelPause(Application app) {
        super();
        JButton resumeButton = new JButton("Continuer la partie");
		JButton restartButton = new JButton("Relancer une nouvelle partie");		
		JButton saveButton = new JButton("Sauvegarder la partie");
		JButton exitButton = new JButton("Quitter la partie");
		Application.designMenuButton(saveButton);
		Application.designMenuButton(resumeButton);
		Application.designMenuButton(restartButton);
		Application.designMenuButton(exitButton);

		setLayout(new BorderLayout(0, 0));
        setPreferredSize(new Dimension(app.windowWidth(), app.windowHeight()));
        
		JPanel panelNorth = new JPanel();
		JPanel panelCenter = new JPanel();
		panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.PAGE_AXIS));

		panelNorth.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		

		panelNorth.setPreferredSize(new Dimension(app.windowWidth(), 50));
		panelNorth.add(new JLabel("La partie est en pause !"));

		panelCenter.add(Box.createRigidArea(new Dimension(0, 20)));
		panelCenter.add(resumeButton);
		panelCenter.add(Box.createRigidArea(new Dimension(0, 20)));
		panelCenter.add(restartButton);
		panelCenter.add(Box.createRigidArea(new Dimension(0, 20)));
		panelCenter.add(saveButton);
		panelCenter.add(Box.createRigidArea(new Dimension(0, 20)));
		panelCenter.add(exitButton);
		add(panelNorth, BorderLayout.NORTH);
	    add(panelCenter, BorderLayout.CENTER);

		resumeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel contentPane = (JPanel) app.getContentPane();
				contentPane.remove(app.panelPause());
				contentPane.add(app.panelGame());
				contentPane.addKeyListener(app.controller());
				app.pack();
				app.revalidate();
				app.repaint();
				app.timer().start();
			}
		});

		restartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel contentPane = (JPanel) app.getContentPane();
				contentPane.remove(app.panelPause());
				contentPane.add(app.panelGame());
				contentPane.addKeyListener(app.controller());
				app.pack();
				app.revalidate();
				app.repaint();
				app.game().initMap();
				app.timer().start();
			}
		});

		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog(null, "Sauvegarde", "Nom de la sauvegarde :", JOptionPane.OK_CANCEL_OPTION);
				JSONObject save = app.game().getJSON(name);
				if (name == null || name == "") {
					return;
				}

				JSONObject saves = Utility.openJSON("./saves.json");
				saves.put(name, save);				
				Utility.saveJSON(saves, "./saves.json");
		    }
		});

		exitButton.addActionListener(new ActionListener() {

		    @Override
		    public void actionPerformed(ActionEvent e) {
				app.timer().stop();
				JPanel contentPane = (JPanel) app.getContentPane();
				contentPane.remove(app.panelPause());
				contentPane.add(app.panelMenu());
				app.resetGame();
				app.pack();
		        app.revalidate();
		        app.repaint();
		    }
		});
    }
}