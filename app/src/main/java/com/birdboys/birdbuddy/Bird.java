package com.birdboys.birdbuddy;

import java.io.Serializable;

/**
 * Created by Alex on 11/28/17.
 */

public class Bird{

    private String name;
    private String url_name;
    private boolean seen;

    public Bird(String name) {
        this.name = name;

        String[] split = name.split(" ");
        for (int i = 0; i < split.length; i++) {
            split[i] = Character.toUpperCase(split[i].charAt(0)) + split[i].substring(1);
            split[i] = split[i].replace("\'", "");
        }

        this.url_name = split[0];
        if (split.length > 1) {
            for (int i = 1; i < split.length; i++) {
                url_name += "_" + split[i];
            }
        }

        this.seen = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri_name() {
        return url_name;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
