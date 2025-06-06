package GuiTileMapThing;

import PlayerNPCgitu.NPC;
import Pokemon.PokemonBasics.PokemonAllType.Pokemon;
import Pokemon.PokemonReader.PokemonData;
import Pokemon.PokemonReader.PokemonListParser;
import java.io.IOException;
import java.util.*;

public class NpcSpawnManager {
    private final String[] skinPath = new String[]{"Polisi", "glasses", "chubby", "Alice"};
    private final String[] direction = new String[]{"up","down","left","right"};
    private final List<NPC> npcs = new ArrayList<>();
    private final Random random = new Random();
    private final GamePanel gp;

    private final List<PokemonData> validPokemonList = new ArrayList<>();

    public NpcSpawnManager(GamePanel gp) {
        this.gp = gp;
        try {
            loadValidPokemon();
        } catch (IOException e) {
            System.err.println("Error loading Pokémon data: " + e.getMessage());
        }
    }

    private void loadValidPokemon() throws IOException {
        Map<String, PokemonData> allPokemon = PokemonListParser.loadPokemonFromTxt();
        for (PokemonData p : allPokemon.values()) {
            if (p.id >= 1 && p.id <= 100) {
                validPokemonList.add(p);
            }
        }
    }

    public void spawnRandomNPCs(int count) {
        npcs.clear();
        int spawned = 0;
        int attempts = 0;

        while (spawned < count && attempts < 1000) {
            int tileX = random.nextInt(100) + 1;
            int tileY = random.nextInt(100) + 1;

            if (!gp.cc.isTileSolid(tileX, tileY)) {
                String skin = skinPath[random.nextInt(skinPath.length)];
                int worldX = tileX * gp.tileSize;
                int worldY = tileY * gp.tileSize;

                NPC npc = new NPC(gp, "NPC" + spawned, worldX, worldY, "down", skin);
                int random = (int)(Math.random() * 6) + 1;
                Pokemon[] pokemonList = new Pokemon[6];
                for(int i = 0; i< random;i++){
                    pokemonList[i] = gp.pf.createPokemonRandom();
                }
                npc.setPokemonList(pokemonList);
                npcs.add(npc);
                spawned++;
            }
            attempts++;
        }
    }
    public List<NPC> getnpcList(){
        return npcs;
    }
}
