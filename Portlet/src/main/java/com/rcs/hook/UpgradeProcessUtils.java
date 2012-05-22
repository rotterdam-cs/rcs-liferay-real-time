package com.rcs.hook;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.*;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.*;
import com.liferay.portal.service.*;
import com.liferay.portal.service.permission.PortletPermissionUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalArticleConstants;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import java.util.*;

/**
 *
 * @author juan
 * @author ggenovese
 */
public class UpgradeProcessUtils {

    /**
     * Add a top level page to the site.
     *
     * @param group
     * @param pageName
     * @param isPrivate
     * @param friendlyURL
     * @param pageLayoutName
     * @return
     * @throws Exception
     */
    public static Layout addPage(Group group, String pageName, boolean isPrivate, String friendlyURL, String pageLayoutName) throws Exception {

        //build a service context.
        ServiceContext context = new ServiceContext();

        //create a new page.
        Layout ret = LayoutLocalServiceUtil.addLayout(
                group.getCreatorUserId(), //creator user id.
                group.getGroupId(), //the creator group id
                isPrivate, //if is private or public
                LayoutConstants.DEFAULT_PARENT_LAYOUT_ID, //the parent page (top level) 
                pageName, //name
                StringPool.BLANK, //title 
                StringPool.BLANK, //description
                LayoutConstants.TYPE_PORTLET, //type
                false, //hidden
                friendlyURL, //the url
                context //the service context.
                );        

        //configure the layout template.
        LayoutTypePortlet layoutType = (LayoutTypePortlet) ret.getLayoutType();        

        //set the layout id.
        layoutType.setLayoutTemplateId(
                0, //the user name 
                pageLayoutName, //the layout id
                false //avoid checking the permissions
                );

        //add the dock bar
        addResources(ret, PortletKeys.DOCKBAR);


        return ret;
    }

    /**
     * Add a child page to a parent page.
     *
     * @param group
     * @param pageName
     * @param isPrivate
     * @param friendlyURL
     * @param pageLayoutName
     * @param parent
     * @return
     * @throws Exception
     */
    public static Layout addChildPage(Group group, String pageName, boolean isPrivate, String friendlyURL, String pageLayoutName, Layout parent) throws Exception {

        //build a service context.
        ServiceContext context = new ServiceContext();

        //create a new page.
        Layout ret = LayoutLocalServiceUtil.addLayout(
                group.getCreatorUserId(), //cre creator user id.
                group.getGroupId(), //the creator group id
                isPrivate, //if is private or public
                parent.getLayoutId(), //the parent page
                pageName, //name
                StringPool.BLANK, //title 
                StringPool.BLANK, //description
                LayoutConstants.TYPE_PORTLET, //type
                false, //hidden
                friendlyURL, //the url
                context //the service context.
                );

        //configure the layout template.
        LayoutTypePortlet layoutType = (LayoutTypePortlet) ret.getLayoutType();

        //set the layout id.
        layoutType.setLayoutTemplateId(
                0, //the user name 
                pageLayoutName, //the layout id
                false //avoid checking the permissions
                );

        //add the dock bar
        addResources(ret, PortletKeys.DOCKBAR);


        return ret;
    }

    /**
     * Add a resource to the page, AKA portlet :)
     *
     * @param page
     * @param portletId
     * @throws Exception
     */
    public static void addResources(Layout page, String portletId) throws Exception {

        //get the root portlet id
        String rootPortletId = PortletConstants.getRootPortletId(portletId);

        //create a portlet primary key.
        String portletPrimaryKey = PortletPermissionUtil.getPrimaryKey(page.getPlid(), rootPortletId);

        ResourceLocalServiceUtil.addResources(
                page.getCompanyId(), //the page company id.
                page.getGroupId(), //the page group id.
                0, //the owner user id 
                rootPortletId, //the portlet id.
                portletPrimaryKey, //the portlet primary key.
                true, //add the portlet actions. 
                true, //add the community permissions. 
                true //add the guest permissions.
                );
    }

    /**
     * Add a portlet to the page on the given column.
     *
     * @param layout
     * @param portletId
     * @param columnId
     * @return
     * @throws Exception
     */
    public static String addPortletIdToLayout(Layout layout, String portletId, String columnId) throws Exception {

        LayoutTypePortlet layoutType = (LayoutTypePortlet) layout.getLayoutType();

        portletId = layoutType.addPortletId(
                0, //the user id 
                portletId, //the proper portlet id. 
                columnId, //the column where to place the portlet.
                -1, //the overall position on the column
                false //wether to check permissions or not.
                );

        //add it as a page resoruce
        addResources(layout, portletId);

        //update the page layout in the database.
        updateLayout(layout);

        return portletId;
    }

    /**
     * Update the layout in the database.
     *
     * @param layout
     * @throws Exception
     */
    public static void updateLayout(Layout layout) throws Exception {
        LayoutLocalServiceUtil.updateLayout(
                layout.getGroupId(), //the layout group id.
                layout.isPrivateLayout(), //wether is private layout or not.
                layout.getLayoutId(), //the id of the layout.
                layout.getTypeSettings() //the type settings.
                );
    }

    /**
     * Add a journal article from a package resource.
     *
     * @param userId
     * @param groupId
     * @param title
     * @param fileName
     * @param context
     * @return
     * @throws Exception
     */
    public static JournalArticle addJournalArticle(long userId, long groupId, String title, String fileName, ServiceContext context) throws Exception {

        //read the file
        String content = getString(fileName);

        context.setAddGroupPermissions(true);
        context.setAddGuestPermissions(true);

        Map<Locale, String> titleMap = new HashMap<Locale, String>();
        setLocalizedValue(titleMap, title);

        //create a journal article with parameters
        JournalArticle journalArticle = JournalArticleLocalServiceUtil.addArticle(
                userId, //the user id
                groupId, //the group id
                0, //class name id
                0, //class primary key
                StringPool.BLANK, //the article id
                true, //to create automatically the article id.
                JournalArticleConstants.VERSION_DEFAULT, //the version of the article
                titleMap, //the title of the article
                null,//the description of the content
                content, //the content
                "general", //the article type
                StringPool.BLANK, //structure id
                StringPool.BLANK, //template id
                StringPool.BLANK, //layout uuid
                0, //display month
                1, //display day
                2012, //display year
                0, //display hour
                0, //display minute
                0, //expiration month
                0, //expiration day
                0, //expiration year
                0, //expiration hour.
                0, //expiration minute
                true, //never expire
                0, //review month
                0, //review day
                0, //review year
                0, //review hour
                0, //review minute
                true, //never review
                true, //indexable
                false, //small image
                StringPool.BLANK, //small image url
                null, //small image
                null, //images
                StringPool.BLANK, //article url.
                context //the service context
                );

        //set the article as approved
        JournalArticleLocalServiceUtil.updateStatus(userId, groupId, journalArticle.getArticleId(),
                journalArticle.getVersion(), WorkflowConstants.STATUS_APPROVED,
                StringPool.BLANK, context);

        return journalArticle;
    }

    /**
     * Setup the journal content to load the journal article.
     *
     * @param layout
     * @param portletId
     * @param articleId
     * @throws Exception
     */
    public static void configureJournalContent(Layout layout, String portletId, String articleId) throws Exception {
        javax.portlet.PortletPreferences portletSetup = PortletPreferencesFactoryUtil.getLayoutPortletSetup(layout, portletId);
        portletSetup.setValue("group-id", String.valueOf(layout.getGroupId()));
        portletSetup.setValue("article-id", articleId);

        portletSetup.store();
    }

    /**
     * Read a package resource into a string.
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    public static String getString(String fileName) throws Exception {
        byte[] bytes = FileUtil.getBytes(UpgradeProcessUtils.class.getResourceAsStream(fileName));
        return new String(bytes);
    }

    /**
     * Tells whether a role exists
     *
     * @param companyId
     * @param roleName
     * @return
     */
    public static boolean roleExists(long companyId, String roleName) {
        return getRoleByName(companyId, roleName) != null;
    }

    /**
     * Tells whether an organization exists
     *
     * @param companyId
     * @param roleName
     * @return
     */
    public static boolean organizationExists(long companyId, String organizationName) {
        return getOrganizationByName(companyId, organizationName) != null;

    }

    /**
     * Finds a role by its name
     *
     * @param companyId
     * @param roleName
     * @return The role or null if was not found
     */
    public static Role getRoleByName(long companyId, String roleName) {
        try {
            Role role = RoleLocalServiceUtil.getRole(companyId, roleName);
            return role;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Finds an organization by its name
     *
     * @param companyId
     * @param organizationName
     * @return The role or null if was not found
     */
    public static Organization getOrganizationByName(long companyId, String organizationName) {
        try {
            Organization organization = OrganizationLocalServiceUtil.getOrganization(companyId, organizationName);
            return organization;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Add a new role to the company.
     *
     * @param companyId
     * @param roleName
     */
    public static Role addRole(long companyId, long userId, String roleName) throws Exception {
        Map<Locale, String> descriptionMap = new HashMap<Locale, String>();
        setLocalizedValue(descriptionMap, roleName);

        return RoleLocalServiceUtil.addRole(
                userId, //the creator user id
                companyId, //the company
                roleName, //the role name
                null, //the title map.
                descriptionMap, //the description
                RoleConstants.TYPE_REGULAR //the type of the role.
                );
    }

    /**
     * Add a new organization to the company.
     *
     * @param companyId
     * @param defaultUserId
     * @param organizationName
     * @return
     * @throws Exception
     */
    public static Organization addOrganization(long companyId, long defaultUserId, String organizationName) throws Exception {

        long parentOrgId = OrganizationConstants.DEFAULT_PARENT_ORGANIZATION_ID;
        String orgType = OrganizationConstants.TYPE_REGULAR_ORGANIZATION;
        int statusId = GetterUtil.getInteger(PropsUtil.get("sql.data.com.liferay.portal.model.ListType.organization.status"));

        ServiceContext context = new ServiceContext();
        context.setAddGroupPermissions(true);
        context.setAddGuestPermissions(true);

        return OrganizationLocalServiceUtil.addOrganization(
                defaultUserId, //the user id
                parentOrgId, //the organization id
                organizationName, //the organization name
                orgType, //the organization type
                true, //recursable
                0, //region id
                0, //country id
                statusId, //the status id
                null, //the comments
                true, //site
                context //the service context.
                );
    }

    /**
     * Get the portal preferences for a portal instance.
     *
     * @param companyId
     * @return
     */
    public static javax.portlet.PortletPreferences getPreferencesForCompany(long companyId) throws Exception {
        long ownerId = companyId;
        int ownerType = PortletKeys.PREFS_OWNER_TYPE_COMPANY;
        long plid = PortletKeys.PREFS_PLID_SHARED;
        String portletId = PortletKeys.LIFERAY_PORTAL;

        return PortletPreferencesLocalServiceUtil.getPreferences(
                companyId, ownerId, ownerType, plid, portletId);
    }

    /**
     * Save the portlet preferences for a given company.
     *
     * @param prefs
     * @param companyId
     * @throws Exception
     */
    public static void updatePreferences(javax.portlet.PortletPreferences prefs, long companyId) throws Exception {
        long ownerId = companyId;
        int ownerType = PortletKeys.PREFS_OWNER_TYPE_COMPANY;
        long plid = PortletKeys.PREFS_PLID_SHARED;
        String portletId = PortletKeys.LIFERAY_PORTAL;
        PortletPreferencesLocalServiceUtil.updatePreferences(ownerId, ownerType, plid, portletId, prefs);
    }

    /**
     * Add a new user in liferay, with mostly default data. The user has agreed
     * to the terms
     *
     * @param firstName
     * @param lastName
     * @param password
     * @param email
     * @param roleIds
     * @return
     * @throws Exception
     */
    public static long addUser(String firstName, String lastName, String password, String email, Date dob, long[] roleIds, long[] organizationIds) {
        try {
            long companyId = PortalUtil.getDefaultCompanyId();
            long defaultUserId = UserLocalServiceUtil.getDefaultUserId(companyId);

            long creatorUserId = defaultUserId;
            boolean autoPassword = false;
            String password1 = password;
            String password2 = password;
            boolean autoScreenName = true;
            String emailAddress = email;
            long facebookId = 0;
            String openId = StringPool.BLANK;
            String middleName = StringPool.BLANK;
            int prefixId = 0;
            int suffixId = 0;

            Calendar dateOfBirth = Calendar.getInstance();
            dateOfBirth.setTime(dob);
            int birthdayMonth = dateOfBirth.get(Calendar.MONTH);
            int birthdayDay = dateOfBirth.get(Calendar.DAY_OF_MONTH);
            int birthdayYear = dateOfBirth.get(Calendar.YEAR);

            //get the guest group
            Group guestGroup = GroupLocalServiceUtil.getGroup(companyId, GroupConstants.GUEST);
            long[] groupIds = {guestGroup.getGroupId()};            
            long[] userGroupIds = null;
            boolean sendEmail = false;
            boolean male = true;

            ServiceContext context = new ServiceContext();
            context.setScopeGroupId(guestGroup.getGroupId());

            User ret = UserLocalServiceUtil.addUser(
                    creatorUserId,
                    companyId,
                    autoPassword,
                    password1, password2,
                    autoScreenName,
                    StringPool.BLANK,
                    emailAddress,
                    facebookId,
                    openId,
                    Locale.getDefault(),
                    firstName, middleName, lastName,
                    prefixId, suffixId,
                    male,
                    birthdayMonth, birthdayDay, birthdayYear,
                    StringPool.BLANK,
                    groupIds, organizationIds, roleIds, userGroupIds,
                    sendEmail, context);

            ret.setAgreedToTermsOfUse(true);
            ret.setReminderQueryQuestion("Created by");
            ret.setReminderQueryAnswer("Rotterdam CS");

            UserLocalServiceUtil.updateUser(ret, true);

            return ret.getUserId();
        } catch (PortalException ex) {
            return 0;
        } catch (SystemException ex) {
            return 0;
        }
    }

    /**
     * Disable the border from a portlet.
     *
     * @param layout
     * @param portletId
     * @throws Exception
     */
    public static void removePortletBorder(Layout layout, String portletId) throws Exception {

        //get the portlet preferences.
        javax.portlet.PortletPreferences portletSetup = PortletPreferencesFactoryUtil.getLayoutPortletSetup(layout, portletId);

        //change the property to show borders to false.
        portletSetup.setValue("portlet-setup-show-borders", String.valueOf(Boolean.FALSE));

        portletSetup.store();
    }
    private static int _PERMISSIONS_USER_CHECK_ALGORITHM =
            GetterUtil.getInteger(
            PropsUtil.get(PropsKeys.PERMISSIONS_USER_CHECK_ALGORITHM));

    /**
     * Set the permissions to a given role.
     *
     * @param role
     * @param name
     * @param actionIds
     * @throws Exception
     */
    static void setRolePermissions(Role role, String name, String... actionIds) throws Exception {
        long roleId = role.getRoleId();
        long companyId = role.getCompanyId();
        int scope = ResourceConstants.SCOPE_COMPANY;

        String primaryKey = String.valueOf(companyId);

        if (_PERMISSIONS_USER_CHECK_ALGORITHM == 6) {
            ResourcePermissionLocalServiceUtil.setResourcePermissions(
                    companyId, name, scope, primaryKey, roleId, actionIds);
        } else {
            PermissionLocalServiceUtil.setRolePermissions(
                    roleId, companyId, name, scope, primaryKey, actionIds);
        }
    }

    private static void setLocalizedValue(Map<Locale, String> map, String value) {
        Locale locale = LocaleUtil.getDefault();
        map.put(locale, value);
        if (!locale.equals(Locale.US)) {
            map.put(Locale.US, value);
        }
    }
}