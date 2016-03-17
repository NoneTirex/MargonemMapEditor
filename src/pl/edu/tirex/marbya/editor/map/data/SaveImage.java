package pl.edu.tirex.marbya.editor.map.data;

import javafx.scene.image.Image;
import pl.edu.tirex.marbya.editor.utils.keys.CoordinateKey;

public class SaveImage
{
    public SaveImage(Image img, int tier, int level, CoordinateKey ck)
    {
        this.image = img;
        this.tier = tier;
        this.level = level;
        this.coordinateKey = ck;
    }

    public Image getImage()
    {
        return image;
    }

    public int getTier()
    {
        return tier;
    }

    public int getLevel()
    {
        return level;
    }

    public CoordinateKey getCoordinateKey()
    {
        return coordinateKey;
    }

    private Image image;
    private int tier;
    private int level;
    private CoordinateKey coordinateKey;
}
