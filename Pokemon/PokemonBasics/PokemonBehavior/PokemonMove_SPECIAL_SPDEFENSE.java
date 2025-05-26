// package Pokemon.PokemonBasics.PokemonBehavior;

// import Pokemon.PokemonBasics.PokemonAllType.Pokemon;
// import java.util.Map;
// import java.util.HashMap;
// import java.util.Random;

// public class PokemonMove_SPECIAL_SPDEFENSE extends PokemonMove {
//     protected int defenseBuffValue;
//     protected double defenseBuffPercent;
//     protected boolean isBuffInPercent;

//     public PokemonMove_SPECIAL_SPDEFENSE(String moveName, int maxSp, String desc, int minLvl, int defenseBuffValue, PokemonMoveType moveType, PokemonMoveCategory moveCategory) {
//         super(moveName, maxSp, desc, minLvl, moveType, moveCategory);
//         this.defenseBuffValue = defenseBuffValue;
//         isBuffInPercent = false;
//     }

//     public PokemonMove_SPECIAL_SPDEFENSE(String moveName, int maxSp, String desc, int minLvl, double defenseBuffPercent, PokemonMoveType moveType, PokemonMoveCategory moveCategory) {
//         super(moveName, maxSp, desc, minLvl, moveType, moveCategory);
//         this.defenseBuffPercent = defenseBuffPercent;
//         isBuffInPercent = true;
//     }

//     public int getDefenseBuffValue() {
//         return defenseBuffValue;
//     }

//     public void setDefenseBuffValue(int defenseBuffValue) {
//         if (defenseBuffValue < 0) defenseBuffValue = 0;
//         else this.defenseBuffValue = defenseBuffValue;
//     }

//     public double getDefenseBuffPercent() {
//         return defenseBuffPercent;
//     }

//     public void setDefenseBuffPercent(double defenseBuffPercent) {
//         if (defenseBuffPercent < 0) defenseBuffPercent = 0;
//         else this.defenseBuffPercent = defenseBuffPercent;
//     }

//     public boolean isBuffInPercent() {
//         return isBuffInPercent;
//     }

//     public void setBuffInPercent(boolean buffInPercent) {
//         isBuffInPercent = buffInPercent;
//     }

//     public Map<String, String> move(Pokemon pokemon) {

//     }
// }
