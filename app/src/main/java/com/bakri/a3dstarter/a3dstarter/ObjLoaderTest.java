package com.bakri.a3dstarter.a3dstarter;

import android.opengl.GLU;

import javax.microedition.khronos.opengles.GL10;

import framework.Game;
import framework.gl.Screen;
import framework.gl.vertices.ObjLoader2;
import framework.gl.light.PointLight;
import framework.gl.texture.Texture;
import framework.gl.vertices.Vertices3;

public class ObjLoaderTest extends Game {

    @Override
    public Screen getStartScreen() {
        return new ObjLoaderScreen(this);
    }


    class ObjLoaderScreen extends Screen {

        Texture texture;
        Vertices3 vertices;
        PointLight pointLight;
        float angle = 0;
        int lines = 0;


        ObjLoaderScreen(Game game) {
            super(game);
            texture = new Texture(game, "model.png");
            vertices = ObjLoader2.load(game, "model.obj");

            pointLight = new PointLight();
            pointLight.setAmbient(0.1f, 0.1f, 0.1f, 1);
            pointLight.setDiffuse(0.9f, 0.9f, 0.9f, 1);
            pointLight.setPosition(-5, 0, 10);
        }

        @Override
        public void resume() {
            texture.load();
        }

        @Override
        public void update(float deltaTime) {
            angle += deltaTime * 45;
            if (lines >= vertices.getNumIndices()) {
                lines = vertices.getNumIndices();
            } else {
                lines += 3;
            }
        }

        @Override
        public void present(float deltaTime) {
            GL10 gl = graphics.getGL();
            gl.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            GLU.gluPerspective(gl, 67,
                    (float) graphics.getWidth() / graphics.getHeight(),
                    0.1f, 40f);

            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();
            GLU.gluLookAt(gl, 0, 0, 17,
                    0, 0, 0,
                    0, 1, 0);

            gl.glEnable(GL10.GL_DEPTH_TEST);
            gl.glEnable(GL10.GL_LIGHTING);
            gl.glEnable(GL10.GL_TEXTURE_2D);

            pointLight.enable(gl, GL10.GL_LIGHT0);
            texture.bind();
            vertices.bind();

            gl.glRotatef(angle, 0, 1, 0);
            vertices.draw(GL10.GL_TRIANGLES, 0, lines);
            if (vertices.getNumIndices() != lines) {
                vertices.draw(GL10.GL_LINES, lines, vertices.getNumIndices() - lines);
            }

            vertices.unbind();
            pointLight.disable(gl);

            gl.glDisable(GL10.GL_TEXTURE_2D);
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
