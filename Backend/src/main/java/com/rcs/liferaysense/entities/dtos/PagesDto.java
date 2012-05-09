package com.rcs.liferaysense.entities.dtos;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author Prj.M@x <pablo.rendon@rotterdam-cs.com>
 */
public class PagesDto implements Serializable {
    private static final long serialVersionUID = 1L;
        
    private String page;
    private Long id;
    private int visits;
    private int currentvisitors = 0;
    boolean current = false;
    private String color;
    private HashMap<Long, String> usersInPage = new HashMap<Long, String>();
    private String UsersInPageInfo;
    private String url;
    
    public PagesDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public int getCurrentvisitors() {
        return currentvisitors;
    }

    public void setCurrentvisitors(int currentvisitors) {
        this.currentvisitors = currentvisitors;
    }

    
    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public String getColor() {
        return color;
    }
    
    public String getUsersInPageInfo() {
        return UsersInPageInfo;
    }

    public void setUsersInPageInfo(String UsersInPageInfo) {
        this.UsersInPageInfo = UsersInPageInfo;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public HashMap<Long, String> getUsersInPage() {
        return usersInPage;
    }

    public void setUsersInPage(HashMap<Long, String> usersInPage) {
        this.usersInPage = usersInPage;
    }

    public void addUsersInPage(Long userId, String userInfo) {
        this.currentvisitors ++;
        this.usersInPage.put(userId, userInfo);
    }
    
    public void removeUsersInPage(Long userId) {
        this.currentvisitors --;
        this.usersInPage.remove(userId);
    }

    public boolean isUserInPage(Long userId) {
        if (!this.usersInPage.isEmpty() && this.usersInPage.get(userId) != null) {
            return true;
        } else {
            return false;
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    
    
    
}