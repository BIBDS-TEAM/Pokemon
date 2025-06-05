package Pokemon.PokemonBasics.PokemonBehavior;

public enum PokemonMoveEffect {
    LIFESTEAL,         // Recovers HP based on damage dealt
    ATTACK,
    DEFENCE,
    ACCURACY,
    SPEED,
    POISON,             // Inflicts poison status 
    FREEZE,             // Inflicts freeze status
    MULTIPLE,   // Hits multiple times in one turn
    INVINCIBLE,         // User becomes temporarily invincible or raises defenses sharply (like Barrier)
    FLINCH,             // May cause the target to flinch if user attacks first
    PARALYSIS,          // Inflicts paralysis status 
    CONFUSE,            // Inflicts confusion status
    CRIT,               // Increased critical hit ratio 
    INFLICT,        // User takes recoil damage proportional to damage dealt
    BURN,               // Inflicts burn status
    FAINT,         // User faints after using the move
    ONEHIT,         // A one-hit knockout move
    SLEEP,              // Inflicts sleep status
    HEAL,          // User recovers HP
 // User takes a fixed amount or type of recoil damage
}
