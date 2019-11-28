package framework.gl.vertices;

import javax.microedition.khronos.opengles.GL10;

import framework.gl.Graphics;
import framework.gl.texture.Texture;
import framework.gl.texture.TextureRegion;
import framework.math.Vector2;

public class SpriteBatcher {

    private final Vertices vertices;
    private final float[] verticesBuffer;
    private int bufferIndex;
    private int numSprites;


    public SpriteBatcher(Graphics graphics, int maxSprites) {
        this.verticesBuffer = new float[maxSprites * 4 * 4];
        this.vertices = new Vertices(graphics, maxSprites * 4, maxSprites * 6, false, true);
        this.bufferIndex = 0;
        this.numSprites = 0;

        short[] indices = new short[maxSprites * 6];
        int len = indices.length;
        short j = 0;

        for (int i = 0; i < len; i += 6, j += 4) {
            indices[i    ] =          j;
            indices[i + 1] = (short) (j + 1);
            indices[i + 2] = (short) (j + 2);
            indices[i + 3] = (short) (j + 2);
            indices[i + 4] = (short) (j + 3);
            indices[i + 5] =          j;
        }
        vertices.setIndices(indices, 0, len);
    }

    public void beginBatch(Texture texture) {
        texture.bind();
        numSprites = 0;
        bufferIndex = 0;
    }

    public void endBatch() {
        vertices.setVertices(verticesBuffer, 0, bufferIndex);
        vertices.bind();
        vertices.draw(GL10.GL_TRIANGLES, 0, numSprites * 6);
        vertices.unbind();
    }

    public void drawSprite(float centerX, float centerY, float width, float height, TextureRegion region) {
        float x1 = centerX - width / 2;
        float x2 = centerX + width / 2;
        float y1 = centerY - height / 2;
        float y2 = centerY + height / 2;

        verticesBuffer[bufferIndex++] = x1;
        verticesBuffer[bufferIndex++] = y1;
        verticesBuffer[bufferIndex++] = region.u1;
        verticesBuffer[bufferIndex++] = region.v2;

        verticesBuffer[bufferIndex++] = x2;
        verticesBuffer[bufferIndex++] = y1;
        verticesBuffer[bufferIndex++] = region.u2;
        verticesBuffer[bufferIndex++] = region.v2;

        verticesBuffer[bufferIndex++] = x2;
        verticesBuffer[bufferIndex++] = y2;
        verticesBuffer[bufferIndex++] = region.u2;
        verticesBuffer[bufferIndex++] = region.v1;

        verticesBuffer[bufferIndex++] = x1;
        verticesBuffer[bufferIndex++] = y2;
        verticesBuffer[bufferIndex++] = region.u1;
        verticesBuffer[bufferIndex++] = region.v1;

        numSprites++;
    }

    public void drawSprite(float centerX, float centerY, float width, float height, float angle, TextureRegion region) {
        float halfWidth = width / 2;
        float halfHeight = height / 2;

        float rad = angle * Vector2.TO_RADIANS;
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);

        float x1 = -halfWidth * cos - (-halfHeight) * sin;
        float y1 = -halfWidth * sin + (-halfHeight) * cos;
        float x2 = halfWidth * cos - (-halfHeight) * sin;
        float y2 = halfWidth * sin + (-halfHeight) * cos;
        float x3 = halfWidth * cos - halfHeight * sin;
        float y3 = halfWidth * sin + halfHeight * cos;
        float x4 = -halfWidth * cos - halfHeight * sin;
        float y4 = -halfWidth * sin + halfHeight * cos;

        x1 += centerX; x2 += centerX; x3 += centerX; x4 += centerX;
        y1 += centerY; y2 += centerY; y3 += centerY; y4 += centerY;

        verticesBuffer[bufferIndex++] = x1;
        verticesBuffer[bufferIndex++] = y1;
        verticesBuffer[bufferIndex++] = region.u1;
        verticesBuffer[bufferIndex++] = region.v2;

        verticesBuffer[bufferIndex++] = x2;
        verticesBuffer[bufferIndex++] = y2;
        verticesBuffer[bufferIndex++] = region.u2;
        verticesBuffer[bufferIndex++] = region.v2;

        verticesBuffer[bufferIndex++] = x3;
        verticesBuffer[bufferIndex++] = y3;
        verticesBuffer[bufferIndex++] = region.u2;
        verticesBuffer[bufferIndex++] = region.v1;

        verticesBuffer[bufferIndex++] = x4;
        verticesBuffer[bufferIndex++] = y4;
        verticesBuffer[bufferIndex++] = region.u1;
        verticesBuffer[bufferIndex++] = region.v1;

        numSprites++;
    }
}
