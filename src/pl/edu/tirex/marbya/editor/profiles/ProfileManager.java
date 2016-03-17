package pl.edu.tirex.marbya.editor.profiles;

import javafx.scene.canvas.Canvas;
import pl.edu.tirex.marbya.editor.MarbyaEditor;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class ProfileManager
{
    private final List<Profile> PROFILES = new ArrayList<Profile>();

    public ProfileManager(File dir)
    {
        for (File f : dir.listFiles(new FileFilter()
        {
            @Override
            public boolean accept(File pathname)
            {
                return pathname.isDirectory();
            }
        }))
        {
            Profile p = new Profile(f);
            this.PROFILES.add(p);
        }
    }

    public void disableProfiles()
    {
        this.PROFILES.forEach(p -> p.kit = null);
        MarbyaEditor.getEditorController().helperList.setVisible(true);
        MarbyaEditor.getEditorController().scrollProfile.setVisible(false);
    }

    public Profile getSelectedProfile()
    {
        for (Profile p : this.PROFILES)
        {
            if (p.isActive())
            {
                return p;
            }
        }
        return null;
    }

    public List<Profile> getProfiles()
    {
        return this.PROFILES;
    }
}
