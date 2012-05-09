package com.rcs.liferaysense.service.commonsense;

import java.io.Serializable;

/**
 *
 * @author juan
 */
class SenseUser implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private CommonSenseUserData user;

    public SenseUser() {
    }

    public CommonSenseUserData getUser() {
        return user;
    }

    public void setUsers(CommonSenseUserData user) {
        this.user = user;
    }
    
}
