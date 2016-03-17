package pl.edu.tirex.marbya.editor.main;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import pl.edu.tirex.marbya.editor.MarbyaEditor;
import pl.edu.tirex.marbya.editor.ini.Preferences;
import pl.edu.tirex.marbya.editor.ini.Settings;
import pl.edu.tirex.marbya.editor.profiles.ProfileManager;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Editor extends Application
{
    public static void main(String[] args)
    {
        try {
            Enumeration<URL> resources =  MarbyaEditor.class.getClassLoader().getResources("resource/extract");
            System.out.println(resources);
            while (resources.hasMoreElements())
            {
                URL url = resources.nextElement();
                File f = new File(url.getFile());
                System.out.println("dsd");
                System.out.println(url.getFile());
                if (!f.exists()) Files.copy(url.openStream(), f.toPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Editor.launch(args);
    }

    private Settings config;

    public Editor()
    {
        MarbyaEditor.setEditor(this);
        File f = this.getDirectory("config");
        MarbyaEditor.setConfig(new Settings(new Preferences(new File(f, "config.ini"))));
        f = this.getDirectory("assets");
        MarbyaEditor.setProfileConfig(new Settings(new Preferences(new File(f, "config.ini"))));
        MarbyaEditor.setProfileManager(new ProfileManager(f));
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        MarbyaEditor.setMainStage(primaryStage);
        Settings gui = MarbyaEditor.getConfig().getNode("gui");

        Parent root = FXMLLoader.load(getClass().getResource("editor.fxml"));
        primaryStage.setTitle("Marbya MapEditor");
        primaryStage.setScene(new Scene(root, gui.getInt("width", 1280), gui.getInt("height", 720)));
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(480);
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
        {
            @Override
            public void handle(WindowEvent event)
            {
                if (!MarbyaEditor.getMarbyaMap().edited()) return;

                if (Editor.this.cancelSave(null, null, null))
                {
                    event.consume();
                }
            }
        });

//        primaryStage.setMaximized(true);
    }

    public List<String> getProfileList()
    {
        List<String> list  = new ArrayList<String>();
        File f = new File("assets");
        if (!f.exists())
        {
            f.mkdir();
            return list;
        }
        for (String file : f.list(new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String name)
            {
                return new File(dir, name).isDirectory();
            }
        }))
        {
            list.add(file);
        }
        return list;
    }

    private File getDirectory(String name)
    {
        File f = new File(name);
        if (!f.exists())
        {
            f.mkdir();
        }
        return f;
    }

    public boolean cancelSave(String title, String headerText, String contentText)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title == null || title.isEmpty() ? "Zanim wyjdziesz" : title);
        alert.setHeaderText(headerText == null || headerText.isEmpty() ? "Nie zapisałeś swojego projektu" : headerText);
        alert.setContentText(contentText == null || contentText.isEmpty() ? "Czy na pewno chcesz wyjść z programu?" : contentText);
        ButtonType btnTypeYes = new ButtonType("Tak", ButtonBar.ButtonData.APPLY);
        ButtonType btnTypeNo = new ButtonType("Nie", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnTypeYes, btnTypeNo);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == btnTypeNo;
    }
}
