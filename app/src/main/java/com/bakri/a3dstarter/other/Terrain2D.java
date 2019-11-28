package com.bakri.a3dstarter.other;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import framework.gl.camera.EulerCamera;
import framework.Game;
import framework.gl.Screen;
import framework.gl.vertices.Vertices3;
import framework.math.noise.OpenSimplexNoise;
import framework.math.Vector3;

public class Terrain2D extends Game {

    @Override
    public Screen getStartScreen() {
        return new Terrain2DScreen(this);
    }






    class Terrain2DScreen extends Screen {

        EulerCamera camera;
        Vertices3 mesh;
        float lastX = -1, lastY = -1;

        Terrain2DScreen(Game game) {
            super(game);

            OpenSimplexNoise noise = new OpenSimplexNoise((new Random()).nextInt(2000000));

            int xCount = 120;
            int yCount = 120;
            float xOff =0 , yOff = 0;
            float random;
            float[] color;

            int i = 0;
            float[] vertices = new float[xCount * yCount * 7];
            for (int y = 0; y < xCount; y++) {
                for (int x = 0; x < yCount; x++) {

                    random = ((float) noise.eval(xOff, yOff) + 1) / 2;
                    color = randomToRGB(random);

                    vertices[i++] = x;
                    vertices[i++] = y;
                    vertices[i++] = random * 5;

                    vertices[i++] = color[0];
                    vertices[i++] = color[1];
                    vertices[i++] = color[2];
                    vertices[i++] = 1;

                    xOff += 0.1f;
                }
                xOff = 0;
                yOff += 0.1f;
            }

            i = 0;
            short[] indices = new short[(xCount - 1) * (yCount - 1) * 6];
            for (short y = 0; y < xCount - 1; y++) {
                for (short x = 0; x < yCount - 1; x++) {
                    indices[i++] = (short) ((y * xCount) + x);
                    indices[i++] = (short) ((y * xCount) + x + 1);
                    indices[i++] = (short) ((y + 1) * xCount + x);

                    indices[i++] = (short) ((y * xCount) + x + 1);
                    indices[i++] = (short) ((y + 1) * xCount + x);
                    indices[i++] = (short) ((y + 1) * xCount + x + 1);
                }
            }

            camera = new EulerCamera(67,
                    graphics.getWidth() / (float) graphics.getHeight(),
                    0.1f, 50f, 90, new Vector3(0, 0, 1));
            camera.getPosition().set((xCount - 1) / 2.0f, -5, 4);
            camera.setAngles(0, 90);

            mesh = new Vertices3(graphics, vertices.length / 7, indices.length,
                    true, false, false);
            mesh.setVertices(vertices, 0, vertices.length);
            mesh.setIndices(indices, 0, indices.length);
        }

        float[] randomToRGB(float random) {
            if (random >= 0.75f) {
                random -= 0.75f;
                random *= 4;
                random = 1 - random;
                return new float[] {1, random, 0};
            }

            if (random >= 0.5f) {
                random -= 0.5f;
                random *= 4;
                return new float[] {random, 1, 0};
            }

            if (random >= 0.25f) {
                random -= 0.25f;
                random *= 4;
                random = 1 - random;
                return new float[] {0, 1, random};
            }

            random *= 4;
            return new float[] {0, random, 1};
        }

        @Override
        public void resume() {

        }

        @Override
        public void update(float deltaTime) {
            game.getInput().getTouchEvents();
            game.getInput().getKeyEvents();

            if (game.getInput().isTouchDown(0)) {
                float x = game.getInput().getTouchX(0);
                float y = game.getInput().getTouchY(0);

                if (lastX == -1) {
                    lastX = x;
                    lastY = y;
                } else {
                    camera.rotate((x - lastX) / 10, (y - lastY) / 10);
                    lastX = x;
                    lastY = y;
                }
                if (x < graphics.getWidth() / 2) {
                    camera.getPosition().add(camera.getDirection().mul(-10 * deltaTime));
                } else {
                    camera.getPosition().add(camera.getDirection().mul(10 * deltaTime));
                }

            } else {
                lastX = lastY = -1;
            }

        }

        @Override
        public void present(float deltaTime) {
            GL10 gl = graphics.getGL();
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            gl.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());

            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            camera.setMatrices(gl);

            gl.glEnable(GL10.GL_DEPTH_TEST);

            mesh.bind();
            mesh.draw(GL10.GL_LINES, 0, mesh.getNumIndices());
            mesh.unbind();

            gl.glDisable(GL10.GL_DEPTH_TEST);
        }

        @Override
        public void pause() {

        }

        @Override
        public void dispose() {

        }
    }
}
