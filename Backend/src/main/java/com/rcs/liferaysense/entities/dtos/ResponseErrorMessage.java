package com.rcs.liferaysense.entities.dtos;

import java.io.Serializable;
/**
 *
 * @author Prj.M@x <pablo.rendon@rotterdam-cs.com>
 */
public class ResponseErrorMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String error;    
    
    public ResponseErrorMessage(){
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    
    
    
}
