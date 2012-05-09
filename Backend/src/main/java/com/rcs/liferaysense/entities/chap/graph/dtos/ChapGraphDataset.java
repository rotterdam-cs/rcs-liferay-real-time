package com.rcs.liferaysense.entities.chap.graph.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Prj.M@x <pablo.rendon@rotterdam-cs.com>
 */
public class ChapGraphDataset implements Serializable {    
    private static final long serialVersionUID = 1L;
    
    private String label;
    private List <ChapGraphData> data = new ArrayList<ChapGraphData>();


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<ChapGraphData> getData() {
        return data;
    }

    public void setData(List<ChapGraphData> data) {
        this.data = data;
    }
    
    public void addData(ChapGraphData chapGraphData) {
        this.data.add(chapGraphData);
    }
    
    
}
