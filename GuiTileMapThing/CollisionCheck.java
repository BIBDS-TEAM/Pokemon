package GuiTileMapThing;

import PlayerNPCgitu.Entity;

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

    private boolean isCollision(int tileNum) {
        return gp.tileManager.tiles[tileNum].collision;
    }
}
