package Pokemon.PokemonBasics.PokemonBehavior;

import Pokemon.PokemonBasics.PokemonAllType.Pokemon;

import java.util.Map;
import java.util.HashMap;

public class PokemonMove_STATUS_BUFF extends PokemonMove {
    protected int buffValue;
    protected boolean isBuffInPercent;
    protected String buffType;

    public PokemonMove_STATUS_BUFF(String moveName, int maxSp, String desc, int buffValue, String buffType,
            boolean isBuffInPercent) {
        super(moveName, maxSp, desc);
        System.out.println("Buff move created" + moveName);
        System.out.println("buff type" + buffType);
        this.buffValue = buffValue;
        this.isBuffInPercent = isBuffInPercent;
        this.buffType = buffType.toUpperCase();
    }

    public int getBuffValue() {
        return buffValue;
    }

    public void setBuffValue(int buffValue) {
        this.buffValue = buffValue < 0 ? 0 : buffValue;
    }

    public boolean isBuffInPercent() {
        return isBuffInPercent;
    }

    public void setBuffInPercent(boolean buffInPercent) {
        isBuffInPercent = buffInPercent;
    }

    public String getBuffType() {
        return buffType;
    }

    public void setBuffType(String buffType) {
        this.buffType = buffType.toUpperCase();
    }

    public Map<String, String> move() {
        throw new UnsupportedOperationException("The current class doesn't support this method yet.");
    }

    public Map<String, String> move(Pokemon pokemon) {
        Map<String, String> response = new HashMap<>();
        String message;

        response.put("moveName", moveName);
        response.put("moveType", moveType.toString());
        response.put("sp", String.valueOf(sp));
        response.put("Desc", desc);

        if (isSpZero()) {
            System.out.println("SP is not enough");
            response.put("error", "SP not enough");
            return response;
        } else {
            useMove();
            message = pokemon.getName() + " used " + moveName + " and increased " + buffType + " by ";
            int value = 0;
            switch (buffType) {
                case "HP":
                    int hp = pokemon.getHp();
                    if (isBuffInPercent)
                        value = (hp * (buffValue));
                    else
                        value = (buffValue);
                    pokemon.setHp(hp + value);
                    break;
                case "ATK":
                    int atk = pokemon.getAtk();
                    if (isBuffInPercent)
                        value = ((int) (atk * (buffValue)));
                    else
                        value = (buffValue);
                    pokemon.setAtk(atk + value);
                    break;
                case "DEF":
                    int def = pokemon.getDef();
                    if (isBuffInPercent)
                        value = ((int) (def * (buffValue)));
                    else
                        value = (buffValue);
                    pokemon.setDef(def + value);
                    break;
                case "SPATK":
                    int spAtk = pokemon.getSpAtk();
                    if (isBuffInPercent)
                        value = ((int) (spAtk * (buffValue)));
                    else
                        value = (buffValue);
                    pokemon.setSpAtk(spAtk + value);
                    break;
                case "SPDEF":
                    int spDef = pokemon.getSpDef();
                    if (isBuffInPercent)
                        value = ((int) (spDef * (buffValue)));
                    else
                        value = (buffValue);
                    pokemon.setSpDef(spDef + value);
                    break;
                case "SPD":
                    int spd = pokemon.getSpd();
                    if (isBuffInPercent)
                        value = ((int) (spd * (buffValue)));
                    else
                        value = (buffValue);
                    pokemon.setSpd(spd + value);
                    break;
                default:
                    System.out.println("Invalid buff type");
                    response.put("error", "Invalid buff type");
                    response.put("flag", "false");
                    return response;
            }
            message += value + "!";

            response.put("message", message);
            response.put("flag", "true");

            System.out.println(message);
            return response;
        }
    }

    public Map<String, String> move(Pokemon buffer, Pokemon target) {
        Map<String, String> response = new HashMap<>();
        String message;

        response.put("moveName", moveName);
        response.put("moveType", moveType.toString());
        response.put("sp", String.valueOf(sp));
        response.put("desc", desc);

        if (isSpZero()) {
            response.put("error", "SP not enough");
            response.put("flag", "false");
            return response;
        } else {
            useMove();
            message = buffer.getName() + " has used " + moveName + " to " + target.getName() + " and increased "
                    + buffType + " by ";
            int value = 0;
            switch (buffType) {
                case "HP":
                    int hp = target.getHp();
                    if (isBuffInPercent)
                        value = (hp * (buffValue));
                    else
                        value = (buffValue);
                    target.setHp(hp + value);
                    break;
                case "ATK":
                    int atk = target.getAtk();
                    if (isBuffInPercent)
                        value = ((int) (atk * (buffValue)));
                    else
                        value = (buffValue);
                    target.setAtk(atk + value);
                    break;
                case "DEF":
                    int def = target.getDef();
                    if (isBuffInPercent)
                        value = ((int) (def * (buffValue)));
                    else
                        value = (buffValue);
                    target.setDef(def + value);
                    break;
                case "SPATK":
                    int spAtk = target.getSpAtk();
                    if (isBuffInPercent)
                        value = ((int) (spAtk * (buffValue)));
                    else
                        value = (buffValue);
                    target.setSpAtk(spAtk + value);
                    break;
                case "SPDEF":
                    int spDef = target.getSpDef();
                    if (isBuffInPercent)
                        value = ((int) (spDef * (buffValue)));
                    else
                        value = (buffValue);
                    target.setSpDef(spDef + value);
                    break;
                case "SPD":
                    int spd = target.getSpd();
                    if (isBuffInPercent)
                        value = ((int) (spd * (buffValue)));
                    else
                        value = (buffValue);
                    target.setSpd(spd + value);
                    break;
                default:
                    System.out.println("Invalid buff type");
                    response.put("error", "Invalid buff type");
                    response.put("flag", "false");
                    return response;
            }

            message += value + "!";

            System.out.println(message);
            response.put("message", message);
            response.put("flag", "true");
        }

        return response;
    }
}
