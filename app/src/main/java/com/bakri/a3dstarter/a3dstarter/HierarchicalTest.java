package com.bakri.a3dstarter.a3dstarter;

import android.opengl.GLU;

import javax.microedition.khronos.opengles.GL10;

import framework.Game;
import framework.gl.Screen;
import framework.gl.texture.Texture;
import framework.gl.vertices.Vertices3;

public class HierarchicalTest extends Game {

    @Override
    public Screen getStartScreen() {
        return new HierarchicalScreen(this);
    }





    class HierarchicalScreen extends Screen {

        Vertices3 cube;
        Texture texture;
        HierarchicalObject sun;


        public HierarchicalScreen(Game game) {
            super(game);
            texture = new Texture((Game) game, "crate.png");
            cube = createCube();

            sun = new HierarchicalObject(cube, false);
            sun.z = -5f;

            HierarchicalObject planet = new HierarchicalObject(cube, true);
            planet.x = 3;
            planet.scale = 0.2f;
            sun.children.add(planet);

            HierarchicalObject moon = new HierarchicalObject(cube, true, 180);
            moon.x = 1;
            moon.scale = 0.1f;
            planet.children.add(moon);
        }

        private Vertices3 createCube() {
            float[] vertices = new float[] {
                    //front
                    -0.5f, -0.5f,  0.5f,    0, 1, //0
                     0.5f, -0.5f,  0.5f,    1, 1, //1
                     0.5f,  0.5f,  0.5f,    1, 0, //2
                    -0.5f,  0.5f,  0.5f,    0, 0, //3

                    //right
                     0.5f, -0.5f,  0.5f,    0, 1, //1
                     0.5f, -0.5f, -0.5f,    1, 1, //5
                     0.5f,  0.5f, -0.5f,    1, 0, //6
                     0.5f,  0.5f,  0.5f,    0, 0, //2

                    //behind
                     0.5f, -0.5f, -0.5f,    0, 1, //5
                    -0.5f, -0.5f, -0.5f,    1, 1, //4
                    -0.5f,  0.5f, -0.5f,    1, 0, //7
                     0.5f,  0.5f, -0.5f,    0, 0, //6

                    //left
                    -0.5f, -0.5f, -0.5f,    0, 1, //4
                    -0.5f, -0.5f,  0.5f,    1, 1, //0
                    -0.5f,  0.5f,  0.5f,    1, 0, //3
                    -0.5f,  0.5f, -0.5f,    0, 0, //7

                    //top
                    -0.5f,  0.5f,  0.5f,    0, 1, //3
                     0.5f,  0.5f,  0.5f,    1, 1, //2
                     0.5f,  0.5f, -0.5f,    1, 0, //6
                    -0.5f,  0.5f, -0.5f,    0, 0, //7

                    //bottom
                    -0.5f, -0.5f, -0.5f,    0, 1, //4
                     0.5f, -0.5f, -0.5f,    1, 1, //5
                     0.5f, -0.5f,  0.5f,    1, 0, //1
                    -0.5f, -0.5f,  0.5f,    0, 0  //0
            };

            short[] indices = new short[] {
                     0,  1,  3,     1,  2,  3,
                     4,  5,  7,     5,  6,  7,
                     8,  9, 11,     9, 10, 11,
                    12, 13, 15,    13, 14, 15,
                    16, 17, 19,    17, 18, 19,
                    20, 21, 23,    21, 22, 23
            };

            Vertices3 cube = new Vertices3(graphics, 24, 36,
                    false, true, false);
            cube.setVertices(vertices, 0, vertices.length);
            cube.setIndices(indices, 0, indices.length);
            return cube;
        }

        @Override
        public void update(float deltaTime) {
            sun.update(deltaTime);
        }

        @Override
        public void present(float deltaTime) {
            GL10 gl = graphics.getGL();
            gl.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            GLU.gluPerspective(gl, 67,
                               graphics.getWidth() / (float) graphics.getHeight(),
                               0.1f, 12);

            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();
            GLU.gluLookAt(gl,
                    3, 3, 0,
                    0, 0, -5,
                    0, 1, 0);

            gl.glEnable(GL10.GL_TEXTURE_2D);
            gl.glEnable(GL10.GL_DEPTH_TEST);

            texture.bind();
            cube.bind();
            sun.render(gl);
            cube.unbind();

            gl.glDisable(GL10.GL_DEPTH_TEST);
            gl.glDisable(GL10.GL_TEXTURE_2D);
        }

        @Override
        public void resume() {
            texture.load();
        }

        @Override
        public void pause() {
        }

        @Override
        public void dispose() {
        }
    }
}
