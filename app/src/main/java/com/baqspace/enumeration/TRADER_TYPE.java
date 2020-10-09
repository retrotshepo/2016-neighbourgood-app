package com.baqspace.enumeration;

import java.io.Serializable;

/**
 * Created by Tshepo Lebusa on 03/07/2016.
 */
public enum TRADER_TYPE implements Serializable
{
    CUISINE("CUISINE"), BEVERVAGE("BEVERAGE"), MISC("MISCELLANEOUS");

    private final String type;

    private TRADER_TYPE(String type)
    {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}