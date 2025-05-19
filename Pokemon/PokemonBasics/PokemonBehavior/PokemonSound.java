package Pokemon.PokemonBasics.PokemonBehavior;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import javax.sound.sampled.*;
import java.net.URL;

public class PokemonSound {
    private static final double MAX_VOLUME = 100.0;
    private static final double MIN_VOLUME = 0.0;
    private String PokemonSoundName;
    private String soundFilePath;
    private PokemonSoundType soundType;
    private Double volume;

    public PokemonSound(String PokemonSoundName, String soundFilePath, PokemonSoundType soundType, Double volume) {
        this.PokemonSoundName = PokemonSoundName;
        this.soundFilePath = soundFilePath;
        this.soundType = soundType;
        this.volume = volume;
    }

    public String getPokemonSoundName() {
        return PokemonSoundName;
    }
    public void setPokemonSoundName(String PokemonSoundName) {
        this.PokemonSoundName = PokemonSoundName;
    }

    public String getSoundFilePath() {
        return soundFilePath;
    }
    public void setSoundFilePath(String soundFilePath) {
        this.soundFilePath = soundFilePath;
    }

    public PokemonSoundType getSoundType() {
        return soundType;
    }

    public void setSoundType(PokemonSoundType soundType) {
        this.soundType = soundType;
    }

    public Double getVolume() {
        return volume;
    }
    public void setVolume(Double volume) {
        if (volume < MIN_VOLUME) {
            this.volume = MIN_VOLUME;
        } else if (volume > MAX_VOLUME) {
            this.volume = MAX_VOLUME;
        } else {
            this.volume = volume;
        }
    }

    public Map<String, String> getSoundInfo() {
        Map<String, String> soundInfo = new HashMap<>();
        soundInfo.put("PokemonSoundName", PokemonSoundName);
        soundInfo.put("soundFilePath", soundFilePath);
        soundInfo.put("soundType", soundType.toString());
        soundInfo.put("volume", String.valueOf(volume));
        return soundInfo;
    }

    public void playSound() {
        Thread pokemonSoundThread = new Thread(() -> {
            try {
                URL pokemonSoundURL = PokemonSound.class.getResource(soundFilePath);
                if (pokemonSoundURL == null) {
                    System.err.println("Sound file not found: " + soundFilePath);
                    return;
                }
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(pokemonSoundURL);

                Clip pokemonSoundClip = AudioSystem.getClip();
                pokemonSoundClip.open(audioInputStream);

                if (pokemonSoundClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl gainControl = (FloatControl) pokemonSoundClip.getControl(FloatControl.Type.MASTER_GAIN);
                    
                    float min = gainControl.getMinimum();
                    float max = gainControl.getMaximum();
                    float value = (float) ((max - min) * volume + min);
                    
                    gainControl.setValue(value);
                }
                pokemonSoundClip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.err.println("Error playing the pokemon sound: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.err.println("Volume control not supported: " + e.getMessage());
            }
        });
        pokemonSoundThread.start();
    }
}
