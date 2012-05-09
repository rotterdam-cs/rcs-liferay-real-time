package com.rcs.liferaysense.entities.enums;

/**
 * @author Prj.M@x <pablo.rendon@rotterdam-cs.com>
 */
public enum SenseGraphicTimeRange {

    LASTHOUR("sensegraphictimerante.lasthour"),
    LASTDAY("sensegraphictimerante.lastday"),
    LASTWEEK("sensegraphictimerante.lastweek"),
    LASTMONTH("sensegraphictimerante.lastmonth"),
    OTHER("sensegraphictimerante.other");
    
    private String key;

    private SenseGraphicTimeRange(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
