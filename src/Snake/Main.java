package Snake;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import Snake.Apples.Apple;
import Snake.Player.SnakeTile;

public class Main extends Canvas implements Runnable{
	
	//frame-infos
	private JFrame frame;
	public static String version = "2.3";
	public static final int WIDTH = 1024;
	public static final int HEIGHT = WIDTH / 12 * 9;
	private static final long serialVersionUID = 4433339372853233415L;
	
	//score
	public static int score = 0;
	private int lastScore = score;
	private double scoreZoom = 0;
	
	//thread
	public Thread thread;
	public boolean running = false;
	
	//handler
	private Text text;
	private Player player;
	private Apples apples;
	private Menu menu;
	
	//inputs
	private KeyInput keyinput;
	
	public static enum GameStates {
		Menu(),
		Play(),
		GameO(),
	};
	public static GameStates state = GameStates.Menu;
	
	public Main(){
		loadFrame("Snake " + version);
		start();
	}

	public synchronized void start(){
		player = new Player(35, 35);
		apples = new Apples();
		text = new Text();
		keyinput = new KeyInput(player, apples, text);
		menu = new Menu();
		this.addKeyListener(keyinput);
		
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	public synchronized void stop(){
		try{
			thread.join();
			running = false;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void run(){
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		
		while(running){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1){
				tick();
				delta--;
			}
			if(running)
				render();
			frames++;
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				frame.setTitle("Snake " + version + ", fps: "+frames);
				frames = 0;
			}
		}
		stop();
	}
	
	private void tick(){
		if(state == GameStates.Menu){ menu.tick(); }
		
		if(state == GameStates.Play){
			player.tick();
			apples.tick();
			text.tick();
			
			// apfel - apfel collision
			for(int i1 = 0; i1 < apples.apples.size(); i1++) {
				Apple a1 = apples.apples.get(i1);
				for(int i2 = 0; i2 < apples.apples.size(); i2++) {
					Apple a2 = apples.apples.get(i2);
					if(a1 != a2) {
						double diffx = a1.getx() - a2.getx();
						double diffy = a1.gety() - a2.gety();
						double dist = Math.sqrt(diffx * diffx + diffy * diffy);
						if(dist < a1.getr()+a2.getr()) {
							apples.apples.remove(a2);
						}
					}
				}
			}
			
			// player - apfel collision
			for(int i = 0; i < player.tiles.size(); i++) {
				Player.SnakeTile s = player.tiles.get(i);
				for(int j = 0; j < apples.apples.size(); j++) {
					Apples.Apple a = apples.apples.get(j);
					double diffx = s.getx() - a.getx();
					double diffy = s.gety() - a.gety();
					double dist = Math.sqrt(diffx * diffx + diffy * diffy);
					
					if(s == player.tiles.get(0)) {
						if(dist < a.getr() + s.getr()) {
							a.remove(player, text);
							player.warteschlange+=3;
							player.normalSpeed+=0.125;
							score += rand(7, 15);
							break;
						}
					}else{
						int loop = 0;
						while(dist < a.getr() + s.getr() && loop < 20) {
							loop++;
							s.setX(s.getx() - (int)((-1/dist) * diffx * (dist/5)));
							s.setY(s.gety() - (int)((-1/dist) * diffy * (dist/5)));
							diffx = s.getx() - a.getx();
							diffy = s.gety() - a.gety();
							dist = Math.sqrt(diffx * diffx + diffy * diffy);
						}
					}
				}
			}
			
			// player - player collision
			for(int i1 = 0; i1 < player.tiles.size(); i1++) {
				int toleranz = 5;
				SnakeTile tile1 = player.tiles.get(i1);
				for(int i2 = toleranz; i2 < player.tiles.size(); i2++) {
					SnakeTile tile2 = player.tiles.get(i2);
					if(tile1 != tile2) {
						double diffX = tile1.getx() - tile2.getx();
						double diffY = tile1.gety() - tile2.gety();
						double dist = Math.sqrt(diffX*diffX + diffY*diffY);
						if(tile1 == player.tiles.get(0)) {
							if(i2 > toleranz) {
								if(dist < tile1.getr() + tile2.getr()) {
									state = GameStates.GameO;
								}
							}
						}else{
							if(i2 < i1-(toleranz*3) || i2 > i1+(toleranz*3)) {
								if(dist < tile2.getr() + tile1.getr()) {
									tile1.setX(tile1.getx() - (int)((-1/dist) * diffX * (dist/5)));
									tile1.setY(tile1.gety() - (int)((-1/dist) * diffY * (dist/5)));
								}
							}
						}
					}
				}
			}
			
			//player - wall collision
			for(int i = 0; i < player.tiles.size(); i++){
				Player.SnakeTile s = player.tiles.get(i);
				if(s.getx()+s.getr() > WIDTH || s.getx()-s.getr() < 0 || s.gety()+s.getr()*4 > HEIGHT || s.gety()-s.getr() < 0){
					state = GameStates.GameO;
				}
			}
			
			// score zoom effect
			if(scoreZoom > 0) {
				scoreZoom -= scoreZoom/2;
			}
			if(score != lastScore) {
				lastScore = score;
				scoreZoom += 10;
			}
		}
		
		if(state == GameStates.GameO) {
			menu.tick();
		}
	}
	
	private void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null){
			this.createBufferStrategy(2);
			return;
		}
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		g.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(
			RenderingHints.KEY_TEXT_ANTIALIASING,
			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		Rectangle r = new Rectangle(0, 0, WIDTH, HEIGHT);
		RadialGradientPaint rgp=new RadialGradientPaint(new Point2D.Double(Main.WIDTH/2,Main.HEIGHT/2),Main.HEIGHT-200,new float[]{0.0f,1.0f},new Color[]{Color.gray,Color.gray.darker()});
		g.setPaint(rgp);
  		g.fill(r);
		
		if(state == GameStates.Play){
			player.render(g);
			apples.render(g);
			text.render(g);
			
			g.setColor(Color.white);
			g.setFont(font(18));
			String text = "Score: ";
			g.drawString(text, 25, 25);
			String textScore = "" + score;
			int xpos = 25+getFWidth(text, g)+5;
			g.setFont(font(18+scoreZoom));
			g.drawString(textScore, xpos, 25);
		}
		
		if(state == GameStates.Menu){ menu.render(g); }
		
		if(state == GameStates.GameO) {
			player.render(g);
			apples.render(g);
			
			g.setColor(Color.red.darker());
			g.setFont(font((int) (70+menu.fontzoom+Main.HEIGHT/20)));
			g.drawString("Game Over!", Main.WIDTH/2-getFWidth("Game Over!", g)/2, 152+HEIGHT/7);
			g.setColor(Color.red);
			g.drawString("Game Over!", Main.WIDTH/2-getFWidth("Game Over!", g)/2, 150+HEIGHT/7);
			
			g.setColor(Color.white.darker());
			g.setFont(font(20));
			String text = "Score: "+score;
			g.drawString(text, ((WIDTH/2)-getFWidth(text, g)/2), Main.HEIGHT/2+getFHeight("Press Enter", g)/2-10);
			g.setColor(Color.white);
			g.drawString(text, ((WIDTH/2)-getFWidth(text, g)/2), Main.HEIGHT/2+getFHeight("Press Enter", g)/2-11);
			
			g.setFont(font((int) (30+(menu.fontzoom*-1))));
			g.setColor(Color.white.darker());
			g.drawString("Press Enter", Main.WIDTH/2-getFWidth("Press Enter", g)/2, Main.HEIGHT/2+getFHeight("Press Enter", g)/2+22);
			g.setColor(Color.white);
			g.drawString("Press Enter", Main.WIDTH/2-getFWidth("Press Enter", g)/2, Main.HEIGHT/2+getFHeight("Press Enter", g)/2+20);
		}
		
		g.dispose();
		bs.show();
	}

	private void loadFrame(String titel) {
		frame = new JFrame(titel);
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.add(this);
	}
	
	public static int rand(int min, int max) {
		int range = (max - min) + 1;     
		return (int)(Math.random() * range) + min;
	}
	
	public static Font font(double size){
		return new Font("Purisa", Font.BOLD, (int) size);
	}
	public static int getFWidth(String s, Graphics2D g) {
		return (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
	}
	public static int getFHeight(String s, Graphics2D g) {
		return (int) g.getFontMetrics().getStringBounds(s, g).getHeight();
	}
	
	public static void main (String args[]){ new Main(); }
}