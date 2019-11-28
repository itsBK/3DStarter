package com.bakri.a3dstarter.a3dstarter;

import javax.microedition.khronos.opengles.GL10;

import framework.gl.camera.Camera2D;
import framework.gl.camera.EulerCamera;
import framework.Game;
import framework.gl.Screen;
import framework.gl.light.PointLight;
import framework.gl.vertices.SpriteBatcher;
import framework.gl.texture.Texture;
import framework.gl.texture.TextureRegion;
import framework.gl.vertices.Vertices3;
import framework.math.Vector2;
import framework.math.Vector3;

public class EulerCameraTest extends Game {

    @Override
    public Screen getStartScreen() {
        return new EulerCameraScreen(this);
    }




    class EulerCameraScreen extends Screen {

        Texture crateTexture;
        Vertices3 cube;
        PointLight light;
        EulerCamera camera;

        Texture buttonTexture;
        SpriteBatcher batcher;
        Camera2D guiCamera;
        TextureRegion buttonRegion;
        Vector2 touchPos;
        float lastX = -1;
        float lastY = -1;


        public EulerCameraScreen(Game game) {
            super(game);

            crateTexture = new Texture(this.game, "crate.png", true);
            cube = createCube();
            light = new PointLight();
            light.setPosition(3, 3, -3);
            camera = new EulerCamera(67,
                    graphics.getWidth() / (float) graphics.getHeight(),
                    1, 100);
            camera.getPosition().set(0, 1, 3);

            buttonTexture = new Texture(this.game, "button.png");
            batcher = new SpriteBatcher(graphics, 1);
            float ratio = (float) graphics.getWidth() / graphics.getHeight();
            guiCamera = new Camera2D(graphics, ratio * 320, 320);
            buttonRegion = new TextureRegion(buttonTexture, 0, 0, 64, 64);
            touchPos = new Vector2();
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
            crateTexture.load();
            buttonTexture.load();
        }

        @Override
        public void update(float deltaTime) {
            game.getInput().getKeyEvents();
            game.getInput().getTouchEvents();
            float x = game.getInput().getTouchX(0);
            float y = game.getInput().getTouchY(0);
            guiCamera.touchToWorld(touchPos.set(x, y));

            if (game.getInput().isTouchDown(0)) {
                if (touchPos.x < 64 && touchPos.y < 64) {
                    Vector3 direction = camera.getDirection();
                    camera.getPosition().add(direction.mul(deltaTime));
                } else {
                    if (lastX == -1) {
                        lastX = x;
                        lastY = y;
                    } else {
                        camera.rotate((x - lastX) / 10, (y - lastY) / 10);
                        lastX = x;
                        lastY = y;
                    }
                }
            } else {
                lastX = -1;
                lastY = -1;
            }
        }

        @Override
        public void present(float deltaTime) {
            GL10 gl = graphics.getGL();
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            gl.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());

            camera.setMatrices(gl);

            gl.glEnable(GL10.GL_DEPTH_TEST);
            gl.glEnable(GL10.GL_TEXTURE_2D);
            gl.glEnable(GL10.GL_LIGHTING);

            crateTexture.bind();
            cube.bind();
            light.enable(gl, GL10.GL_LIGHT0);

            for (int z = 0; z >= -8; z -= 2) {
                for (int x = -4; x <=4; x += 2) {
                    gl.glPushMatrix();
                    gl.glTranslatef(x, 0, z);
                    cube.draw(GL10.GL_TRIANGLES, 0, 36);
                    gl.glPopMatrix();
                }
            }
            cube.unbind();

            gl.glDisable(GL10.GL_LIGHTING);
            gl.glDisable(GL10.GL_DEPTH_TEST);

            gl.glEnable(GL10.GL_BLEND);
            gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

            gl.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());
            guiCamera.setMatrices();
            batcher.beginBatch(buttonTexture);
            batcher.drawSprite(32, 32, 64, 64, buttonRegion);
            batcher.endBatch();

            gl.glDisable(GL10.GL_BLEND);
            gl.glDisable(GL10.GL_TEXTURE_2D);
        }

        @Override
        public void pause() {

        }

        @Override
        public void dispose() {
            buttonTexture.dispose();
            crateTexture.dispose();
        }
    }
}
