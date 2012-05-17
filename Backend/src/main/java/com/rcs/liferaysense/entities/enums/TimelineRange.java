package com.rcs.liferaysense.entities.enums;

import java.util.*;

/**
 * @author Prj.M@x <pablo.rendon@rotterdam-cs.com>
 */
public enum TimelineRange {
    LAST_HOUR(4) { 
        @Override
        public Date getDate() {            
            gc.add(Calendar.HOUR, -1);
            return gc.getTime();
        }
    },
    LAST_DAY(3) {  
        @Override
        public Date getDate() {            
            gc.add(Calendar.HOUR, -24);
            return gc.getTime();
        }
    },
    LAST_WEEK(2) {  
        @Override
        public Date getDate() {            
            gc.add(Calendar.DAY_OF_YEAR, -7);
            return gc.getTime();
        }
    },
    LAST_MONTH(1) {  
        @Override
        public Date getDate() {            
            gc.add(Calendar.MONTH, -1);
            return gc.getTime();
        }
    };
    
    private int id;
    public Calendar gc;
    private static final Map<Integer, Date> lookup = new HashMap<Integer, Date>();
    
    abstract Date getDate();
    
    static {
        for (TimelineRange s : TimelineRange.values()) {
            lookup.put(s.getId(), s.getDate());
        }
    }
    
    private TimelineRange (int r) {
        gc = GregorianCalendar.getInstance();
        gc.setTime(new Date());
        id = r;
    }
    
    public int getId() {
        return id;
    }
    
    public static Date get(int id) {
        return lookup.get(id);
    }
    
}