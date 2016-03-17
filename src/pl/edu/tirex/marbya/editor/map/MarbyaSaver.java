package pl.edu.tirex.marbya.editor.map;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import pl.edu.tirex.marbya.editor.MarbyaEditor;
import pl.edu.tirex.marbya.editor.map.data.SaveImage;
import pl.edu.tirex.marbya.editor.paints.PaintLevel;
import pl.edu.tirex.marbya.editor.paints.PaintTier;
import pl.edu.tirex.marbya.editor.utils.keys.CoordinateKey;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.*;

//TODO Zrobic Zapis Swiata
public class MarbyaSaver
{
    private final File file;

    //POSTEPY
    public MarbyaSaver(File file)
    {
        this.file = file;
    }

    public void save()
    {
        if (this.file == null) return;
        if (!this.file.exists())
        {
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        HashMap<Image, String> imageId = new HashMap<Image, String>();
        List<SaveImage> images = new ArrayList<SaveImage>();

        for (int ti = 0; ti < MarbyaEditor.getMarbyaMap().getPaint().getTiers().length; ti++)
        {
            PaintTier tier = MarbyaEditor.getMarbyaMap().getPaint().getTiers()[ti];
            if (tier == null) continue;
            for (int li = 0; li < tier.getLevels().length; li++)
            {
                PaintLevel level = tier.getLevels()[li];
                if (level == null) continue;
                for (Map.Entry<CoordinateKey, Image> entry : level.getImages().entrySet())
                {
                    Image image = entry.getValue();
                    CoordinateKey ck = entry.getKey();
                    if (image == null || ck == null) continue;
                    String id = imageId.get(image);
                    if (id == null)
                    {
                        id = UUID.randomUUID().toString();
                        imageId.put(image, id);
                    }
                    images.add(new SaveImage(image, ti, li, ck));
                }
            }
        }

        StringBuilder sb = new StringBuilder();

        sb.append("MBMINIT");
        sb.append(System.lineSeparator());

        sb.append("width=" + MarbyaEditor.getMarbyaMap().getWidth());
        sb.append(System.lineSeparator());
        sb.append("height=" + MarbyaEditor.getMarbyaMap().getHeight());
        sb.append(System.lineSeparator());

        sb.append("MBMIMAGE");
        sb.append(System.lineSeparator());

        Base64.Encoder base64_enc = Base64.getEncoder();

        for (Map.Entry<Image, String> entry : imageId.entrySet())
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(entry.getKey(), null), "png", baos);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] pgnBytes = baos.toByteArray();
            sb.append("id=" + entry.getValue() + ":image=" + base64_enc.encodeToString(pgnBytes));
            sb.append(System.lineSeparator());
        }
        sb.append("MBMENDIMAGE");
        sb.append(System.lineSeparator());
        sb.append("MBMMAP");
        sb.append(System.lineSeparator());
        for (SaveImage si : images)
        {
            String id = imageId.get(si.getImage());
            sb.append("id=" + id + ":tier=" + si.getTier() + ":level=" + si.getLevel() + ":x=" + si.getCoordinateKey().getX() + ":y=" + si.getCoordinateKey().getY());
            sb.append(System.lineSeparator());
        }
        sb.append("MBMENDMAP");

        String converted = sb.toString();



        try(BufferedWriter bw = new BufferedWriter(new FileWriter(this.file)))
        {
            bw.write(converted);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
