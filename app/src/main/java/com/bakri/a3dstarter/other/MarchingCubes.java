package com.bakri.a3dstarter.other;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import framework.Game;
import framework.gl.Screen;
import framework.gl.camera.LookAtCamera;
import framework.gl.vertices.Vertices3;
import framework.math.noise.OpenSimplexNoise;

import static com.bakri.a3dstarter.other.TriangulationsTable.triTable;

import static javax.microedition.khronos.opengles.GL10.GL_COLOR_BUFFER_BIT;
import static javax.microedition.khronos.opengles.GL10.GL_DEPTH_BUFFER_BIT;
import static javax.microedition.khronos.opengles.GL10.GL_DEPTH_TEST;
import static javax.microedition.khronos.opengles.GL10.GL_LINES;

public class MarchingCubes extends Game {
    @Override
    public Screen getStartScreen() {
        return new MarchingCubesScreen(this);
    }




    class MarchingCubesScreen extends Screen {

        Vertices3 terrain;
        LookAtCamera camera;
        OpenSimplexNoise noise;

        int lastX = -1, lastY = -1;
        float lastAngle = 0;
        int surfaceLevel = 20;

        int points = 0;
        int additionAmount = 0;

        int xCount = 40;
        int yCount = 25;
        int zCount = 40;

        float[] vertices;
        int[][][] pointsTable;
        float[][] edgePointPosition = {
                {    0, -0.5f, -0.5f},
                { 0.5f, -0.5f,     0},
                {    0, -0.5f,  0.5f},
                {-0.5f, -0.5f,     0},

                {    0,  0.5f, -0.5f},
                { 0.5f,  0.5f,     0},
                {    0,  0.5f,  0.5f},
                {-0.5f,  0.5f,     0},

                {-0.5f,     0, -0.5f},
                { 0.5f,     0, -0.5f},
                { 0.5f,     0,  0.5f},
                {-0.5f,     0,  0.5f}
        };


        MarchingCubesScreen(Game game) {
            super(game);

            noise = new OpenSimplexNoise(new Random().nextInt(2000000));
            float xOff = 0 , yOff = 0 , zOff = 0;
            float random;
            int randomMapped;
            pointsTable = new int[zCount][yCount][xCount];

            for (int z = 1; z < zCount - 1; z++) {
                for (int y = 1; y < yCount - 1; y++) {
                    for (int x = 1; x < xCount - 1; x++) {

                        random = ((float) noise.eval(xOff, yOff, zOff) + 1) / 2;
                        randomMapped = (int) (random * 50);

                        if (randomMapped >= surfaceLevel)
                            pointsTable[z][y][x] = 0;
                        else
                            pointsTable[z][y][x] = 1;

                        xOff += 0.1f;
                    }
                    xOff = 0;
                    yOff += 0.1f;
                }
                yOff = 0;
                zOff += 0.1f;
            }

            int index;
            int j = 0;
            for (int z = 0; z < zCount - 1; z++) {
                for (int y = 0; y < yCount - 1; y++) {
                    for (int x = 0; x < xCount - 1; x++) {
                        index =         pointsTable[z  ][y  ][x  ]
                                + 2 *   pointsTable[z  ][y  ][x+1]
                                + 4 *   pointsTable[z+1][y  ][x+1]
                                + 8 *   pointsTable[z+1][y  ][x  ]
                                + 16 *  pointsTable[z  ][y+1][x  ]
                                + 32 *  pointsTable[z  ][y+1][x+1]
                                + 64 *  pointsTable[z+1][y+1][x+1]
                                + 128 * pointsTable[z+1][y+1][x  ];

                        for (int i = 0; i < 16; i++) {
                            if (triTable[index][i] >= 0) {
                                j++;
                            }
                        }
                    }
                }
            }

            vertices = new float[j * 7];
            j = 0;
            for (int z = 0; z < zCount - 1; z++) {
                for (int y = 0; y < yCount - 1; y++) {
                    for (int x = 0; x < xCount - 1; x++) {

                        index =         pointsTable[z  ][y  ][x  ]
                                + 2   * pointsTable[z  ][y  ][x+1]
                                + 4   * pointsTable[z+1][y  ][x+1]
                                + 8   * pointsTable[z+1][y  ][x  ]
                                + 16  * pointsTable[z  ][y+1][x  ]
                                + 32  * pointsTable[z  ][y+1][x+1]
                                + 64  * pointsTable[z+1][y+1][x+1]
                                + 128 * pointsTable[z+1][y+1][x  ];

                        for (int i = 0; i < 16; i++) {
                            if (triTable[index][i] >= 0) {
                                vertices[j++] = edgePointPosition[triTable[index][i]][0] + x;
                                vertices[j++] = edgePointPosition[triTable[index][i]][1] + y;
                                vertices[j++] = edgePointPosition[triTable[index][i]][2] + z;

                                vertices[j++] = 0.92f;
                                vertices[j++] = 0.38f;
                                vertices[j++] = 0.02f;
                                vertices[j++] = 1;
                            }
                        }

                    }
                }
            }


            terrain = new Vertices3(graphics, vertices.length / 7, 0,
                    true, false, false);
            terrain.setVertices(vertices);

            camera = new LookAtCamera(67,
                    (float) graphics.getWidth() / graphics.getHeight(),
                    0.1f, 100f);

            camera.getLookAt().set(0, 0, 0);
            camera.getPosition().set(0, 0, zCount);
    }
        @Override
        public void resume() {
            graphics.getGL().glViewport(0, 0, graphics.getWidth(), graphics.getHeight());
        }

        @Override
        public void update(float deltaTime) {
            if (game.getInput().isTouchDown(0)) {

                int x = game.getInput().getTouchX(0);
                int y = game.getInput().getTouchY(0);

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

            gl.glRotatef(lastAngle, 0, 1, 0);
            gl.glTranslatef(-(xCount - 1) / 2, -(yCount - 1) / 2, -(zCount - 1) /2);
            terrain.bind();
            terrain.draw(GL_LINES, 0, points);
            if (points < terrain.getNumVertices()) {
                points += additionAmount;
                additionAmount++;
            }
            else
                points = terrain.getNumVertices();
            terrain.unbind();

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
