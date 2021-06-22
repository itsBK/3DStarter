package com.bakri.a3dstarter.other;

import javax.microedition.khronos.opengles.GL10;

import framework.Game;
import framework.gl.Screen;
import framework.gl.camera.LookAtCamera;
import framework.gl.vertices.Vertices3;
import framework.input.Input;
import framework.math.Vector3;

import static javax.microedition.khronos.opengles.GL10.GL_COLOR_BUFFER_BIT;
import static javax.microedition.khronos.opengles.GL10.GL_DEPTH_BUFFER_BIT;
import static javax.microedition.khronos.opengles.GL10.GL_DEPTH_TEST;
import static javax.microedition.khronos.opengles.GL10.GL_FOG;
import static javax.microedition.khronos.opengles.GL10.GL_FOG_END;
import static javax.microedition.khronos.opengles.GL10.GL_FOG_MODE;
import static javax.microedition.khronos.opengles.GL10.GL_FOG_START;
import static javax.microedition.khronos.opengles.GL10.GL_LINEAR;
import static javax.microedition.khronos.opengles.GL10.GL_LINES;
import static javax.microedition.khronos.opengles.GL10.GL_LINE_STRIP;
import static javax.microedition.khronos.opengles.GL10.GL_POINTS;


public class ConeDistribution extends Game {

    @Override
    public Screen getStartScreen() {
        return new ConeScreen(this);
    }




    class ConeScreen extends Screen {

        private static final int MAX_POINTS = 300;

        Vertices3 vertices;
        LookAtCamera camera;
        float[] coordinates;

        int lastX = -1;
        int lastY = -1;
        float lastAngle;

        float goldenRatio = (1 + (float) Math.sqrt(5)) / 2;
        float angleIncrement = 2 * (float) Math.PI * goldenRatio;
        Vector3 range;
        Vector3 direction;
        float distance;
        float x, y, z;

        int totalNumOfPoints;
        int j = 0;
        int primitiveType = GL_LINES;


        ConeScreen(Game game) {
            super(game);
            coordinates = new float[2 * 7 * MAX_POINTS];
            range = new Vector3(1f,0.3f,0).nor();
            direction = new Vector3(1, 0f, 0).nor();
            distance = range.distSquared(direction);

            vertices = new Vertices3(graphics, coordinates.length / 7, 0,
                    true, false, false);

            float ratio = graphics.getWidth() / (float) graphics.getHeight();
            camera = new LookAtCamera(67, ratio, 0.1f, 20f);
            camera.getPosition().set(0, 0, 2f);
            camera.getLookAt().set(0, 0, 0);

            totalNumOfPoints = MAX_POINTS;
            int numPoints = 0;
            double sin;

            long startTime = System.nanoTime();
            for (int k = 0; k < 2; k++) {
                for (int i = 0; i < totalNumOfPoints; i++) {
                    double inclination = Math.acos(1 - 2 * i / (float) totalNumOfPoints);
                    float azimuth = angleIncrement * i;

                    sin = Math.sin(inclination);
                    x = (float) (sin * Math.cos(azimuth));
                    y = (float) (sin * Math.sin(azimuth));
                    z = (float) Math.cos(inclination);

                    if (direction.distSquared(x, y, z) <= distance) {
                        numPoints++;

                        if (k == 1) {
                            coordinates[j++] = 0;
                            coordinates[j++] = 0;
                            coordinates[j++] = 0;
                            coordinates[j++] = 0f;
                            coordinates[j++] = 1f;
                            coordinates[j++] = 0f;
                            coordinates[j++] = 1f;

                            coordinates[j++] = x;
                            coordinates[j++] = y;
                            coordinates[j++] = z;
                            coordinates[j++] = 0f;
                            coordinates[j++] = 1f;
                            coordinates[j++] = 0f;
                            coordinates[j++] = 1f;
                        }
                    }
                }

                if (numPoints < MAX_POINTS * 9 / 10 && k == 0) {
                    int q = (int) (0.98f * totalNumOfPoints / (numPoints + 1));
                    totalNumOfPoints *= q;
                    System.out.println("multiplied by: " + q);
                    j = 0;
                }
            }
            float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
            System.out.println("Total Points needed: " + totalNumOfPoints);
            System.out.println("Total Points Rendered: " + numPoints);
            System.out.println("Total time needed: " + deltaTime);
            vertices.setVertices(coordinates);
        }

        @Override
        public void resume() {
            graphics.getGL().glViewport(0, 0, graphics.getWidth(), graphics.getHeight());
        }

        @Override
        public void update(float deltaTime) {
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
                    totalNumOfPoints = 0;
                    coordinates = new float[7 * MAX_POINTS];
                }

                if (lastX == -1) {
                    lastX = x;
                    lastY = y;
                } else {
                    lastAngle += (x - lastX) / 5f;
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
