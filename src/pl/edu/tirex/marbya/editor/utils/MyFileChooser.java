package pl.edu.tirex.marbya.editor.utils;

import javafx.stage.FileChooser;
import pl.edu.tirex.marbya.editor.MarbyaEditor;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MyFileChooser
{
    private static final List<FileChooser.ExtensionFilter> EXTENSIONS_FILTER = Arrays.asList(
            new FileChooser.ExtensionFilter("Zapisz do obrazka (*.png)", "*.png"),
            new FileChooser.ExtensionFilter("Zapisz do formatu Marbya (*.mbm)", "*.mbm")
    );
    private static FileChooser.ExtensionFilter lastExtensionFilter;
    private static File lastChooseFile;

    private String title;
    private boolean saveAs;
    private boolean pngFile;
    private boolean mbmFile;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public boolean isSaveAs()
    {
        return saveAs;
    }

    public void setSaveAs(boolean saveAs)
    {
        this.saveAs = saveAs;
    }

    public File showSaveDialogAndWait()
    {
        if (!this.saveAs && MyFileChooser.lastChooseFile != null && MyFileChooser.lastExtensionFilter != null)
        {
            this.setExtensionFilter(MyFileChooser.lastExtensionFilter);
            return MyFileChooser.lastChooseFile;
        }
        FileChooser fc = new FileChooser();
        fc.setTitle(this.title == null ? "Zapisz jako" : this.title);
        fc.getExtensionFilters().setAll(MyFileChooser.EXTENSIONS_FILTER);
        if (MyFileChooser.lastExtensionFilter != null) fc.setSelectedExtensionFilter(MyFileChooser.lastExtensionFilter);
        fc.setInitialDirectory(MyFileChooser.lastChooseFile == null ? new File(".") : MyFileChooser.lastChooseFile.getParentFile());
        if (MyFileChooser.lastChooseFile != null) fc.setInitialFileName(MyFileChooser.lastChooseFile.getName());
        File f = fc.showSaveDialog(MarbyaEditor.getMainStage());
        if (f != null) MyFileChooser.lastChooseFile = f;
        this.setExtensionFilter(fc.getSelectedExtensionFilter());
        MyFileChooser.lastExtensionFilter = fc.getSelectedExtensionFilter();
        return f;
    }

    public boolean isMbmFile()
    {
        return mbmFile;
    }

    public boolean isPngFile()
    {
        return pngFile;
    }

    private void setExtensionFilter(FileChooser.ExtensionFilter filter)
    {
        this.mbmFile = false;
        this.pngFile = false;
        if (filter.getExtensions().contains("*.mbm")) this.mbmFile = true;
//        else if (fc.getSelectedExtensionFilter().getExtensions().contains("*.png")) this.pngFile = true;
        else this.pngFile = true;
    }
}
