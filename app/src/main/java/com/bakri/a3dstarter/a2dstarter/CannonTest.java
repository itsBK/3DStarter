package com.bakri.a3dstarter.a2dstarter;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import framework.Game;
import framework.gl.Graphics;
import framework.gl.Screen;
import framework.gl.vertices.Vertices;
import framework.input.Input.TouchEvent;
import framework.math.Vector2;

public class CannonTest extends Game {

    @Override
    public Screen getStartScreen() {
        return new CannonScreen(this);
    }




    class CannonScreen extends Screen {

        float FRUSTUM_WIDTH = 4.8f;
        float FRUSTUM_HEIGHT = 3.2f;
        Graphics graphics;
        Vertices vertices;
        Vector2 cannonPos = new Vector2(2.4f, 0.5f);
        float cannonAngle = 0;
        Vector2 touchPos = new Vector2();


        public CannonScreen(Game game) {
            super(game);
            graphics = game.getGraphics();
            vertices = new Vertices(graphics, 3, 0, false, false);
            vertices.setVertices(new float[] {
                    -0.5f, -0.5f,
                    0.5f, 0.0f,
                    -0.5f, 0.5f }, 0, 6);
        }

        @Override
        public void update(float deltaTime) {
            List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
            game.getInput().getKeyEvents();

            int len = touchEvents.size();
            for (int i = 0; i < len; i++) {
                TouchEvent event = touchEvents.get(i);

                touchPos.x = (event.x / (float) graphics.getWidth())
                        * FRUSTUM_WIDTH;
                touchPos.y = (1.0f - event.y / (float) graphics.getHeight())
                        * FRUSTUM_HEIGHT;
                cannonAngle = touchPos.sub(cannonPos).angle();
            }
        }

        @Override
        public void present(float deltaTime) {
            GL10 gl = graphics.getGL();

            gl.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glOrthof(0, FRUSTUM_WIDTH, 0, FRUSTUM_HEIGHT, 1, -1);
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();

            gl.glTranslatef(cannonPos.x, cannonPos.y, 0);
            gl.glRotatef(cannonAngle, 0, 0, 1);
            vertices.bind();
            vertices.draw(GL10.GL_TRIANGLES, 0, 3);
            vertices.unbind();
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
