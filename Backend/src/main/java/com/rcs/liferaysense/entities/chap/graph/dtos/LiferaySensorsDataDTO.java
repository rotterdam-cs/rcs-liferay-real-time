package com.rcs.liferaysense.entities.chap.graph.dtos;

import com.rcs.liferaysense.entities.dtos.PagesDto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pablo
 */
public class LiferaySensorsDataDTO  implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List <LiferaySensorDataDTO> liferaySensorsData = new ArrayList<LiferaySensorDataDTO>();
    private List<PagesDto> pages = new ArrayList<PagesDto>();
    private long stepSeconds;
    private long startTime;
    private long endTime;

    public List<LiferaySensorDataDTO> getLiferaySensorsData() {
        return liferaySensorsData;
    }

    public void setLiferaySensorsData(List<LiferaySensorDataDTO> liferaySensorsData) {
        this.liferaySensorsData = liferaySensorsData;
    }

    public List<PagesDto> getPages() {
        return pages;
    }

    public void setPages(List<PagesDto> pages) {
        this.pages = pages;
    }

    public long getStepSeconds() {
        return stepSeconds;
    }

    public void setStepSeconds(long stepSeconds) {
        this.stepSeconds = stepSeconds;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    
    
}