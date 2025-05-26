package Pokemon.PokemonBasics.PokemonBehavior;

import Pokemon.PokemonBasics.PokemonAllType.Pokemon;
import java.util.Map;
import java.util.HashMap;

public abstract class PokemonMove {
    protected String moveName;
    protected PokemonMoveType moveType;
    protected PokemonMoveCategory moveCategory;
    protected int maxSp;
    protected int sp;
    protected int minLvl;
    protected String desc;

    public PokemonMove(String moveName, int maxSp, String desc) {
        this.moveName = moveName;
        this.maxSp = maxSp;
        this.sp = maxSp;
        this.desc = desc;
    }

    public PokemonMove(String moveName, int maxSp, String desc, int minLvl) {
        this.moveName = moveName;
        this.maxSp = maxSp;
        this.sp = maxSp;
        this.minLvl = minLvl;
        this.desc = desc;
    }

    public PokemonMove(String moveName, int maxSp, String desc, PokemonMoveType moveType, PokemonMoveCategory moveCategory) {
        this.moveName = moveName;
        this.maxSp = maxSp;
        this.sp = maxSp;
        this.desc = desc;
        this.moveType = moveType;
        this.moveCategory = moveCategory;
    }

    public PokemonMove(String moveName, int maxSp, String desc, int minLvl, PokemonMoveType MoveType, PokemonMoveCategory moveCategory) {
        this.moveName = moveName;
        this.maxSp = maxSp;
        this.sp = maxSp;
        this.minLvl = minLvl;
        this.desc = desc;
        this.moveType = MoveType;
        this.moveCategory = moveCategory;
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

    public void isLvlEnough(Pokemon pokemon) {
        if (pokemon.getLvl() < minLvl) {
            System.out.println("Lvl not enough");
        }
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

    public String toString() {
        return moveName + " " + moveType + " " + sp + " " + desc;
    }
}