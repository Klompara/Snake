package Snake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Menu {
	public double fontzoom;
	private double angle;
	private boolean fontzoomb;
	private boolean angleb;
	
	public Menu() {
		angle = 0;
		fontzoom = 0;
		fontzoomb = true;
	}
	
	public void tick() {
		if(fontzoom < 10 && fontzoomb){ fontzoom+=0.2; }else{ fontzoomb = false; }
		if(fontzoom > -10 && !fontzoomb){ fontzoom-=0.2; }else{ fontzoomb = true; }
		
		if(angle < 10 && angleb){ angle+=0.2; }else{ angleb = false; }
		if(angle > -10 && !angleb){ angle-=0.2; }else{ angleb = true; }
	}
	
	public void render(Graphics2D g) {
		g.setFont(Main.font(58+Main.HEIGHT/30));
		g.setColor(Color.white.darker());
		String headlineString = "Snake";
		g.drawString(headlineString, Main.WIDTH/2-Main.getFWidth(headlineString, g)/2-80, 202-Main.getFHeight(headlineString, g)/2+Main.HEIGHT/7);
		g.setColor(Color.white);
		g.drawString(headlineString, Main.WIDTH/2-Main.getFWidth(headlineString, g)/2-80, 200-Main.getFHeight(headlineString, g)/2+Main.HEIGHT/7);

		g.setFont(Main.font(10));
		g.drawString("Michael K. ™", Main.getFWidth("Michael K.™", g)-30, Main.HEIGHT-Main.getFWidth("Michael K.™", g)+14);
		
		AffineTransform affineTransform = new AffineTransform();
		affineTransform.rotate(Math.toRadians(angle), (Main.getFWidth(Main.version, g)/2), (Main.getFHeight(Main.version, g)/2));
		Font rotatedFont = Main.font((int)(50+fontzoom+Main.HEIGHT/30)).deriveFont(affineTransform);
		g.setFont(rotatedFont);
		g.setColor(Color.white.darker());
		g.drawString(Main.version,Main.getFWidth(Main.version, g)/2+Main.WIDTH/2+1, Main.getFHeight(Main.version, g)/2+132+Main.HEIGHT/10);
		g.setColor(Color.white);
		g.drawString(Main.version,Main.getFWidth(Main.version, g)/2+Main.WIDTH/2, Main.getFHeight(Main.version, g)/2+130+Main.HEIGHT/10);
		
		g.setFont(Main.font((int) (30+(fontzoom*-0.75))));
		g.setColor(Color.white.darker());
		g.drawString("Press Enter", Main.WIDTH/2-Main.getFWidth("Press Enter", g)/2, Main.HEIGHT/2+Main.getFHeight("Press Enter", g)/2+22);
		g.setColor(Color.white);
		g.drawString("Press Enter", Main.WIDTH/2-Main.getFWidth("Press Enter", g)/2, Main.HEIGHT/2+Main.getFHeight("Press Enter", g)/2+20);
	}
}
