package Pokemon.PokemonReader;

import Pokemon.PokemonBasics.PokemonBehavior.PokemonMoveType;
import Pokemon.PokemonBasics.PokemonBehavior.PokemonMoveCategory;  // Changed from PokemonMoveType

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
               ", type=" + type +
               ", category=" + category +
               ", power=" + power +
               ", accuracy=" + accuracy +
               ", isBuffInPercent=" + isBuffInPercent +
               ", buffValuePercent=" + buffValuePercent +
               ", buffValueInt=" + buffValueInt +
               ", affectedAttribute='" + affectedAttribute + '\'' +
               ", sp=" + sp +
               ", effect='" + effect + '\'' +
               ", description='" + description + '\'' +
               '}';
    }
}
