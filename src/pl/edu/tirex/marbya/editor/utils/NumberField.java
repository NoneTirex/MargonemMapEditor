package pl.edu.tirex.marbya.editor.utils;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class NumberField extends TextField
{
    private final IntegerProperty value;
    private int minValue;
    private int maxValue;

    public int getValue()
    {
        return this.value.get();
    }

    public boolean isEmpty()
    {
        return this.getText() == null || this.getText().trim().isEmpty();
    }

    public NumberField(int minValue, int maxValue)
    {
        if (minValue > maxValue)
        {
            throw new IllegalArgumentException("IntField min value " + minValue + " greater than max value " + maxValue);
        }

        this.minValue = minValue;
        this.maxValue = maxValue;

        this.value = new SimpleIntegerProperty();

        this.value.addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                if (newValue == null)
                {
                    NumberField.this.setText("");
                    return;
                }
                if (newValue.intValue() < NumberField.this.minValue)
                {
                    NumberField.this.value.setValue(oldValue);
                }
                if (newValue.intValue() > NumberField.this.maxValue)
                {
                    NumberField.this.value.setValue(oldValue);
                }
                if (NumberField.this.value.get() != 0) NumberField.this.setText(Integer.toString(NumberField.this.value.get()));
            }
        });

        this.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent keyEvent)
            {
                String text = keyEvent.getCharacter();
                if (!text.matches("[0-9]")) keyEvent.consume();
            }
        });

        this.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                if (newValue == null || newValue.isEmpty())
                {
                    NumberField.this.value.setValue(0);
                    return;
                }
                int intValue = 0;
                try {
                    intValue = Integer.parseInt(newValue);
                } catch (NumberFormatException e) {
                    NumberField.this.setText("");
                }
                NumberField.this.value.set(intValue);
            }
        });
    }
}
