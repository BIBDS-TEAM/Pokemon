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
                    if (isBuffInPercent)
                        value = (pokemon.getHp() * (buffValue));
                    else
                        value = (buffValue);
                    pokemon.setHp(value);
                    break;
                case "ATK":
                    if (isBuffInPercent)
                        value = ((int) (pokemon.getAtk() * (buffValue)));
                    else
                        value = (buffValue);
                    pokemon.setAtk(value);
                    break;
                case "DEF":
                    if (isBuffInPercent)
                        value = ((int) (pokemon.getDef() * (buffValue)));
                    else
                        value = (buffValue);
                    pokemon.setDef(value);
                    break;
                case "SPATK":
                    if (isBuffInPercent)
                        value = ((int) (pokemon.getSpAtk() * (buffValue)));
                    else
                        value = (buffValue);
                    pokemon.setSpAtk(value);
                    break;
                case "SPDEF":
                    if (isBuffInPercent)
                        value = ((int) (pokemon.getSpDef() * (buffValue)));
                    else
                        value = (buffValue);
                    pokemon.setSpDef(value);
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
            message = buffer.getName() + " has used " + moveName + " to " + target.getName() + " and increased " + buffType + " by ";
            int value = 0;
            switch(buffType) {
                case "HP":
                    if (isBuffInPercent)
                        value = (target.getHp() * (buffValue));
                    else
                        value = (buffValue);
                    target.setHp(value);
                    break;
                case "ATK":
                    if (isBuffInPercent)
                        value = ((int) (target.getAtk() * (buffValue)));
                    else
                        value = (buffValue);
                    target.setAtk(value);
                    break;
                case "DEF":
                    if (isBuffInPercent)
                        value = ((int) (target.getDef() * (buffValue)));
                    else
                        value = (buffValue);
                    target.setDef(value);
                    break;
                case "SPATK":
                    if (isBuffInPercent)
                        value = ((int) (target.getSpAtk() * (buffValue)));
                    else
                        value = (buffValue);
                    target.setSpAtk(value);
                    break;
                case "SPDEF":
                    if (isBuffInPercent)
                        value = ((int) (target.getSpDef() * (buffValue)));
                    else
                        value = (buffValue);
                    target.setSpDef(value);
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
