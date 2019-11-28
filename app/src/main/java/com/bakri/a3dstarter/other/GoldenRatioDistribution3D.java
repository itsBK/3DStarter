package com.bakri.a3dstarter.other;

import javax.microedition.khronos.opengles.GL10;

import framework.Game;
import framework.gl.Screen;
import framework.gl.camera.LookAtCamera;
import framework.gl.vertices.Vertices3;
import framework.input.Input;


import static javax.microedition.khronos.opengles.GL10.GL_COLOR_BUFFER_BIT;
import static javax.microedition.khronos.opengles.GL10.GL_DEPTH_BUFFER_BIT;
import static javax.microedition.khronos.opengles.GL10.GL_DEPTH_TEST;
import static javax.microedition.khronos.opengles.GL10.GL_EXP;
import static javax.microedition.khronos.opengles.GL10.GL_FOG;
import static javax.microedition.khronos.opengles.GL10.GL_FOG_COLOR;
import static javax.microedition.khronos.opengles.GL10.GL_FOG_DENSITY;
import static javax.microedition.khronos.opengles.GL10.GL_FOG_END;
import static javax.microedition.khronos.opengles.GL10.GL_FOG_MODE;
import static javax.microedition.khronos.opengles.GL10.GL_FOG_START;
import static javax.microedition.khronos.opengles.GL10.GL_LINEAR;
import static javax.microedition.khronos.opengles.GL10.GL_LINES;
import static javax.microedition.khronos.opengles.GL10.GL_LINE_STRIP;
import static javax.microedition.khronos.opengles.GL10.GL_POINTS;


public class GoldenRatioDistribution3D extends Game {

    @Override
    public Screen getStartScreen() {
        return new GoldenRatioScreen(this);
    }




    class GoldenRatioScreen extends Screen {

        private static final int MAX_POINTS = 1000;

        Vertices3 vertices;
        LookAtCamera camera;
        float[] coordinates;

        int lastX = -1;
        int lastY = -1;
        float lastAngle;

        float goldenRatio = (1 + (float) Math.sqrt(5)) / 2;
        float angleIncrement = 2 * (float) Math.PI * goldenRatio;

        int numPoints = 0;
        int j = 0;
        int primitiveType = GL_POINTS;


        GoldenRatioScreen(Game game) {
            super(game);
            coordinates = new float[7 * MAX_POINTS];

            vertices = new Vertices3(graphics, coordinates.length / 7, 0,
                    true, false, false);

            float ratio = graphics.getWidth() / (float) graphics.getHeight();
            camera = new LookAtCamera(67, ratio, 0.1f, 20f);
            camera.getPosition().set(0, 0, 2);
            camera.getLookAt().set(0, 0, 0);
        }

        @Override
        public void resume() {
            graphics.getGL().glViewport(0, 0, graphics.getWidth(), graphics.getHeight());
        }

        @Override
        public void update(float deltaTime) {
            j = 0;
            if (numPoints < MAX_POINTS)
                numPoints += 1;
            if (numPoints > MAX_POINTS)
                numPoints = MAX_POINTS;

            for (int i = 0; i < numPoints; i++) {
                float inclination = (float) Math.acos(1 - 2 * i / (float) numPoints);
                float azimuth = angleIncrement * i;

                coordinates[j++] = (float) (Math.sin(inclination) * Math.cos(azimuth));
                coordinates[j++] = (float) (Math.sin(inclination) * Math.sin(azimuth));
                coordinates[j++] = (float) Math.cos(inclination);

                if ((i + 1) % 8 == 0) {
                    coordinates[j++] = 0.44f;
                    coordinates[j++] = 0.07f;
                    coordinates[j++] = 0.99f;
                } else {
                    coordinates[j++] = 1.0f;
                    coordinates[j++] = 0.45f;
                    coordinates[j++] = 0.65f;
                }
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
                    numPoints = 0;
                    coordinates = new float[7 * MAX_POINTS];
                }

                if (lastX == -1) {
                    lastX = x;
                    lastY = y;
                } else {
                    lastAngle += (x - lastX) / 5;
                    lastX = x;
                    lastY = y;
                }

            } else {
                lastX = -1;
                lastY = -1;
            }
        }

        @Override
        public void present(float deltaTime) {
            GL10 gl = graphics.getGL();
            gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            camera.setMatrices(gl);

            gl.glEnable(GL_DEPTH_TEST);
            gl.glEnable(GL_FOG);
            gl.glFogf(GL_FOG_MODE, GL_LINEAR);
            gl.glFogf(GL_FOG_START, 1);
            gl.glFogf(GL_FOG_END, 3f);

            vertices.bind();
            gl.glRotatef(lastAngle, 0, 1, 0);
            gl.glPointSize(5);
            vertices.draw(primitiveType, 0, vertices.getNumVertices());
            vertices.unbind();

            gl.glDisable(GL_FOG);
            gl.glDisable(GL_DEPTH_TEST);
        }

        @Override
        public void pause() {

        }

        @Override
        public void dispose() {

        }
    }
}
