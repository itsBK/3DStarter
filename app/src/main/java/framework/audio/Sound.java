package framework.audio;

import android.media.SoundPool;

public class Sound {

    private int soundId;
    private SoundPool soundPool;


    public Sound(SoundPool soundPool, int soundId) {
        this.soundId = soundId;
        this.soundPool = soundPool;
    }

    public void play(float lVolume, float rVolume) {
        soundPool.play(soundId, lVolume, rVolume, 0, 0, 1);
    }

    public void dispose() {
        soundPool.unload(soundId);
    }
}
