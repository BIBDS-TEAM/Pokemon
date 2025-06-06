package GuiTileMapThing;

import PlayerNPCgitu.Entity;
import java.util.*;

public class CollisionCheck {

    GamePanel gp;

    public CollisionCheck(GamePanel gp) {
        this.gp = gp;
    }

    public void cekTile(Entity entity) {
        int eLeftWX = entity.worldX + entity.solidArea.x;
        int eRightWX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int eTopWY = entity.worldY + entity.solidArea.y;
        int eBottomWY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int eLeftC = eLeftWX / gp.tileSize;
        int eRightC = eRightWX / gp.tileSize;
        int eTopR = eTopWY / gp.tileSize;
        int eBottomR = eBottomWY / gp.tileSize;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                eTopR = (eTopWY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManager.mapTileNum[eTopR][eLeftC];
                tileNum2 = gp.tileManager.mapTileNum[eTopR][eRightC];
                if (isCollision(tileNum1) || isCollision(tileNum2)) {
                    entity.collisionOn = true;
                }
                break;

            case "down":
                eBottomR = (eBottomWY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManager.mapTileNum[eBottomR][eLeftC];
                tileNum2 = gp.tileManager.mapTileNum[eBottomR][eRightC];
                if (isCollision(tileNum1) || isCollision(tileNum2)) {
                    entity.collisionOn = true;
                }
                break;

            case "left":
                eLeftC = (eLeftWX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManager.mapTileNum[eTopR][eLeftC];
                tileNum2 = gp.tileManager.mapTileNum[eBottomR][eLeftC];
                if (isCollision(tileNum1) || isCollision(tileNum2)) {
                    entity.collisionOn = true;
                }
                break;

            case "right":
                eRightC = (eRightWX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManager.mapTileNum[eTopR][eRightC];
                tileNum2 = gp.tileManager.mapTileNum[eBottomR][eRightC];
                if (isCollision(tileNum1) || isCollision(tileNum2)) {
                    entity.collisionOn = true;
                }
                break;
        }
    }

    public void checkEntity(Entity entity, List<? extends Entity> targets) {
        for (Entity target : targets) {
            if (target != null && target != entity) {
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;
                }
                target.solidArea.x = target.worldX + target.solidArea.x;
                target.solidArea.y = target.worldY + target.solidArea.y;

                if (entity.solidArea.intersects(target.solidArea)) {
                    entity.collisionOn = true;
                }

                entity.solidArea.x = entity.defaultSolidAreaX;
                entity.solidArea.y = entity.defaultSolidAreaY;
                target.solidArea.x = target.defaultSolidAreaX;
                target.solidArea.y = target.defaultSolidAreaY;
            }
        }
    }

    private boolean isCollision(int tileNum) {
        return gp.tileManager.tiles[tileNum].collision;
    }
    private boolean isValidTileCoordinate(int row, int col) {
        if (gp.tileManager == null || gp.tileManager.mapTileNum == null) {
            return false;
        }
        int maxRows = gp.tileManager.mapTileNum.length;
        if (maxRows == 0) return false;
        int maxCols = gp.tileManager.mapTileNum[0].length; 

        return row >= 0 && row < maxRows && col >= 0 && col < maxCols;
    }
    public boolean isTileSolid(int mapColumn, int mapRow) {
        if (gp.tileManager == null || gp.tileManager.mapTileNum == null) {
            System.err.println("CollisionCheck: TileManager or mapTileNum not initialized!");
            return true; 
        }

        int maxRows = gp.tileManager.mapTileNum.length;
        if (maxRows == 0) {
            System.err.println("CollisionCheck: mapTileNum has no rows!");
            return true; 
        }

        if (gp.tileManager.mapTileNum[0] == null) {
             System.err.println("CollisionCheck: mapTileNum[0] is null!");
             return true;
        }
        int maxCols = gp.tileManager.mapTileNum[0].length;
        if (maxCols == 0 && mapColumn >= 0) { 
            System.err.println("CollisionCheck: mapTileNum rows have no columns!");
            return true; 
        }
        
        if (isValidTileCoordinate(mapRow, mapColumn)) {
            int tileNum = gp.tileManager.mapTileNum[mapRow][mapColumn];
            return isCollision(tileNum); 
        } else {

            return true;
        }
    }
}
