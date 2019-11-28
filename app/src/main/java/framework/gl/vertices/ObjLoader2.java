package framework.gl.vertices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import framework.Game;

public class ObjLoader2 {

    public static Vertices3 load(Game game, String file) {

        int vLines = 0, vnLines = 0, vtLines = 0;
        int verticesRequired = 0, indicesRequired = 0;
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(
				game.getFileIO().readAsset(file)));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("v ")) {
                    vLines++;
                    continue;
                }

                if (line.startsWith("vt")) {
                    vtLines++;
                    continue;
                }

                if (line.startsWith("vn")) {
                    vnLines++;
                    continue;
                }

                if (line.startsWith("f")) {
                    String[] points = line.split("[ ]+");
                    verticesRequired += (points.length - 1);
                    indicesRequired += (points.length * 3 - 9);
                }
            }
            reader.close();

            float[] x = new float[vLines];
            float[] y = new float[vLines];
            float[] z = new float[vLines];

            float[] u = new float[vtLines];
            float[] v = new float[vtLines];

            float[] i = new float[vnLines];
            float[] j = new float[vnLines];
            float[] k = new float[vnLines];

            float[] vertices = new float[verticesRequired * 8];
            short[] indices = new short[indicesRequired];

            int vertCounter = 0, coordCounter = 0, normCounter = 0;
            int modelCounter = 0, indicesCounter = 0;
            short pointCounter = 0;

            reader = new BufferedReader(new InputStreamReader(
				game.getFileIO().readAsset(file)));

            while ((line = reader.readLine()) != null) {

                if (line.startsWith("v ")) {
                    String[] tokens = line.split("[ ]+");
                    x[vertCounter] = Float.parseFloat(tokens[1]);
                    y[vertCounter] = Float.parseFloat(tokens[2]);
                    z[vertCounter] = Float.parseFloat(tokens[3]);
                    vertCounter++;
                    continue;
                }

                if (line.startsWith("vt")) {
                    String[] tokens = line.split("[ ]+");
                    u[coordCounter] = Float.parseFloat(tokens[1]);
                    v[coordCounter] = 1 - Float.parseFloat(tokens[2]);
                    coordCounter++;
                    continue;
                }

                if (line.startsWith("vn")) {
                    String[] tokens = line.split("[ ]+");
                    i[normCounter] = Float.parseFloat(tokens[1]);
                    j[normCounter] = Float.parseFloat(tokens[2]);
                    k[normCounter] = Float.parseFloat(tokens[3]);
                    normCounter++;
                    continue;
                }

                if (line.startsWith("f")) {
                    String[] tokens = line.split("[ ]+");
                    int tokensLength = tokens.length;
                    String[] point;

                    for (int c = 1; c < tokensLength; c++) {
                        point = tokens[c].split("/");
                        vertices[modelCounter++] = x[Integer.parseInt(point[0]) - 1];
                        vertices[modelCounter++] = y[Integer.parseInt(point[0]) - 1];
                        vertices[modelCounter++] = z[Integer.parseInt(point[0]) - 1];

                        vertices[modelCounter++] = u[Integer.parseInt(point[1]) - 1];
                        vertices[modelCounter++] = v[Integer.parseInt(point[1]) - 1];

                        vertices[modelCounter++] = i[Integer.parseInt(point[2]) - 1];
                        vertices[modelCounter++] = j[Integer.parseInt(point[2]) - 1];
                        vertices[modelCounter++] = k[Integer.parseInt(point[2]) - 1];
                    }

                    for (int c = 3; c < tokensLength; c++) {
                        indices[indicesCounter++] = pointCounter;
                        indices[indicesCounter++] = (short) (pointCounter + c - 2);
                        indices[indicesCounter++] = (short) (pointCounter + c - 1);
                    }
                    pointCounter += (tokensLength - 1);
                }
            }

            Vertices3 model3D = new Vertices3(game.getGraphics(), verticesRequired, indicesRequired,
                    false, true, true);
            model3D.setVertices(vertices, 0, vertices.length);
            model3D.setIndices(indices, 0, indices.length);

            return model3D;

        } catch (IOException e) {
            throw new RuntimeException("couldn't load '" + file + "'", e);

        } finally {
            if (reader != null)
                try {
		    reader.close();
                } catch (Exception ignored) {}
        }
    }
}
