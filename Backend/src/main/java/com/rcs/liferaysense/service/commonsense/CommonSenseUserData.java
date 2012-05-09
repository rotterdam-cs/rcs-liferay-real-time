package com.rcs.liferaysense.service.commonsense;

import java.io.Serializable;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Holds the data for a common sense user.
 * @author juan
 */
public class CommonSenseUserData implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String username;
    private String name;
    private String surname;
    private String mobile;
    private String UUID;
    private int openid;
    @NotBlank
    private String password;

    public CommonSenseUserData() {
    }

    public CommonSenseUserData(String email, String username, String name, String surname, String mobile, String password, int id, String UUID, int openid) {
        this.email = email;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.mobile = mobile;
        this.password = password;
        this.id = id;
        this.UUID = UUID;
        this.openid = openid;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public int getOpenid() {
        return openid;
    }

    public void setOpenid(int openid) {
        this.openid = openid;
    }

        
    @Override
    public String toString() {
        return "CommonSenseUserData{" + "id=" + id + ", email=" + email + ", username=" + username + ", name=" + name + ", surname=" + surname + ", mobile=" + mobile + ", password=" + password + ", UUID=" + UUID + ", openid=" + openid + '}';
    }
}
