package com.rcs.liferaysense.entities;

import com.liferay.portal.service.UserLocalServiceUtil;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Prj.M@x <pablo.rendon@rotterdam-cs.com>
 */
@Entity
@Table(name = "sense_user")
public class SenseUser extends SenseEntity {
    public static final String LIFERAYUSERID = "liferayUserId";
    
    private static final long serialVersionUID = 1L;   
    
    @NotNull
    private long liferayUserId;
    
    @NotNull
    private long userId;
    
    @NotBlank
    private String username;
    
    @NotBlank
    private String password;
    
    private Boolean autoLogin = true;
    
    @OneToMany
    @JoinColumn(name = "senseuser_id")
    @Cascade(CascadeType.DELETE)
    private List<SenseSensor> sensors;
    
    

    /*
     Getters & Setters
    */
    
    public Boolean getAutoLogin() {
        return autoLogin;
    }

    public void setAutoLogin(Boolean autoLogin) {
        this.autoLogin = autoLogin;
    }

    public long getLiferayUserId() {
        return liferayUserId;
    }

    public void setLiferayUserId(long liferayUserId) {
        this.liferayUserId = liferayUserId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public com.liferay.portal.model.User getLiferayUser() {
        try {
            return UserLocalServiceUtil.getUser(liferayUserId);
        } catch (Exception ex){
            return null;
        }
    }  

}