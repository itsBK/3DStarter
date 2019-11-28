package framework.audio;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Audio {

    private AssetManager assets;
    private SoundPool soundPool;
    private List<Music> musics;
    private List<Sound> sounds;

    public Audio(Activity activity) {
        musics = new ArrayList<>();
        sounds = new ArrayList<>();

        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        this.assets = activity.getAssets();
        this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
    }

    public Music newMusic(String fileName) {
        try {
            AssetFileDescriptor assetFileDescriptor = assets.openFd(fileName);
            Music music = new Music(assetFileDescriptor);
            musics.add(music);
            return music;

        } catch (IOException e) {
            throw new RuntimeException("Couldn't load music '" + fileName + "'");
        }
    }

    public Sound newSound(String fileName) {
        try {
            AssetFileDescriptor assetFileDescriptor = assets.openFd(fileName);
            int soundId = soundPool.load(assetFileDescriptor, 0);
            Sound sound = new Sound(soundPool, soundId);
            sounds.add(sound);
            return sound;

        } catch (IOException e){
            throw new RuntimeException("Couldn't load sound '" + fileName + "'");
        }
    }

    public void dispose() {
        for (int i = 0; i < musics.size(); i++)
            musics.remove(i).dispose();
        for (int i = 0; i < sounds.size(); i++)
            sounds.remove(i).dispose();
    }
}
