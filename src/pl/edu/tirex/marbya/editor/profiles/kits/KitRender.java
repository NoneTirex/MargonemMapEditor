package pl.edu.tirex.marbya.editor.profiles.kits;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import pl.edu.tirex.marbya.editor.utils.keys.CoordinateKey;

import java.util.HashMap;
import java.util.Map;

public class KitRender
{
//    private Image[][] elements = new Image[0][8];
    private HashMap<CoordinateKey, Image> elements = new HashMap<CoordinateKey, Image>();
    private int height;
    private final Canvas canvas;

    public KitRender()
    {
        this.canvas = new Canvas(8 * 32, 32);
    }

    public void addImage(Image img)
    {
        int maxx = (int) Math.ceil(img.getWidth() / 32);
        int maxy = (int) Math.ceil(img.getHeight() / 32);

        PixelReader reader = img.getPixelReader();

        int preStartY = this.height;
        int prex = 0;
        int prey = 0;
        int usex = 0;
        int usey = 0;
        for (int x = 0; x < maxx; x++)
        {
            for (int y = 0; y < maxy; y++)
            {
                int startx = x * 32;
                int cutx = startx + 32;
                if (cutx > img.getWidth()) cutx = (int) img.getWidth();
                int starty = y * 32;
                int cuty = starty + 32;
                if (cuty > img.getHeight()) cuty = (int) img.getHeight();
                this.elements.put(new CoordinateKey(usex, preStartY + usey), new WritableImage(reader, startx, starty, cutx - startx, cuty - starty));
                usey++;
            }
            usey = prey;
            usex++;
            if (usex >= 8)
            {
                usex = 0;
                prey += maxy;
                usey = prey;
                prex++;
            }
        }
        this.height += prex == 0 ? maxy : prex * maxy;
    }

    public Canvas render()
    {
        this.canvas.setHeight(this.height * 32);
        GraphicsContext ctx = this.canvas.getGraphicsContext2D();
        ctx.clearRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
        for (Map.Entry<CoordinateKey, Image> entry : this.elements.entrySet())
        {
            Image image = entry.getValue();
            if (image == null) continue;
            CoordinateKey ck = entry.getKey();
            if (ck == null) continue;
            ctx.drawImage(image, ck.getX() * 32, ck.getY() * 32);
        }
        return this.canvas;
    }

    public int getHeight()
    {
        return this.height;
    }

    public int getWidth()
    {
        return 8;
    }

    public HashMap<CoordinateKey, Image> getImages()
    {
        return this.elements;
    }
}
