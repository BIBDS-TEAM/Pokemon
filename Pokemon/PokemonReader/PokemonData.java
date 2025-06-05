package Pokemon.PokemonReader;

import java.util.Arrays;

public class PokemonData {
    public String name;
    public String[] types;
    public int id;
    public int maxHp;
    public int atk;
    public int def;
    public int spAtk;
    public int spDef;
    public int spd;

    @Override
    public String toString() {
        return  "PokemonData{" +
                "name='" + name + '\'' +
                ", types=" + Arrays.toString(types) +
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