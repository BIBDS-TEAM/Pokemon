package GuiTileMapThing;

import PlayerNPCgitu.Entity;

public class EncounterCheck {
    private final GamePanel gp;

    public EncounterCheck(GamePanel gp) {
        this.gp = gp;
    }

    public boolean cekTile(Entity entity) {
        int centerX = entity.worldX + entity.solidArea.x + entity.solidArea.width / 2;
        int centerY = entity.worldY + entity.solidArea.y + entity.solidArea.height / 2;

        int col = centerX / gp.tileSize;
        int row = centerY / gp.tileSize;

        if (col >= 0 && col < gp.tileManager.mapTileNum[0].length &&
            row >= 0 && row < gp.tileManager.mapTileNum.length) {

            int tileNum = gp.tileManager.mapTileNum[row][col];
            if(isEncounterable(tileNum)){
                System.out.println("???");
            }
            else {
                System.out.println("kontol");
            }
            return isEncounterable(tileNum);
        }

        return false;
    }

    private boolean isEncounterable(int tileNum) {
        if (tileNum < 0 || tileNum >= gp.tileManager.tiles.length) {
            return false;
        }
        return gp.tileManager.tiles[tileNum].encounterable;
   }


}