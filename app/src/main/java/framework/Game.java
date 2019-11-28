package framework;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import framework.audio.Audio;
import framework.gl.Graphics;
import framework.gl.Screen;
import framework.input.Input;
import framework.input.SensorHandler;

public abstract class Game extends Activity implements GLSurfaceView.Renderer {

    enum GLGameState {
        Initialized, Running, Paused, Finished, Idle
    }

    private GLSurfaceView glView;
    private Graphics graphics;
    private Audio audio;
    private Input input;
    private FileIO fileIO;
    private Screen screen;

    private GLGameState state = GLGameState.Initialized;
    private final Object stateChanged = new Object();
    private long startTime = System.nanoTime();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);

        glView = new GLSurfaceView(this);
        glView.setRenderer(this);
        setContentView(glView);

        graphics = new Graphics(glView);
        audio = new Audio(this);
        fileIO = new FileIO(this);
        input = new Input(this, glView, 1, 1,
                100, SensorHandler.TYPE_ACCELEROMETER);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
    }

    @Override
    protected void onPause() {
        synchronized (stateChanged) {
            if (isFinishing())
                state = GLGameState.Finished;
            else
                state = GLGameState.Paused;

            while (true) {
                try {
                    stateChanged.wait();
                    break;
                } catch (InterruptedException ignored) {}
            }
        }

        glView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glView.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        graphics.setGL(gl);

        synchronized (stateChanged) {
            if (state == GLGameState.Initialized)
                screen = getStartScreen();
            state = GLGameState.Running;
            screen.resume();
            startTime = System.nanoTime();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLGameState state;

        synchronized (stateChanged) {
            state = this.state;
        }

        if (state == GLGameState.Running) {
            float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
            startTime = System.nanoTime();

            screen.update(deltaTime);
            screen.present(deltaTime);
        }

        if (state == GLGameState.Paused) {
            screen.pause();
            synchronized (stateChanged) {
                this.state = GLGameState.Idle;
                stateChanged.notifyAll();
            }
        }

        if (state == GLGameState.Finished) {
            screen.pause();
            screen.dispose();
            synchronized (stateChanged) {
                this.state = GLGameState.Idle;
                stateChanged.notifyAll();
            }
        }
    }


    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");

        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public Audio getAudio() {
        return audio;
    }

    public FileIO getFileIO() {
        return fileIO;
    }

    public Input getInput() {
        return input;
    }

    public Screen getCurrentScreen() {
        return screen;
    }

    public abstract Screen getStartScreen();

}
