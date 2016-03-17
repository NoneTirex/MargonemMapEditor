package pl.edu.tirex.marbya.editor.paints;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import pl.edu.tirex.marbya.editor.utils.keys.CoordinateKey;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.Map;

public class Paint
{
    public PaintTier[] getTiers()
    {
        return tiers;
    }

    private PaintTier[] tiers = new PaintTier[3];
    private int selectedTier;

    private int width;
    private int height;

    private final Canvas canvas;

    protected boolean edited;

    public Paint(int width, int height)
    {
        this.width = width;
        this.height = height;
        this.canvas = new Canvas(width * 32, height * 32);
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public Canvas render()
    {
        this.canvas.setWidth(this.width * 32);
        this.canvas.setHeight(this.height * 32);
        GraphicsContext ctx = this.canvas.getGraphicsContext2D();
        ctx.clearRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
        for (int i = 0; i < this.tiers.length; i++)
        {
            PaintTier tier = this.tiers[i];
            if (tier == null || !tier.visible()) continue;
            for (int t = 0; t < tier.getLevels().length; t++)
            {
                PaintLevel level = tier.getLevels()[t];
                if (level == null || !level.visible()) continue;
                for (Map.Entry<CoordinateKey, Image> entry : level.getImages().entrySet())
                {
                    Image image = entry.getValue();
                    if (image == null) continue;
                    CoordinateKey ck = entry.getKey();
                    if (ck == null) continue;
                    ctx.drawImage(image, ck.getX() * 32, ck.getY() * 32);
                }
            }
        }
        return this.canvas;
    }

    public RenderedImage renderToImage()
    {
        Canvas canvas = this.render();
        WritableImage img = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());

        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.TRANSPARENT);

        canvas.snapshot(sp, img);
        return SwingFXUtils.fromFXImage(img, null);
    }

    public PaintTier getSelectedTier()
    {
        if (this.selectedTier + 1 > this.tiers.length) return null;
        PaintTier tier = this.tiers[this.selectedTier];
        if (tier == null)
        {
            tier = new PaintTier(this);
            this.tiers[this.selectedTier] = tier;
        }
        return tier;
    }

    public void setSelectedTier(int selectedTier)
    {
        this.selectedTier = selectedTier;
    }

    public void setEdited(boolean b)
    {
        this.edited = b;
    }

    public boolean edited()
    {
        if (!this.edited) return false;
        return this.edited;
    }
}
