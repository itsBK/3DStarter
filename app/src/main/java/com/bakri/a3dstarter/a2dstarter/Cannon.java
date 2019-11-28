package com.bakri.a3dstarter.a2dstarter;

import framework.gameObject.DynamicObject2D;

public class Cannon extends DynamicObject2D {

    public float angle;


    public Cannon(float x, float y, float width, float height) {
        super(x, y, width, height);
        angle = 0;
    }
}

