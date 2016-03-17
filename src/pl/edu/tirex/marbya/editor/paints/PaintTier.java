package pl.edu.tirex.marbya.editor.paints;

public class PaintTier
{
    private PaintLevel[] levels = new PaintLevel[10];
    private int selectedLevel;
    private boolean hide;

    private final Paint parent;

    public PaintTier(Paint parent)
    {
        this.parent = parent;
    }

    public Paint getParent()
    {
        return parent;
    }

    public PaintLevel[] getLevels()
    {
        return this.levels;
    }

    public PaintLevel getSelectedLevel()
    {
        if (this.selectedLevel + 1 > this.levels.length) return null;
        PaintLevel level = this.levels[this.selectedLevel];
        if (level == null)
        {
            level = new PaintLevel(this);
            this.levels[this.selectedLevel] = level;
        }
        return level;
    }

    public void setSelectedLevel(int selectedLevel)
    {
        this.selectedLevel = selectedLevel;
    }

    public boolean visible()
    {
        return !this.hide;
    }
}
