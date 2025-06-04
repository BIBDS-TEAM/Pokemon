package Pokemon.PokemonReader;

import Pokemon.PokemonBasics.PokemonBehavior.PokemonMoveCategory;
import Pokemon.PokemonBasics.PokemonBehavior.PokemonMoveType;

public class MoveData {
    public String moveName;
    public PokemonMoveType type;
    public PokemonMoveCategory category;
    public Integer power; 
    public Integer accuracy; 
    public boolean isBuffInPercent;
    public double buffValuePercent;
    public int buffValueInt;
    public String affectedAttribute;
    public int sp;
    public String effect;
    public String description;

    @Override
    public String toString() {
        return "MoveData{" +
               "moveName='" + moveName + '\'' +
               ", type='" + type + '\'' +
               ", category='" + category + '\'' +
               ((type.equals("BUFF") || type.equals("DEBUFF") || type.equals("DEFENSE")) ? (", buffValue=" + (isBuffInPercent ? buffValuePercent : buffValueInt)) : (type.equals("RUN")) ? "" : (", power=" + power))+
               ", accuracy=" + accuracy +
               ", sp=" + sp +
               ", effect='" + effect + '\'' +
               ", description='" + description + '\'' +
               '}';
    }
}
