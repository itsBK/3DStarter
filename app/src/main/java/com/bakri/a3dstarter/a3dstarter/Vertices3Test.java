package com.bakri.a3dstarter.a3dstarter;

import javax.microedition.khronos.opengles.GL10;

import framework.Game;
import framework.gl.Screen;
import framework.gl.vertices.Vertices3;

public class Vertices3Test extends Game {

    @Override
    public Screen getStartScreen() {
        return new Vertices3Screen(this);
    }





    class Vertices3Screen extends Screen {

        Vertices3 vertices;


        public Vertices3Screen(Game game) {
            super(game);
            vertices = new Vertices3(graphics, 6, 0, true, false, false);
            vertices.setVertices(new float[] {
                    -0.5f, -0.5f, -3,     1, 0, 0, 1,
                     0.5f, -0.5f, -3,     1, 0, 0, 1,
                     0.0f,  0.5f, -3,     1, 0, 0, 1,

                     0.0f, -0.5f, -5,     0, 1, 0, 1,
                     1.0f, -0.5f, -5,     0, 1, 0, 1,
                     0.5f,  0.5f, -5,     0, 1, 0, 1
            }, 0, 7 * 6);
        }

        @Override
        public void update(float deltaTime) {
        }

        @Override
        public void present(float deltaTime) {
            GL10 gl = graphics.getGL();
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
            gl.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());

            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glOrthof( -1, 1, -1, 1, 10, -10);

            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();
            vertices.bind();
            vertices.draw(GL10.GL_TRIANGLES, 0, 6);
            vertices.unbind();
        }

        @Override
        public void resume() {
        }

        @Override
        public void pause() {
        }

        @Override
        public void dispose() {
        }
    }
}
