package Pokemon.PokemonBasics.PokemonBehavior;

import Pokemon.PokemonBasics.PokemonAllType.Pokemon;
import java.util.Map;
import java.util.HashMap;

public abstract class PokemonMove {
    protected String MoveName;
    protected PokemonMoveType moveType;
    protected PokemonMoveCategory moveCategory;
    protected int maxSp;
    protected int sp;
    protected String desc;

    public PokemonMove(String MoveName, int maxSp, String desc) {
        this.MoveName = MoveName;
        this.maxSp = maxSp;
        this.sp = maxSp;
        this.desc = desc;
    }

    public String getMoveName() {
        return MoveName;
    }
    public void setMoveName(String MoveName) {
        this.MoveName = MoveName;
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

    public Map<String, String> useMoveInfo() {
        if (!isSpZero()) {
            System.out.println("SP not enough");
            return null;
        } else {
            Map<String, String> map = new HashMap<String, String>();
            map.put("MoveName", MoveName);
            map.put("MoveType", moveType.toString());
            map.put("sp", String.valueOf(sp));
            map.put("desc", desc);
            return map;
        }
    }

    public abstract void move();

    public abstract void move(Pokemon pokemon);

    public abstract void move(Pokemon pokemon, Pokemon pokemon2);

    public String toString() {
        return MoveName + " " + moveType + " " + sp + " " + desc;
    }
}
