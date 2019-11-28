package com.bakri.a3dstarter.a2dstarter;

import javax.microedition.khronos.opengles.GL10;

import framework.Game;
import framework.gameObject.DynamicObject2D;
import framework.gl.Graphics;
import framework.gl.Screen;
import framework.gl.camera.Camera2D;
import framework.gl.texture.Animation;
import framework.gl.texture.Texture;
import framework.gl.texture.TextureRegion;
import framework.gl.vertices.SpriteBatcher;

public class AnimationTest extends Game {

    static final float WORLD_WIDTH = 4.8f;
    static final float WORLD_HEIGHT = 3.2f;

    @Override
    public Screen getStartScreen() {
        return new AnimationScreen(this);
    }




    class AnimationScreen extends Screen {

        static final int NUM_CAVEMAN = 10;
        Graphics graphics;
        Caveman[] cavemen;
        SpriteBatcher batcher;
        Camera2D camera;
        Texture texture;
        Animation walkAnim;


        public AnimationScreen(Game game) {
            super(game);
            graphics = game.getGraphics();
            cavemen = new Caveman[NUM_CAVEMAN];
            for (int i = 0; i < NUM_CAVEMAN; i++)
                cavemen[i] = new Caveman((float) Math.random(), (float) Math.random(), 1, 1);

            texture = new Texture(game, "walkanim.png");
            batcher = new SpriteBatcher(graphics, NUM_CAVEMAN);
            camera = new Camera2D(graphics, WORLD_WIDTH, WORLD_HEIGHT);
        }

        @Override
        public void resume() {
            texture.load();
            walkAnim = new Animation(0.2f,
                    new TextureRegion(texture, 0, 0, 64, 64),
                    new TextureRegion(texture, 64, 0, 64, 64),
                    new TextureRegion(texture, 128, 0, 64, 64),
                    new TextureRegion(texture, 192, 0, 64, 64));
        }

        @Override
        public void update(float deltaTime) {
            for (int i = 0; i < NUM_CAVEMAN; i++)
                cavemen[i].update(deltaTime);
        }

        @Override
        public void present(float deltaTime) {
            GL10 gl = graphics.getGL();
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
            gl.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());
            camera.setMatrices();

            gl.glEnable(GL10.GL_BLEND);
            gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
            gl.glEnable(GL10.GL_TEXTURE_2D);

            batcher.beginBatch(texture);
            for (int i = 0; i < NUM_CAVEMAN; i++) {
                Caveman caveman = cavemen[i];
                TextureRegion keyFrame = walkAnim.getKeyFrame(caveman.walkingTime, Animation.ANIMATION_LOOPING);
                batcher.drawSprite(caveman.position.x, caveman.position.y, caveman.velocity.x < 0? 1:-1, 1, keyFrame);
            }
            batcher.endBatch();
        }

        @Override
        public void pause() {

        }

        @Override
        public void dispose() {

        }
    }





    static class Caveman extends DynamicObject2D {

        private float walkingTime;

        private Caveman(float x, float y, float width, float height) {
            super(x, y, width, height);
            this.position.set((float) Math.random() * WORLD_WIDTH,
                    (float) Math.random() * WORLD_HEIGHT);
            this.velocity.set(Math.random() > 0.5f? -0.5f:0.5f, 0);
            this.walkingTime = (float) Math.random() * 10;
        }

        public void update(float deltaTime) {
            position.add(velocity.x * deltaTime, velocity.y * deltaTime);
            if (position.x < 0) position.x = WORLD_WIDTH;
            if (position.x > WORLD_WIDTH) position.x = 0;
            walkingTime += deltaTime;
        }
    }
}
