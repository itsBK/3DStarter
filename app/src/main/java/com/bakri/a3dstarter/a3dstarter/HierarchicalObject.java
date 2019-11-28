package com.bakri.a3dstarter.a3dstarter;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import framework.gl.vertices.Vertices3;

public class HierarchicalObject {

    public float x, y, z;
    public float scale = 1;
    public float rotationY, rotationParent;
    public float orbitSpeed;
    public boolean hasParent;
    public final Vertices3 mesh;
    public final List<HierarchicalObject> children = new ArrayList<>();


    public HierarchicalObject(Vertices3 mesh, boolean hasParent) {
        this.mesh = mesh;
        this.hasParent = hasParent;
        this.orbitSpeed = 45;
    }

    public HierarchicalObject(Vertices3 mesh, boolean hasParent, float orbitSpeed) {
        this.mesh = mesh;
        this.hasParent = hasParent;
        this.orbitSpeed = orbitSpeed;
    }

    public void update(float deltaTime) {
        rotationY += 45 * deltaTime;
        rotationParent += orbitSpeed * deltaTime;

        int len = children.size();
        for (int i = 0; i < len; i++)
            children.get(i).update(deltaTime);
    }

    public void render(GL10 gl) {
        gl.glPushMatrix();
        if (hasParent)
            gl.glRotatef(rotationParent, 0, 1, 0);
        gl.glTranslatef(x, y, z);

        gl.glPushMatrix();
        gl.glRotatef(rotationY, 0, 1, 0);
        gl.glScalef(scale, scale, scale);
        mesh.draw(GL10.GL_TRIANGLES, 0, 36);
        gl.glPopMatrix();

        int len = children.size();
        for (int i = 0; i < len; i++)
            children.get(i).render(gl);
        gl.glPopMatrix();
    }
}
