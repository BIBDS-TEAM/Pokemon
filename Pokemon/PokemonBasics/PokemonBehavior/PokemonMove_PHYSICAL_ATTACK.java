package Pokemon.PokemonBasics.PokemonBehavior;

import Pokemon.PokemonBasics.PokemonAllType.Pokemon;

public class PokemonMove_PHYSICAL_ATTACK  extends PokemonMove{
    protected int power;
    protected double accuracy;

    public PokemonMove_PHYSICAL_ATTACK(String moveName, int maxSp, String desc, int power, double accuracy, PokemonMoveType moveType, PokemonMoveCategory moveCategory) {
        super(moveName, maxSp, desc);
        this.power = power;
        this.accuracy = accuracy;
        this.moveType = moveType;
        this.moveCategory = moveCategory;
    }

    public int getPower() {
        return power;
    }

    public double getAccuracy() {
        return accuracy;
    }
    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
        if (accuracy < 0) this.accuracy = 0;
    }

    public void move() {

    }

    public void move(Pokemon pokemon) {
        
    }

    // pokemon1 attack pokemon2
    public void move(Pokemon pokemon, Pokemon pokemon2) {
        if (!isSpZero()) {
            System.out.println("SP not enough");
            return;
        }
        useMove();
        int pokemonAtk = pokemon.getAtk();
        int pokemon2Def = pokemon2.getDef();
        int damage = (pokemonAtk * power / pokemon2Def);
        pokemon2.setHp(pokemon2.getHp() - damage);
    }
}
