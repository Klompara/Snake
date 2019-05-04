package Snake;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Apples {
	public ArrayList<Apple> apples;
	private int finalSpawnApples;
	private int maxApples = 3;
	public int moreApples = 0;
	private long timer;
	private boolean timing = false;
	
	public Apples() {
		reset();
	}
	public void reset() {
		timer = System.currentTimeMillis()/1000;
		timing = false;
		maxApples = 3;
		moreApples = 0;
		apples = new ArrayList<Apple>();
		apples.add(new Apple(Main.rand(32, Main.WIDTH-32), Main.rand(32, Main.HEIGHT-32)));
	}
	
	public void tick() {
		for(int i = 0; i < apples.size(); i++) {
			Apple a = apples.get(i);
			a.tick();
		}
		
		if(moreApples != 0) {
			if(!timing) {
				timer = System.currentTimeMillis()/1000;
				timing = true;
			}
			if(System.currentTimeMillis()/1000 - timer > 10) {
				timer = System.currentTimeMillis()/1000;
				moreApples--;
			}
		}else if(timing) {
			timing = false;
		}
		
		finalSpawnApples = maxApples + moreApples*10;
		
		if(apples.size() < finalSpawnApples) {
			apples.add(new Apple(Main.rand(32, Main.WIDTH-32), Main.rand(32, Main.HEIGHT-32)));
		}else while(apples.size() > finalSpawnApples) {
			apples.remove(apples.size()-1);
		}
	}
	
	public void render(Graphics2D g) {
		if(apples != null) {
			for(int i = 0; i < apples.size(); i++){
				Apple apple = apples.get(i);
				apple.render(g);
			}
		}

	}
	
	//##########################################################################################################
	public class Apple {
		private double x, y, deathTime;
		private int r, type;
		private Color color = Color.white;
		private long bornTime, liveTime;
		
		public Apple(int x, int y) {
			bornTime = System.currentTimeMillis();
			deathTime = Main.rand(30, 60);
			this.x = x;
			this.y = y;
			r = 12;
			
			type = (int) (Math.random() * 100);
			if (type < 97) { // 0-79
			    color = Color.red;
			}else if (type < 99) { // 80-94
			    color = new Color(255,215,0  );
			}else{ // 95-99
				color = new Color(139,0  ,139);
			}
			System.out.println("new Apple: x: "+x+", y: "+y+", type: "+type);
		}
		
		public void tick() {
			liveTime = System.currentTimeMillis() - bornTime;
			if(liveTime/1000 > deathTime) {
				apples.remove(this);
			}
		}
		
		public void render(Graphics2D g) {
			if(liveTime == 0) liveTime = 1;
			int alpha = (int) ((255*(liveTime/1000))/deathTime);
			if(alpha > 255) alpha = 255;
			if(alpha < 0) alpha = 0;
			alpha = alpha * -1 + 255;
			Color c = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
			g.setColor(c);
			g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
			g.setStroke(new BasicStroke(3));
			g.setColor(c.darker());
			g.drawOval((int) (x - r-1.5), (int) (y - r-1.5), 2 * r + 3, 2 * r + 3);
			g.setStroke(new BasicStroke(1));	
		}
		
		public void remove(Player player, Text text) {
			if(type == 97 || type == 98 ) {
				moreApples++;
				text.addTextEffect("More Apples!", x, y);
			}
			if(type == 99 || type == 100) {
				player.normalSpeed -= 2.5;
				text.addTextEffect("Slow Down!", x, y);
			}
			apples.remove(this);
		}
		
		public double getx() { return x; }
		public double gety() { return y; }
		public double getr() { return r; }
	}
}
