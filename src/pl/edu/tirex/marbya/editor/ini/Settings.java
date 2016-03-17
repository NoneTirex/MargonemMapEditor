package pl.edu.tirex.marbya.editor.ini;

public class Settings
{
    private final Preferences prefs;
    private Preferences target;

    public Settings(Preferences prefs)
    {
        this.prefs = prefs;
        this.target = this.prefs;
    }

    public Settings getNode(String name)
    {
        if (name == null || name.isEmpty())
        {
            this.target = this.prefs;
            return this;
        }
        Preferences pref = this.prefs.node(name);
        if (pref == null) pref = this.prefs.create(name);
        this.target = pref;
        return this;
    }

    public String get(String key, String def)
    {
        return this.target.get(key, def);
    }

    public boolean getBoolean(String key, boolean def)
    {
        return this.target.getBoolean(key, def);
    }

    public double getDouble(String key, double def)
    {
        return this.target.getDouble(key, def);
    }

    public float getFloat(String key, float def)
    {
        return this.target.getFloat(key, def);
    }

    public int getInt(String key, int def)
    {
        return this.target.getInt(key, def);
    }

    public long getLong(String key, long def)
    {
        return this.target.getLong(key, def);
    }
}
