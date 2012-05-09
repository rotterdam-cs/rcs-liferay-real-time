package com.rcs.hook;

import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import static com.rcs.hook.UpgradeProcessUtils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Process for the initial data for the SensePortlet application.
 * @author juan
 */
public class InitialDataProcess_1_0_0 extends UpgradeProcess {

    private static final Logger logger = LoggerFactory.getLogger(InitialDataProcess_1_0_0.class);
    private Role adminRole;
    
    private long companyId;
    private long defaultUserId;
    
    @Override
    public int getThreshold() {
        logger.info("Called getThreshold");
        return 1;
    }

    
    /**
     * Implement the upgrade process.
     * @throws Exception
     */
    @Override
    protected void doUpgrade() throws Exception {
        
        logger.info("Creating initial data for LiferaySense...");
        
        companyId = PortalUtil.getDefaultCompanyId();
        defaultUserId = UserLocalServiceUtil.getDefaultUserId(companyId);
        //create the initial liferay roles and organizations
        createInitialLiferayRolesAndOrgs(companyId, defaultUserId);
        
    }
    
    /**
     * 
     * @param companyId
     * @param defaultUserId
     * @throws Exception 
     */
    private void createInitialLiferayRolesAndOrgs(long companyId, long defaultUserId) throws Exception {
        //add the roles
        logger.info("Creating roles...");

        if (!roleExists(companyId, "Sense Administrator")) {
            adminRole = addRole(companyId, defaultUserId, "Sense Administrator");
        } else {
            adminRole = getRoleByName(companyId, "Sense Administrator");
        }

        //add the impersonate permission to the role.
        UpgradeProcessUtils.setRolePermissions(adminRole, User.class.getName(), ActionKeys.IMPERSONATE);
    }

}