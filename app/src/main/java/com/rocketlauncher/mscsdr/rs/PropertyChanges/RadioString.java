package com.rocketlauncher.mscsdr.rs.PropertyChanges;

import android.util.Log;

import com.rocketlauncher.mscsdr.rs.Model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Created by Mortif3r on 29.12.2018.
 */

public class RadioString {
    private String value = "";

    private final PropertyChangeSupport valuePCS = new PropertyChangeSupport(this);

    public synchronized void setValue(String value) {
        this.value = value;
        valuePCS.firePropertyChange("value", null, value);
    }

    public synchronized String getValue() {
        return value;
    }

    public void subscribeToValue(PropertyChangeListener l) {
        valuePCS.addPropertyChangeListener(l);
    }

    public void unsubcribeFromValue(PropertyChangeListener l) { // CHECK IF THIS WORKS LATER 4Head
        valuePCS.removePropertyChangeListener(l);
    }
}
