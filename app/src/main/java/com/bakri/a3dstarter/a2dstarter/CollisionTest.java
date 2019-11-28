package com.bakri.a3dstarter.a2dstarter;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import framework.Game;
import framework.gameObject.DynamicObject2D;
import framework.gameObject.SpatialHashGrid2D;
import framework.gameObject.StaticObject2D;
import framework.gl.Graphics;
import framework.gl.Screen;
import framework.gl.vertices.Vertices;
import framework.input.Input.TouchEvent;
import framework.math.OverlapTester;
import framework.math.Vector2;
import framework.math.bounds.Circle;
import framework.math.bounds.Rectangle;

public class CollisionTest extends Game {

    @Override
    public Screen getStartScreen() {
        return new CollisionScreen(this);
    }



    class CollisionScreen extends Screen {

        final int NUM_TARGETS = 80;
        final float WORLD_WIDTH = 9.6f;
        final float WORLD_HEIGHT;
        final float cellSize = 2.5f;
        Graphics graphics;
        Cannon cannon;
        DynamicObject2D ball;
        List<StaticObject2D> targets;
        List<StaticObject2D> colliders;
        SpatialHashGrid2D grid;

        Vertices cannonVertices;
        Vertices ballVertices;
        Vertices targetVertices;
        Vertices linesVertices;

        Vector2 touchPos = new Vector2();
        Vector2 gravity = new Vector2(0, -10);

        public CollisionScreen(Game game) {
            super(game);
            graphics = game.getGraphics();
            WORLD_HEIGHT = graphics.getHeight() / (float) graphics.getWidth() * WORLD_WIDTH;
            cannon = new Cannon(0, 0, 1, 1);
            ball = new DynamicObject2D(0, 0, 0.1f);
            targets = new ArrayList<>(NUM_TARGETS);
            grid = new SpatialHashGrid2D(WORLD_WIDTH, WORLD_HEIGHT, cellSize);

            for (int i = 0 ; i < NUM_TARGETS; i++) {
                StaticObject2D target = new StaticObject2D(
                        (float) Math.random() * WORLD_WIDTH,
                        (float) Math.random() * WORLD_HEIGHT,
                        0.5f, 0.5f);
                grid.insertStaticObject(target);
                targets.add(target);
            }

            cannonVertices = new Vertices(graphics, 3, 0,
                    false, false);
            cannonVertices.setVertices(new float[] { -0.5f, -0.5f,
                    0.5f,  0.0f,
                    -0.5f,  0.5f }, 0, 6);

            ballVertices = new Vertices(graphics, 4, 6,
                    false, false);
            ballVertices.setVertices(new float[] { -0.1f, -0.1f,
                    0.1f, -0.1f,
                    0.1f,  0.1f,
                    -0.1f,  0.1f }, 0, 8);
            ballVertices.setIndices(new short[] { 0, 1, 2, 2, 3, 0}, 0, 6);

            targetVertices = new Vertices(graphics, 4, 6,
                    false, false);
            targetVertices.setVertices(new float[] { -0.25f, -0.25f,
                    0.25f, -0.25f,
                    0.25f,  0.25f,
                    -0.25f,  0.25f }, 0, 8);
            targetVertices.setIndices(new short[] { 0, 1, 2, 2, 3, 0}, 0, 6);

            linesVertices = new Vertices(graphics, 10, 0,
                    false, false);
            linesVertices.setVertices(new float[]{
                           0.0f, cellSize,
                    WORLD_WIDTH, cellSize,
                           0.0f, cellSize * 2,
                    WORLD_WIDTH, cellSize * 2,

                    cellSize, 0.0f,
                    cellSize, WORLD_HEIGHT,
                    2 * cellSize, 0.0f,
                    2 * cellSize, WORLD_HEIGHT,
                    3 * cellSize, 0.0f,
                    3 * cellSize, WORLD_HEIGHT
            });
        }

        @Override
        public void update(float deltaTime) {
            List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
            game.getInput().getKeyEvents();

            int len = touchEvents.size();
            for (int i = 0; i < len; i++) {
                TouchEvent event = touchEvents.get(i);

                touchPos.x = (event.x / (float) graphics.getWidth()) * WORLD_WIDTH;
                touchPos.y = (1 - event.y / (float) graphics.getHeight()) * WORLD_HEIGHT;

                cannon.angle = touchPos.sub(cannon.position).angle();

                if (event.type == TouchEvent.TOUCH_UP) {
                    float radians = cannon.angle * Vector2.TO_RADIANS;
                    float ballSpeed = touchPos.len() * 2;
                    ball.position.set(cannon.position);
                    ball.velocity.x = (float) Math.cos(radians) * ballSpeed;
                    ball.velocity.y = (float) Math.sin(radians) * ballSpeed;
                    ((Circle) ball.bounds).center.set(ball.position.x, ball.position.y);
                }
            }

            ball.velocity.add(gravity.x * deltaTime, gravity.y * deltaTime);
            ball.position.add(ball.velocity.x * deltaTime, ball.velocity.y * deltaTime);
            ((Circle) ball.bounds).center.set(ball.position.x, ball.position.y);

            colliders = grid.getPotentialColliders(ball);
            len = colliders.size();

            for (int i = 0; i < len; i++) {
                StaticObject2D collider = colliders.get(i);
                if (OverlapTester.overlapCircleRectangle((Circle) ball.bounds,
                        (Rectangle) collider.bounds)) {
                    grid.removeObject(collider);
                    targets.remove(collider);
                }
            }
        }

        @Override
        public void present(float deltaTime) {
            GL10 gl = graphics.getGL();

            gl.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glOrthof(0, WORLD_WIDTH, 0, WORLD_HEIGHT, 1, -1);
            gl.glMatrixMode(GL10.GL_MODELVIEW);

            targetVertices.bind();
            int len = targets.size();

            for (int i = 0; i < len; i++) {
                StaticObject2D target = targets.get(i);

                if (colliders.contains(target))
                    gl.glColor4f(0, 0, 1, 1);
                else
                    gl.glColor4f(0, 1, 0, 1);

                gl.glLoadIdentity();
                gl.glTranslatef(target.position.x, target.position.y, 0);
                targetVertices.draw(GL10.GL_TRIANGLES, 0, 6);
            }
            targetVertices.unbind();

            gl.glLoadIdentity();
            gl.glTranslatef(ball.position.x, ball.position.y, 0);
            gl.glColor4f(1, 0, 0, 1);
            ballVertices.bind();
            ballVertices.draw(GL10.GL_TRIANGLES, 0, 6);
            ballVertices.unbind();

            gl.glLoadIdentity();
            gl.glColor4f(1, 0, 0, 1);
            linesVertices.bind();
            linesVertices.draw(GL10.GL_LINES, 0,10);
            linesVertices.unbind();

            gl.glLoadIdentity();
            gl.glTranslatef(cannon.position.x, cannon.position.y, 0);
            gl.glRotatef(cannon.angle, 0, 0, 1);
            gl.glColor4f(1, 1, 1, 1);
            cannonVertices.bind();
            cannonVertices.draw(GL10.GL_TRIANGLES, 0, 3);
            cannonVertices.unbind();
        }

        @Override
        public void resume() {

        }

        @Override
        public void pause() {

        }

        @Override
        public void dispose() {

        }
    }

}