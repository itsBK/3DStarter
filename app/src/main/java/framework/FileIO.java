package framework;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileIO {

    private AssetManager assets;
    private String externalStoragePath;


    public FileIO(Context context) {
        this.assets = context.getAssets();
        this.externalStoragePath = context.getExternalFilesDir(null)
                .getAbsolutePath() + java.io.File.separator;
    }

    public InputStream readAsset(String fileName) throws IOException {
        return assets.open(fileName);
    }

    public InputStream readFile(String fileName) throws IOException {
        return new FileInputStream(externalStoragePath + fileName);
    }

    public OutputStream writeFile(String fileName) throws IOException {
        return new FileOutputStream(externalStoragePath + fileName);
    }
}
