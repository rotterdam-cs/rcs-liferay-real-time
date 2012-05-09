package com.rcs.liferaysense.service.local;

import com.rcs.common.service.CRUDServiceImpl;
import com.rcs.common.service.ServiceActionResult;
import com.rcs.liferaysense.entities.SenseConfiguration;
import com.rcs.liferaysense.entities.SenseEntity;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Prj.M@x <pablo.rendon@rotterdam-cs.com>
 */
@Service
@Transactional
class SenseConfigurationServiceImpl extends CRUDServiceImpl<SenseConfiguration> implements SenseConfigurationService {
    @Override
    public ServiceActionResult<SenseConfiguration> findByProperty(long groupId, long companyId, String property) {        
                
        boolean success = false;
        List<String> validationKeys = new ArrayList<String>();
        SenseConfiguration entity = null;
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(getEntityClass());
            
            criteria.add(Restrictions.eq(SenseConfiguration.SENSECONFIGURATIONPROPERTY, property));
            criteria.add(Restrictions.eq(SenseConfiguration.COMPANYID, companyId));        
            criteria.add(Restrictions.eq(SenseConfiguration.GROUPID, groupId));
            criteria.addOrder(Order.desc(SenseEntity.ID));
            
            List<SenseConfiguration> entityObject = template.findByCriteria(criteria,0,1);
            if (entityObject != null && !entityObject.isEmpty()) {
                entity = (SenseConfiguration) entityObject.get(0);
                success = true;
            }
        } catch (DataAccessException ex) {
            String error = "Couldnt not retrieve the object by his id";
            logger.error(error, ex);
        } catch (Exception ex) {
            String error = "General error";
            logger.error(error, ex);
        }
        template.clear();
        ServiceActionResult<SenseConfiguration> result = new ServiceActionResult<SenseConfiguration>(success, entity, validationKeys);
        return result;
    }
   
}