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
import static javax.microedition.khronos.opengles.GL10.GL_LINES;
import static javax.microedition.khronos.opengles.GL10.GL_LINE_STRIP;
import static javax.microedition.khronos.opengles.GL10.GL_POINTS;


public class PyramidRays extends Game {

    @Override
    public Screen getStartScreen() {
        return new PyramidRaysScreen(this);
    }




    class PyramidRaysScreen extends Screen {

        private static final int MAX_POINTS = 300;

        Vertices3 vertices;
        LookAtCamera camera;
        float[] coordinates;

        int lastX = -1;
        int lastY = -1;
        float xAngle, yAngle;

        Vector3 origin;
        Vector3 opening;
        Vector3 direction;
        Vector3 rangeProjOnDir;
        Vector3 i, j;
        Vector3 dI, dJ;
        Vector3 ray;

        int k = 0;
        int primitiveType = GL_LINES;


        PyramidRaysScreen(Game game) {
            super(game);

            float ratio = graphics.getWidth() / (float) graphics.getHeight();
            camera = new LookAtCamera(77, ratio, 0.1f, 20f);
            camera.getPosition().set(0, 0, 4);
            camera.getLookAt().set(0, 0, 0);

            origin = new Vector3(0, 0, 0);
            opening = new Vector3(1f,0.5f,0f).nor();
            direction = new Vector3(1, 0, 0).nor();
            float range = 5;

            int density = 10;

            rangeProjOnDir = new Vector3(direction);
            rangeProjOnDir.mul(opening.dotProduct(direction));
            i = new Vector3(opening).sub(rangeProjOnDir);
            float width = 2 * i.len();
            i.nor();
            j = i.crossProduct(direction).nor();

            dI = new Vector3();
            dJ = new Vector3();
            ray = new Vector3();

            int rayPerRow = (int) (width / rangeProjOnDir.len() * range * density);
            float step = width / rayPerRow;

            coordinates = new float[3 * 7 * rayPerRow * rayPerRow + 4 * 7/*extra 2 for direction and range*/];
            vertices = new Vertices3(graphics, coordinates.length / 7, 0,
                    true, false, false);

            long startTime = System.nanoTime();
            for (float y = -width/2; y < width/2; y += step) {
                for (float x = -width/2; x < width/2; x += step) {

                    dI.set(i).mul(x);
                    dJ.set(j).mul(y);
                    ray.set(rangeProjOnDir.x + dI.x + dJ.x,
                            rangeProjOnDir.y + dI.y + dJ.y,
                            rangeProjOnDir.z + dI.z + dJ.z).mul(range);

                    coordinates[k++] = origin.x;
                    coordinates[k++] = origin.y;
                    coordinates[k++] = origin.z;
                    coordinates[k++] = 0f;
                    coordinates[k++] = 1f;
                    coordinates[k++] = 0f;
                    coordinates[k++] = 1f;

                    coordinates[k++] = origin.x + ray.x;
                    coordinates[k++] = origin.y + ray.y;
                    coordinates[k++] = origin.z + ray.z;
                    coordinates[k++] = 0f;
                    coordinates[k++] = 1f;
                    coordinates[k++] = 0f;
                    coordinates[k++] = 1f;
                }
            }

            coordinates[k++] = origin.x;
            coordinates[k++] = origin.y;
            coordinates[k++] = origin.z;
            coordinates[k++] = 1f;
            coordinates[k++] = 1f;
            coordinates[k++] = 1f;
            coordinates[k++] = 1f;

            coordinates[k++] = origin.x + direction.x;
            coordinates[k++] = origin.y + direction.y;
            coordinates[k++] = origin.z + direction.z;
            coordinates[k++] = 1f;
            coordinates[k++] = 1f;
            coordinates[k++] = 1f;
            coordinates[k++] = 1f;

            coordinates[k++] = origin.x;
            coordinates[k++] = origin.y;
            coordinates[k++] = origin.z;
            coordinates[k++] = 0f;
            coordinates[k++] = 0f;
            coordinates[k++] = 1f;
            coordinates[k++] = 1f;

            coordinates[k++] = origin.x + opening.x;
            coordinates[k++] = origin.y + opening.y;
            coordinates[k++] = origin.z + opening.z;
            coordinates[k++] = 0f;
            coordinates[k++] = 0f;
            coordinates[k++] = 1f;
            coordinates[k++] = 1f;

            float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
            System.out.println("Total of " + (rayPerRow * rayPerRow) + " rays casted within: "
                    + deltaTime + " Second");
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

                }

                else if (x > 9.0f * width / 10 && y < 1.0f * height / 10) {
                    primitiveType = GL_LINE_STRIP;

                }

                else if (x < 1.0f * width / 10 && y > 9.0f * height / 10) {
                    primitiveType = GL_POINTS;

                }

                else if (x > 9.0f * width / 10 && y > 9.0f * height / 10) {
                    coordinates = new float[7 * MAX_POINTS];
                }

                if (lastX == -1) {
                    lastX = x;
                    lastY = y;
                } else {
                    xAngle += (x - lastX) / 5f;
                    yAngle += (y - lastY) / 5f;
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
            gl.glClearColor(0.5f, 0.5f, 0.5f, 0.3f);
            camera.setMatrices(gl);

            gl.glEnable(GL_DEPTH_TEST);

            vertices.bind();
            gl.glRotatef(xAngle, 0, 1, 0);
            gl.glRotatef(yAngle, 1, 0, 0);
            gl.glPointSize(5);
            vertices.draw(primitiveType, 0, vertices.getNumVertices());
            vertices.unbind();

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
