package framework.gl.vertices;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import framework.gl.Graphics;

public class Vertices3 {

    private final Graphics graphics;
    private final FloatBuffer vertices;
    private final ShortBuffer indices;
    private final boolean hasColor;
    private final boolean hasTexCoords;
    private final boolean hasNormals;
    private final int vertexSize;
    private final int numVertices;


    public Vertices3(Graphics graphics, int maxVertices, int maxIndices,
                     boolean hasColor, boolean hasTexCoords, boolean hasNormals) {

        this.graphics = graphics;
        this.hasColor = hasColor;
        this.hasTexCoords = hasTexCoords;
        this.hasNormals = hasNormals;
        this.numVertices = (maxIndices == 0)? maxVertices : maxIndices;
        this.vertexSize = (3 + (hasColor ? 4:0) + (hasTexCoords ? 2:0) + (hasNormals ? 3:0)) * 4;

        ByteBuffer buffer = ByteBuffer.allocateDirect(vertexSize * maxVertices);
        buffer.order(ByteOrder.nativeOrder());
        vertices = buffer.asFloatBuffer();

        if (maxIndices > 0) {
            buffer = ByteBuffer.allocateDirect(2 * maxIndices);
            buffer.order(ByteOrder.nativeOrder());
            indices = buffer.asShortBuffer();
        } else
            indices = null;
    }

    public void setVertices(float[] vertices) {
        this.vertices.clear();
        this.vertices.put(vertices);
        this.vertices.flip();
    }

    public void setIndices(short[] indices) {
        this.indices.clear();
        this.indices.put(indices);
        this.indices.flip();
    }

    public void setVertices(float[] vertices, int offSet, int length) {
        this.vertices.clear();
        this.vertices.put(vertices, offSet, length);
        this.vertices.flip();
    }

    public void setIndices(short[] indices, int offSet, int length) {
        this.indices.clear();
        this.indices.put(indices, offSet, length);
        this.indices.flip();
    }

    public void bind() {
        GL10 gl = graphics.getGL();

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        vertices.position(0);
        gl.glVertexPointer(3, GL10.GL_FLOAT, vertexSize, vertices);

        if (hasColor) {
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            vertices.position(3);
            gl.glColorPointer(4, GL10.GL_FLOAT, vertexSize, vertices);
        }

        if (hasTexCoords) {
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            vertices.position(hasColor ? 7:3);
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, vertexSize, vertices);
        }

        if (hasNormals) {
            gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

            int offSet = hasColor? 7 : 3;
            if (hasTexCoords) offSet += 2;
            vertices.position(offSet);

            gl.glNormalPointer(GL10.GL_FLOAT, vertexSize, vertices);
        }
    }

    public void draw() {
        GL10 gl = graphics.getGL();

        if (indices != null) {
            indices.position(0);
            gl.glDrawElements(GL10.GL_TRIANGLES, this.numVertices, GL10.GL_UNSIGNED_SHORT, indices);
        } else
            gl.glDrawArrays(GL10.GL_TRIANGLES, 0, this.numVertices);
    }

    public void draw(int primitiveType, int offSet, int numVertices) {
        GL10 gl = graphics.getGL();

        if (indices != null) {
            indices.position(offSet);
            gl.glDrawElements(primitiveType, numVertices, GL10.GL_UNSIGNED_SHORT, indices);
        } else
            gl.glDrawArrays(primitiveType, offSet, numVertices);
    }

    public void unbind() {
        GL10 gl = graphics.getGL();
        if (hasTexCoords)
            gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        if (hasColor)
            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        if (hasNormals)
            gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
    }

    public int getNumIndices() {
        return indices.limit();
    }

    public int getNumVertices() {
        return vertices.limit() / (vertexSize / 4);
    }
}
