package com.bakri.a3dstarter.other;

import javax.microedition.khronos.opengles.GL10;

import framework.Game;
import framework.gl.Screen;
import framework.gl.camera.LookAtCamera;
import framework.gl.light.PointLight;
import framework.gl.vertices.Vertices3;
import framework.math.Vector2;
import framework.math.Vector3;

public class CubeWave extends Game {
    @Override
    public Screen getStartScreen() {
        return new CubeWaveScreen(this);
    }

    enum Phase {INCREASE, DECREASE}

    class Column {
        Vector3 position;
        float length;
        final float minLength;
        final float maxLength;
        float diff;
        Phase phase = Phase.DECREASE;

        Column(float x, float y, float z, float diff, float minLength, float maxLength) {
            position = new Vector3(x, y, z);
            this.minLength = minLength;
            this.maxLength = maxLength;
            this.length = (maxLength - minLength) / 2;
            this.diff = diff;
        }

        void update(float deltaTime) {
            if (length >= maxLength) {
                phase = Phase.DECREASE;
                length = maxLength;
            }
            if (length <= minLength) {
                phase = Phase.INCREASE;
                length = minLength;
            }

            if (phase == Phase.INCREASE)
                increase(diff * deltaTime);
            else if (phase == Phase.DECREASE)
                increase(-diff * deltaTime);
        }

        void increase(float inc) {
            this.length += inc;
        }
    }

    class CubeWaveScreen extends Screen {

        LookAtCamera camera;
        Vertices3 vertices;
        PointLight light;

        int count = 18;
        Column[][] columns = new Column[count][count];


        CubeWaveScreen(Game game) {
            super(game);
            float ratio = (float) getGraphics().getWidth() / getGraphics().getHeight();
            camera = new LookAtCamera(70, ratio, 0.1f, 50f);
            camera.getLookAt().set(0, 0, 0);
            camera.getPosition().set(-20, 20, 20);

            light = new PointLight();
            light.setDiffuse(0.2f, 0.6f, 0.2f, 1);
            light.setPosition(-18, 5, 0);

            vertices = new Vertices3(getGraphics(), 24, 36,
                    true, false, true);

            float[] coords = new float[]{
                    //front
                    -0.5f, -1f, 0.5f, 0.13f, 0.545f, 0.13f, 1, 0, 0, 1,  //0
                    0.5f, -1f, 0.5f, 0.13f, 0.545f, 0.13f, 1, 0, 0, 1,  //1
                    0.5f, 1f, 0.5f, 0.13f, 0.545f, 0.13f, 1, 0, 0, 1,  //2
                    -0.5f, 1f, 0.5f, 0.13f, 0.545f, 0.13f, 1, 0, 0, 1,  //3

                    //right
                    0.5f, -1f, 0.5f, 0.13f, 0.545f, 0.13f, 1, 1, 0, 0,  //1
                    0.5f, -1f, -0.5f, 0.13f, 0.545f, 0.13f, 1, 1, 0, 0,  //5
                    0.5f, 1f, -0.5f, 0.13f, 0.545f, 0.13f, 1, 1, 0, 0,  //6
                    0.5f, 1f, 0.5f, 0.13f, 0.545f, 0.13f, 1, 1, 0, 0,  //2

                    //behind
                    0.5f, -1f, -0.5f, 0.13f, 0.545f, 0.13f, 1, 0, 0, -1, //5
                    -0.5f, -1f, -0.5f, 0.13f, 0.545f, 0.13f, 1, 0, 0, -1, //4
                    -0.5f, 1f, -0.5f, 0.13f, 0.545f, 0.13f, 1, 0, 0, -1, //7
                    0.5f, 1f, -0.5f, 0.13f, 0.545f, 0.13f, 1, 0, 0, -1, //6

                    //left
                    -0.5f, -1f, -0.5f, 0.13f, 0.545f, 0.13f, 1, -1, 0, 0,  //4
                    -0.5f, -1f, 0.5f, 0.13f, 0.545f, 0.13f, 1, -1, 0, 0,  //0
                    -0.5f, 1f, 0.5f, 0.13f, 0.545f, 0.13f, 1, -1, 0, 0,  //3
                    -0.5f, 1f, -0.5f, 0.13f, 0.545f, 0.13f, 1, -1, 0, 0,  //7

                    //top
                    -0.5f, 1f, 0.5f, 0.13f, 0.545f, 0.13f, 1, 0, 1, 0,  //3
                    0.5f, 1f, 0.5f, 0.13f, 0.545f, 0.13f, 1, 0, 1, 0,  //2
                    0.5f, 1f, -0.5f, 0.13f, 0.545f, 0.13f, 1, 0, 1, 0,  //6
                    -0.5f, 1f, -0.5f, 0.13f, 0.545f, 0.13f, 1, 0, 1, 0,  //7

                    //bottom
                    -0.5f, -1f, -0.5f, 0.13f, 0.545f, 0.13f, 1, 0, -1, 0, //4
                    0.5f, -1f, -0.5f, 0.13f, 0.545f, 0.13f, 1, 0, -1, 0, //5
                    0.5f, -1f, 0.5f, 0.13f, 0.545f, 0.13f, 1, 0, -1, 0, //1
                    -0.5f, -1f, 0.5f, 0.13f, 0.545f, 0.13f, 1, 0, -1, 0  //0
            };

            short[] indices = new short[]{
                    0, 1, 3, 1, 2, 3,
                    4, 5, 7, 5, 6, 7,
                    8, 9, 11, 9, 10, 11,
                    12, 13, 15, 13, 14, 15,
                    16, 17, 19, 17, 18, 19,
                    20, 21, 23, 21, 22, 23
            };

            vertices.setVertices(coords);
            vertices.setIndices(indices);

            float middle = count / 2f;
            Vector2 mid = new Vector2(middle, middle);
            Vector2 edgeMid1 = new Vector2(0, middle);
            Vector2 edgeMid2 = new Vector2(middle, 0);
            Vector2 edgeMid3 = new Vector2(count, middle);
            Vector2 edgeMid4 = new Vector2(middle, count);
            float radius = mid.dist(0, 0) / 2;


            for (int y = 0; y < count; y++)
                for (int x = 0; x < count; x++) {
                    float maxLength = Math.abs(mid.dist(x, y) - radius);
                    float diff = Math.min(edgeMid1.dist(x, y),
                            Math.min(edgeMid2.dist(x, y),
                            Math.min(edgeMid3.dist(x, y),
                            edgeMid4.dist(x, y))));

                    columns[y][x] = new Column(
                            x - middle, 0, y - middle,
                            diff, 0.1f, 0.1f + maxLength);
                }
        }

        @Override
        public void resume() {
            GL10 gl = graphics.getGL();
            gl.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());
            gl.glEnable(GL10.GL_DEPTH_TEST);
            gl.glEnable(GL10.GL_LIGHTING);
            gl.glEnable(GL10.GL_COLOR_MATERIAL);

            light.enable(gl, GL10.GL_LIGHT0);
        }

        @Override
        public void update(float deltaTime) {
            for (int y = 0; y < count; y++) {
                for (int x = 0; x < count; x++) {
                    columns[y][x].update(deltaTime);
                }
            }
        }

        @Override
        public void present(float deltaTime) {
            GL10 gl = graphics.getGL();
            gl.glClear(GL10.GL_DEPTH_BUFFER_BIT | GL10.GL_COLOR_BUFFER_BIT);
            camera.setMatrices(gl);


            vertices.bind();
            for (int y = 0; y < count; y++)
                for (int x = 0; x < count; x++) {
                    Vector3 pos = columns[y][x].position;
                    float length = columns[y][x].length;

                    gl.glScalef(1, length, 1);
                    gl.glTranslatef(pos.x, pos.y, pos.z);
                    vertices.draw();
                    gl.glTranslatef(-pos.x, -pos.y, -pos.z);
                    gl.glScalef(1, 1f / length, 1);
                }
            vertices.unbind();

        }

        @Override
        public void pause() {
            GL10 gl = graphics.getGL();
            gl.glDisable(GL10.GL_LIGHT0);
            gl.glDisable(GL10.GL_DEPTH_TEST);
        }

        @Override
        public void dispose() {

        }
    }

    float map(float value, float start, float end, float toStart, float toEnd) {
        return (value - start) / (end - start) * (toEnd - toStart) + toStart;
    }
}
