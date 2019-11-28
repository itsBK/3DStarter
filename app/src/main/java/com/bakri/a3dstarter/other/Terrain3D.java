package com.bakri.a3dstarter.other;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import framework.Game;
import framework.gl.Screen;
import framework.gl.camera.LookAtCamera;
import framework.gl.light.PointLight;
import framework.gl.vertices.Vertices3;
import framework.math.Vector3;
import framework.math.noise.OpenSimplexNoise;

import static com.bakri.a3dstarter.other.TriangulationsTable.triTable;
import static javax.microedition.khronos.opengles.GL10.GL_COLOR_BUFFER_BIT;
import static javax.microedition.khronos.opengles.GL10.GL_COLOR_MATERIAL;
import static javax.microedition.khronos.opengles.GL10.GL_DEPTH_BUFFER_BIT;
import static javax.microedition.khronos.opengles.GL10.GL_DEPTH_TEST;
import static javax.microedition.khronos.opengles.GL10.GL_LIGHT0;
import static javax.microedition.khronos.opengles.GL10.GL_LIGHTING;
import static javax.microedition.khronos.opengles.GL10.GL_TRIANGLES;

public class Terrain3D extends Game {

    @Override
    public Screen getStartScreen() {
        return new CrossProductScreen(this);
    }




    class CrossProductScreen extends Screen {

        Vertices3 terrain;
        LookAtCamera camera;
        PointLight pointLight;

        int lastX, lastY;
        float lastAngle;
        int surfaceLevel = 15;

        int[][][][] blocks;
        float[] vertices;

        int xCount = 40;
        int yCount = 25;
        int zCount = 40;

        int points = 0;
        int additionAmount = 0;

        float[][] edgePoints = {
                {0.5f,    0,    0},
                {   1,    0, 0.5f},
                {0.5f,    0,    1},
                {   0,    0, 0.5f},

                {0.5f,    1,    0},
                {   1,    1, 0.5f},
                {0.5f,    1,    1},
                {   0,    1, 0.5f},

                {   0, 0.5f,    0},
                {   1, 0.5f,    0},
                {   1, 0.5f,    1},
                {   0, 0.5f,    1}
        };


        CrossProductScreen(Game game) {
            super(game);

            blocks = new int[zCount][yCount][xCount][8];
            intializeBlocks(blocks, 0, 50, new Random().nextInt(2000000), 0.1f);
            vertices = intializeVertices(blocks);
            blocksToVertices(vertices, blocks, surfaceLevel);

            terrain = new Vertices3(graphics, vertices.length / 10, 0,
                    true, false, true);
            terrain.setVertices(vertices);

            camera = new LookAtCamera(67,
                    (float) graphics.getWidth() / graphics.getHeight(),
                    0.1f, 100f);

            camera.getLookAt().set(0, 0, 0);
            camera.getPosition().set(0, 0, zCount);

            pointLight = new PointLight();
            pointLight.setPosition(-zCount, zCount, zCount);
            pointLight.setAmbient(0.1f, 0.1f, 0.1f, 1);
            pointLight.setDiffuse(0.6f, 0.6f, 0.6f, 1);
        }

        @Override
        public void resume() {
            GL10 gl = graphics.getGL();
            gl.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());
            gl.glEnable(GL_DEPTH_TEST);
            gl.glEnable(GL_LIGHTING);
            gl.glEnable(GL_COLOR_MATERIAL);

            pointLight.enable(gl, GL_LIGHT0);
            terrain.bind();
        }

        @Override
        public void update(float deltaTime) {
            if (game.getInput().isTouchDown(0)) {

                int x = game.getInput().getTouchX(0);
                int y = game.getInput().getTouchY(0);

                float width = graphics.getWidth();
                float height = graphics.getHeight();

                if (x < 0.1f * width && y < 0.1f * height) {
                    blocksToVertices(vertices, blocks, surfaceLevel);
                    terrain.setVertices(vertices);

                } else if (x > 0.9f * width && y < 0.1f * height) {
                    blocksToVerticesNoInterpolation(vertices, blocks, surfaceLevel);
                    terrain.setVertices(vertices);

                } else if (x > 9.0f * width / 10 && y > 9.0f * height / 10) {
                    points = 0;
                    additionAmount = 0;
                }

                if (lastX == -1) {
                    lastX = x;
                    lastY = y;
                } else {
                    float angle = (x - lastX) / 5;
                    lastAngle += angle;
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

            gl.glRotatef(lastAngle, 0, 1, 0);
            gl.glTranslatef(-(xCount - 1) / 2, -(yCount - 1) / 2, -(zCount - 1) /2);
            terrain.draw(GL_TRIANGLES, 0, points);
            if (points < terrain.getNumVertices()) {
                points += additionAmount;
                additionAmount++;
            }
            else
                points = terrain.getNumVertices();

        }

        @Override
        public void pause() {
            terrain.unbind();
            pointLight.disable(graphics.getGL());
            graphics.getGL().glDisable(GL_DEPTH_TEST);
        }

        @Override
        public void dispose() {

        }



        private void intializeBlocks(int[][][][] blocks,
                                     int startValue, int endValue,
                                     int seed, float increment) {

            OpenSimplexNoise noise = new OpenSimplexNoise(seed);
            int[][][] points =  new int [blocks.length + 1]
                    [blocks[0].length + 1]
                    [blocks[0][0].length + 1];
            float noiseValue;
            int mappedValue;
            float xOff = 0;
            float yOff = 0;
            float zOff = 0;

            for (int z = 0; z < points.length; z++) {
                for (int y = 0; y < points[0].length; y++) {
                    for (int x = 0; x < points[0][0].length; x++) {

                        noiseValue = ((float) noise.eval(xOff, yOff, zOff) + 1) / 2;
                        mappedValue = (int) (noiseValue * (endValue - startValue)) + startValue;
                        points[z][y][x] = mappedValue;

                        xOff += increment;
                    }
                    xOff = 0;
                    yOff += increment;
                }
                yOff = 0;
                zOff += increment;
            }

            for (int z = 0; z < blocks.length; z++) {
                for (int y = 0; y < blocks[0].length; y++) {
                    for (int x = 0; x < blocks[0][0].length; x++) {

                        blocks[z][y][x] = new int[] {
                                points[z][y][x],
                                points[z][y][x + 1],
                                points[z + 1][y][x + 1],
                                points[z + 1][y][x],

                                points[z][y + 1][x],
                                points[z][y + 1][x + 1],
                                points[z + 1][y + 1][x + 1],
                                points[z + 1][y + 1][x]
                        };
                    }
                }
            }
        }

        private float[] intializeVertices(int[][][][] blocks) {
            int index;
            int j = 0;

            for (int[][][] zBlock : blocks) {
                for (int[][] yBlock : zBlock) {
                    for (int[] xBlock : yBlock) {

                        index = 0;
                        for (int i = 0; i < 8; i++)
                            if (xBlock[i] < surfaceLevel)
                                index += 1 << i;

                        for (int i = 0; i < 16; i++)
                            if (triTable[index][i] >= 0)
                                j++;
                    }
                }
            }

            return new float[j * 10];
        }

        private void blocksToVertices(float[] vertices,
                                      int[][][][] blocks,
                                      int surfaceLevel) {

            int index;
            int j = 0;

            float diff;
            int[] block;
            int[] b = {
                    1, 2, 2, 3,
                    5, 6, 6, 7,
                    4, 5, 6, 7
            };
            int[] s = {
                    0, 1, 3, 0,
                    4, 5, 7, 4,
                    0, 1, 2, 3,
            };
            int[] c = {
                    0, 2, 0, 2,
                    0, 2, 0, 2,
                    1, 1, 1, 1
            };

            for (int z = 0; z < blocks.length; z++) {
                for (int y = 0; y < blocks[0].length; y++) {
                    for (int x = 0; x < blocks[0][0].length; x++) {

                        index = 0;
                        for (int i = 0; i < 8; i++)
                            if (blocks[z][y][x][i] < surfaceLevel)
                                index += 1 << i;



                        if (index != 0 && index != 255) {

                            block = blocks[z][y][x];

                            for (int i = 0; i < 12; i++) {
                                if ((block[b[i]] - block[s[i]]) != 0)
                                    diff = (surfaceLevel - block[s[i]]) / (float) (block[b[i]] - block[s[i]]);
                                else
                                    diff = 0.5f;
                                edgePoints[i][c[i]] = diff;
                            }

                            for (int k = 0; k < 5; k++) {
                                if (triTable[index][k * 3 + 2] >= 0) {
                                    for (int i = 0; i < 3; i++) {
                                        vertices[j++] = edgePoints[triTable[index][k * 3 + i]][0] + x;
                                        vertices[j++] = edgePoints[triTable[index][k * 3 + i]][1] + y;
                                        vertices[j++] = edgePoints[triTable[index][k * 3 + i]][2] + z;

                                        vertices[j++] = 0.92f;
                                        vertices[j++] = 0.38f;
                                        vertices[j++] = 0.02f;
                                        /*
                                        color = randomToRGB((vertices[j-2]) / (yCount+1));
                                        vertices[j++] = color[0];
                                        vertices[j++] = color[1];
                                        vertices[j++] = color[2];

                                         */
                                        vertices[j++] = 1;

                                        vertices[j++] = 0;
                                        vertices[j++] = 0;
                                        vertices[j++] = 0;
                                    }

                                    Vector3 normals = computeNormals(
                                            new Vector3(
                                                    edgePoints[triTable[index][k * 3]][0] + x,
                                                    edgePoints[triTable[index][k * 3]][1] + y,
                                                    edgePoints[triTable[index][k * 3]][2] + z),
                                            new Vector3(
                                                    edgePoints[triTable[index][k * 3 + 1]][0] + x,
                                                    edgePoints[triTable[index][k * 3 + 1]][1] + y,
                                                    edgePoints[triTable[index][k * 3 + 1]][2] + z),
                                            new Vector3(
                                                    edgePoints[triTable[index][k * 3 + 2]][0] + x,
                                                    edgePoints[triTable[index][k * 3 + 2]][1] + y,
                                                    edgePoints[triTable[index][k * 3 + 2]][2] + z)
                                    );

                                    vertices[j - 3] = vertices[j - 13] = vertices[j - 23] = normals.x;
                                    vertices[j - 2] = vertices[j - 12] = vertices[j - 22] = normals.y;
                                    vertices[j - 1] = vertices[j - 11] = vertices[j - 21] = normals.z;
                                }
                            }
                        }
                    }
                }
            }
        }

        private void blocksToVerticesNoInterpolation(float[] vertices,
                                                     int[][][][] blocks,
                                                     int surfaceLevel) {

            int index;
            int j = 0;
            int[] h = {
                    0, 2, 0, 2,
                    0, 2, 0, 2,
                    1, 1, 1, 1
            };

            for (int i = 0; i < 12; i++) {
                edgePoints[i][h[i]] = 0.5f;
            }

            for (int z = 0; z < blocks.length; z++) {
                for (int y = 0; y < blocks[0].length; y++) {
                    for (int x = 0; x < blocks[0][0].length; x++) {

                        index = 0;
                        for (int i = 0; i < 8; i++)
                            if (blocks[z][y][x][i] < surfaceLevel)
                                index += 1 << i;

                        if (index != 0 && index != 255) {


                            for (int k = 0; k < 5; k++) {
                                if (triTable[index][k * 3 + 2] >= 0) {
                                    for (int i = 0; i < 3; i++) {
                                        vertices[j++] = edgePoints[triTable[index][k * 3 + i]][0] + x;
                                        vertices[j++] = edgePoints[triTable[index][k * 3 + i]][1] + y;
                                        vertices[j++] = edgePoints[triTable[index][k * 3 + i]][2] + z;

                                        j += 4;

                                        vertices[j++] = 0;
                                        vertices[j++] = 0;
                                        vertices[j++] = 0;
                                    }

                                    Vector3 normals = computeNormals(
                                            new Vector3(
                                                    edgePoints[triTable[index][k * 3]][0] + x,
                                                    edgePoints[triTable[index][k * 3]][1] + y,
                                                    edgePoints[triTable[index][k * 3]][2] + z),
                                            new Vector3(
                                                    edgePoints[triTable[index][k * 3 + 1]][0] + x,
                                                    edgePoints[triTable[index][k * 3 + 1]][1] + y,
                                                    edgePoints[triTable[index][k * 3 + 1]][2] + z),
                                            new Vector3(
                                                    edgePoints[triTable[index][k * 3 + 2]][0] + x,
                                                    edgePoints[triTable[index][k * 3 + 2]][1] + y,
                                                    edgePoints[triTable[index][k * 3 + 2]][2] + z)
                                    );

                                    vertices[j - 3] = vertices[j - 13] = vertices[j - 23] = normals.x;
                                    vertices[j - 2] = vertices[j - 12] = vertices[j - 22] = normals.y;
                                    vertices[j - 1] = vertices[j - 11] = vertices[j - 21] = normals.z;
                                }
                            }
                        }
                    }
                }
            }
        }

        private Vector3 computeNormals(Vector3 pointA, Vector3 pointB, Vector3 pointC) {
            Vector3 a = pointB.cpy().sub(pointA);
            Vector3 b = pointC.cpy().sub(pointB);

            return new Vector3(
                    a.y * b.z  -  a.z * b.y,
                    a.x * b.z  -  a.z * b.x,
                    a.x * b.y  -  a.y * b.x).nor();
        }
    }
}
