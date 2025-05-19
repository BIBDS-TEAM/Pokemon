package Item;

import Pokemon.PokemonBasics.PokemonAllType.*;

public abstract class Item {
    protected String name;
    protected ItemType type;
    protected int value;

    public void print() {
        System.out.println("Name: " + name);
        System.out.println("Type: " + type);
        System.out.println("Value: " + value);
    }

    public Item(String name, ItemType type, int value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public ItemType getType() {
        return type;
    }
    public void setType(ItemType type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }

    // determine first the type of the item
    public abstract void use(Pokemon pokemon);
}
