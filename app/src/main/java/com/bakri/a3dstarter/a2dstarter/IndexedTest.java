package com.bakri.a3dstarter.a2dstarter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import framework.Game;
import framework.gl.Graphics;
import framework.gl.Screen;
import framework.gl.texture.Texture;

public class IndexedTest extends Game {

	public Screen getStartScreen() {
		return new IndexedScreen(this);
	}




	class IndexedScreen extends Screen {

	    final int VERTEX_SIZE = (2 + 2) * 4;
	    Graphics graphics;
	    FloatBuffer vertices;   
	    ShortBuffer indices;
	    Texture texture;


	    public IndexedScreen(Game game) {
	        super(game);
	        graphics = game.getGraphics();
	                    
	        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * VERTEX_SIZE);
	        byteBuffer.order(ByteOrder.nativeOrder());
	        vertices = byteBuffer.asFloatBuffer();
	        vertices.put(new float[] {  100.0f, 100.0f, 0.0f, 1.0f,
	                                    228.0f, 100.0f, 1.0f, 1.0f,
	                                    228.0f, 228.0f, 1.0f, 0.0f,
	                                    100.0f, 228.0f, 0.0f, 0.0f });
	        vertices.flip();
	        
	        byteBuffer = ByteBuffer.allocateDirect(6 * 2);
	        byteBuffer.order(ByteOrder.nativeOrder());
	        indices = byteBuffer.asShortBuffer();
	        indices.put(new short[] { 0, 1, 2,
	                                  2, 3, 0 });
	        indices.flip();
	        
	        texture = new Texture(game, "bobrgb888.png");
	    }         

	    @Override
	    public void present(float deltaTime) {
	        GL10 gl = graphics.getGL();
	        gl.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());
	        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	        gl.glMatrixMode(GL10.GL_PROJECTION);
	        gl.glLoadIdentity();
	        gl.glOrthof(0, 320, 0, 480, 1, -1);

	        gl.glEnable(GL10.GL_TEXTURE_2D);
	        texture.bind();
	        
	        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);            
	        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	        
	        vertices.position(0);
	        gl.glVertexPointer(2, GL10.GL_FLOAT, VERTEX_SIZE, vertices);                                   
	        vertices.position(2);
	        gl.glTexCoordPointer(2, GL10.GL_FLOAT, VERTEX_SIZE, vertices);
	        
	        gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, indices);
	    }

		@Override
		public void update(float deltaTime) {
		}

		@Override
		public void pause() {
		}

		@Override
		public void resume() {
	    	texture.load();
		}

		@Override
		public void dispose() {
		}
	}
}
