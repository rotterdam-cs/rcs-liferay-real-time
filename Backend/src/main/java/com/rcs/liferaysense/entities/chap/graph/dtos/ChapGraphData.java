package com.rcs.liferaysense.entities.chap.graph.dtos;

import java.io.Serializable;

/**
 * @author Prj.M@x <pablo.rendon@rotterdam-cs.com>
 */
public class ChapGraphData implements Serializable  {
    private static final long serialVersionUID = 1L;
    
    private String value;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }    
    
}