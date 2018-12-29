package com.rocketlauncher.mscsdr.rs.PropertyChanges;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Created by Mortif3r on 29.12.2018.
 */

public class RadioString {
    private String value = "";

    private final PropertyChangeSupport valuePCS = new PropertyChangeSupport(this);

    public synchronized void setValue(String value) {
        String oldValue = value;
        this.value = value;
        valuePCS.firePropertyChange("value", oldValue, value);
    }

    public synchronized String getValue() {
        return value;
    }

    public void subscribeToValue(PropertyChangeListener l){
        valuePCS.addPropertyChangeListener(l);
    }

    public void unsubcribeFromValue(PropertyChangeListener l){
        valuePCS.removePropertyChangeListener(l);
    }
}
