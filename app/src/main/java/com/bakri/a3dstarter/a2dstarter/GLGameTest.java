package com.bakri.a3dstarter.a2dstarter;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import framework.Game;
import framework.gl.Graphics;
import framework.gl.Screen;

public class GLGameTest extends Game {
	
    public Screen getStartScreen() {
        return new TestScreen(this);
    }



    class TestScreen extends Screen {

        Graphics graphics;
        Random rand = new Random();


        public TestScreen(Game game) {
            super(game);
            graphics = game.getGraphics();
        }

        @Override
        public void present(float deltaTime) {
            GL10 gl = graphics.getGL();
            gl.glClearColor(rand.nextFloat(), rand.nextFloat(),
                    rand.nextFloat(), 1);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        }

        @Override
        public void update(float deltaTime) {
        }

        @Override
        public void pause() {
        }

        @Override
        public void resume() {
        }

        @Override
        public void dispose() {
        }
    }
}
