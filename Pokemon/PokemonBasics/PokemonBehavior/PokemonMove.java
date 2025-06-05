package Pokemon.PokemonBasics.PokemonBehavior;

import Pokemon.PokemonBasics.PokemonAllType.Pokemon;
import java.io.File;
import javax.sound.sampled.*;

public class PokemonMove {
    private Clip clip;
    protected String moveName;
    protected PokemonMoveType moveType;
    protected PokemonMoveCategory moveCategory;
    protected int maxSp;
    protected int sp;
    protected String desc;
    protected String pokemonMoveSoundPath;
    protected String Effect;

    public PokemonMove(String moveName, int maxSp, String desc, PokemonMoveType MoveType, PokemonMoveCategory moveCategory) {
        this.moveName = moveName;
        this.maxSp = maxSp;
        this.sp = maxSp;
        this.desc = desc;
        this.moveType = MoveType;
        this.moveCategory = moveCategory;
        this.pokemonMoveSoundPath = "../Pokemon/audioSave/"+ moveName +".wav";
        loadStatusEffect();
    }
    public void playPokemonMoveSound(){
        new Thread(() -> {
            try {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(pokemonMoveSoundPath));
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
                
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });
            } catch (Exception e) {
                System.err.println("Error playing sound: " + pokemonMoveSoundPath);
                e.printStackTrace();
            }
        }).start();
    }
    public String getMoveName() {
        return moveName;
    }
    public void setmoveName(String moveName) {
        this.moveName = moveName;
    }

    public PokemonMoveType getMoveType() {
        return moveType;
    }
    public void setMoveType(PokemonMoveType moveType) {
        this.moveType = moveType;
    }

    public int getSp() {
        return sp;
    }
    public boolean isSpZero() {
        return sp > 0;
    }

    public void useMove(Pokemon user, Pokemon target) {
        if (!isSpZero()) {
            System.out.println("SP not enough");
            return;
        }
        else {

        }
        sp--;
    }

    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    public void loadStatusEffect(){

    }


}