package Pokemon;
import Pokemon.PokemonBasics.PokemonAllType.PokemonType;
import Pokemon.PokemonBasics.PokemonBehavior.PokemonMove;
import Pokemon.PokemonReader.MoveData;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class PokemonMoveFactory {
    private Map<String, MoveData> moveDataMap;

    public PokemonMoveFactory(Map<String, MoveData> moveDataMap) {
        this.moveDataMap = moveDataMap;
    }

    public PokemonMove createMove(String moveName) {
        MoveData data = moveDataMap.get(moveName);
        if (data == null) {
            System.err.println("No data found for move: " + moveName);
            return null;
        }
        return new PokemonMove(data);
    }

    public PokemonMove[] generateRandomMoveSet(PokemonType[] type) {
    Set<MoveData> selectedMoves = new HashSet<>();
    Random random = new Random();
    List<MoveData> type1Moves = new ArrayList<>();
    List<MoveData> type2Moves = new ArrayList<>();
    List<MoveData> physicalMoves = new ArrayList<>();
    List<MoveData> specialMoves = new ArrayList<>();
    List<MoveData> statusMoves = new ArrayList<>();

    PokemonType type1 = type[0];
    PokemonType type2 = type.length > 1 ? type[1] : null;

    for (MoveData move : moveDataMap.values()) {

        if (move.type != null) {

            if (move.type.name().equals(type1.name())) {
                type1Moves.add(move);
            }
            if (type2 != null && move.type.name().equals(type2.name())) {
                type2Moves.add(move);
            }
        }

        if (move.category != null) {
            switch (move.category) {
                case PHYSICAL:
                    physicalMoves.add(move);
                    break;
                case SPECIAL:
                    specialMoves.add(move);
                    break;
                case STATUS:
                    statusMoves.add(move);
                    break;
            }
        }
    }

    int attempts = 0;
    while (selectedMoves.size() < 4 && attempts < 200) {
        int roll = random.nextInt(100);
        List<MoveData> movePool = null;

        if (roll < 40) { 
            boolean canUseType2 = (type2 != null && !type2Moves.isEmpty());
            boolean useType2 = canUseType2 && random.nextBoolean(); 

            if (useType2) {
                movePool = type2Moves; 
            } else if (!type1Moves.isEmpty()) {
                movePool = type1Moves; 
            } else if (canUseType2) {
                movePool = type2Moves;
            }
        } else if (roll < 65) {
            movePool = physicalMoves;
        } else if (roll < 90) {
            movePool = specialMoves;
        } else { 
            movePool = statusMoves;
        }

        if (movePool != null && !movePool.isEmpty()) {
            MoveData randomMove = movePool.get(random.nextInt(movePool.size()));
            selectedMoves.add(randomMove);
        }
        attempts++;
    }
    
    PokemonMove[] finalMoves = new PokemonMove[selectedMoves.size()];
    int index = 0;
    for (MoveData data : selectedMoves) {
        finalMoves[index++] = new PokemonMove(data);
    }

    return finalMoves;
}
}
