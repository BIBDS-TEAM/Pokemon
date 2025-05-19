package GuiTileMapThing;

import PlayerNPCgitu.Player;
import java.awt.*;
import javax.swing.*;
public class GamePanel extends JPanel implements Runnable{
    int FPS = 60;
    KeyInput  keyI = new KeyInput();
    Thread gameThread;
    TileManager tileManager = new TileManager(this);
    Player player= new Player(this,keyI);
    
    public GamePanel(){
            setPreferredSize(new Dimension(480, 480));
            setBackground(Color.DARK_GRAY);
            addKeyListener(keyI);
            setFocusable(true);
            requestFocusInWindow();
            startGameThread();
        }
        public void startGameThread(){
            gameThread = new Thread(this);
            gameThread.start();
    }
    @Override
    public void run(){

        double interval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        long drawCount = 0;
        while(gameThread != null){
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / interval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            if(delta >= 1){
            update();
            repaint();
            delta--;
            drawCount++;
            }
            if(timer >= 1000000000){
                System.out.println("FPS" + drawCount);
                drawCount = 0;
                timer = 0;
            }
            
        }
    }
    public void update(){
        player.update();

    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g; 
        tileManager.draw(g2);
        player.draw(g2);
        
        g2.dispose();
    }
}

