package GuiTileMapThing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

public class KeyInput implements KeyListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed,enterPressed,spacePressed;

    private LinkedList<String> directionList = new LinkedList<>();

    public String getCurrentDirection() {
        if (!directionList.isEmpty()) {
            return directionList.getLast();
        }
        return null;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_SPACE){
            spacePressed = true;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if(code == KeyEvent.VK_W){
            upPressed = true;
            if(!directionList.contains("up")){
                directionList.add("up");
            }
        }
        if(code == KeyEvent.VK_A){
            leftPressed = true;
            if(!directionList.contains("left")){
                directionList.add("left");
            }
        }
        if(code == KeyEvent.VK_S){
            downPressed = true;
            if(!directionList.contains("down")){
                directionList.add("down");
            }
        }
        if(code == KeyEvent.VK_D){
            rightPressed = true;
            if(!directionList.contains("right")){
                directionList.add("right");
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_SPACE){
            spacePressed = true;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }
        if(code == KeyEvent.VK_W){
            upPressed = false;
            directionList.remove("up");
        }
        if(code == KeyEvent.VK_A){
            leftPressed = false;
            directionList.remove("left");
        }
        if(code == KeyEvent.VK_S){
            downPressed = false;
            directionList.remove("down");
        }
        if(code == KeyEvent.VK_D){
            rightPressed = false;
            directionList.remove("right");
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}