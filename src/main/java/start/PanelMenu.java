package start;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.json.simple.JSONObject;

import tools.Utility;

public class PanelMenu extends JPanel {
    private static final long serialVersionUID = 1L;

    public PanelMenu(Application app) {
        super();
        JButton startButton = new JButton("Lancer une partie");
		JButton exitButton = new JButton("Quitter le jeu");
		JButton loadButton = new JButton("Charger une partie");
		Application.designMenuButton(loadButton);
		Application.designMenuButton(startButton);
		Application.designMenuButton(exitButton);

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		setPreferredSize(new Dimension(app.windowWidth(), app.windowHeight()));


		add(Box.createRigidArea(new Dimension(0, 20)));
		add(startButton);
		add(Box.createRigidArea(new Dimension(0, 20)));
		add(loadButton);
		add(Box.createRigidArea(new Dimension(0, 20)));
        add(exitButton);

		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel contentPane = (JPanel) app.getContentPane();
				contentPane.remove(app.panelMenu());
				contentPane.add(app.panelGame());
				contentPane.addKeyListener(app.controller());
				app.pack();
				app.revalidate();
				app.repaint();

				if (!app.isGameSet()) {
                    app.game().initMap();
                    app.setGameSet(true);
				}

				app.timer().start();
			}
		});

		loadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JSONObject saves = Utility.openJSON("./saves.json");
				ArrayList<String> choices = new ArrayList<String>();

				saves.forEach((k,v) -> {
					choices.add(k.toString());
				});

				if(choices.size() == 0) {
					choices.add("Aucune sauvegarde");
				}
				
				String input = (String) JOptionPane.showInputDialog(null, "Choose now...",
				"The Choice of a Lifetime", JOptionPane.QUESTION_MESSAGE, null, // Use
																				// default
																				// icon
				choices.toArray(), // Array of choices
				choices.get(0)); // Initial choice

				if(input == null || input == "Aucune sauvegarde"){
					return;
				}

				app.game().loadJSON((JSONObject) saves.get(input));
				app.setGameSet(true);

				JPanel contentPane = (JPanel) app.getContentPane();
				contentPane.remove(app.panelMenu());
				contentPane.add(app.panelGame());
				contentPane.addKeyListener(app.controller());
				app.pack();
				app.revalidate();
				app.repaint();

				app.timer().start();
			}
		});

		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				app.dispose();
			}
		});
    }
}