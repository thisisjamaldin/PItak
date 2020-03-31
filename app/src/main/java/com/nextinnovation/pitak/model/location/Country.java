package com.nextinnovation.pitak.model.location;

import java.util.List;

public class Country {
    private List<String> country;

    @Override
    public String toString() {
        String result = "";
        for (String name : country) {
            result = result + name;
        }
        return result;
    }
}
