package com.rcs.liferaysense.common;

public interface Constants {
    public static final int UNDEFINED = -1;
    public static final String ORDER_BY_ASC = "asc";
    public static final String ORDER_BY_DESC = "desc"; 
    public static final int PAGINATION_DEFAULT_LIMIT = 5;    
    public static final String DATE_BACK_FORMAT = "yyyy-MM-dd";    
    public static final String DATE_FRONT_FORMAT = "Y-m-d";
    
    public static final String JQFORM_ACTION_ADD = "add";
    public static final String JQFORM_ACTION_EDIT = "edit";
    public static final String JQFORM_ACTION_DEL = "del";
    
    public static final String ADMIN_SECTION_ACCOUNT = "account";
    public static final String ADMIN_SECTION_ANALYTICS = "analytics";
    public static final String ADMIN_SECTION_GLOBAL_SETTINGS = "global-settings";
    
    public static final String ADMIN_SUBSECTION_TOP_MESSAGES = "top_messages";
    
    public static final String ADMIN_CONFIGURATION_ALLOW_AUTO_REGISTER = "auto_register";
    public static final String ADMIN_CONFIGURATION_ALLOW_CHANGE_SENSE_ACCOUNT = "allow_change_account";
    public static final String ADMIN_CONFIGURATION_DEFAULT_SENSE_USERNAME = "default_sense_username";
    public static final String ADMIN_CONFIGURATION_DEFAULT_SENSE_PASSWORD = "default_sense_pass";
    
    public static final String CHECKBOX_SELECTED_VALUE = "on";
    
    public static final int LIFERAY_SENSOR_ID = 138514;//remote
    //public static final int LIFERAY_SENSOR_ID = 134898;//local
}