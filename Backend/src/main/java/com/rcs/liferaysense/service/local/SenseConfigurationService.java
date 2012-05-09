package com.rcs.liferaysense.service.local;

import com.rcs.common.service.CRUDService;
import com.rcs.common.service.ServiceActionResult;
import com.rcs.liferaysense.entities.SenseConfiguration;

/**
 * @author Prj.M@x <pablo.rendon@rotterdam-cs.com>
 */
public interface SenseConfigurationService extends CRUDService<SenseConfiguration> {

    ServiceActionResult<SenseConfiguration> findByProperty(long groupId, long companyId, String property);
}