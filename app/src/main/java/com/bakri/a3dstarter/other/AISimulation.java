package com.bakri.a3dstarter.other;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import framework.AI.Boids2D;
import framework.Game;
import framework.gl.Screen;
import framework.gl.camera.Camera2D;
import framework.gl.vertices.Vertices;
import framework.input.Input.TouchEvent;


import static javax.microedition.khronos.opengles.GL10.GL_COLOR_BUFFER_BIT;
import static javax.microedition.khronos.opengles.GL10.GL_DEPTH_BUFFER_BIT;

public class AISimulation extends Game {


    @Override
    public Screen getStartScreen() {
        return new Boids2DScreen(this);
    }



    class Boids2DScreen extends Screen {

        Camera2D camera;
        Boids2D boids2D;
        List<TouchEvent> touchEvents;


        Boids2DScreen(Game game) {
            super(game);

            float ratio = graphics.getWidth() / (float) graphics.getHeight();
            float height = 10f;
            float width = height * ratio;
            camera = new Camera2D(graphics, width, height);

            boids2D = new Boids2D(100, 0.3f, 2f, 2f,
                    width, height);

            boids2D.setVertices(new Vertices(graphics, 3, 0,
                    false, false));
            boids2D.getVertices().setVertices(new float[] {
                    -0.05f,  0.05f,
                    -0.05f, -0.05f,
                     0.10f,     0f
            });
        }


        @Override
        public void update(float deltaTime) {
            touchEvents = game.getInput().getTouchEvents();
            int len = touchEvents.size();

            for (int i = 0; i < len; i++) {
                if (touchEvents.get(i).type == TouchEvent.TOUCH_UP) {
                    int x = touchEvents.get(i).x;
                    int y = touchEvents.get(i).y;

                    float width = graphics.getWidth();
                    float height = graphics.getHeight();

                    if (x < 0.1f * width && y < 0.1f * height) {
                        boids2D.avoidance = !boids2D.avoidance;

                    } else if (x > 0.9f * width && y < 0.1f * height) {
                        boids2D.alignment = !boids2D.alignment;

                    } else if (x < 0.1f * width && y > 0.9f * height) {
                        boids2D.cohesion = !boids2D.cohesion;

                    } else if (x > 0.9f * width && y > 0.9f * height) {
                        boids2D.avoidance = boids2D.alignment = boids2D.cohesion = false;
                    }

                    break;
                }
            }

            boids2D.update(deltaTime);
        }

        @Override
        public void resume() {
            boids2D.getVertices().bind();
        }

        @Override
        public void present(float deltaTime) {
            GL10 gl = graphics.getGL();
            gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            camera.setMatrices();

            boids2D.draw(gl);
        }

        @Override
        public void pause() {
            boids2D.getVertices().unbind();
        }

        @Override
        public void dispose() {

        }
    }
}
