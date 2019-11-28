package framework.gl.texture;

import framework.gl.vertices.SpriteBatcher;

public class Font {

    private final int glyphWidth;
    private final int glyphHeight;
    private final TextureRegion[] glyphs = new TextureRegion[96];


    public Font(Texture texture,
                int offsetX, int offsetY,
                int glyphsPerRow, int glyphWidth, int glyphHeight) {

        this.glyphWidth = glyphWidth;
        this.glyphHeight = glyphHeight;
        int x = offsetX;
        int y = offsetY;

        for (int i = 0; i < 96; i++) {
            glyphs[i] = new TextureRegion(texture, x, y, glyphWidth, glyphHeight);
            x += glyphWidth;
            if (x == offsetX + glyphsPerRow * glyphWidth) {
                x = offsetX;
                y += glyphHeight;
            }
        }
    }

    public void drawText(SpriteBatcher batcher, String text, float centerX, float centerY) {
        int len = text.length();
        for (int i = 0; i < len; i++) {
            int c = text.charAt(i) - ' ';
            if (c < 0 || c >= glyphs.length)
                continue;

            batcher.drawSprite(centerX, centerY, glyphWidth, glyphHeight, glyphs[c]);
            centerX += glyphWidth;
        }
    }
}
