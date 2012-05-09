package com.rcs.liferaysense.service.local;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.rcs.common.service.CRUDServiceImpl;
import com.rcs.common.service.ServiceActionResult;
import com.rcs.liferaysense.entities.SenseEntity;
import com.rcs.liferaysense.entities.SenseUser;
import java.util.LinkedList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Prj.M@x <pablo.rendon@rotterdam-cs.com>
 */
@Service
@Transactional
public class SenseUserServiceImpl extends CRUDServiceImpl<SenseUser> implements SenseUserService {

    private static Log log = LogFactoryUtil.getLog(SenseUserServiceImpl.class);  

    
    /**
     * 
     * @param groupId
     * @param companyId
     * @param liferayUserId
     * @return 
     */
    @Override
    public SenseUser findByLiferayId(long groupId, long companyId, long liferayUserId) {
        
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SenseUser.class);      
        
        criteria.add(Restrictions.eq(SenseUser.COMPANYID, companyId));        
        criteria.add(Restrictions.eq(SenseUser.GROUPID, groupId));
        
        
        criteria.add(Restrictions.eq(SenseUser.LIFERAYUSERID, liferayUserId));       
        
        criteria.addOrder(Order.desc(SenseEntity.ID));
        criteria.setMaxResults(1);
        SenseUser user = (SenseUser) criteria.uniqueResult();
        
        return user;
    }
    
    
    /**
     * 
     * @param liferayUserId
     * @return 
     */
    @Override
    public SenseUser findByLiferayId(long liferayUserId) {
        
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SenseUser.class);              
        criteria.add(Restrictions.eq(SenseUser.LIFERAYUSERID, liferayUserId));       
        
        criteria.addOrder(Order.desc(SenseEntity.ID));
        criteria.setMaxResults(1);
        SenseUser user = (SenseUser) criteria.uniqueResult();
        
        return user;
    }

    
    /**
     * 
     * @param username
     * @param password
     * @param liferayUserId
     * @param companyId
     * @param groupId
     * @return 
     */
    @Override
    public ServiceActionResult<SenseUser> addSenseUser(String username, String password, long liferayUserId, int senseUserId, long companyId, long groupId) {
        
        SenseUser senseUser = findByLiferayId(groupId, companyId, liferayUserId);
        if (senseUser != null) {
            delete(senseUser);
        }
        
        SenseUser user = new SenseUser();
        
        user.setAutoLogin(Boolean.TRUE);
        user.setUsername(username);
        user.setPassword(password);
        user.setLiferayUserId(liferayUserId);
        user.setCompanyid(companyId);
        user.setGroupid(groupId);
        user.setUserId(senseUserId);
        
        List<String> violations = new LinkedList<String>();
        fillViolations(validator.validate(user), violations);

        if (violations.isEmpty()) {            
            if (liferayUserId == 0) {
                return ServiceActionResult.buildFailure(null, "com.rcs.sense.service.error.senseuser");
            }
        } else {
            return ServiceActionResult.buildFailure(null, violations);
        }
        ServiceActionResult<SenseUser> result = save(user);
        
        return result;        
    }
    
    
    /**
     * 
     * @param liferayUserId
     * @return 
     */
    @Override
    public boolean isSenseAdministratorByLiferayUserId(long liferayUserId) {
        boolean result = false;
        try {    
            User user = UserLocalServiceUtil.getUserById(liferayUserId);
            for (Role role : user.getRoles()) {
                String roleName = role.getName();
                if (roleName.equals(SENSE_ADMIN_ROLE_NAME)) {
                    result = true;
                }
            }
        } catch (PortalException ex) {
        } catch (SystemException ex) {
        }
        return result;
    }

    
    /**
     * 
     * @param groupId
     * @param companyId
     * @param senseUserId
     * @return 
     */
    @Override
    public boolean isSenseAdministratorBySenseUserId(long groupId, long companyId, long senseUserId) {
        boolean result = false;
        try {
            SenseUser senseUser = findByLiferayId(groupId, companyId, senseUserId);
            User user = UserLocalServiceUtil.getUserById(senseUser.getLiferayUserId());
            for (Role role : user.getRoles()) {
                String roleName = role.getName();
                if (roleName.equals(SENSE_ADMIN_ROLE_NAME)) {
                    result = true;
                }
            }
        } catch (PortalException ex) {
        } catch (SystemException ex) {
        }
        return result;
    }
}
