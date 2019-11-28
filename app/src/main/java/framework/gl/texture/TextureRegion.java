package framework.gl.texture;

public class TextureRegion {

    public final float u1, u2;
    public final float v1, v2;
    public final Texture texture;


    public TextureRegion(Texture texture, float startX, float startY, float width, float height) {
        u1 = startX / texture.width;
        v1 = startY / texture.height;
        u2 = u1 + width / texture.width;
        v2 = v1 + height / texture.height;
        this.texture = texture;
    }
}
