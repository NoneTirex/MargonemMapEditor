package pl.edu.tirex.marbya.editor.paints;

import javafx.scene.image.Image;
import pl.edu.tirex.marbya.editor.utils.keys.CoordinateKey;
import pl.edu.tirex.marbya.editor.MarbyaEditor;
import pl.edu.tirex.marbya.editor.profiles.kits.CacheKit;

import java.util.HashMap;

public class PaintLevel
{
    private HashMap<CoordinateKey, Image> images = new HashMap<CoordinateKey, Image>();
    private boolean hide;

    private final PaintTier parent;

    public PaintLevel(PaintTier parent)
    {
        this.parent = parent;
    }

    public HashMap<CoordinateKey, Image> getImages()
    {
        return this.images;
    }

    public void pasteCache(CacheKit cache, int startX, int startY)
    {
        if (cache == null) return;
        int width = cache.getWidth();
        if (startX + width >= this.parent.getParent().getWidth()) width = this.parent.getParent().getWidth() - startX;
        int height = cache.getHeight();
        if (startY + height >= this.parent.getParent().getHeight()) height = this.parent.getParent().getHeight() - startY;

        boolean edited = false;

        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                CoordinateKey ck = new CoordinateKey(startX + x, startY + y);
                Image img = null;
                if (cache.getImage() != null)
                {
                    img = cache.getImage().get(new CoordinateKey(x, y));
                }
                if (this.images.remove(ck) != null) edited = true;
                if (img == null) continue;
                this.images.put(ck, img);
                edited = true;
            }
        }
        if (edited) this.parent.getParent().edited = true;
        MarbyaEditor.getEditorController().updateSaveAlert();
    }

    public boolean visible()
    {
        return !this.hide;
    }
}
