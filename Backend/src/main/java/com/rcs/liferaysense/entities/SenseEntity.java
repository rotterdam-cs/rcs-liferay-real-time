package com.rcs.liferaysense.entities;

import com.rcs.common.entity.RCSEntity;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

/**
 * Default things for all entities.
 * @author Ariel Parra <ariel@rotterdam-cs.com>
 */
//
@MappedSuperclass
public abstract class SenseEntity extends RCSEntity implements Serializable {
    
    public static final String GROUPID = "groupid";
    public static final String COMPANYID = "companyid";
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private long groupid;
    
    @NotNull
    private long companyid;
    
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public long getCompanyid() {
        return companyid;
    }

    public void setCompanyid(long companyid) {
        this.companyid = companyid;
    }

    public long getGroupid() {
        return groupid;
    }

    public void setGroupid(long groupid) {
        this.groupid = groupid;
    }
}
