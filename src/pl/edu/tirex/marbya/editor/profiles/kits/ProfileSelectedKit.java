package pl.edu.tirex.marbya.editor.profiles.kits;

import javafx.scene.image.Image;
import pl.edu.tirex.marbya.editor.utils.keys.CoordinateKey;
import pl.edu.tirex.marbya.editor.profiles.Profile;

import java.util.HashMap;

public class ProfileSelectedKit
{
    private final Profile parent;
    private boolean selected;
    private CacheKit cache;

    private int preStartX;
    private int preStartY;

    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public ProfileSelectedKit(Profile parent)
    {
        this.parent = parent;
    }

    public void select(int startX, int startY, int endX, int endY)
    {
        this.startX = startX;
        if (this.startX >= 8) this.startX = 7;
        if (this.startX < 0) this.startX = 0;
        this.startY = startY;
        if (this.startY < 0) this.startY = 0;
        if (this.startY >= this.parent.getHeight()) this.startY = this.parent.getHeight() - 1;

        this.endX = endX;
        if (this.endX >= 8) this.endX = 7;
        if (this.endX < 0) this.endX = 0;
        this.endY = endY;
        if (this.endY >= this.parent.getHeight()) this.endY = this.parent.getHeight() - 1;
        if (this.endY < 0) this.endY = 0;

        this.selected = startX >= 0 && startY >= 0 && endX >= 0 && endY >= 0;
        if (this.isSelected())
        {
            int width = this.startX >= this.endX ? 1 : (this.endX - this.startX) + 1;
            int height = this.startY >= this.endY ? 1 : (this.endY - this.startY) + 1;
            HashMap<CoordinateKey, Image> cacheImage = new HashMap<CoordinateKey, Image>(height * width);
            HashMap<CoordinateKey, Image> original = this.parent.getKitRender().getImages();
            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    CoordinateKey ck = new CoordinateKey(startX + x, startY + y);
                    Image img = original.get(ck);
                    cacheImage.put(new CoordinateKey(x, y), img);
                }
            }
            this.cache = new CacheKit(cacheImage, width, height);
            this.parent.render();
        }
    }

    public void preSelect(int startX, int startY)
    {
        this.preStartX = startX;
        this.preStartY = startY;
    }

    public void endSelect(int endX, int endY)
    {
        int preStartX = this.preStartX;
        int preStartY = this.preStartY;
        if (preStartX > endX)
        {
            preStartX = endX;
            endX = this.preStartX;
        }
        if (preStartY > endY)
        {
            preStartY = endY;
            endY = this.preStartY;
        }
        this.select(preStartX, preStartY, endX, endY);
    }

    public boolean isSelected()
    {
        return this.selected;
    }

    public int getStartX()
    {
        return this.startX;
    }

    public int getStartY()
    {
        return this.startY;
    }

    public int getEndX()
    {
        return this.endX;
    }

    public int getEndY()
    {
        return this.endY;
    }

    public final Profile getParent()
    {
        return this.parent;
    }

    public CacheKit getCache()
    {
        return cache;
    }
}
