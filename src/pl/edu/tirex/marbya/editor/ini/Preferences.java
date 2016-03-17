package pl.edu.tirex.marbya.editor.ini;

import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Preferences
{
    protected final HashMap<String, String> VALUES = new HashMap<String, String>();
    private final HashMap<String, Preferences> NODES = new HashMap<String, Preferences>();

    private String name;
    private File file;
    private Preferences parent;
    private Preferences target;

    public Preferences(File f)
    {
        if (f == null) return;
        this.file = f;
        if (!this.file.exists()) return;
        try(BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null)
            {
                if (line == null || line.isEmpty()) continue;
                if (line.startsWith("[") && line.endsWith("]"))
                {
                    String name = line.substring(1, line.length() - 1);
                    this.target = new Preferences(name, this);
                    this.NODES.put(name.toLowerCase(), this.target);
                    continue;
                }
                String[] split = line.split("=", 2);
                if (split.length == 2)
                {
                    String name = split[0];
                    if (name == null || name.isEmpty()) continue;
                    String value = split[1];
                    if (value == null || value.isEmpty()) continue;
                    if (this.target == null)
                    {
                        this.VALUES.put(name.toLowerCase(), value);
                        continue;
                    }
                    this.target.VALUES.put(name.toLowerCase(), value);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Preferences(String name, Preferences parent)
    {
        this.name = name;
        this.parent = parent;
    }

    public Preferences node(String name)
    {
        if (name == null) return null;
        return this.NODES.get(name.toLowerCase());
    }

    public Preferences create(String name)
    {
        if (name == null || name.isEmpty()) return null;
        Preferences p = new Preferences(name, this);
        this.NODES.put(name.toLowerCase(), p);
        return p;
    }

    public String get(String key, String def)
    {
        String value = this.VALUES.get(key.toLowerCase());
        if (value == null)
        {
            setValue(key, def);
            return def;
        }
        return value;
    }

    public String get(String key)
    {
        return this.get(key, null);
    }

    public int getInt(String key, int def)
    {
        try {
            String value = this.get(key);
            if (value == null)
            {
                setValue(key, def);
                return def;
            }
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            setValue(key, def);
            return def;
        }
    }

    public int getInt(String key)
    {
        return this.getInt(key, 0);
    }

    public long getLong(String key, long def)
    {
        try {
            String value = this.get(key);
            if (value == null)
            {
                setValue(key, def);
                return def;
            }
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            setValue(key, def);
            return def;
        }
    }

    public long getLong(String key)
    {
        return this.getLong(key, 0);
    }

    public double getDouble(String key, double def)
    {
        try {
            String value = this.get(key);
            if (value == null)
            {
                setValue(key, def);
                return def;
            }
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            setValue(key, def);
            return def;
        }
    }

    public double getDouble(String key)
    {
        return this.getDouble(key, 0);
    }

    public float getFloat(String key, float def)
    {
        try {
            String value = this.get(key);
            if (value == null)
            {
                setValue(key, def);
                return def;
            }
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            setValue(key, def);
            return def;
        }
    }

    public float getFloat(String key)
    {
        return this.getFloat(key, 0);
    }

    public boolean getBoolean(String key, boolean def)
    {
        String value = this.get(key);
        if (value == null)
        {
            setValue(key, def);
            return def;
        }
        return Boolean.parseBoolean(value);
    }

    public boolean getBoolean(String key)
    {
        return this.getBoolean(key, false);
    }


    public void setValue(String key, Object value)
    {
        if (key == null || key.isEmpty()) return;
        if (value == null) return;
        this.VALUES.put(key.toLowerCase(), value.toString());
        this.save();
    }

    protected List<String> getSaved()
    {
        List<String> save = new ArrayList<String>();
        if (this.name != null) save.add("[" + this.name + "]");
        for (Map.Entry<String, String> entry : this.VALUES.entrySet())
        {
            save.add(entry.getKey() + "=" + entry.getValue());
        }
        for (Preferences pref : this.NODES.values())
        {
            save.addAll(pref.getSaved());
        }
        return save;
    }

    private void save()
    {
        if (this.parent != null)
        {
            this.parent.save();
            return;
        }
        if (this.file == null) return;
        if (!this.file.exists()) try {
            this.file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(this.file))) {
            for (String str : this.getSaved())
            {
                bw.write(str);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
