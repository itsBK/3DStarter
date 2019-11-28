package framework.gl.texture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import framework.FileIO;
import framework.Game;
import framework.gl.Graphics;

public class Texture {

    private Graphics graphics;
    private FileIO fileIO;
    private String fileName;
    private int textureId;
    private int minFilter;
    private int magFilter;
    protected int width;
    protected int height;
    private boolean mipmapped;


    public Texture(Game game, String fileName) {
        this(game, fileName, false);
    }

    public Texture(Game game, String fileName, boolean mipmapped) {
        this.graphics = game.getGraphics();
        this.fileIO = game.getFileIO();
        this.fileName = fileName;
        this.mipmapped = mipmapped;

        magFilter = GL10.GL_NEAREST;
        minFilter = GL10.GL_NEAREST;
        if (mipmapped)
            minFilter = GL10.GL_LINEAR_MIPMAP_NEAREST;
    }

    public void load() {
        GL10 gl = graphics.getGL();
        int[] textureIds = new int[1];
        gl.glGenTextures(1, textureIds, 0);
        textureId = textureIds[0];

        InputStream in = null;
        try {
            in = fileIO.readAsset(fileName);
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            width = bitmap.getWidth();
            height = bitmap.getHeight();

            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
            setFilters(minFilter, magFilter);

            if (mipmapped)
                createMipmaps(bitmap);
            else
                GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

            gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
            bitmap.recycle();

        } catch (IOException e) {
            throw new RuntimeException("Couldn't load texture '" + fileName + "'", e);

        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException ignored) {}
        }
    }

    private void createMipmaps(Bitmap bitmap) {
        int level = 0;
        int newWidth = width;
        int newHeight = height;
        Canvas canvas;
        Bitmap newBitmap = null;

        while (true) {
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, level, bitmap, 0);

            newWidth /= 2;
            newHeight /= 2;
            if (newWidth <= 0)
                break;

            newBitmap = Bitmap.createBitmap(newWidth, newHeight, bitmap.getConfig());
            canvas = new Canvas(newBitmap);
            canvas.drawBitmap(bitmap,
                    new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
                    new Rect(0, 0, newWidth, newHeight), null);
            bitmap.recycle();
            bitmap = newBitmap;
            level++;
        }
        newBitmap.recycle();
    }

    public void setFilters(int minFilter, int magFilter) {
        this.minFilter = minFilter;
        this.magFilter = magFilter;
        GL10 gl = graphics.getGL();
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, minFilter);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, magFilter);
    }

    public void bind() {
        graphics.getGL().glBindTexture(GL10.GL_TEXTURE_2D, textureId);
    }

    public void dispose() {
        GL10 gl = graphics.getGL();
        gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
        gl.glDeleteTextures(1, new int[] { textureId }, 0);
    }
}
