package Pokemon.PokemonReader;

class MoveData {
    String moveName;
    String type;
    String category;
    Integer power; 
    Integer accuracy; 
    int pp;
    String effect;

    @Override
    public String toString() {
        return "MoveData{" +
               "moveName='" + moveName + '\'' +
               ", type='" + type + '\'' +
               ", category='" + category + '\'' +
               ", power=" + power +
               ", accuracy=" + accuracy +
               ", pp=" + pp +
               ", effect='" + effect + '\'' +
               '}';
    }
}
