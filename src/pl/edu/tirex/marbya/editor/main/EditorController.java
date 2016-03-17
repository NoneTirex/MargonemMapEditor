package pl.edu.tirex.marbya.editor.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.Pair;
import pl.edu.tirex.marbya.editor.MarbyaEditor;
import pl.edu.tirex.marbya.editor.map.MarbyaMap;
import pl.edu.tirex.marbya.editor.map.MarbyaSaver;
import pl.edu.tirex.marbya.editor.paints.Brush;
import pl.edu.tirex.marbya.editor.paints.brushes.DefaultBrush;
import pl.edu.tirex.marbya.editor.paints.brushes.RubberBrush;
import pl.edu.tirex.marbya.editor.profiles.Profile;
import pl.edu.tirex.marbya.editor.profiles.ProfileCell;
import pl.edu.tirex.marbya.editor.utils.MyFileChooser;
import pl.edu.tirex.marbya.editor.utils.NumberField;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditorController implements Initializable
{
    @FXML
    public MenuItem newMapItem;

    @FXML
    public MenuItem openMapItem;

    @FXML
    public MenuItem saveMapItem;

    @FXML
    public MenuItem saveMapAsItem;


    @FXML
    public Button backButton;

    @FXML
    public Accordion helperList;

    @FXML
    public TitledPane helperProfile;

    @FXML
    public ListView profileList;


    @FXML
    public ScrollPane scrollProfile;

    @FXML
    public AnchorPane drawProfile;

    @FXML
    public AnchorPane drawMap;


    @FXML
    public ToolBar mainToolbar;


    @FXML
    public Label saveAlert;



    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        MarbyaEditor.setEditorController(this);
        this.newMapItem.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                if (MarbyaEditor.getMarbyaMap().edited())
                    if (MarbyaEditor.getEditor().cancelSave("Zanim stworzysz mape", "Nie zapisałeś swojego projektu", "Czy na pewno chcesz kontynuować?")) return;
                Dialog<Pair<Integer, Integer>> dialog = new Dialog();
                dialog.setTitle("Ustawienia Nowej Mapy");
                dialog.setHeaderText("Wybierz ustawienia nowej mapki");

                ButtonType btnTypeFinish = new ButtonType("Stwórz", ButtonBar.ButtonData.FINISH);
                ButtonType btnTypeCancel = new ButtonType("Anuluj", ButtonBar.ButtonData.CANCEL_CLOSE);
                dialog.getDialogPane().getButtonTypes().addAll(btnTypeFinish, btnTypeCancel);

                Node finishBtn = dialog.getDialogPane().lookupButton(btnTypeFinish);
                finishBtn.setDisable(true);

                NumberField width = new NumberField(0, 256);
                width.setPromptText("Szerokość");
                NumberField height = new NumberField(0, 256);
                height.setPromptText("Wysokość");

                width.textProperty().addListener((observable, oldValue, newValue) ->
                {
                    finishBtn.setDisable(width.isEmpty() || height.isEmpty());
                });
                height.textProperty().addListener((observable, oldValue, newValue) ->
                {
                    finishBtn.setDisable(width.isEmpty() || height.isEmpty());
                });

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                grid.add(new Label("Szerokość: "), 0, 0);
                grid.add(width, 1, 0);
                grid.add(new Label("Wysokość: "), 0, 1);
                grid.add(height, 1, 1);

                dialog.getDialogPane().setContent(grid);

                dialog.setResultConverter(dialogButton ->
                {
                    if (dialogButton == btnTypeFinish)
                    {
                        return new Pair<Integer, Integer>(width.getValue(), height.getValue());
                    }
                    return null;
                });

                Optional<Pair<Integer, Integer>> result = dialog.showAndWait();
                result.ifPresent(pair ->
                {
                    MarbyaEditor.setMarbyaMap(new MarbyaMap(pair.getKey(), pair.getValue()));
                    MarbyaEditor.getMarbyaMap().render();
                });
            }
        });
        this.openMapItem.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                if (MarbyaEditor.getMarbyaMap().edited())
                    if (MarbyaEditor.getEditor().cancelSave("Zanim otworzysz mape", "Nie zapisałeś swojego projektu", "Czy na pewno chcesz kontynuować?")) return;
            }
        });
        this.saveMapItem.setOnAction(this.saveMap(false));
        this.saveMapAsItem.setOnAction(this.saveMap(true));

        this.updateProfileList();
        this.setDrawProfileMouseHandlers();
        this.setDrawMapMouseHandlers();

        this.backButton.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                MarbyaEditor.getProfileManager().disableProfiles();
            }
        });

        this.mainToolbar.getItems().addAll(
                new Separator(),
                new Label("Narzędzia"),
                this.createBrushButton(DefaultBrush.class, "icon_brush.png"),
                this.createBrushButton(RubberBrush.class, "icon_rubber.png"),
                new Separator(),
                new Label("Poziom"),
                this.createTierButton(0),
                this.createTierButton(1),
                this.createTierButton(2),
                new Separator(),
                new Label("Warstwa"),
                this.createLayerButton(1),
                this.createLayerButton(2),
                this.createLayerButton(3),
                this.createLayerButton(4),
                this.createLayerButton(5),
                this.createLayerButton(6),
                this.createLayerButton(7),
                this.createLayerButton(8),
                this.createLayerButton(9),
                this.createLayerButton(10),
                new Separator()
        );
    }

    private void updateProfileList()
    {
        ObservableList<Profile> items = FXCollections.observableArrayList();
        for (final Profile p : MarbyaEditor.getProfileManager().getProfiles())
        {
            items.add(p);
        }
        this.profileList.setItems(items);

        this.profileList.setCellFactory(new Callback<ListView, ListCell>()
        {
            @Override
            public ListCell call(ListView list)
            {
                return new ProfileCell();
            }
        });
    }

    private void setDrawProfileMouseHandlers()
    {
        this.drawProfile.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                int startx = (int) (event.getX() / 32);
                if (startx >= 8) startx = 7;
                int starty = (int) (event.getY() / 32);
                MarbyaEditor.getSelectedKit().preSelect(startx, starty);
                MarbyaEditor.getSelectedKit().select(startx, starty, startx, starty);
            }
        });

        this.drawProfile.setOnMouseDragged(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                int startx = (int) (event.getX() / 32);
                if (startx >= 8) startx = 7;
                int starty = (int) (event.getY() / 32);
                MarbyaEditor.getSelectedKit().endSelect(startx, starty);
            }
        });
    }

    private void setDrawMapMouseHandlers()
    {
        this.drawMap.setOnMouseMoved(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                int x = (int) (event.getX() / 32);
                int y = (int) (event.getY() / 32);
                MarbyaEditor.getMarbyaMap().getBrush().hover(x, y);
            }
        });

        this.drawMap.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                int x = (int) (event.getX() / 32);
                int y = (int) (event.getY() / 32);
                MarbyaEditor.getMarbyaMap().getBrush().preSelect(x, y);
                MarbyaEditor.getMarbyaMap().getBrush().select(x, y, x, y);
            }
        });

        this.drawMap.setOnMouseDragged(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                int x = (int) (event.getX() / 32);
                int y = (int) (event.getY() / 32);
                MarbyaEditor.getMarbyaMap().getBrush().endSelect(x, y);
            }
        });
    }

    private Button lastTier;
    private Button lastLayer;
    private Button lastBrush;

    private Button createBrushButton(Class<? extends Brush> brushClass, String resource)
    {
        Button btn = new Button();
        this.buttonSetIcon(btn, "design/" + resource, 25, 25, 20, 20);
        if (brushClass.equals(DefaultBrush.class))
        {
            btn.setStyle("-fx-base: #66CCFF;");
            this.lastBrush = btn;
        }
        btn.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                if (MarbyaEditor.getMarbyaMap() == null) return;
                if (EditorController.this.lastBrush != null) EditorController.this.lastBrush.setStyle("");
                btn.setStyle("-fx-base: #66CCFF;");
                EditorController.this.lastBrush = btn;
                try {
                    MarbyaEditor.getMarbyaMap().setBrush(brushClass.newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        return btn;
    }

    private Button createTierButton(final int id)
    {
        final Button btn = new Button(Integer.toString(id));
        if (id == 0)
        {
            btn.setStyle("-fx-base: #66CCFF;");
            this.lastTier = btn;
        }
        btn.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                if (MarbyaEditor.getMarbyaMap() == null) return;
                if (EditorController.this.lastTier != null) EditorController.this.lastTier.setStyle("");
                btn.setStyle("-fx-base: #66CCFF;");
                EditorController.this.lastTier = btn;
                MarbyaEditor.getMarbyaMap().getPaint().setSelectedTier(id);
            }
        });

        return btn;
    }

    private Button createLayerButton(final int id)
    {
        final Button btn = new Button(Integer.toString(id));
        if (id == 1)
        {
            btn.setStyle("-fx-base: #66CCFF;");
            this.lastLayer = btn;
        }
        btn.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                if (MarbyaEditor.getMarbyaMap() == null) return;
                if (EditorController.this.lastLayer != null) EditorController.this.lastLayer.setStyle("");
                btn.setStyle("-fx-base: #66CCFF;");
                EditorController.this.lastLayer = btn;
                MarbyaEditor.getMarbyaMap().getPaint().getSelectedTier().setSelectedLevel(id - 1);
            }
        });

        return btn;
    }

    private void buttonSetIcon(Button btn, String resource, int btnWidth, int btnHeight, int width, int height)
    {
        URL u = MarbyaEditor.getResource(resource);
        btn.setPrefWidth(btnWidth);
        btn.setPrefHeight(btnHeight);
        btn.setMinWidth(btnWidth);
        btn.setMinHeight(btnHeight);
        btn.setMaxWidth(btnWidth);
        btn.setMaxHeight(btnHeight);
        if (u == null) return;
        ImageView icon = new ImageView(new Image(u.toString()));
        icon.setFitWidth(width);
        icon.setFitHeight(height);
        btn.setGraphic(icon);
    }

    public void updateSaveAlert()
    {
        if (MarbyaEditor.getMarbyaMap().edited()) MarbyaEditor.getEditorController().saveAlert.setText("Zapisz CTRL + S");
        else MarbyaEditor.getEditorController().saveAlert.setText("Zapisano");
    }

    private EventHandler<ActionEvent> saveMap(final boolean saveAs)
    {
        return new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                MyFileChooser fc = new MyFileChooser();
                fc.setSaveAs(saveAs);
                File f = fc.showSaveDialogAndWait();
                if (fc.isPngFile()) MarbyaEditor.getMarbyaMap().saveToFile(f);
                else if (fc.isMbmFile())
                {
                    MarbyaSaver saver = new MarbyaSaver(f);
                    saver.save();
                }
                MarbyaEditor.getMarbyaMap().getPaint().setEdited(false);
                MarbyaEditor.getEditorController().updateSaveAlert();
            }
        };
    }
}
