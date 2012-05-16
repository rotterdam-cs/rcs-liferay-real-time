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
        
}