package Snake;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class Text {
	
	private List<TextEffect> textEffects = new ArrayList<TextEffect>();
	
	public Text() {}
	
	public void tick() {
		for(int i = 0; i < textEffects.size(); i++) {
			TextEffect t = textEffects.get(i);
			t.tick();
		}
	}
	
	public void render(Graphics2D g) {
		for(int i = 0; i < textEffects.size(); i++) {
			TextEffect t = textEffects.get(i);
			t.render(g);
		}
	}
	
	public void addTextEffect(String text, double x, double y) {
		textEffects.add(new TextEffect(text, x, y));
	}
	
	//########################################################################
	public class TextEffect {
		private String text;
		private double x, y, alpha = 254;
		
		public TextEffect(String text, double x, double y) {
			this.text = text;
			this.x = x;
			this.y = y;
		}
		
		public void tick() {
			alpha-=2;
			y-=alpha/255;
			if(alpha == 0){
				textEffects.remove(this);
			}
		}
		
		public void render(Graphics2D g) {
			g.setFont(Main.font(14));
			g.setColor(new Color(255,255,255,(int)alpha));
			g.drawString(text, (int)x-(Main.getFWidth(text, g)/2), (int)y);
		}
	}
}
