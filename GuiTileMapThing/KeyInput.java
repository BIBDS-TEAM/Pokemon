package GuiTileMapThing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

public class KeyInput implements KeyListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean enterPressed, spacePressed, Fpressed, ePressed, escPressed; // Changed Ppressed to Fpressed, added escPressed

    private boolean eAlreadyProcessed;
    private boolean fAlreadyProcessed; 
    private boolean spaceAlreadyProcessed;
    private boolean enterAlreadyProcessed;
    private boolean escAlreadyProcessed;

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

        if (code == KeyEvent.VK_E) {
            if (!eAlreadyProcessed) {
                ePressed = true;
                eAlreadyProcessed = true;
            }
        }
        if (code == KeyEvent.VK_F) { 
            if (!fAlreadyProcessed) { 
                Fpressed = true;      
                fAlreadyProcessed = true; 
            }
        }
        if (code == KeyEvent.VK_SPACE) {
            if (!spaceAlreadyProcessed) {
                spacePressed = true;
                spaceAlreadyProcessed = true;
            }
        }
        if (code == KeyEvent.VK_ENTER) {
            if (!enterAlreadyProcessed) {
                enterPressed = true;
                enterAlreadyProcessed = true;
            }
        }
        if (code == KeyEvent.VK_ESCAPE) { 
            if (!escAlreadyProcessed) {
                escPressed = true;
                escAlreadyProcessed = true;
            }
        }

        // --- Movement Keys (WASD) ---
        if (code == KeyEvent.VK_W) {
            upPressed = true;
            if (!directionList.contains("up")) {
                directionList.add("up");
            }
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = true;
            if (!directionList.contains("left")) {
                directionList.add("left");
            }
        }
        if (code == KeyEvent.VK_S) {
            downPressed = true;
            if (!directionList.contains("down")) {
                directionList.add("down");
            }
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
            if (!directionList.contains("right")) {
                directionList.add("right");
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_E) {
            ePressed = false;
            eAlreadyProcessed = false;
        }
        if (code == KeyEvent.VK_F) { 
            Fpressed = false;      
            fAlreadyProcessed = false; 
        }
        if (code == KeyEvent.VK_SPACE) {
            spacePressed = false;
            spaceAlreadyProcessed = false;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = false;
            enterAlreadyProcessed = false;
        }
        if (code == KeyEvent.VK_ESCAPE) { 
            escPressed = false;
            escAlreadyProcessed = false;
        }

        if (code == KeyEvent.VK_W) {
            upPressed = false;
            directionList.remove("up");
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
            directionList.remove("left");
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
            directionList.remove("down");
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
            directionList.remove("right");
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }
}