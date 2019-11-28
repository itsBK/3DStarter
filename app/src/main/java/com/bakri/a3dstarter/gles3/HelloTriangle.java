package com.bakri.a3dstarter.gles3;

import android.util.Log;


import static android.opengl.GLES30.*;

public class HelloTriangle {

    private static final String TAG = "HelloTiangle";

    private int loadShader(int type, String shaderSrc) {
        int shader;
        int[] compiled = new int[1];

        shader = glCreateShader(type);
        if (shader == 0) return 0;

        glShaderSource(shader, shaderSrc);
        glCompileShader(shader);
        glGetShaderiv(shader, GL_COMPILE_STATUS, compiled, 0);

        if (compiled[0] == 0) {
            Log.e(TAG, glGetShaderInfoLog(shader));
            glDeleteShader(shader);
            return 0;
        }

        return shader;
    }
}
