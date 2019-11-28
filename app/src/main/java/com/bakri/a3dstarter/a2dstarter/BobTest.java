package com.bakri.a3dstarter.a2dstarter;

import javax.microedition.khronos.opengles.GL10;

import framework.Game;
import framework.gl.FPSCounter;
import framework.gl.Screen;
import framework.gl.texture.Texture;
import framework.gl.vertices.Vertices;

public class BobTest extends Game {

    public Screen getStartScreen() {
        return new OptimizedBobScreen(this);
    }




    class OptimizedBobScreen extends Screen {

        static final int NUM_BOBS = 100;
        Texture bobTexture;
        Vertices bobModel;
        Bob[] bobs;
        FPSCounter fpsCounter;


        OptimizedBobScreen(Game game) {
            super(game);
            
            bobTexture = new Texture(game, "bobrgb888-32x32.png");
            
            bobModel = new Vertices(graphics, 4, 12, false, true);
            bobModel.setVertices(new float[] { -16, -16, 0, 1,  
                                                16, -16, 1, 1, 
                                                16,  16, 1, 0,
                                               -16,  16, 0, 0, }, 0, 16);
            bobModel.setIndices(new short[] {0, 1, 2, 2, 3, 0}, 0, 6);

            
            bobs = new Bob[100];
            for(int i = 0; i < 100; i++) {
                bobs[i] = new Bob();
            }
            
            fpsCounter = new FPSCounter();
        }
        
        @Override
        public void update(float deltaTime) {         
            game.getInput().getTouchEvents();
            game.getInput().getKeyEvents();
            
            for(int i = 0; i < NUM_BOBS; i++) {
                bobs[i].update(deltaTime);
            }
        }

        @Override
        public void resume() {
            GL10 gl = graphics.getGL();
            gl.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());
            gl.glClearColor(1, 0, 0, 1);
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glOrthof(0, 320, 0, 480, 1, -1);
            
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            
            bobTexture.load();
            gl.glEnable(GL10.GL_TEXTURE_2D);
            bobTexture.bind();
        }
        
        @Override
        public void present(float deltaTime) {
            GL10 gl = graphics.getGL();
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);                        
                        
            bobModel.bind();
            for(int i = 0; i < NUM_BOBS; i++) {
                gl.glLoadIdentity();
                gl.glTranslatef((int)bobs[i].x, (int)bobs[i].y, 0);
                bobModel.draw(GL10.GL_TRIANGLES, 0, 6);
            }
            bobModel.unbind();
            
            fpsCounter.logFrame();
        }

        @Override
        public void pause() {
        }       

        @Override
        public void dispose() {
        }
    }
}

