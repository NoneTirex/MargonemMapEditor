package pl.edu.tirex.marbya.editor.profiles.kits;

import javafx.scene.image.Image;
import pl.edu.tirex.marbya.editor.utils.keys.CoordinateKey;

import java.util.HashMap;

public class CacheKit
{
    private final HashMap<CoordinateKey, Image> images;
    private final int width;
    private final int height;

    public CacheKit(HashMap<CoordinateKey, Image> images, int width, int height)
    {
        this.images = images;
        this.width = width;
        this.height = height;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public HashMap<CoordinateKey, Image> getImage()
    {
        return this.images;
    }
}
