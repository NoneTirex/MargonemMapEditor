package pl.edu.tirex.marbya.editor.map;

import javafx.scene.canvas.Canvas;
import pl.edu.tirex.marbya.editor.MarbyaEditor;
import pl.edu.tirex.marbya.editor.paints.Brush;
import pl.edu.tirex.marbya.editor.paints.Paint;
import pl.edu.tirex.marbya.editor.paints.brushes.DefaultBrush;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class MarbyaMap
{
    private static final int DEFAULT_WIDTH = 16;
    private static final int DEFAULT_HEIGHT = 16;

    private final Paint paint;
    private Brush brush;

    public MarbyaMap()
    {
        this(MarbyaMap.DEFAULT_WIDTH, MarbyaMap.DEFAULT_HEIGHT);
    }

    public MarbyaMap(int width, int height)
    {
        this.paint = new Paint(width, height);
        this.brush = new DefaultBrush();
    }

    public int getWidth()
    {
        return this.paint.getWidth();
    }

    public int getHeight()
    {
        return this.paint.getHeight();
    }

    public void render()
    {
        Canvas canvas = this.paint.render();

        if (MarbyaEditor.getSelectedKit() != null && this.brush != null)
        {
            this.brush.render(canvas);
        }

        MarbyaEditor.getEditorController().drawMap.setPrefWidth(this.getWidth() * 32);
        MarbyaEditor.getEditorController().drawMap.setPrefHeight(this.getHeight() * 32);

        MarbyaEditor.getEditorController().drawMap.getChildren().clear();
        MarbyaEditor.getEditorController().drawMap.getChildren().add(canvas);
    }

    public void saveToFile(File path)
    {
        if (path == null) return;
        try {
            ImageIO.write(this.paint.renderToImage(), "png", path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Brush getBrush()
    {
        return this.brush;
    }

    public void setBrush(Brush b) { this.brush = b; }

    public Paint getPaint()
    {
        return this.paint;
    }

    public boolean edited()
    {
        return this.paint.edited();
    }
}
