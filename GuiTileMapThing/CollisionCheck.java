package GuiTileMapThing;
import PlayerNPCgitu.Entity;
public class CollisionCheck {

    GamePanel gp;
    public CollisionCheck(GamePanel gp){
        this.gp = gp;
    }
    public void cekTile(Entity entity){
        int eLeftWX  = entity.worldX + entity.solidArea.x;
        int eRightWX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int eTopWY = entity.worldY + entity.solidArea.y;
        int eBottomWY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int eLeftC =  eLeftWX/64;
        int eRightC = eRightWX /64;
        int eTopR =  eTopWY / 64;
        int eBottomR = eBottomWY / 64;

        int tileNum1,tileNum2;
        switch(entity.direction){
            case "up" :
                eTopR = (eTopWY - entity.speed)/ 64;
                tileNum1 = gp.tileManager.mapTileNum[eLeftC][eTopR];
                tileNum2 = gp.tileManager.mapTileNum[eRightC][eTopR];
                if(gp.tileManager.tiles[tileNum1].collision || gp.tileManager.tiles[tileNum2].collision){
                    System.out.println("chekedTop");
                    entity.collisionOn = true;
                }
                else {
                    entity.collisionOn =false;
                }
                break;
            case "down":
            break;
            case "left":
            break;
            case "right":
            break;
        }
    }
}
