package pl.edu.tirex.marbya.editor;

import javafx.stage.Stage;
import pl.edu.tirex.marbya.editor.ini.Settings;
import pl.edu.tirex.marbya.editor.main.Editor;
import pl.edu.tirex.marbya.editor.main.EditorController;
import pl.edu.tirex.marbya.editor.map.MarbyaMap;
import pl.edu.tirex.marbya.editor.profiles.ProfileManager;
import pl.edu.tirex.marbya.editor.profiles.kits.ProfileSelectedKit;

import java.io.File;
import java.net.URL;

public class MarbyaEditor
{
    private static Editor editor;
    private static Settings config;
    private static Settings profileConfig;
    private static ProfileManager profileManager;
    private static EditorController editorController;
    private static ProfileSelectedKit selectedKit;
    private static MarbyaMap marbyaMap = new MarbyaMap();

    private static Stage mainStage;


    public static URL getResource(String resource)
    {
        return MarbyaEditor.class.getClassLoader().getResource("resource/" + resource);
    }

    public static void setEditor(Editor e)
    {
        if (editor == null)
        {
            editor = e;
        }
    }

    public static Editor getEditor()
    {
        return editor;
    }



    public static void setConfig(Settings e)
    {
        if (config == null)
        {
            config = e;
        }
    }

    public static Settings getConfig()
    {
        return config;
    }



    public static void setProfileConfig(Settings e)
    {
        profileConfig = e;
    }

    public static Settings getProfileConfig()
    {
        return profileConfig;
    }



    public static void setProfileManager(ProfileManager e)
    {
        profileManager = e;
    }

    public static ProfileManager getProfileManager()
    {
        return profileManager;
    }



    public static void setEditorController(EditorController e)
    {
        if (editorController == null)
        {
            editorController = e;
        }
    }

    public static EditorController getEditorController()
    {
        return editorController;
    }



    public static ProfileSelectedKit getSelectedKit()
    {
        return selectedKit;
    }

    public static void setSelectedKit(ProfileSelectedKit selectedKit)
    {
        MarbyaEditor.selectedKit = selectedKit;
    }



    public static MarbyaMap getMarbyaMap()
    {
        return marbyaMap;
    }

    public static void setMarbyaMap(MarbyaMap marbyaMap)
    {
        MarbyaEditor.marbyaMap = marbyaMap;
    }

    public static Stage getMainStage()
    {
        return mainStage;
    }

    public static void setMainStage(Stage mainStage)
    {
        MarbyaEditor.mainStage = mainStage;
    }
}
