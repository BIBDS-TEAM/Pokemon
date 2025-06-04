package Pokemon.PokemonReader;

import java.util.List;
import java.util.Arrays; // Required for Arrays.asList

class PokemonData {
    String name;
    List<String> types;
    int id;
    int maxHp;
    int atk;
    int def;
    int spAtk;
    int spDef;
    int spd;

    @Override
    public String toString() {
        return "PokemonData{" +
               "name='" + name + '\'' +
               ", types=" + types +
               ", id=" + id +
               ", maxHp=" + maxHp +
               ", atk=" + atk +
               ", def=" + def +
               ", spAtk=" + spAtk +
               ", spDef=" + spDef +
               ", spd=" + spd +
               '}';
    }
}