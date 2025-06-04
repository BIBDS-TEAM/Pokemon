package GuiTileMapThing;

import PlayerNPCgitu.Entity;;

public class EncounterManager {;
    private final GamePanel gp;

    public EncounterManager(GamePanel gp) {
        this.gp = gp;
    }

    public void tryTriggerEncounter(Entity player) {
        if (gp.eCheck.cekTile(player)) {
            double chance = Math.random();
            if (chance < 0.1) { 
                triggerEncounter();
            }
        }
    }

    private void triggerEncounter() {
        
    }
}