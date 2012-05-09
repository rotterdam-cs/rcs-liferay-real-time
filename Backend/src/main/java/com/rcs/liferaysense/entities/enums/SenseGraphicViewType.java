package com.rcs.liferaysense.entities.enums;

/**
 * @author Prj.M@x <pablo.rendon@rotterdam-cs.com>
 */
public enum SenseGraphicViewType {

    TIMELINE("sensegraphicviewtype.timeline"),
    TABLE("sensegraphicviewtype.table"),
    MAP("sensegraphicviewtype.map"),
    NONE("sensegraphicviewtype.none");
    
    private String key;

    private SenseGraphicViewType(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
