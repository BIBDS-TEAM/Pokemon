package Pokemon.PokemonBasics.PokemonAllType;

public class PokemonNormal extends Pokemon{
    // NORMAL
    // https://pokemon.fandom.com/wiki/Normal
    public PokemonNormal(String name, int lvl, int maxHp, int hp, int atk, int def, int spAtk, int spDef, int spd, String miniModelPath, String fightModelPath) {
        super(name, lvl, maxHp, hp, atk, def, spAtk, spDef, spd, miniModelPath, fightModelPath);
        this.type = PokemonType.NORMAL;
    }
    
}
