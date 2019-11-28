package framework.audio;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;

public class Music implements MediaPlayer.OnCompletionListener {

    private MediaPlayer mediaPlayer;
    private boolean isPrepared = false;


    public Music(AssetFileDescriptor assetFileDescriptor) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(),
                    assetFileDescriptor.getLength());
            mediaPlayer.prepare();
            isPrepared = true;
            mediaPlayer.setOnCompletionListener(this);

        } catch (IOException e) {
            throw new RuntimeException("Couldn't load music");
        }
    }

    public void setLooping(boolean isLooping) {
        mediaPlayer.setLooping(isLooping);
    }

    public void setVolume(float volume) {
        mediaPlayer.setVolume(volume, volume);
    }

    public void setVolume(float lVolume, float rVolume) {
        mediaPlayer.setVolume(lVolume, rVolume);
    }

    public void play() {
        if (mediaPlayer.isPlaying())
            return;

        try {
            synchronized (this) {
                if (!isPrepared)
                    mediaPlayer.prepare();
                mediaPlayer.start();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void pause() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    public void stop() {
        synchronized (this){
            mediaPlayer.stop();
            isPrepared = false;
        }
    }

    @Override
    public void onCompletion(MediaPlayer player) {
        synchronized (this) {
            isPrepared = false;
        }
    }

    public boolean isLooping() {
        return mediaPlayer.isLooping();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public boolean isStopped() {
        return !isPrepared;
    }

    public void dispose() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.stop();
        mediaPlayer.release();
    }

}
