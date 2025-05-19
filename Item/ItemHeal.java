package Projek_BIBD_Pokemon.Item;

public class ItemHeal extends Item{
    protected int healVol;

    public ItemHeal(String name, ItemType type, int value, int healVol) {
        super(name, type, value);
        this.healVol = healVol;
    }
    
}
