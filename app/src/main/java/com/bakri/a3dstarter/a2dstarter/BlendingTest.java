package com.bakri.a3dstarter.a2dstarter;

import javax.microedition.khronos.opengles.GL10;

import framework.Game;
import framework.gl.Graphics;
import framework.gl.Screen;
import framework.gl.texture.Texture;
import framework.gl.vertices.Vertices;

public class BlendingTest extends Game {

	public Screen getStartScreen() {
		return new BlendingScreen(this);
	}



	class BlendingScreen extends Screen {

	    Graphics graphics;
	    Vertices vertices;
	    Texture textureRgb;
	    Texture textureRgba;


	    public BlendingScreen(Game game) {
	        super(game);
	        graphics = game.getGraphics();
	        
	        textureRgb = new Texture(game, "bobrgb888.png");
	        textureRgba = new Texture(game, "bobargb8888.png");
	        
	        vertices = new Vertices(graphics, 8, 12, true, true);
	        float[] rects = new float[] {
	                100, 100, 1, 1, 1, 0.5f, 0, 1,
	                228, 100, 1, 1, 1, 0.5f, 1, 1,
	                228, 228, 1, 1, 1, 0.5f, 1, 0,
	                100, 228, 1, 1, 1, 0.5f, 0, 0,
	                
	                100, 300, 1, 1, 1, 1, 0, 1,
	                228, 300, 1, 1, 1, 1, 1, 1,
	                228, 428, 1, 1, 1, 1, 1, 0,
	                100, 428, 1, 1, 1, 1, 0, 0                    
	        };
	        vertices.setVertices(rects, 0, rects.length);
	        vertices.setIndices(new short[] {0, 1, 2, 2, 3, 0,
	                                         4, 5, 6, 6, 7, 4 }, 0, 12);                        
	    }
	    
	    @Override
	    public void present(float deltaTime) {
	        GL10 gl = graphics.getGL();
	        gl.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());
	        gl.glClearColor(1,0,0,1);
	        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	        gl.glMatrixMode(GL10.GL_PROJECTION);
	        gl.glLoadIdentity();
	        gl.glOrthof(0, 320, 0, 480, 1, -1);
	        
	        gl.glEnable(GL10.GL_BLEND);
	        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	        
	        gl.glEnable(GL10.GL_TEXTURE_2D);
	        textureRgb.bind();
	        vertices.draw(GL10.GL_TRIANGLES, 0, 6 );
	        
	        textureRgba.bind();
	        vertices.draw(GL10.GL_TRIANGLES, 6, 6 );
	    }

		@Override
		public void update(float deltaTime) {
		}

		@Override
		public void pause() {
	    	vertices.unbind();
		}

		@Override
		public void resume() {
	    	textureRgb.load();
	    	textureRgba.load();
	    	vertices.bind();
		}

		@Override
		public void dispose() {
		}
	}
}
