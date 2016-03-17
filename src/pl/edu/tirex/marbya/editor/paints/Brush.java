package pl.edu.tirex.marbya.editor.paints;

import javafx.scene.canvas.Canvas;
import pl.edu.tirex.marbya.editor.MarbyaEditor;

public abstract class Brush
{
    protected int hoverX;
    protected int hoverY;
    protected boolean hover;

    private int preStartX;
    private int preStartY;

    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public void hover(int x, int y)
    {
        if (this.hoverX == x && this.hoverY == y) return;
        if (x < 0 || y < 0) return;
        if (x >= MarbyaEditor.getMarbyaMap().getWidth() ||
                y >= MarbyaEditor.getMarbyaMap().getHeight()) return;
        this.hoverX = x;
        this.hoverY = y;
        this.hover = true;
        MarbyaEditor.getMarbyaMap().render();
    }

    public void click(int x, int y)
    {
        this.clickEvent(x, y);
    }

    public void select(int startX, int startY, int endX, int endY)
    {
        int cacheStartX = startX;
        if (cacheStartX >= MarbyaEditor.getMarbyaMap().getWidth()) cacheStartX = MarbyaEditor.getMarbyaMap().getWidth() - 1;
        if (cacheStartX < 0) cacheStartX = 0;
        int cacheStartY = startY;
        if (cacheStartY >= MarbyaEditor.getMarbyaMap().getHeight()) cacheStartY = MarbyaEditor.getMarbyaMap().getHeight() - 1;
        if (cacheStartY < 0) cacheStartY = 0;

        int cacheEndX = endX;
        if (cacheEndX >= MarbyaEditor.getMarbyaMap().getWidth()) cacheEndX = MarbyaEditor.getMarbyaMap().getWidth() - 1;
        if (cacheEndX < 0) cacheEndX = 0;
        int cacheEndY = endY;
        if (cacheEndY >= MarbyaEditor.getMarbyaMap().getHeight()) cacheEndY = MarbyaEditor.getMarbyaMap().getHeight() - 1;
        if (cacheEndY < 0) cacheEndY = 0;

        this.startX = cacheStartX;
        this.startY = cacheStartY;
        this.endX = cacheEndX;
        this.endY = cacheEndY;

        this.selectEvent(this.startX, this.startY, this.endX, this.endY);
    }

    public void preSelect(int startX, int startY)
    {
        if (startX == this.startX && startY == this.startY) return;
        this.preStartX = startX;
        this.preStartY = startY;
    }

    public void endSelect(int endX, int endY)
    {
        if (endX == this.endX && endY == this.endY) return;
        int preStartX = this.preStartX;
        int preStartY = this.preStartY;
        if (preStartX > endX)
        {
            preStartX = endX;
            endX = this.preStartX;
            this.preStartX = preStartX;
        }
        if (preStartY > endY)
        {
            preStartY = endY;
            endY = this.preStartY;
            this.preStartY = preStartY;
        }

        this.select(preStartX, preStartY, endX, endY);
    }

    public abstract void render(Canvas canvas);

    public abstract void clickEvent(int x, int y);

    public abstract void selectEvent(int startX, int startY, int endX, int endY);
}
