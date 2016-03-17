package pl.edu.tirex.marbya.editor.profiles;

import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import pl.edu.tirex.marbya.editor.MarbyaEditor;

public class ProfileCell extends ListCell<Profile>
{
    @Override
    public void updateItem(final Profile item, boolean empty)
    {
        if (item == null || empty) return;
        Rectangle rect = new Rectangle(16, 16);
        Image icon = item.getIcon();
        if (icon != null)
        {
            rect.setFill(new ImagePattern(icon));
        }
        else
        {
            rect.setFill(Color.GREY);
        }

        this.setGraphic(rect);
        this.setText((item.isEmpty() ? "[*] " : "") + item.getId() + ". " + item.getName());

        this.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                item.handle(event);
            }
        });
    }
}
