package com.bakri.a3dstarter.other;

import javax.microedition.khronos.opengles.GL10;

import framework.Game;
import framework.gl.Screen;
import framework.gl.camera.Camera2D;
import framework.gl.vertices.Vertices;
import framework.input.Input;


import static javax.microedition.khronos.opengles.GL10.GL_COLOR_BUFFER_BIT;
import static javax.microedition.khronos.opengles.GL10.GL_DEPTH_BUFFER_BIT;
import static javax.microedition.khronos.opengles.GL10.GL_LINES;
import static javax.microedition.khronos.opengles.GL10.GL_LINE_STRIP;
import static javax.microedition.khronos.opengles.GL10.GL_POINTS;

public class PointsDistribution2D extends Game {

    @Override
    public Screen getStartScreen() {
        return new PointsDistribution2DScreen(this);
    }




    class PointsDistribution2DScreen extends Screen {

        private static final int NUM_POINTS = 1000;

        Vertices vertices;
        Camera2D camera;

        float[] coordinates;
        float turnFraction = 1.0f;
        float increment;
        int j = 0;
        int primitiveType = GL_POINTS;


        PointsDistribution2DScreen(Game game) {
            super(game);

            coordinates = new float[6 * NUM_POINTS];

            vertices = new Vertices(graphics, coordinates.length / 6, 0,
                    true, false);

            float ratio = graphics.getWidth() / (float) graphics.getHeight();
            camera = new Camera2D(graphics, 6.4f * ratio, 6.4f);
            camera.getPosition().set(0, 0);
        }

        @Override
        public void resume() {
            graphics.getGL().glViewport(0, 0, graphics.getWidth(), graphics.getHeight());
        }

        @Override
        public void update(float deltaTime) {
            if (turnFraction <= 1.15) {
                increment += deltaTime * 60.0f * 0.0000001f;
                turnFraction += increment;
            }
            else {
                turnFraction += deltaTime * 60.0f * 0.00005f;
            }

            j = 0;

            for (int i = 0; i < NUM_POINTS; i++) {
                float dst = (float) Math.pow(i / (NUM_POINTS - 1f), -0.7f);
                float angle = 2 * (float) Math.PI * turnFraction * i;

                float x = dst * (float) Math.cos(angle);
                float y = dst * (float) Math.sin(angle);

                coordinates[j++] = x;
                coordinates[j++] = y;

                coordinates[j++] = 1.0f;
                coordinates[j++] = 0.45f;
                coordinates[j++] = 0.65f;
                coordinates[j++] = 1;

            }

            vertices.setVertices(coordinates);


            Input input = game.getInput();
            if (input.isTouchDown(0)) {

                int x = input.getTouchX(0);
                int y = input.getTouchY(0);

                float width = graphics.getWidth();
                float height = graphics.getHeight();

                if (x < 1.0f * width / 10 && y < 1.0f * height / 10) {
                    primitiveType = GL_LINES;

                } else if (x > 9.0f * width / 10 && y < 1.0f * height / 10) {
                    primitiveType = GL_LINE_STRIP;

                } else if (x < 1.0f * width / 10 && y > 9.0f * height / 10) {
                    primitiveType = GL_POINTS;

                } else if (x > 9.0f * width / 10 && y > 9.0f * height / 10) {
                    turnFraction = 1.0f;
                    increment = 0;
                }
            }
        }

        @Override
        public void present(float deltaTime) {
            GL10 gl = graphics.getGL();
            gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            camera.setMatrices();

            vertices.bind();
            gl.glPointSize(5);
            vertices.draw(primitiveType, 0, vertices.getNumVertices());
            vertices.unbind();
        }

        @Override
        public void pause() {

        }

        @Override
        public void dispose() {

        }
    }
}
