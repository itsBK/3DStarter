package com.bakri.a3dstarter.other;

import java.util.Random;

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


public class CylinderRays extends Game {

    @Override
    public Screen getStartScreen() {
        return new CylinderRaysScreen(this);
    }




    class CylinderRaysScreen extends Screen {

        private static final int MAX_POINTS = 300;

        Vertices3 vertices;
        LookAtCamera camera;
        float[] coordinates;

        int lastX = -1;
        int lastY = -1;
        float xAngle, yAngle;

        float goldenRatio = (1 + (float) Math.sqrt(5)) / 2;
        float angle = 2 * (float) Math.PI * goldenRatio;
        float radius;
        float dist;
        Vector3 origin;
        Vector3 dimension;
        Vector3 direction;
        Vector3 pointProjOnDir;
        Vector3 i, j;
        Vector3 dI, dJ;
        Vector3 startPoint;

        int rayCount;
        int k = 0;
        int primitiveType = GL_LINES;

        CylinderRaysScreen(Game game) {
            super(game);
            coordinates = new float[2 * 7 * MAX_POINTS + 4 * 7/*extra 2 for direction and range*/];
            vertices = new Vertices3(graphics, coordinates.length / 7, 0,
                    true, false, false);

            float ratio = graphics.getWidth() / (float) graphics.getHeight();
            camera = new LookAtCamera(77, ratio, 0.1f, 20f);
            camera.getPosition().set(2, 0, 3);
            camera.getLookAt().set(2, 0, 0);

            Random rand = new Random();

            origin = new Vector3(0, 0, 0);
            dimension = new Vector3(1,2,5);
            direction = new Vector3(1, 0, 0).nor();
            pointProjOnDir = new Vector3(direction);

            Vector3 randomPoint = new Vector3(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
            pointProjOnDir.mul(randomPoint.dotProduct(direction));

            radius = (dimension.x >= dimension.y)? dimension.x/2f : dimension.y/2f;
            float range = dimension.z;

            i = new Vector3(randomPoint).sub(pointProjOnDir).nor();
            j = i.crossProduct(direction).nor();
            dI = new Vector3();
            dJ = new Vector3();
            startPoint = new Vector3();

            rayCount = MAX_POINTS;
            long startTime = System.nanoTime();
            for (int l = 0; l < rayCount; l++) {
                dist = (float) Math.sqrt(l / (rayCount - 1f)) * radius;

                dI.set(i).mul(dist * (float) Math.cos(angle * l));
                dJ.set(j).mul(dist * (float) Math.sin(angle * l));
                startPoint.set(origin.x + dI.x + dJ.x,
                        origin.y + dI.y + dJ.y,
                        origin.z + dI.z + dJ.z);

                coordinates[k++] = startPoint.x;
                coordinates[k++] = startPoint.y;
                coordinates[k++] = startPoint.z;
                coordinates[k++] = 0f;
                coordinates[k++] = 1f;
                coordinates[k++] = 0f;
                coordinates[k++] = 1f;

                coordinates[k++] = startPoint.x + direction.x * range;
                coordinates[k++] = startPoint.y + direction.y * range;
                coordinates[k++] = startPoint.z + direction.z * range;
                coordinates[k++] = 0f;
                coordinates[k++] = 1f;
                coordinates[k++] = 0f;
                coordinates[k++] = 1f;
            }

            coordinates[k++] = origin.x;
            coordinates[k++] = origin.y;
            coordinates[k++] = origin.z;
            coordinates[k++] = 1f;
            coordinates[k++] = 1f;
            coordinates[k++] = 1f;
            coordinates[k++] = 1f;

            coordinates[k++] = origin.x + i.x;
            coordinates[k++] = origin.y + i.y;
            coordinates[k++] = origin.z + i.z;
            coordinates[k++] = 1f;
            coordinates[k++] = 1f;
            coordinates[k++] = 1f;
            coordinates[k++] = 1f;

            coordinates[k++] = origin.x;
            coordinates[k++] = origin.y;
            coordinates[k++] = origin.z;
            coordinates[k++] = 1f;
            coordinates[k++] = 1f;
            coordinates[k++] = 1f;
            coordinates[k++] = 1f;

            coordinates[k++] = origin.x + j.x;
            coordinates[k++] = origin.y + j.y;
            coordinates[k++] = origin.z + j.z;
            coordinates[k++] = 1f;
            coordinates[k++] = 1f;
            coordinates[k++] = 1f;
            coordinates[k++] = 1f;

            float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
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
