package pastre.flappypastre.java;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import javafx.scene.media.AudioClip;

public class FlappyPastre extends JPanel implements ActionListener, MouseListener, KeyListener {

	private static final long serialVersionUID = 2L;
	public static FlappyPastre flappyBird;
	public final int WIDTH = 800;
	public final int HEIGHT = 800;
	private Renderer renderer;
	private Rectangle bird;
	private ArrayList<Rectangle> columns;
	private int ticks, yMotion, score;
	private boolean gameOver, started;
	private Random rand;
	static AudioClip point;

	int county = 0, highscore, dePointId;

	private String pointSoundPath = "/pastre/flappypastre/res/point.au";
	private String deathSoundPath = "/pastre/flappypastre/res/death.au";
	private String jumpSoundPath = "/pastre/flappypastre/res/jump.AU";
	private String padoPointSoundPath = "/pastre/flappypastre/res/padoPoint.au";
	private String padoDeathSoundPath = "/pastre/flappypastre/res/padoDeath.au";
	private String dePointSoundPath = "/pastre/flappypastre/res/dePoint0.au";
	private String dePointSoundPath1 = "/pastre/flappypastre/res/dePoint1.au";
	private String dePointSoundPath2 = "/pastre/flappypastre/res/dePoint2.au";
	private String deDeathSoundPath = "/pastre/flappypastre/res/deDeath.au";

	boolean isDefaultBird, isLeoBird, isPadoBird, isEsbeltoBird, isRodriBird, isCaiqueBird, isFloresBird, isDuBird,
			isPacheBird, isJuBird;

	ImageIcon birdIcon1 = new ImageIcon(getClass().getResource("/pastre/flappypastre/res/birdIcon.png"));
	ImageIcon leoBirdIcon = new ImageIcon(getClass().getResource("/pastre/flappypastre/res/leoBird.png"));
	ImageIcon pacheBirdIcon = new ImageIcon(getClass().getResource("/pastre/flappypastre/res/pacheBird.png"));
	ImageIcon padoBirdIcon = new ImageIcon(getClass().getResource("/pastre/flappypastre/res/padoBird.png"));
	ImageIcon esbeltoBirdIcon = new ImageIcon(getClass().getResource("/pastre/flappypastre/res/esbeltoBird.png"));
	ImageIcon juBirdIcon = new ImageIcon(getClass().getResource("/pastre/flappypastre/res/juBird.png"));
	ImageIcon caiqueBirdIcon = new ImageIcon(getClass().getResource("/pastre/flappypastre/res/caiqueBird.png"));
	ImageIcon rodriBirdIcon = new ImageIcon(getClass().getResource("/pastre/flappypastre/res/rodriBird.png"));
	ImageIcon floresBirdIcon = new ImageIcon(getClass().getResource("/pastre/flappypastre/res/floresBird.png"));

	ImageIcon tubeBody = new ImageIcon(getClass().getResource("/pastre/flappypastre/res/tubeBody.png"));
	ImageIcon tubeTop = new ImageIcon(getClass().getResource("/pastre/flappypastre/res/tubeTop.png"));
	ImageIcon bg = new ImageIcon(getClass().getResource("/pastre/flappypastre/res/bg.png"));
	ImageIcon floor = new ImageIcon(getClass().getResource("/pastre/flappypastre/res/floor.png"));
	List<Character> inputs = new ArrayList<Character>();

	// Construtor da classe
	public FlappyPastre() {
		JFrame jframe = new JFrame();
		Timer timer = new Timer(20, this);

		renderer = new Renderer();
		rand = new Random();

		jframe.add(renderer);
		jframe.setTitle("Flappy Pastre");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(WIDTH, HEIGHT);
		jframe.addMouseListener(this);
		jframe.addKeyListener(this);
		jframe.setResizable(false);
		jframe.setVisible(true);

		bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
		columns = new ArrayList<Rectangle>();

		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);

		timer.start();
	}

	// Adiciona o tubo
	public void addColumn(boolean start) {
		int space = 300;
		int width = 70;
		int height = getColumnY();

		if (start) {
			columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
		} else {
			columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));

		}
	}

	// Parte grafica da coluna
	public void paintColumn(Graphics g, Rectangle column) {
		Color c = new Color(0, 0, 0, 100);
		g.setColor(c);
	}

	// Pula
	public void jump() {
		// Checa se precisa comecar um jogo novo
		if (gameOver) {
			bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 40, 35);
			columns.clear();
			yMotion = 0;
			score = 0;
			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);
			gameOver = false;
		}

		// Logica para comecar um novo jogo
		if (!started) {
			started = true;
		} else if (!gameOver) {
			if (yMotion > 0) {
				yMotion = 0;
			}

			yMotion -= 12;
		}
		playSound(jumpSoundPath, false);

		// Logica para salvar o high score
		// TODO: Guardar o valor em algum lugar, assim mesmo quando a janela for
		// fechada
		// o highscore continue o mesmo
		if (score > highscore) {
			highscore = score;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int speed = 8;

		ticks++;

		if (started) {
			for (int i = 0; i < columns.size(); i++) {
				Rectangle column = columns.get(i);
				column.x -= speed;

			}

			if (ticks % 2 == 0 && yMotion < 15) {
				yMotion += 2;
			}

			for (int i = 0; i < columns.size(); i++) {
				Rectangle column = columns.get(i);

				if (column.x + column.width < 0) {
					columns.remove(column);

					if (column.y == 0) {
						addColumn(false);
					}
				}
			}

			bird.y += yMotion;

			// Checa se passou do tubo
			for (Rectangle column : columns) {
				if (column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 10
						&& bird.x + bird.width / 2 < column.x + column.width / 2 + 10) {
					score++;
					int i = 0;
					i++;
					if (i != 0 && !gameOver) {
						if (isPadoBird) {
							playSound(padoPointSoundPath, true);
							i = 0;
						} else if (isEsbeltoBird) {
							switch (dePointId) {
							case 0:
								playSound(dePointSoundPath, true);
								dePointId = 1;
								break;
							case 1:
								playSound(dePointSoundPath1, true);
								dePointId = 2;
								break;
							case 2:
								playSound(dePointSoundPath2, true);
								dePointId = 0;
								break;
							}
							System.out.println("dePpoint = " + dePointId);
							i = 0;
						} else {
							playSound(pointSoundPath, true);
							i = 0;
						}
						
					}
					break;
				}
				if (column.intersects(bird)) {
					if (county == 0) {
						if (isPadoBird) {
							playSound(padoDeathSoundPath, false);
							county++;
						} else if (isEsbeltoBird) {
							playSound(deDeathSoundPath, false);
							county++;
						}

						else {
							playSound(deathSoundPath, false);
							county++;
						}
					}
					gameOver = true;

					if (bird.x <= column.x) {
						bird.x = column.x - bird.width;

					} else {
						if (column.y != 0) {
							bird.y = column.y - bird.height;

						} else if (bird.y < column.height) {
							bird.y = column.height;
						}
					}
				}
			}
			if (bird.y > HEIGHT - 120 || bird.y < 0) {
				gameOver = true;
			}
			if (bird.y + yMotion >= HEIGHT - 120) {
				bird.y = HEIGHT - 120 - bird.height;
				gameOver = true;
			}
		}
		renderer.repaint();
	}

	public int getColumnY() {
		return 50 + rand.nextInt(300);
	}

	void setBools(boolean notFalse) {
		List<Boolean> list = new ArrayList<Boolean>();
		list.add(isLeoBird);
		list.add(isPadoBird);
		list.add(isEsbeltoBird);
		list.add(isRodriBird);
		list.add(isCaiqueBird);
		list.add(isFloresBird);
		list.add(isDuBird);
		list.add(isPacheBird);
		list.add(isDefaultBird);
		list.add(isJuBird);
		list.remove(notFalse);
		Collections.fill(list, Boolean.FALSE);
		notFalse = true;
	}

	public void repaint(Graphics g) {
		Color c = new Color(0, 0, 0, 50);
		g.setColor(c);
		bg.paintIcon(this, g, 0, 0);
		floor.paintIcon(this, g, 0, 650);

		if (isLeoBird) {
			setBools(isLeoBird);
			leoBirdIcon.paintIcon(this, g, bird.x, bird.y);
		} else if (isPacheBird) {
			setBools(isPacheBird);
			pacheBirdIcon.paintIcon(this, g, bird.x, bird.y);
		} else if (isPadoBird) {
			setBools(isPadoBird);
			padoBirdIcon.paintIcon(this, g, bird.x, bird.y);
		} else if (isEsbeltoBird) {
			setBools(isEsbeltoBird);
			esbeltoBirdIcon.paintIcon(this, g, bird.x, bird.y);
		} else if (isJuBird) {
			setBools(isJuBird);
			juBirdIcon.paintIcon(this, g, bird.x, bird.y);
		} else if (isCaiqueBird) {
			setBools(isCaiqueBird);
			caiqueBirdIcon.paintIcon(this, g, bird.x, bird.y);
		} else if (isRodriBird) {
			setBools(isRodriBird);
			rodriBirdIcon.paintIcon(this, g, bird.x, bird.y);
		} else if (isFloresBird) {
			setBools(isFloresBird);
			floresBirdIcon.paintIcon(this, g, bird.x, bird.y);
		}

		else {
			setBools(isDefaultBird);
			birdIcon1.paintIcon(this, g, bird.x, bird.y);
		}

		for (Rectangle column : columns) {
			int topX = (int) column.getCenterX() - 35, topY = (int) column.getMinY();
			if (column.getMinY() >= 10) {
				tubeTop.paintIcon(this, g, topX, topY);
				tubeBody.paintIcon(this, g, column.x, column.y + 30);
			} else {
				tubeTop.paintIcon(this, g, (int) column.getCenterX() - 35, (int) column.getMaxY() - 30);
				tubeBody.paintIcon(this, g, column.x, (int) column.getHeight() - 830);

			}
		}

		Color c1 = new Color(255, 255, 255, 200);
		g.setColor(c1);
		String highscoreS = Integer.toString(highscore / 3);
		g.setFont(new Font("comicsans", 10, 50));
		g.drawString("Highscore: " + highscoreS, 10, 150);

		for (Rectangle column : columns) {
			paintColumn(g, column);
		}

		g.setColor(Color.white);
		g.setFont(new Font("Arial", 1, 100));

		if (!started) {
			county = 0;
			g.drawString("Clica ae", 75, HEIGHT / 2 - 50);
		}

		if (gameOver) {
			gameOverScreen(g);
		}

		if (!gameOver && started) {
			county = 0;
			g.drawString(String.valueOf(score / 3), WIDTH / 2 - 25, 100);
		}

	}

	public static void main(String[] args) {
		flappyBird = new FlappyPastre();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		jump();
	}

	private void print(String s) {
		System.out.println(s);
	}

	@SuppressWarnings("unused")
	public boolean equalLists(List<Character> inputs2, List<Character> l) {
		// Check for sizes and nulls
		if ((inputs2.size() != l.size()) || (inputs2 == null && l != null) || (inputs2 != null && l == null)) {
			return false;
		}

		if (inputs2 == null && l == null)
			return true;

		// Sort and compare the two lists
		Collections.sort(inputs2);
		Collections.sort(l);
		return inputs2.equals(l);
	}

	void enumArray(List<Character> l, String s) {
		for (char c : s.toCharArray()) {
			l.add(c);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

		List<Character> leo = new ArrayList<Character>();
		List<Character> pado = new ArrayList<Character>();
		List<Character> rodri = new ArrayList<Character>();
		List<Character> esbelto = new ArrayList<Character>();
		List<Character> ramosLink = new ArrayList<Character>();
		List<Character> caique = new ArrayList<Character>();
		List<Character> flores = new ArrayList<Character>();
		List<Character> pache = new ArrayList<Character>();
		List<Character> ju = new ArrayList<Character>();

		enumArray(leo, "leo");
		enumArray(pado, "pado");
		enumArray(rodri, "rodri");
		enumArray(esbelto, "esbelto");
		enumArray(ramosLink, "ramosLink");
		enumArray(caique, "caique");
		enumArray(flores, "flores");
		enumArray(pache, "pache");
		enumArray(ju, "ju");
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (equalLists(inputs, leo)) {
				isLeoBird = true;
				print("leoBird");
			} else if (equalLists(inputs, pado)) {
				isPadoBird = true;
				print("padoBird");
			} else if (equalLists(inputs, rodri)) {
				isRodriBird = true;
				print("rodriBird");
			} else if (equalLists(inputs, esbelto)) {
				isEsbeltoBird = true;
				print("esbeltoBird");
			} else if (equalLists(inputs, caique)) {
				isCaiqueBird = true;
				print("CaiqueBird");
			} else if (equalLists(inputs, flores)) {
				isFloresBird = true;
				print("floresBird");
			} else if (equalLists(inputs, ramosLink)) {
				isDuBird = true;
				print("ramosLinkBird");
			} else if (equalLists(inputs, pache)) {
				isPacheBird = true;
				print("pacheBird");
			} else if (equalLists(inputs, ju)) {
				isJuBird = true;
				print("juBird");
			}
			inputs.clear();
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			jump();
		} else {
			inputs.add(e.getKeyChar());
		}
		print(inputs.toString());
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	public void playSound(String soundId, boolean needsStop) {
		try {
			// AudioInputStream audioInputStream =
			// AudioSystem.getAudioInputStream(new
			// File(soundId).getAbsoluteFile());
			URL soundFile = getClass().getResource(soundId);
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);

			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();

			if (needsStop && soundId == pointSoundPath) {
				clip.stop();
				clip.start();
			}

		} catch (Exception ex) {
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}

	private void playDeSound() {
	}

	private void gameOverScreen(Graphics g) {
		// todooooooo
	}
}