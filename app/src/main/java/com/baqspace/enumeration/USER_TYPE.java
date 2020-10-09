package com.baqspace.enumeration;

import java.io.Serializable;

/**
 * Created by Tshepo Lebusa on 08/08/2016.
 */
public enum USER_TYPE implements Serializable  {

    MANAGER("MANAGER"), TRADER("TRADER"), CUSTOMER("CUSTOMER"),
    BROWSER("BROWSER");

    private final String type;

    private USER_TYPE(String type)
    {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
