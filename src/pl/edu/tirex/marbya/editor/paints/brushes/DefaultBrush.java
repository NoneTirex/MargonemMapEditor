package pl.edu.tirex.marbya.editor.paints.brushes;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import pl.edu.tirex.marbya.editor.MarbyaEditor;
import pl.edu.tirex.marbya.editor.paints.Brush;
import pl.edu.tirex.marbya.editor.paints.PaintLevel;
import pl.edu.tirex.marbya.editor.paints.PaintTier;
import pl.edu.tirex.marbya.editor.profiles.kits.CacheKit;

public class DefaultBrush extends Brush
{

    @Override
    public void render(Canvas canvas)
    {
        if (!super.hover) return;
        GraphicsContext ctx = canvas.getGraphicsContext2D();
        CacheKit cache = MarbyaEditor.getSelectedKit().getCache();
        if (cache != null)
        {
            ctx.setLineWidth(5.0);
            ctx.setStroke(Color.rgb(255, 255, 255, 0.85));
            ctx.strokeRect(super.hoverX * 32, super.hoverY * 32, cache.getWidth() * 32, cache.getHeight() * 32);
            ctx.setLineWidth(2.0);
            ctx.setStroke(Color.rgb(0, 0, 0, 0.85));
            ctx.strokeRect(super.hoverX * 32, super.hoverY * 32, cache.getWidth() * 32, cache.getHeight() * 32);
        }
    }

    @Override
    public void clickEvent(int x, int y)
    {
        if (MarbyaEditor.getMarbyaMap() == null) return;
        if (MarbyaEditor.getSelectedKit() == null) return;
        PaintTier tier = MarbyaEditor.getMarbyaMap().getPaint().getSelectedTier();
        if (tier == null) return;
        PaintLevel level = tier.getSelectedLevel();
        if (level == null) return;

        level.pasteCache(MarbyaEditor.getSelectedKit().getCache(), x, y);
        MarbyaEditor.getMarbyaMap().render();
    }

    @Override
    public void selectEvent(int startX, int startY, int endX, int endY)
    {
        super.hover(endX, endY);
        super.click(endX, endY);
    }
}
