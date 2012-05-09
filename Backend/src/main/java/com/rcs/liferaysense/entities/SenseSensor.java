package com.rcs.liferaysense.entities;

import com.rcs.liferaysense.entities.enums.SenseSensorStatus;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.NotBlank;
/**
 * @author Prj.M@x <pablo.rendon@rotterdam-cs.com>
 */
@Entity
@Table(name = "sense_sensor")
public class SenseSensor extends SenseEntity {
    public static final String SENSEUSERID = "senseuser_id";
    public static final String SENSEGRAPHICID = "sensegraphic_id";
    private static final long serialVersionUID = 1L;
    
    @NotNull
    private long senseUser_id;
    
    @NotNull
    private long senseId;
    
    @NotBlank
    private String name;    
    
    @Enumerated(EnumType.STRING)
    private SenseSensorStatus status;
    
    @NotBlank
    private String type;
    
    @NotNull
    private long deviceId;
    
    @ManyToOne
    @JoinColumn(name="senseuser_id")
    private SenseUser senseUser;
    
    @OneToOne
    @Cascade(CascadeType.DELETE)
    private SenseGraphic senseGraphic;
    
    /*
     Getters & Setters
    */

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SenseGraphic getSenseGraphic() {
        return senseGraphic;
    }

    public void setSenseGraphic(SenseGraphic senseGraphic) {
        this.senseGraphic = senseGraphic;
    }

    public long getSenseId() {
        return senseId;
    }

    public void setSenseId(long senseId) {
        this.senseId = senseId;
    }

    public SenseUser getSenseUser() {
        return senseUser;
    }

    public void setSenseUser(SenseUser senseUser) {
        this.senseUser = senseUser;
    }

    public long getSenseUser_id() {
        return senseUser_id;
    }

    public void setSenseUser_id(long senseUser_id) {
        this.senseUser_id = senseUser_id;
    }

    public SenseSensorStatus getStatus() {
        return status;
    }

    public void setStatus(SenseSensorStatus status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    
}
