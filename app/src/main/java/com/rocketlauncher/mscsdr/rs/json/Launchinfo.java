package com.rocketlauncher.mscsdr.rs.json;

import java.util.Arrays;

/**
 * Created by Mortif3r on 31.12.2018.
 */

public class Launchinfo {
    int[] pins;
    double delay;

    public Launchinfo(int[] pins, double delay) {
        this.pins = pins;
        this.delay = delay;
    }

    @Override
    public String toString() {
        return "Pins: " + Arrays.toString(pins) + ", Delay: " + delay;
    }
}
