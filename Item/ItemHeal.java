package Item;

import Pokemon.PokemonBasics.PokemonAllType.*;;

public class ItemHeal extends Item{
    protected int healVol;

    public void print() {
        System.out.println("Name: " + name);
        System.out.println("Type: " + type);
        System.out.println("Value: " + value);
        System.out.println("Heal Volume: " + healVol);
    }

    public ItemHeal(String name, ItemType type, int value, int healVol) {
        super(name, type, value);
        this.healVol = healVol;
    }

    public int getHealVol() {
        return healVol;
    }
    public void setHealVol(int healVol) {
        this.healVol = healVol;
    }
    
    public void use(Pokemon pokemon) {
        pokemon.setHp(pokemon.getHp() + healVol);
    }
}
