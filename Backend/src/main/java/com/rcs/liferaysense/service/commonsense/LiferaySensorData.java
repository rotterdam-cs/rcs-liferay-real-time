package com.rcs.liferaysense.service.commonsense;

import java.io.Serializable;

/**
 *
 * @author pablo
 */
public class LiferaySensorData implements Serializable {
    private static final long serialVersionUID = 1L;
        
    private String ip;
    private String page;
    private String previous_page;
    private long pageId;
    private long previous_pageId;
    private String userAgent;
    private long liferayUserId;
    

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPrevious_page() {
        return previous_page;
    }

    public void setPrevious_page(String previous_page) {
        this.previous_page = previous_page;
    }

    public long getPageId() {
        return pageId;
    }

    public void setPageId(long pageId) {
        this.pageId = pageId;
    }

    public long getPrevious_pageId() {
        return previous_pageId;
    }

    public void setPrevious_pageId(long previous_pageId) {
        this.previous_pageId = previous_pageId;
    }
    
    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;        
        
    }

    public long getLiferayUserId() {
        return liferayUserId;
    }

    public void setLiferayUserId(long liferayUserId) {
        this.liferayUserId = liferayUserId;
    }
    
    
    
}
