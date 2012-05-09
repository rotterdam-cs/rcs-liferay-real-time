package com.rcs.liferaysense.entities;

import com.rcs.liferaysense.entities.enums.SenseGraphicTimeRange;
import com.rcs.liferaysense.entities.enums.SenseGraphicViewType;
import java.util.Date;
import javax.persistence.*;

/**
 * @author Prj.M@x <pablo.rendon@rotterdam-cs.com>
 */
@Entity
@Table(name = "sense_graphic")
public class SenseGraphic extends SenseEntity {
    public static final String SENSESENSORID = "sensesensor_id";
    private static final long serialVersionUID = 1L;
    
    @Enumerated(EnumType.STRING)
    private SenseGraphicViewType viewType;
    
    @Enumerated(EnumType.STRING)
    private SenseGraphicTimeRange timeRange;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
            
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    
    @OneToOne
    private SenseSensor senseSensor;
    
    private Boolean allowChange = false;
    
    
    /*
     Getters & Setters
    */

    public Boolean getAllowChange() {
        return allowChange;
    }

    public void setAllowChange(Boolean allowChange) {
        this.allowChange = allowChange;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public SenseSensor getSenseSensor() {
        return senseSensor;
    }

    public void setSenseSensor(SenseSensor senseSensor) {
        this.senseSensor = senseSensor;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public SenseGraphicTimeRange getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(SenseGraphicTimeRange timeRange) {
        this.timeRange = timeRange;
    }

    public SenseGraphicViewType getViewType() {
        return viewType;
    }

    public void setViewType(SenseGraphicViewType viewType) {
        this.viewType = viewType;
    }

    
}
