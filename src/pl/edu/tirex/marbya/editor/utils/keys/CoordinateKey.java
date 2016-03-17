package pl.edu.tirex.marbya.editor.utils.keys;

public class CoordinateKey
{
    private final int x;
    private final int y;

    public CoordinateKey(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public int getY()
    {
        return y;
    }

    public int getX()
    {
        return x;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CoordinateKey that = (CoordinateKey) o;

        if (x != that.x) return false;
        if (y != that.y) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString()
    {
        return "CoordinateKey{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
