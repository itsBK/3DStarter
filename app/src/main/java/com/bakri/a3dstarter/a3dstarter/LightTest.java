package com.bakri.a3dstarter.a3dstarter;

import android.opengl.GLU;

import javax.microedition.khronos.opengles.GL10;

import framework.gl.light.AmbientLight;
import framework.gl.light.DirectionalLight;
import framework.Game;
import framework.gl.Screen;
import framework.gl.light.PointLight;
import framework.gl.texture.Texture;
import framework.gl.vertices.Vertices3;

public class LightTest extends Game {

    @Override
    public Screen getStartScreen() {
        return new LightScreen(this);
    }





    public class LightScreen extends Screen {

        float angle;
        Vertices3 cube;
        Texture texture;
        AmbientLight ambientLight;
        PointLight pointLight;
        DirectionalLight directionalLight;

        LightScreen(Game game) {
            super(game);

            cube = createCube();
            texture = new Texture(this.game, "crate.png");

            ambientLight = new AmbientLight();
            ambientLight.setColor(0, 0.2f, 0, 1);

            pointLight = new PointLight();
            pointLight.setDiffuse(1, 0, 0, 1);
            pointLight.setPosition(3, 3, 0);

            directionalLight = new DirectionalLight();
            directionalLight.setDiffuse(0, 0, 1, 1);
            directionalLight.setDirection(1, 0, 0);
        }

        private Vertices3 createCube() {
            float[] vertices = new float[] {
                    //front
                    -0.5f, -0.5f,  0.5f,    0, 1,    0, 0, 1, //0
                     0.5f, -0.5f,  0.5f,    1, 1,    0, 0, 1, //1
                     0.5f,  0.5f,  0.5f,    1, 0,    0, 0, 1, //2
                    -0.5f,  0.5f,  0.5f,    0, 0,    0, 0, 1, //3

                    //right
                     0.5f, -0.5f,  0.5f,    0, 1,    1, 0, 0, //1
                     0.5f, -0.5f, -0.5f,    1, 1,    1, 0, 0, //5
                     0.5f,  0.5f, -0.5f,    1, 0,    1, 0, 0, //6
                     0.5f,  0.5f,  0.5f,    0, 0,    1, 0, 0, //2

                    //behind
                     0.5f, -0.5f, -0.5f,    0, 1,    0, 0, -1, //5
                    -0.5f, -0.5f, -0.5f,    1, 1,    0, 0, -1, //4
                    -0.5f,  0.5f, -0.5f,    1, 0,    0, 0, -1, //7
                     0.5f,  0.5f, -0.5f,    0, 0,    0, 0, -1, //6

                    //left
                    -0.5f, -0.5f, -0.5f,    0, 1,   -1, 0, 0, //4
                    -0.5f, -0.5f,  0.5f,    1, 1,   -1, 0, 0, //0
                    -0.5f,  0.5f,  0.5f,    1, 0,   -1, 0, 0, //3
                    -0.5f,  0.5f, -0.5f,    0, 0,   -1, 0, 0, //7

                    //top
                    -0.5f,  0.5f,  0.5f,    0, 1,    0, 1, 0, //3
                     0.5f,  0.5f,  0.5f,    1, 1,    0, 1, 0, //2
                     0.5f,  0.5f, -0.5f,    1, 0,    0, 1, 0, //6
                    -0.5f,  0.5f, -0.5f,    0, 0,    0, 1, 0, //7

                    //bottom
                    -0.5f, -0.5f, -0.5f,    0, 1,    0, -1, 0, //4
                     0.5f, -0.5f, -0.5f,    1, 1,    0, -1, 0, //5
                     0.5f, -0.5f,  0.5f,    1, 0,    0, -1, 0, //1
                    -0.5f, -0.5f,  0.5f,    0, 0,    0, -1, 0  //0
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
                    false, true, true);
            cube.setVertices(vertices, 0, vertices.length);
            cube.setIndices(indices, 0, indices.length);
            return cube;
        }

        @Override
        public void resume() {
            texture.load();
        }

        @Override
        public void update(float deltaTime) {
            angle += deltaTime * 20;
        }

        @Override
        public void present(float deltaTime) {
            GL10 gl = graphics.getGL();
            gl.glClearColor(0.05f, 0.05f, 0.05f, 1.0f);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            gl.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());

            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            GLU.gluPerspective(gl, 67,
                               graphics.getWidth() / (float) graphics.getHeight(),
                               0.1f, 10f);

            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();
            GLU.gluLookAt(gl, 0, 1, 3, 0, 0, 0, 0, 1, 0);

            gl.glEnable(GL10.GL_DEPTH_TEST);
            gl.glEnable(GL10.GL_LIGHTING);

            ambientLight.enable(gl);
            pointLight.enable(gl, GL10.GL_LIGHT0);
            directionalLight.enable(gl, GL10.GL_LIGHT1);

            gl.glEnable(GL10.GL_TEXTURE_2D);
            texture.bind();

            gl.glRotatef(angle, 0, 1, 0);
            cube.bind();
            cube.draw(GL10.GL_TRIANGLES, 0, 36);
            cube.unbind();

            pointLight.disable(gl);
            directionalLight.disable(gl);

            gl.glDisable(GL10.GL_TEXTURE_2D);
            gl.glDisable(GL10.GL_DEPTH_TEST);
        }

        @Override
        public void pause() {

        }

        @Override
        public void dispose() {
            texture.dispose();
        }
    }
}
