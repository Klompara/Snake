package Snake;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class Player {
	public ArrayList<SnakeTile>tiles;
	public List<Color> colors;
	
	public boolean isColorfull;
	private int ColorBound = 500;
	
	public double rad;
	public double normalSpeed;
	public double distanceBetween;
	
	public boolean left;
	public boolean right;
	private double angleMove = 4;
	
	private Color color;
	
	public int warteschlange;
	
	public Player(int x, int y) {
		reset(x, y);
	}
	public void reset(int x, int y) {
		isColorfull = false;
		distanceBetween = 1;
		warteschlange = 3;
		rad = 0;
		normalSpeed = 3;
		this.color = Color.green.darker();
		
		tiles = new ArrayList<SnakeTile>();
		tiles.add(new SnakeTile(x, y, color, tiles.size()));
		
		colors = new ArrayList<Color>();
		colors.add(new Color(255,0  ,0  ));
		colors.add(new Color(255,96 ,0  ));
		colors.add(new Color(255,192,0  ));
		colors.add(new Color(222,255,0  ));
		colors.add(new Color(126,255,0  ));
		colors.add(new Color(30 ,255,0  ));
		colors.add(new Color(0  ,255,60 ));
		colors.add(new Color(0  ,255,162));
		colors.add(new Color(0  ,252,255));
		colors.add(new Color(0  ,156,255));
		colors.add(new Color(0  ,60 ,255));
		colors.add(new Color(36 ,0  ,255));
		colors.add(new Color(132,0  ,255));
		colors.add(new Color(228,0  ,255));
		colors.add(new Color(255,0  ,186));
		colors.add(new Color(255,0  ,90 ));
	}
	
	public void tick() {
		if(Main.score >= ColorBound) {
			if(!isColorfull) {
				isColorfull = !isColorfull;
				for(int i = 0; i < tiles.size(); i++) {
					SnakeTile s = tiles.get(i);
					s.setColorfull(i);
				}
			}
		}
		
		if(left) rad-=angleMove+normalSpeed/3;
		if(right) rad+=angleMove+normalSpeed/3;

		if(warteschlange != 0){
			addTile();
			warteschlange--;
		}
		
		for(int i = 0; i < tiles.size(); i++){
			SnakeTile tile = tiles.get(i);
			tile.tick(rad);
		}
	}
	
	public void render(Graphics2D g) {
		for(int i = 0; i < tiles.size(); i++) {
			SnakeTile tile = tiles.get(i);
			tile.render(g);
		}
	}
	
	public void addTile() {
		SnakeTile s = tiles.get(tiles.size()-1);
		tiles.add(new SnakeTile(s.getx()-s.getdx(), s.gety()-s.getdy(), color, tiles.size()-1));
	}
	
	//################################################################################################################
	public class SnakeTile {
		private double x;
		private double y;
		
		private int r;
		private int index;
		
		private double dx;
		private double dy;
		private double rad;
		private double speed;
		
		public Color color;

		public SnakeTile(double x, double y, Color color, int index) {
			this.x = x;
			this.y = y;
			if(isColorfull) setColorfull(index);
			else this.color = color;

			this.index = index;
			r = 5;
			this.speed = normalSpeed;
		}

		public double getx() { return x; }
		public double gety() { return y; }
		public double getr() { return r; }
		public double getdx() { return dx; }
		public double getdy() { return dy; }
		public void setX(double x) { this.x = x; }
		public void setY(double y) { this.y = y; }
		
		public void tick(double angle) {			
			if(tiles.get(0) == this){
				r = 9;
				speed = normalSpeed;
				rad = Math.toRadians(angle);
				dx = Math.cos(rad) * speed;
				dy = Math.sin(rad) * speed;
			}else{
				r = 6;
				double diffX = (x - tiles.get(index).getx());
				double diffY = (y - tiles.get(index).gety());
				double distance = Math.sqrt((diffX*diffX) + (diffY*diffY));
				
				if(distance-r < normalSpeed*distanceBetween) {
					speed = normalSpeed - distanceBetween;
				}else{
					speed = normalSpeed + distanceBetween/2;
				}
				
				dx = ((-speed/distance) * diffX);
				dy = ((-speed/distance) * diffY);
			}
			
			x+=dx;
			y+=dy;
		}
		
		public void render(Graphics2D g) {
			g.setColor(color);
			g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
			if(tiles.get(0) == this) {
				g.setColor(color.darker());
				g.setStroke(new BasicStroke(2));
				g.drawOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
				g.setStroke(new BasicStroke(1));
			}
		}
		
		public void setColorfull(int Color) {
			while(Color > 15) Color -= 15;
			this.color = colors.get(Color);
		}
	}
}
