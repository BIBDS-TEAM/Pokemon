package Pokemon.PokemonBasics.PokemonBehavior;

import Pokemon.PokemonBasics.PokemonAllType.Pokemon;
import Pokemon.PokemonBasics.PokemonAllType.PokemonType;
import Pokemon.PokemonReader.MoveData;

import java.util.Map;
import java.util.HashMap;

public abstract class PokemonMove {
    protected String moveName;
    protected PokemonMoveType moveType;
    protected PokemonMoveCategory moveCategory;
    protected int maxSp;
    protected int sp;
    protected String desc;
    protected String pokemonMoveSoundPath;

    public PokemonMove(String moveName, int maxSp, String desc) {
        this.moveName = moveName;
        this.maxSp = maxSp;
        this.sp = maxSp;
        this.desc = desc;
    }

    public PokemonMove(String moveName, int maxSp, String desc, PokemonMoveType MoveType, PokemonMoveCategory moveCategory) {
        this.moveName = moveName;
        this.maxSp = maxSp;
        this.sp = maxSp;
        this.desc = desc;
        this.moveType = MoveType;
        this.moveCategory = moveCategory;
    }

    public PokemonMove(String moveName, int maxSp, String desc, PokemonMoveType MoveType, PokemonMoveCategory moveCategory, String pokemonMoveSoundPath) {
        this.moveName = moveName;
        this.maxSp = maxSp;
        this.sp = maxSp;
        this.desc = desc;
        this.moveType = MoveType;
        this.moveCategory = moveCategory;
        this.pokemonMoveSoundPath = pokemonMoveSoundPath;
    }

    public String getmoveName() {
        return moveName;
    }
    public void setmoveName(String moveName) {
        this.moveName = moveName;
    }

    public PokemonMoveType getMoveType() {
        return moveType;
    }
    public void setMoveType(PokemonMoveType moveType) {
        this.moveType = moveType;
    }

    public int getSp() {
        return sp;
    }
    public boolean isSpZero() {
        return sp > 0;
    }

    public void useMove() {
        if (!isSpZero()) {
            System.out.println("SP not enough");
            return;
        }
        sp--;
    }

    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    public static PokemonMove loadPokemonMoveByType(MoveData moveData) {
        // PokemonMove move = null; // Original problematic line
        PokemonMove createdMove = null; // Use a new variable to store the created move
                
        // Ensure moveData and its type are not null to prevent NullPointerExceptions here
        if (moveData == null || moveData.type == null) {
            System.err.println("Error: MoveData or MoveData.type is null. Cannot load move.");
            return null; // Return null if essential data is missing
        }

        String moveTypeStr = moveData.type.name();
        switch (moveTypeStr) {
            case "ATTACK":
                createdMove = new PokemonMove_PHYSICAL_ATTACK(moveData.moveName, moveData.sp, moveData.description, moveData.power, moveData.accuracy, moveData.type, moveData.category);
                break;
            case "SPECIAL_ATTACK":
                createdMove = new PokemonMove_SPECIAL_SPATTACK(moveData.moveName, moveData.sp, moveData.description, moveData.power, moveData.accuracy, moveData.type, moveData.category);
                break;
            case "BUFF":
                if (!moveData.isBuffInPercent) {
                    createdMove = new PokemonMove_STATUS_BUFF(moveData.moveName, moveData.sp, moveData.description, moveData.buffValueInt, moveData.affectedAttribute, moveData.isBuffInPercent);
                } else {
                    createdMove = new PokemonMove_STATUS_BUFF(moveData.moveName, moveData.sp, moveData.description, (int)moveData.buffValuePercent, moveData.affectedAttribute, moveData.isBuffInPercent);
                }
                break;
            case "DEBUFF":
                createdMove = new PokemonMove_STATUS_DEBUFF(moveData.moveName, moveData.sp, moveData.description, moveData.affectedAttribute, moveData.isBuffInPercent, moveData.buffValuePercent, moveData.accuracy);
                break;
            case "DEFENSE":
                if (moveData.isBuffInPercent) { // Note: Original logic seemed swapped, this assumes buffValuePercent means percentage
                    createdMove = new PokemonMove_STATUS_DEFENSE_by_percentage(moveData.moveName, moveData.sp, moveData.description, moveData.buffValuePercent);
                } else {
                    createdMove = new PokemonMove_STATUS_DEFENSE_by_flat(moveData.moveName, moveData.sp, moveData.description, moveData.buffValueInt);
                }
                break;
            case "RUN":
                createdMove = new PokemonMove_RUN(moveData.moveName, moveData.sp, moveData.description, moveData.type, moveData.category);
                break;
            default:
                System.out.println("Invalid move type: " + moveTypeStr);
                break; // createdMove will remain null
        }
        return createdMove; // Return the actual created move object
    }

    public Map<String, String> useMoveInfo() {
            Map<String, String> response = new HashMap<String, String>();
        if (!isSpZero()) {
            System.out.println("SP not enough");
            response.put("error", "SP not enough");
            return response;
        } else {
            response.put("MoveName", moveName);
            response.put("MoveType", moveType.toString());
            response.put("sp", String.valueOf(sp));
            response.put("desc", desc);
            return response;
        }
    }
    
    public abstract Map<String, String> move();

    public abstract Map<String, String> move(Pokemon pokemon);

    public abstract Map<String, String> move(Pokemon pokemon, Pokemon pokemon2);

    public void playSound() {

    }

    public String toString() {
        return moveName + " " + moveType + " " + sp + " " + desc;
    }
}