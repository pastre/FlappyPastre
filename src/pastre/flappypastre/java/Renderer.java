package pastre.flappypastre.java;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class Renderer extends JPanel
{

	private static final long serialVersionUID = 1L;
	@Override
	protected void paintComponent(Graphics g)
	{
		Image bg = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/pastre/flappypastre/res/bg.png"));
		
		super.paintComponent(g);
		g.drawImage(bg, 0, 0, null);
		FlappyPastre.flappyBird.repaint(g);
		
	}
	
}
