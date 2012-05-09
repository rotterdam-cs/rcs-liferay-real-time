package com.rcs.liferaysense.service.local;

import com.rcs.common.service.CRUDService;
import com.rcs.common.service.ServiceActionResult;
import com.rcs.liferaysense.entities.SenseUser;

/**
 * @author Prj.M@x <pablo.rendon@rotterdam-cs.com>
 */
public interface SenseUserService extends CRUDService<SenseUser> {

    public final static String SENSE_ADMIN_ROLE_NAME = "Sense Administrator";
    
    SenseUser findByLiferayId(long groupId, long companyId, long liferayUserId);
    
    SenseUser findByLiferayId(long liferayUserId);
    
    boolean isSenseAdministratorByLiferayUserId(long liferayUserId);
    
    boolean isSenseAdministratorBySenseUserId(long groupId, long companyId, long senseUserId);
    
    ServiceActionResult<SenseUser> addSenseUser(String username, String password, long liferayUserId, int senseUserId, long companyId, long groupId);
}