package pl.edu.tirex.marbya.editor.paints.brushes;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import pl.edu.tirex.marbya.editor.MarbyaEditor;
import pl.edu.tirex.marbya.editor.paints.Brush;
import pl.edu.tirex.marbya.editor.paints.PaintLevel;
import pl.edu.tirex.marbya.editor.paints.PaintTier;
import pl.edu.tirex.marbya.editor.profiles.kits.CacheKit;

public class RubberBrush extends Brush
{
    @Override
    public void render(Canvas canvas)
    {
        if (!super.hover) return;
        GraphicsContext ctx = canvas.getGraphicsContext2D();
        ctx.setLineWidth(5.0);
        ctx.setStroke(Color.rgb(255, 255, 255, 0.85));
        ctx.strokeRect(super.hoverX * 32, super.hoverY * 32, 32, 32);
        ctx.setLineWidth(2.0);
        ctx.setStroke(Color.rgb(0, 0, 0, 0.85));
        ctx.strokeRect(super.hoverX * 32, super.hoverY * 32, 32, 32);
    }

    @Override
    public void clickEvent(int x, int y)
    {
        if (MarbyaEditor.getMarbyaMap() == null) return;
        PaintTier tier = MarbyaEditor.getMarbyaMap().getPaint().getSelectedTier();
        if (tier == null) return;
        PaintLevel level = tier.getSelectedLevel();
        if (level == null) return;
        level.pasteCache(new CacheKit(null, 1, 1), x, y);
        MarbyaEditor.getMarbyaMap().render();
    }

    @Override
    public void selectEvent(int startX, int startY, int endX, int endY)
    {
        super.hover(endX, endY);
        super.click(endX, endY);
    }
}
