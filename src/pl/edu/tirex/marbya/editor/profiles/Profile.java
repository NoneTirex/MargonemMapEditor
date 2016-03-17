package pl.edu.tirex.marbya.editor.profiles;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import pl.edu.tirex.marbya.editor.MarbyaEditor;
import pl.edu.tirex.marbya.editor.ini.Settings;
import pl.edu.tirex.marbya.editor.profiles.kits.KitRender;
import pl.edu.tirex.marbya.editor.profiles.kits.ProfileSelectedKit;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

public class Profile
{
    private static long last_id;

    private final long id = ++last_id;

    private String name;
    private File dir;
    protected KitRender kit;

    public Profile(File dir)
    {
        this.dir = dir;
        this.name = dir.getName();
        Settings config = MarbyaEditor.getProfileConfig().getNode(this.name);
        this.name = config.get("name", this.name);
    }

    public boolean isEmpty()
    {
        return this.dir.isDirectory() && this.dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File filePath) {
                return filePath.isFile();
            }
        }).length == 0;
    }

    public String getName()
    {
        return this.name;
    }

    public boolean isActive()
    {
        return this.kit != null;
    }

    public void update()
    {
        this.kit = new KitRender();
        for (File f : this.dir.listFiles())
        {
            if (f.isFile() && f.canRead())
            {
                Image img = new Image(f.toURI().toString());
                if (img == null || img.getException() != null) continue;
                kit.addImage(img);
            }
        }
    }

    public void render()
    {
        if (this.kit == null) this.update();

        Canvas canvas = this.kit.render();
        GraphicsContext ctx = canvas.getGraphicsContext2D();

        ProfileSelectedKit selectedKit = MarbyaEditor.getSelectedKit();

        if (selectedKit != null && selectedKit.isSelected())
        {
            int rectW = selectedKit.getStartX() >= selectedKit.getEndX() ? 1 : (selectedKit.getEndX() - selectedKit.getStartX()) + 1;
            rectW *= 32;
            int rectH = selectedKit.getStartY() >= selectedKit.getEndY() ? 1 : (selectedKit.getEndY() - selectedKit.getStartY()) + 1;
            rectH *= 32;
            ctx.setFill(Color.rgb(255, 0, 255, 0.3));
            ctx.fillRect(selectedKit.getStartX() * 32, selectedKit.getStartY() * 32, rectW, rectH);

            ctx.setStroke(Color.BLACK);
            ctx.strokeRect(selectedKit.getStartX() * 32, selectedKit.getStartY() * 32, rectW, rectH);
        }

        MarbyaEditor.getEditorController().drawProfile.setMinWidth(canvas.getWidth());
        MarbyaEditor.getEditorController().drawProfile.setMinHeight(canvas.getHeight());

        MarbyaEditor.getEditorController().drawProfile.getChildren().clear();
        MarbyaEditor.getEditorController().drawProfile.getChildren().add(canvas);

        MarbyaEditor.getEditorController().scrollProfile.setVisible(true);
        MarbyaEditor.getEditorController().helperList.setVisible(false);
    }

    public void handle(MouseEvent e)
    {
        if (MarbyaEditor.getSelectedKit() == null || !MarbyaEditor.getSelectedKit().getParent().equals(this)) MarbyaEditor.setSelectedKit(new ProfileSelectedKit(this));

        this.render();
    }

    public KitRender getKitRender()
    {
        return this.kit;
    }

    public Image getIcon()
    {
        File f = new File(this.dir, "icon.png");
        if (!f.exists())
        {
            f = new File("assets" + File.separator + "icon.png");
            if (!f.exists()) return null;
        }
        if (!f.isFile()) return null;
        Image image = new Image(f.toURI().toString());
        return image;
    }

    public int getHeight()
    {
        if (this.kit == null) return 0;
        return this.kit.getHeight();
    }

    public int getWidth()
    {
        if (this.kit == null) return 0;
        return this.kit.getWidth();
    }

    public long getId()
    {
        return this.id;
    }
}
