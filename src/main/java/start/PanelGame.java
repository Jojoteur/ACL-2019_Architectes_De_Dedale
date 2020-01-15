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
import javax.swing.JPanel;

public class PanelGame extends JPanel {
    private static final long serialVersionUID = 1L;

    public PanelGame(Application app) {
        super();
		setLayout(new BorderLayout(0, 0));
		setPreferredSize(new Dimension(app.windowWidth(), app.windowHeight()));
		
		JPanel panelCenter = new JPanel();
		panelCenter.add(app.engine().getGui().getPanel());

		JPanel panelNorth = new JPanel();
		panelNorth.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		panelNorth.setPreferredSize(new Dimension(app.windowWidth(), 50));
		panelNorth.setLayout(new BoxLayout(panelNorth, BoxLayout.LINE_AXIS));

		JButton pauseButton = new JButton("Pause");

		panelNorth.add(Box.createHorizontalStrut(10));
		panelNorth.add(pauseButton);
		
		add(panelNorth, BorderLayout.NORTH);
		add(panelCenter, BorderLayout.CENTER);

		pauseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				app.timer().stop();
                JPanel contentPane = (JPanel) app.getContentPane();
                contentPane.removeKeyListener(app.controller());
				contentPane.remove(app.panelGame());
				contentPane.add(app.panelPause());
				
				app.controller().reset();
				app.pack();
				app.revalidate();
				app.repaint();
			}
		});
    }
}