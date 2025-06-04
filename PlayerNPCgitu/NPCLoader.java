package PlayerNPCgitu;
import Pokemon.PokemonBasics.PokemonAllType.Pokemon;
import GuiTileMapThing.GamePanel;
import java.io.*;
import java.util.*;

public class NPCLoader {
public java.util.List<NPC> loadAllFromFile(GamePanel gp, File file) {
    java.util.List<NPC> npcList = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        String name = "";
        int worldX = 0, worldY = 0;
        String direction = "down";
        String spriteFolder = "";

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.equals("---")) {
                if (!name.isEmpty()) {
                    NPC npc = new NPC(gp, name, worldX, worldY, direction, spriteFolder);
                    npcList.add(npc);
                    name = "";
                    worldX = worldY = 0;
                    direction = "down";
                    spriteFolder = "";
                }
            } else if (line.startsWith("name:")) {
                name = line.substring(5).trim();
            } else if (line.startsWith("position:")) {
                String[] parts = line.substring(9).trim().split(",");
                worldX = Integer.parseInt(parts[0].trim());
                worldY = Integer.parseInt(parts[1].trim());
            } else if (line.startsWith("direction:")) {
                direction = line.substring(10).trim();
            } else if (line.startsWith("spriteFolder:")) {
                spriteFolder = line.substring(13).trim();
            }
        }
        if (!name.isEmpty()) {
            NPC npc = new NPC(gp, name, worldX, worldY, direction, spriteFolder);
            npcList.add(npc);
        }

    } catch (IOException e) {
        e.printStackTrace();
    }

    return npcList;
}
}

