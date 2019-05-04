package Snake;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Snake.Main.GameStates;

public class KeyInput extends KeyAdapter implements KeyListener{
	private Player player;
	private Apples apples;
	private Text text;
	
	public KeyInput(Player player, Apples apples, Text text) {
		this.player = player;
		this.apples = apples;
		this.text = text;
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(Main.state == GameStates.Play){
			if(key == KeyEvent.VK_RIGHT)player.right = true;
			if(key == KeyEvent.VK_LEFT)player.left = true;
			if(key == KeyEvent.VK_L)text.addTextEffect("Wut", 100, 100);
		}
		if(Main.state == GameStates.Menu){
			if(key == KeyEvent.VK_ENTER)Main.state = GameStates.Play;
		}
		if(Main.state == GameStates.GameO){
			if(key == KeyEvent.VK_ENTER){
				player.reset(35, 35);
				apples.reset();
				Main.state = GameStates.Play;
				Main.score = 0;
				player.right = false;
				player.left = false;
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if(Main.state == GameStates.Play){
			if(key == KeyEvent.VK_RIGHT)player.right = false;
			if(key == KeyEvent.VK_LEFT)player.left = false;
		}
	}
}
