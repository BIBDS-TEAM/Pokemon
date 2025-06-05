package Pokemon;
import Pokemon.PokemonBasics.PokemonBehavior.PokemonMove;
import Pokemon.PokemonReader.MoveData;
import java.util.Map;

public class PokemonMoveFactory {
    private Map<String, MoveData> moveDataMap;

    public PokemonMoveFactory(Map<String, MoveData> moveDataMap) {
        this.moveDataMap = moveDataMap;
    }

    public PokemonMove createMove(String moveName) {
        MoveData data = moveDataMap.get(moveName.toUpperCase());
        if (data == null) {
            System.err.println("No data found for move: " + moveName);
            return null;
        }

        // Create a new PokemonMove using the data
        return new PokemonMove(
                data.name,
                data.maxSp,
                data.desc,
                data.moveType,
                data.moveCategory
        );
    }
}
