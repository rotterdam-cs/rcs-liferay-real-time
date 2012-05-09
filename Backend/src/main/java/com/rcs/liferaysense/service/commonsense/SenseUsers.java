package com.rcs.liferaysense.service.commonsense;

import java.io.Serializable;

/**
 *
 * @author juan
 */
class SenseUsers implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private CommonSenseUserData[] users;

    public SenseUsers() {
    }

    public CommonSenseUserData[] getUsers() {
        return users;
    }

    public void setUsers(CommonSenseUserData[] users) {
        this.users = users;
    }
    
}
