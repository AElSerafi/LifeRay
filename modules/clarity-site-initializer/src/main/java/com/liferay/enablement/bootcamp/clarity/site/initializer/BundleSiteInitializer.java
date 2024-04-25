package com.liferay.enablement.bootcamp.clarity.site.initializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liferay.account.constants.AccountConstants;
import com.liferay.account.model.AccountEntry;
import com.liferay.account.model.AccountEntryModel;
import com.liferay.account.model.AccountGroup;
import com.liferay.account.service.AccountEntryLocalService;
import com.liferay.account.service.AccountEntryOrganizationRelLocalService;
import com.liferay.account.service.AccountGroupLocalService;
import com.liferay.account.service.AccountGroupRelService;
import com.liferay.account.service.AccountRoleLocalService;
import com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil;
import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.list.model.AssetListEntry;
import com.liferay.asset.list.service.AssetListEntryLocalService;
import com.liferay.asset.list.util.comparator.ClassNameModelResourceComparator;
import com.liferay.client.extension.constants.ClientExtensionEntryConstants;
import com.liferay.client.extension.service.ClientExtensionEntryLocalService;
import com.liferay.client.extension.type.CET;
import com.liferay.client.extension.type.manager.CETManager;
import com.liferay.client.extension.util.CETUtil;
import com.liferay.data.engine.rest.dto.v2_0.DataDefinition;
import com.liferay.data.engine.rest.resource.v2_0.DataDefinitionResource;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFileEntryType;
import com.liferay.document.library.kernel.service.DLAppLocalServiceUtil;
import com.liferay.document.library.util.DLURLHelper;
import com.liferay.dynamic.data.mapping.constants.DDMTemplateConstants;
import com.liferay.dynamic.data.mapping.exception.NoSuchStructureException;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalService;
import com.liferay.dynamic.data.mapping.util.DefaultDDMStructureHelper;
import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.model.ExpandoColumnConstants;
import com.liferay.expando.kernel.service.ExpandoValueLocalService;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
import com.liferay.fragment.importer.FragmentsImportStrategy;
import com.liferay.fragment.importer.FragmentsImporter;
import com.liferay.headless.admin.list.type.dto.v1_0.ListTypeDefinition;
import com.liferay.headless.admin.list.type.dto.v1_0.ListTypeEntry;
import com.liferay.headless.admin.list.type.resource.v1_0.ListTypeDefinitionResource;
import com.liferay.headless.admin.list.type.resource.v1_0.ListTypeEntryResource;
import com.liferay.headless.admin.taxonomy.dto.v1_0.TaxonomyCategory;
import com.liferay.headless.admin.taxonomy.dto.v1_0.TaxonomyVocabulary;
import com.liferay.headless.admin.taxonomy.resource.v1_0.TaxonomyCategoryResource;
import com.liferay.headless.admin.taxonomy.resource.v1_0.TaxonomyVocabularyResource;
import com.liferay.headless.admin.user.dto.v1_0.Account;
import com.liferay.headless.admin.user.dto.v1_0.AccountRole;
import com.liferay.headless.admin.user.dto.v1_0.Organization;
import com.liferay.headless.admin.user.dto.v1_0.UserAccount;
import com.liferay.headless.admin.user.resource.v1_0.AccountResource;
import com.liferay.headless.admin.user.resource.v1_0.AccountRoleResource;
import com.liferay.headless.admin.user.resource.v1_0.OrganizationResource;
import com.liferay.headless.admin.user.resource.v1_0.UserAccountResource;
import com.liferay.headless.admin.workflow.dto.v1_0.WorkflowDefinition;
import com.liferay.headless.admin.workflow.resource.v1_0.WorkflowDefinitionResource;
import com.liferay.headless.delivery.dto.v1_0.Document;
import com.liferay.headless.delivery.dto.v1_0.DocumentFolder;
import com.liferay.headless.delivery.dto.v1_0.KnowledgeBaseArticle;
import com.liferay.headless.delivery.dto.v1_0.KnowledgeBaseFolder;
import com.liferay.headless.delivery.dto.v1_0.StructuredContentFolder;
import com.liferay.headless.delivery.resource.v1_0.DocumentFolderResource;
import com.liferay.headless.delivery.resource.v1_0.DocumentResource;
import com.liferay.headless.delivery.resource.v1_0.KnowledgeBaseArticleResource;
import com.liferay.headless.delivery.resource.v1_0.KnowledgeBaseFolderResource;
import com.liferay.headless.delivery.resource.v1_0.StructuredContentFolderResource;
import com.liferay.journal.constants.JournalArticleConstants;
import com.liferay.journal.constants.JournalFolderConstants;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.layout.helper.LayoutCopyHelper;
import com.liferay.layout.importer.LayoutsImportStrategy;
import com.liferay.layout.importer.LayoutsImporter;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.model.LayoutPageTemplateStructure;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalService;
import com.liferay.layout.page.template.service.LayoutPageTemplateStructureLocalService;
import com.liferay.layout.page.template.service.LayoutPageTemplateStructureRelLocalService;
import com.liferay.layout.util.structure.LayoutStructure;
import com.liferay.layout.utility.page.converter.LayoutUtilityPageEntryTypeConverter;
import com.liferay.layout.utility.page.model.LayoutUtilityPageEntry;
import com.liferay.layout.utility.page.service.LayoutUtilityPageEntryLocalService;
import com.liferay.list.type.service.ListTypeEntryLocalService;
import com.liferay.notification.rest.dto.v1_0.NotificationTemplate;
import com.liferay.notification.rest.resource.v1_0.NotificationTemplateResource;
import com.liferay.object.admin.rest.dto.v1_0.ObjectDefinition;
import com.liferay.object.admin.rest.dto.v1_0.ObjectField;
import com.liferay.object.admin.rest.dto.v1_0.ObjectRelationship;
import com.liferay.object.admin.rest.dto.v1_0.util.ObjectActionUtil;
import com.liferay.object.admin.rest.resource.v1_0.ObjectDefinitionResource;
import com.liferay.object.admin.rest.resource.v1_0.ObjectFieldResource;
import com.liferay.object.admin.rest.resource.v1_0.ObjectRelationshipResource;
import com.liferay.object.rest.dto.v1_0.ObjectEntry;
import com.liferay.object.rest.manager.v1_0.ObjectEntryManager;
import com.liferay.object.service.ObjectActionLocalService;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.object.service.ObjectFieldLocalService;
import com.liferay.object.service.ObjectRelationshipLocalService;
import com.liferay.petra.function.UnsafeRunnable;
import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.GroupModel;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.model.LayoutTypePortlet;
import com.liferay.portal.kernel.model.OrganizationModel;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.Theme;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.module.service.Snapshot;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.LayoutSetLocalService;
import com.liferay.portal.kernel.service.OrganizationLocalService;
import com.liferay.portal.kernel.service.PortletPreferencesLocalService;
import com.liferay.portal.kernel.service.ResourceActionLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.ThemeLocalService;
import com.liferay.portal.kernel.service.UserGroupLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.service.WorkflowDefinitionLinkLocalService;
import com.liferay.portal.kernel.settings.ArchivedSettingsFactory;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.NaturalOrderStringComparator;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.kernel.util.ReleaseInfo;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.TextFormatter;
import com.liferay.portal.kernel.util.URLUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.UnicodePropertiesBuilder;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.zip.ZipWriter;
import com.liferay.portal.kernel.zip.ZipWriterFactory;
import com.liferay.portal.language.override.service.PLOEntryLocalService;
import com.liferay.portal.security.service.access.policy.model.SAPEntry;
import com.liferay.portal.security.service.access.policy.service.SAPEntryLocalService;
import com.liferay.portal.vulcan.dto.converter.DefaultDTOConverterContext;
import com.liferay.portal.vulcan.multipart.BinaryFile;
import com.liferay.portal.vulcan.multipart.MultipartBody;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.segments.model.SegmentsEntry;
import com.liferay.segments.model.SegmentsExperience;
import com.liferay.segments.service.SegmentsEntryLocalService;
import com.liferay.segments.service.SegmentsExperienceLocalService;
import com.liferay.site.exception.InitializationException;
import com.liferay.site.initializer.SiteInitializer;
import com.liferay.site.initializer.extender.CommerceSiteInitializer;
import com.liferay.site.initializer.extender.OSBSiteInitializer;
import com.liferay.site.initializer.extender.SiteInitializerUtil;
import com.liferay.site.navigation.menu.item.layout.constants.SiteNavigationMenuItemTypeConstants;
import com.liferay.site.navigation.model.SiteNavigationMenu;
import com.liferay.site.navigation.model.SiteNavigationMenuItem;
import com.liferay.site.navigation.service.SiteNavigationMenuItemLocalService;
import com.liferay.site.navigation.service.SiteNavigationMenuLocalService;
import com.liferay.site.navigation.type.SiteNavigationMenuItemType;
import com.liferay.site.navigation.type.SiteNavigationMenuItemTypeRegistry;
import com.liferay.style.book.zip.processor.StyleBookEntryZipProcessor;
import com.liferay.template.model.TemplateEntry;
import com.liferay.template.service.TemplateEntryLocalService;

import java.io.InputStream;
import java.io.Serializable;

import java.net.URL;
import java.net.URLConnection;

import java.text.DateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletContext;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
        service = BundleSiteInitializer.class
)
public class BundleSiteInitializer implements SiteInitializer {

    @Override
    public String getDescription(Locale locale) {
        Dictionary<String, String> headers = _bundle.getHeaders(
                StringPool.BLANK);

        return GetterUtil.getString(
                headers.get("Liferay-Site-Initializer-Description"));
    }

    @Override
    public String getKey() {
        return _bundle.getSymbolicName();
    }

    @Override
    public String getName(Locale locale) {
        Dictionary<String, String> headers = _bundle.getHeaders(
                StringPool.BLANK);

        return GetterUtil.getString(
                headers.get("Liferay-Site-Initializer-Name"),
                headers.get("Bundle-Name"));
    }

    @Override
    public String getThumbnailSrc() {
        return _servletContext.getContextPath() + "/thumbnail.png";
    }

    @Override
    public void initialize(long groupId) throws InitializationException {

        BundleWiring bundleWiring = _bundle.adapt(BundleWiring.class);

        _classLoader = bundleWiring.getClassLoader();
        _classNameIdStringUtilReplaceValues = _getClassNameIdStringUtilReplaceValues();
        _releaseInfoStringUtilReplaceValues = _getReleaseInfoStringUtilReplaceValues();

        long startTime = System.currentTimeMillis();

        if (_log.isInfoEnabled()) {
            _log.info(
                    StringBundler.concat(
                            "Initializing ", getKey(), " for group ", groupId));
        }

        try {
            User user = _userLocalService.getUser(
                    PrincipalThreadLocal.getUserId());

            ServiceContext serviceContextThreadLocal =
                    ServiceContextThreadLocal.getServiceContext();

            ServiceContext serviceContext =
                    (ServiceContext)serviceContextThreadLocal.clone();

            serviceContext.setAddGroupPermissions(true);
            serviceContext.setAddGuestPermissions(true);
            serviceContext.setCompanyId(user.getCompanyId());
            serviceContext.setScopeGroupId(groupId);
            serviceContext.setTimeZone(user.getTimeZone());
            serviceContext.setUserId(user.getUserId());

            ServiceContextThreadLocal.pushServiceContext(serviceContext);

            SiteNavigationMenuItemSettingsBuilder
                    siteNavigationMenuItemSettingsBuilder =
                    new SiteNavigationMenuItemSettingsBuilder();

            Map<String, String> stringUtilReplaceValues = new HashMap<>();

            //_invoke(() -> _addAccountGroups(serviceContext));
            _invoke(() -> _addAccounts(serviceContext));

            _invoke(() -> _addAccountGroupAssignments(serviceContext));

            _invoke(
                    () -> _addOrUpdateDataDefinitions(
                            serviceContext, stringUtilReplaceValues));
            _invoke(
                    () -> _addOrUpdateDDMStructures(
                            serviceContext, stringUtilReplaceValues));

            _invoke(
                    () -> _addAssetListEntries(
                            serviceContext, stringUtilReplaceValues));

            _invoke(
                    () -> _addOrUpdateDocuments(
                            serviceContext, siteNavigationMenuItemSettingsBuilder,
                            stringUtilReplaceValues));

            _invoke(
                    () -> _addFragmentEntries(
                            serviceContext, stringUtilReplaceValues));

            _invoke(() -> _addOrUpdateExpandoColumns(serviceContext));
            _invoke(() -> _addOrUpdateKnowledgeBaseArticles(serviceContext));
            _invoke(() -> _addOrUpdateOrganizations(serviceContext));

            _invoke(() -> _addAccountsOrganizations(serviceContext));

            _invoke(
                    () -> _addOrUpdateRoles(
                            serviceContext, stringUtilReplaceValues));

            _invoke(() -> _addOrUpdateSAPEntries(serviceContext));

            _invoke(
                    () -> _addOrUpdateSegmentsEntries(
                            serviceContext, stringUtilReplaceValues));

            _invoke(() -> _addSiteConfiguration(serviceContext));
            _invoke(() -> _addSiteSettings(serviceContext));
            _invoke(() -> _addStyleBookEntries(serviceContext));
            _invoke(
                    () -> _addOrUpdateSXPBlueprint(
                            serviceContext, stringUtilReplaceValues));
            _invoke(() -> _addOrUpdateUserGroups(serviceContext));

            _invoke(
                    () -> _addOrUpdateTaxonomyVocabularies(
                            serviceContext, siteNavigationMenuItemSettingsBuilder,
                            stringUtilReplaceValues));

            _invoke(() -> _addPortletSettings(serviceContext));
            _invoke(
                    () -> _updateLayoutSets(
                            serviceContext, stringUtilReplaceValues));

            _invoke(
                    () -> _addOrUpdateListTypeDefinitions(
                            serviceContext, stringUtilReplaceValues));

            _invoke(() -> _addUserAccounts(serviceContext));

            Map<String, ObjectDefinition>
                    accountEntryRestrictedObjectDefinitions = new HashMap<>();

            List<Long> objectDefinitionIds = new ArrayList<>();

            _invoke(
                    () -> _addObjectDefinitions(
                            accountEntryRestrictedObjectDefinitions,
                            objectDefinitionIds, serviceContext,
                            stringUtilReplaceValues));

            _invoke(
                    () -> _addOrUpdateObjectRelationships(
                            serviceContext, stringUtilReplaceValues));
            _invoke(
                    () -> _addOrUpdateObjectFields(
                            serviceContext, stringUtilReplaceValues));
            _invoke(
                    () -> _publishObjectDefinitions(
                            objectDefinitionIds, serviceContext));

            _invoke(
                    () -> _addOrUpdateAccountEntryRestrictions(
                            accountEntryRestrictedObjectDefinitions, serviceContext));
            _invoke(
                    () -> _addObjectOrUpdateActions(
                            serviceContext, stringUtilReplaceValues));
            _invoke(
                    () -> _addOrUpdateObjectEntries(
                            serviceContext, siteNavigationMenuItemSettingsBuilder,
                            stringUtilReplaceValues));

            _invoke(
                    () -> _addOrUpdateDDMTemplates(
                            serviceContext, stringUtilReplaceValues));

            _invoke(
                    () -> _addOrUpdateJournalArticles(
                            serviceContext, siteNavigationMenuItemSettingsBuilder,
                            stringUtilReplaceValues));

            _invoke(
                    () -> _addOrUpdateNotificationTemplates(
                            serviceContext, stringUtilReplaceValues));

            Map<String, Layout> layoutsMap = _invoke(
                    () -> _addOrUpdateLayouts(
                            serviceContext, stringUtilReplaceValues));

            _invoke(
                    () -> _addCPDefinitions(
                            serviceContext, stringUtilReplaceValues));

            // LPS-172108 Layouts have to be created first so that links in
            // layout page templates work

            _invoke(
                    () -> _addLayoutPageTemplates(
                            serviceContext, stringUtilReplaceValues));

            _invoke(
                    () -> _addLayoutUtilityPageEntries(
                            serviceContext, stringUtilReplaceValues));

            // TODO Review order/dependency

            _invoke(
                    () -> _addOrUpdateClientExtensionEntries(
                            serviceContext, stringUtilReplaceValues));

            _invoke(
                    () -> _addOrUpdateLayoutsContent(
                            layoutsMap, serviceContext,
                            siteNavigationMenuItemSettingsBuilder.build(),
                            stringUtilReplaceValues));

            _invoke(() -> _addRolesAssignments(serviceContext));

            _invoke(
                    () -> _addSegmentsExperiences(
                            serviceContext, stringUtilReplaceValues));
            _invoke(() -> _addUserRoles(serviceContext));

            _invoke(
                    () -> _addWorkflowDefinitions(
                            serviceContext, stringUtilReplaceValues));

            _invoke(
                    () -> _addOrUpdateResourcePermissions(
                            serviceContext, stringUtilReplaceValues));
            _invoke(() -> _setPLOEntries(serviceContext));

            _invoke(
                    () -> _addExpandoValues(
                            serviceContext, stringUtilReplaceValues));
            _invoke(() -> _updateGroupSiteInitializerKey(groupId));
        }
        catch (Exception exception) {
            _log.error(exception);

            throw new InitializationException(exception);
        }
        finally {
            ServiceContextThreadLocal.popServiceContext();
        }

        if (_log.isInfoEnabled()) {
            _log.info(
                    StringBundler.concat(
                            "Initialized ", getKey(), " for group ", groupId, " in ",
                            System.currentTimeMillis() - startTime, " ms"));
        }
    }

    @Override
    public boolean isActive(long companyId) {
        Dictionary<String, String> headers = _bundle.getHeaders(
                StringPool.BLANK);

        String featureFlagKey = headers.get(
                "Liferay-Site-Initializer-Feature-Flag");

        if (Validator.isNotNull(featureFlagKey) &&
                !FeatureFlagManagerUtil.isEnabled(featureFlagKey)) {

            return false;
        }

        return true;
    }

    protected void setServletContext(ServletContext servletContext) {
        _servletContext = servletContext;
    }

    protected void _addAccountGroupAssignments(ServiceContext serviceContext)
            throws Exception {

        String json = SiteInitializerUtil.read(
                "/site-initializer/account-group-assignments.json",
                _servletContext);

        if (json == null) {
            return;
        }

        JSONArray jsonArray = _jsonFactory.createJSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            JSONArray accountsJSONArray = jsonObject.getJSONArray("accounts");

            if (JSONUtil.isEmpty(accountsJSONArray)) {
                continue;
            }

            List<AccountEntry> accountEntries = new ArrayList<>();

            for (int j = 0; j < accountsJSONArray.length(); j++) {
                accountEntries.add(
                        _accountEntryLocalService.
                                getAccountEntryByExternalReferenceCode(
                                        accountsJSONArray.getString(j),
                                        serviceContext.getCompanyId()));
            }

            if (ListUtil.isEmpty(accountEntries)) {
                continue;
            }

            AccountGroup accountGroup =
                    _accountGroupLocalService.
                            fetchAccountGroupByExternalReferenceCode(
                                    jsonObject.getString(
                                            "accountGroupExternalReferenceCode"),
                                    serviceContext.getCompanyId());

            if (accountGroup == null) {
                continue;
            }

            _accountGroupRelService.addAccountGroupRels(
                    accountGroup.getAccountGroupId(), AccountEntry.class.getName(),
                    ListUtil.toLongArray(
                            accountEntries, AccountEntryModel::getAccountEntryId));
        }
    }



    protected void _addAccounts(ServiceContext serviceContext) throws Exception {
        String json = SiteInitializerUtil.read(
                "/site-initializer/accounts.json", _servletContext);

        if (json == null) {
            return;
        }

        AccountResource.Builder builder = _accountResourceFactory.create();

        AccountResource accountResource = builder.user(
                serviceContext.fetchUser()
        ).build();

        JSONArray jsonArray = _jsonFactory.createJSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            Account account = Account.toDTO(
                    String.valueOf(jsonArray.getJSONObject(i)));

            if (account == null) {
                _log.error("Unable to transform account fro protected void _addAccountGroups(ServiceContext serviceContext)\n" +
                        "            throws Exception {\n" +
                        "\n" +
                        "        CommerceSiteInitializer commerceSiteInitializer =\n" +
                        "                _commerceSiteInitializerSnapshot.get();\n" +
                        "\n" +
                        "        if (commerceSiteInitializer == null) {\n" +
                        "            return;\n" +
                        "        }\n" +
                        "\n" +
                        "        commerceSiteInitializer.addAccountGroups(\n" +
                        "                serviceContext, _servletContext);\n" +
                        "    }m JSON: " + json);

                continue;
            }

            accountResource.putAccountByExternalReferenceCode(
                    account.getExternalReferenceCode(), account);
        }
    }

    protected void _addAccountsOrganizations(ServiceContext serviceContext)
            throws Exception {

        String json = SiteInitializerUtil.read(
                "/site-initializer/accounts-organizations.json", _servletContext);

        if (json == null) {
            return;
        }

        JSONArray jsonArray = _jsonFactory.createJSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            JSONArray organizationJSONArray = jsonObject.getJSONArray(
                    "organizations");

            if (JSONUtil.isEmpty(organizationJSONArray)) {
                continue;
            }

            List<com.liferay.portal.kernel.model.Organization> organizations =
                    new ArrayList<>();

            for (int j = 0; j < organizationJSONArray.length(); j++) {
                organizations.add(
                        _organizationLocalService.
                                getOrganizationByExternalReferenceCode(
                                        organizationJSONArray.getString(j),
                                        serviceContext.getCompanyId()));
            }

            if (ListUtil.isEmpty(organizations)) {
                continue;
            }

            AccountEntry accountEntry =
                    _accountEntryLocalService.
                            getAccountEntryByExternalReferenceCode(
                                    jsonObject.getString("accountExternalReferenceCode"),
                                    serviceContext.getCompanyId());

            if (accountEntry == null) {
                continue;
            }

            _accountEntryOrganizationRelLocalService.
                    addAccountEntryOrganizationRels(
                            accountEntry.getAccountEntryId(),
                            ListUtil.toLongArray(
                                    organizations, OrganizationModel::getOrganizationId));
        }
    }

    protected void _addAssetListEntries(
            ServiceContext serviceContext,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        String json = SiteInitializerUtil.read(
                "/site-initializer/asset-list-entries.json", _servletContext);

        if (json == null) {
            return;
        }

        JSONArray assetListJSONArray = _jsonFactory.createJSONArray(json);

        for (int i = 0; i < assetListJSONArray.length(); i++) {
            JSONObject assetListJSONObject = assetListJSONArray.getJSONObject(
                    i);

            _addOrUpdateAssetListEntry(assetListJSONObject, serviceContext);
        }

        List<AssetListEntry> assetListEntries =
                _assetListEntryLocalService.getAssetListEntries(
                        serviceContext.getScopeGroupId());

        for (AssetListEntry assetListEntry : assetListEntries) {
            String assetListEntryKeyUppercase = StringUtil.toUpperCase(
                    assetListEntry.getAssetListEntryKey());

            stringUtilReplaceValues.put(
                    "ASSET_LIST_ENTRY_ID:" + assetListEntryKeyUppercase,
                    String.valueOf(assetListEntry.getAssetListEntryId()));
        }
    }

    protected void _addCPDefinitions(
            ServiceContext serviceContext,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        CommerceSiteInitializer commerceSiteInitializer =
                _commerceSiteInitializerSnapshot.get();

        if (commerceSiteInitializer == null) {
            return;
        }

        commerceSiteInitializer.addCPDefinitions(
                _bundle, serviceContext, _servletContext, stringUtilReplaceValues);
    }

    protected void _addExpandoValues(
            ServiceContext serviceContext,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        String json = SiteInitializerUtil.read(
                "/site-initializer/expando-values.json", _servletContext);

        if (json == null) {
            return;
        }

        JSONArray jsonArray = _jsonFactory.createJSONArray(
                _replace(json, stringUtilReplaceValues));

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Object data = jsonObject.get("data");

            if (data instanceof JSONObject) {
                Map<Locale, Object> map = new HashMap<>();

                JSONObject dataJSONObject = (JSONObject)data;

                Map<String, Object> dataJSONObjectMap = dataJSONObject.toMap();

                for (Map.Entry<String, Object> entry :
                        dataJSONObjectMap.entrySet()) {

                    Object value = entry.getValue();

                    if (!(value instanceof List)) {
                        map.put(
                                LocaleUtil.fromLanguageId(entry.getKey()), value);

                        continue;
                    }

                    List<?> values = (List<?>)value;

                    map.put(
                            LocaleUtil.fromLanguageId(entry.getKey()),
                            values.toArray(new String[0]));
                }

                data = map;
            }

            _expandoValueLocalService.addValue(
                    serviceContext.getCompanyId(),
                    jsonObject.getString("className"), "CUSTOM_FIELDS",
                    jsonObject.getString("columnName"),
                    jsonObject.getLong("classPk"), data);
        }
    }

    protected void _addFragmentEntries(
            long groupId, String parentResourcePath,
            ServiceContext serviceContext,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        Enumeration<URL> enumeration = _bundle.findEntries(
                parentResourcePath, StringPool.STAR, true);

        if (enumeration == null) {
            return;
        }

        ZipWriter zipWriter = _zipWriterFactory.getZipWriter();

        while (enumeration.hasMoreElements()) {
            URL url = enumeration.nextElement();

            String fileName = url.getFile();

            if (fileName.endsWith("/")) {
                continue;
            }

            if (StringUtil.endsWith(
                    fileName, "fragment-composition-definition.json")) {

                String json = URLUtil.toString(url);

                json = _replace(
                        _replace(json, serviceContext), stringUtilReplaceValues);

                zipWriter.addEntry(
                        _removeFirst(fileName, parentResourcePath), json);
            }
            else {
                try (InputStream inputStream = url.openStream()) {
                    zipWriter.addEntry(
                            _removeFirst(fileName, parentResourcePath),
                            inputStream);
                }
            }
        }

        _fragmentsImporter.importFragmentEntries(
                serviceContext.getUserId(), groupId, 0, zipWriter.getFile(),
                FragmentsImportStrategy.OVERWRITE);
    }

    protected void _addFragmentEntries(
            ServiceContext serviceContext,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        Group group = _groupLocalService.getCompanyGroup(
                serviceContext.getCompanyId());

        _addFragmentEntries(
                group.getGroupId(), "/site-initializer/fragments/company",
                serviceContext, stringUtilReplaceValues);

        _addFragmentEntries(
                serviceContext.getScopeGroupId(),
                "/site-initializer/fragments/group", serviceContext,
                stringUtilReplaceValues);
    }

    protected void _addLayoutPageTemplates(
            ServiceContext serviceContext,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        Enumeration<URL> enumeration = _bundle.findEntries(
                "/site-initializer/layout-page-templates", StringPool.STAR, true);

        if (enumeration == null) {
            return;
        }

        ZipWriter zipWriter = _zipWriterFactory.getZipWriter();

        while (enumeration.hasMoreElements()) {
            URL url = enumeration.nextElement();

            String fileName = url.getFile();

            if (fileName.endsWith("/")) {
                continue;
            }

            String urlPath = url.getPath();

            if (StringUtil.endsWith(urlPath, "display-page-template.json") ||
                    StringUtil.endsWith(urlPath, "page-definition.json")) {

                String json = URLUtil.toString(url);

                json = _replace(
                        _replace(json, serviceContext), stringUtilReplaceValues);

                String css = _replace(
                        SiteInitializerUtil.read(
                                FileUtil.getPath(urlPath) + "/css.css",
                                _servletContext),
                        stringUtilReplaceValues);

                if (Validator.isNotNull(css)) {
                    JSONObject jsonObject = _jsonFactory.createJSONObject(json);

                    JSONObject settingsJSONObject = jsonObject.getJSONObject(
                            "settings");

                    settingsJSONObject.put("css", css);

                    jsonObject.put("settings", settingsJSONObject);

                    json = jsonObject.toString();
                }

                zipWriter.addEntry(
                        _removeFirst(
                                urlPath, "/site-initializer/layout-page-templates"),
                        json);
            }
            else {
                try (InputStream inputStream = url.openStream()) {
                    zipWriter.addEntry(
                            _removeFirst(
                                    urlPath, "/site-initializer/layout-page-templates"),
                            inputStream);
                }
            }
        }

        _layoutsImporter.importFile(
                serviceContext.getUserId(), serviceContext.getScopeGroupId(),
                zipWriter.getFile(), LayoutsImportStrategy.OVERWRITE, true);
    }

    protected void _addLayoutUtilityPageEntries(
            ServiceContext serviceContext,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        Enumeration<URL> enumeration = _bundle.findEntries(
                "/site-initializer/layout-utility-page-entries", StringPool.STAR,
                true);

        if (enumeration == null) {
            return;
        }

        ZipWriter zipWriter = _zipWriterFactory.getZipWriter();

        while (enumeration.hasMoreElements()) {
            URL url = enumeration.nextElement();

            String fileName = url.getFile();

            if (fileName.endsWith("/")) {
                continue;
            }

            String urlPath = url.getPath();

            if (StringUtil.endsWith(urlPath, "page-definition.json")) {
                String json = URLUtil.toString(url);

                json = _replace(
                        _replace(json, serviceContext), stringUtilReplaceValues);

                String css = _replace(
                        SiteInitializerUtil.read(
                                FileUtil.getPath(urlPath) + "/css.css",
                                _servletContext),
                        stringUtilReplaceValues);

                if (Validator.isNotNull(css)) {
                    JSONObject jsonObject = _jsonFactory.createJSONObject(json);

                    JSONObject settingsJSONObject = jsonObject.getJSONObject(
                            "settings");

                    settingsJSONObject.put("css", css);

                    jsonObject.put("settings", settingsJSONObject);

                    json = jsonObject.toString();
                }

                zipWriter.addEntry(
                        _removeFirst(
                                urlPath,
                                "/site-initializer/layout-utility-page-entries"),
                        json);
            }
            else {
                try (InputStream inputStream = url.openStream()) {
                    zipWriter.addEntry(
                            _removeFirst(
                                    urlPath,
                                    "/site-initializer/layout-utility-page-entries"),
                            inputStream);
                }
            }
        }

        _layoutsImporter.importFile(
                serviceContext.getUserId(), serviceContext.getScopeGroupId(),
                zipWriter.getFile(), LayoutsImportStrategy.OVERWRITE, true);

        _setDefaultLayoutUtilityPageEntries(serviceContext);
    }

    protected void _addObjectDefinitions(
            Map<String, ObjectDefinition>
                    accountEntryRestrictedObjectDefinitions,
            List<Long> objectDefinitionIds, ServiceContext serviceContext,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        List<com.liferay.object.model.ObjectDefinition>
                serviceBuilderObjectDefinitions =
                _objectDefinitionLocalService.getObjectDefinitions(
                        serviceContext.getCompanyId(), true,
                        WorkflowConstants.STATUS_APPROVED);

        for (com.liferay.object.model.ObjectDefinition
                serviceBuilderObjectDefinition :
                serviceBuilderObjectDefinitions) {

            stringUtilReplaceValues.put(
                    "OBJECT_DEFINITION_ID:" +
                            serviceBuilderObjectDefinition.getShortName(),
                    String.valueOf(
                            serviceBuilderObjectDefinition.getObjectDefinitionId()));
        }

        Set<String> resourcePaths = _servletContext.getResourcePaths(
                "/site-initializer/object-definitions");

        if (SetUtil.isEmpty(resourcePaths)) {
            return;
        }

        ObjectDefinitionResource.Builder objectDefinitionResourceBuilder =
                _objectDefinitionResourceFactory.create();

        ObjectDefinitionResource objectDefinitionResource =
                objectDefinitionResourceBuilder.user(
                        serviceContext.fetchUser()
                ).build();

        for (String resourcePath : resourcePaths) {
            String json = SiteInitializerUtil.read(
                    resourcePath, _servletContext);

            json = _replace(json, stringUtilReplaceValues);

            ObjectDefinition objectDefinition = ObjectDefinition.toDTO(json);

            if (objectDefinition == null) {
                _log.error(
                        "Unable to transform object definition from JSON: " + json);

                continue;
            }

            Page<ObjectDefinition> objectDefinitionsPage =
                    objectDefinitionResource.getObjectDefinitionsPage(
                            null, null,
                            objectDefinitionResource.toFilter(
                                    StringBundler.concat(
                                            "name eq '", objectDefinition.getName(), "'")),
                            null, null);

            ObjectDefinition existingObjectDefinition =
                    objectDefinitionsPage.fetchFirstItem();

            if (existingObjectDefinition == null) {
                if (GetterUtil.getBoolean(
                        objectDefinition.getAccountEntryRestricted())) {

                    accountEntryRestrictedObjectDefinitions.put(
                            objectDefinition.getName(), objectDefinition);
                }

                objectDefinition =
                        objectDefinitionResource.postObjectDefinition(
                                objectDefinition);

                objectDefinitionIds.add(objectDefinition.getId());
            }
            else {
                objectDefinition =
                        objectDefinitionResource.patchObjectDefinition(
                                existingObjectDefinition.getId(), objectDefinition);
            }

            stringUtilReplaceValues.put(
                    "OBJECT_DEFINITION_ID:" + objectDefinition.getName(),
                    String.valueOf(objectDefinition.getId()));
        }
    }

    protected void _addObjectOrUpdateActions(
            ServiceContext serviceContext,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        Set<String> resourcePaths = _servletContext.getResourcePaths(
                "/site-initializer/object-actions");

        if (SetUtil.isEmpty(resourcePaths)) {
            return;
        }

        for (String resourcePath : resourcePaths) {
            String json = SiteInitializerUtil.read(
                    resourcePath, _servletContext);

            json = _replace(json, stringUtilReplaceValues);

            if (json == null) {
                continue;
            }

            JSONObject jsonObject = _jsonFactory.createJSONObject(json);

            JSONArray jsonArray = jsonObject.getJSONArray("object-actions");

            if (JSONUtil.isEmpty(jsonArray)) {
                continue;
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objectActionJSONObject = jsonArray.getJSONObject(i);

                JSONObject parametersJSONObject =
                        objectActionJSONObject.getJSONObject("parameters");

                _objectActionLocalService.addOrUpdateObjectAction(
                        objectActionJSONObject.getString("externalReferenceCode"),
                        0, serviceContext.getUserId(),
                        jsonObject.getLong("objectDefinitionId"),
                        objectActionJSONObject.getBoolean("active"),
                        objectActionJSONObject.getString("conditionExpression"),
                        objectActionJSONObject.getString("description"),
                        SiteInitializerUtil.toMap(
                                objectActionJSONObject.getString("errorMessage")),
                        SiteInitializerUtil.toMap(
                                objectActionJSONObject.getString("label")),
                        objectActionJSONObject.getString("name"),
                        objectActionJSONObject.getString("objectActionExecutorKey"),
                        objectActionJSONObject.getString("objectActionTriggerKey"),
                        ObjectActionUtil.toParametersUnicodeProperties(
                                parametersJSONObject.toMap()),
                        objectActionJSONObject.getBoolean("system"));
            }
        }
    }

    protected void _addOrganizationUser(
            JSONArray jsonArray, ServiceContext serviceContext, long userId)
            throws Exception {

        if (JSONUtil.isEmpty(jsonArray)) {
            return;
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            long organizationId = _organizationLocalService.getOrganizationId(
                    serviceContext.getCompanyId(), jsonObject.getString("name"));

            if (organizationId <= 0) {
                continue;
            }

            _userLocalService.addOrganizationUser(organizationId, userId);
        }
    }

    protected void _addOrKnowledgeBaseObjects(
            boolean folder, long parentKnowledgeBaseObjectId,
            String parentResourcePath, ServiceContext serviceContext)
            throws Exception {

        Set<String> resourcePaths = _servletContext.getResourcePaths(
                parentResourcePath);

        if (SetUtil.isEmpty(resourcePaths)) {
            return;
        }

        for (String resourcePath : resourcePaths) {
            if (!resourcePath.endsWith(".metadata.json")) {
                continue;
            }

            String json = SiteInitializerUtil.read(
                    resourcePath, _servletContext);

            if (json == null) {
                continue;
            }

            JSONObject jsonObject = _jsonFactory.createJSONObject(json);

            if (jsonObject.has("articleBody")) {
                _addOrUpdateKnowledgeBaseArticle(
                        folder, jsonObject, parentKnowledgeBaseObjectId,
                        resourcePath.substring(
                                0, resourcePath.indexOf(".metadata.json")),
                        serviceContext);
            }
            else {
                _addOrUpdateKnowledgeBaseFolder(
                        jsonObject, parentKnowledgeBaseObjectId,
                        resourcePath.substring(
                                0, resourcePath.indexOf(".metadata.json")),
                        serviceContext);
            }
        }
    }

    protected void _addOrUpdateAccountEntryRestrictions(
            Map<String, ObjectDefinition>
                    accountEntryRestrictedObjectDefinitions,
            ServiceContext serviceContext)
            throws Exception {

        for (Map.Entry<String, ObjectDefinition> entry :
                accountEntryRestrictedObjectDefinitions.entrySet()) {

            com.liferay.object.model.ObjectDefinition
                    serviceBuilderObjectDefinition =
                    _objectDefinitionLocalService.fetchObjectDefinition(
                            serviceContext.getCompanyId(), "C_" + entry.getKey());

            com.liferay.object.model.ObjectField serviceBuilderObjectField =
                    _objectFieldLocalService.fetchObjectField(
                            serviceBuilderObjectDefinition.getObjectDefinitionId(),
                            entry.getValue(
                            ).getAccountEntryRestrictedObjectFieldName());

            if (serviceBuilderObjectDefinition.isDefaultStorageType()) {
                _objectDefinitionLocalService.enableAccountEntryRestricted(
                        _objectRelationshipLocalService.
                                fetchObjectRelationshipByObjectFieldId2(
                                        serviceBuilderObjectField.getObjectFieldId()));
            }
            else {
                _objectDefinitionLocalService.
                        enableAccountEntryRestrictedForNondefaultStorageType(
                                serviceBuilderObjectField);
            }
        }
    }

    protected void _addOrUpdateAssetListEntry(
            JSONObject assetListJSONObject, ServiceContext serviceContext)
            throws Exception {

        AssetListEntry assetListEntry = null;

        String assetListEntryKey = StringUtil.toLowerCase(
                _replace(assetListJSONObject.getString("title"), " ", "-"));

        for (AssetListEntry curAssetListEntry :
                _assetListEntryLocalService.getAssetListEntries(
                        serviceContext.getScopeGroupId())) {

            if (Objects.equals(
                    curAssetListEntry.getAssetListEntryKey(),
                    assetListEntryKey)) {

                assetListEntry = curAssetListEntry;

                break;
            }
        }

        JSONObject unicodePropertiesJSONObject =
                assetListJSONObject.getJSONObject("unicodeProperties");

        DDMStructure ddmStructure = _ddmStructureLocalService.getStructure(
                serviceContext.getScopeGroupId(),
                _portal.getClassNameId(
                        unicodePropertiesJSONObject.getString("classNameIds")),
                assetListJSONObject.getString("ddmStructureKey"));

        List<String> classNameIdStrings = new ArrayList<>();

        List<Long> classNameIds = ListUtil.fromArray(
                AssetRendererFactoryRegistryUtil.getIndexableClassNameIds(
                        serviceContext.getCompanyId(), true));

        classNameIds = ListUtil.sort(
                classNameIds,
                new ClassNameModelResourceComparator(
                        true, serviceContext.getLocale()));

        classNameIds.forEach(
                classNameId -> classNameIdStrings.add(classNameId.toString()));

        Map<String, String> map = HashMapBuilder.put(
                "anyAssetType",
                String.valueOf(
                        _portal.getClassNameId(
                                unicodePropertiesJSONObject.getString("classNameIds")))
        ).put(
                unicodePropertiesJSONObject.getString("anyClassType"),
                String.valueOf(ddmStructure.getStructureId())
        ).put(
                "classNameIds", StringUtil.merge(classNameIdStrings, ",")
        ).put(
                unicodePropertiesJSONObject.getString("classTypeIds"),
                String.valueOf(ddmStructure.getStructureId())
        ).put(
                "groupIds", String.valueOf(serviceContext.getScopeGroupId())
        ).build();

        Object[] orderByObjects = JSONUtil.toObjectArray(
                unicodePropertiesJSONObject.getJSONArray("orderBy"));

        for (Object orderByObject : orderByObjects) {
            JSONObject orderByJSONObject = (JSONObject)orderByObject;

            map.put(
                    orderByJSONObject.getString("key"),
                    orderByJSONObject.getString("value"));
        }

        String[] assetTagNames = JSONUtil.toStringArray(
                assetListJSONObject.getJSONArray("assetTagNames"));

        for (int i = 0; i < assetTagNames.length; i++) {
            map.put("queryValues" + i, assetTagNames[i]);

            Object[] queryObjects = JSONUtil.toObjectArray(
                    unicodePropertiesJSONObject.getJSONArray("query"));

            for (Object queryObject : queryObjects) {
                JSONObject queryJSONObject = (JSONObject)queryObject;

                map.put(
                        queryJSONObject.getString("key"),
                        queryJSONObject.getString("value"));
            }
        }

        if (assetListEntry == null) {
            _assetListEntryLocalService.addDynamicAssetListEntry(
                    serviceContext.getUserId(), serviceContext.getScopeGroupId(),
                    assetListJSONObject.getString("title"),
                    UnicodePropertiesBuilder.create(
                            map, true
                    ).buildString(),
                    serviceContext);
        }
        else {
            _assetListEntryLocalService.updateAssetListEntry(
                    assetListEntry.getAssetListEntryId(),
                    assetListJSONObject.getString("title"));
        }
    }

    protected void _addOrUpdateClientExtensionEntries(
            ServiceContext serviceContext,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        List<CET> cets = _cetManager.getCETs(
                serviceContext.getCompanyId(), null, null,
                Pagination.of(QueryUtil.ALL_POS, QueryUtil.ALL_POS), null);

        for (CET cet : cets) {
            stringUtilReplaceValues.put(
                    "CLIENT_EXTENSION_ENTRY_ERC:" + cet.getExternalReferenceCode(),
                    StringBundler.concat(
                            "com_liferay_client_extension_web_internal_portlet_",
                            "ClientExtensionEntryPortlet_", cet.getCompanyId(), "_",
                            CETUtil.normalizeExternalReferenceCodeForPortletId(
                                    cet.getExternalReferenceCode())));
        }

        String json = SiteInitializerUtil.read(
                "/site-initializer/client-extension-entries.json", _servletContext);

        if (json == null) {
            return;
        }

        JSONArray jsonArray = _jsonFactory.createJSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            StringBundler sb = new StringBundler();

            JSONObject propertiesJSONObject = jsonObject.getJSONObject(
                    "properties");

            if (propertiesJSONObject != null) {
                for (String key : propertiesJSONObject.keySet()) {
                    sb.append(key);
                    sb.append(StringPool.EQUAL);
                    sb.append(propertiesJSONObject.getString(key));
                    sb.append(StringPool.NEW_LINE);
                }
            }

            _clientExtensionEntryLocalService.addOrUpdateClientExtensionEntry(
                    jsonObject.getString("externalReferenceCode"),
                    serviceContext.getUserId(), StringPool.BLANK,
                    SiteInitializerUtil.toMap(jsonObject.getString("name_i18n")),
                    sb.toString(), StringPool.BLANK,
                    ClientExtensionEntryConstants.TYPE_CUSTOM_ELEMENT,
                    UnicodePropertiesBuilder.create(
                            true
                    ).put(
                            "cssURLs",
                            _replace(
                                    StringUtil.merge(
                                            JSONUtil.toStringArray(
                                                    jsonObject.getJSONArray("cssURLs")),
                                            StringPool.NEW_LINE),
                                    stringUtilReplaceValues)
                    ).put(
                            "friendlyURLMapping", StringPool.BLANK
                    ).put(
                            "htmlElementName", jsonObject.getString("htmlElementName")
                    ).put(
                            "instanceable", jsonObject.getBoolean("instanceable")
                    ).put(
                            "portletCategoryName",
                            jsonObject.getString("portletCategoryName")
                    ).put(
                            "urls",
                            _replace(
                                    StringUtil.merge(
                                            JSONUtil.toStringArray(
                                                    jsonObject.getJSONArray("elementURLs")),
                                            StringPool.NEW_LINE),
                                    stringUtilReplaceValues)
                    ).put(
                            "useESM", jsonObject.getBoolean("useESM", false)
                    ).buildString());

            stringUtilReplaceValues.put(
                    "CLIENT_EXTENSION_ENTRY_ERC:" +
                            jsonObject.getString("externalReferenceCode"),
                    StringBundler.concat(
                            "com_liferay_client_extension_web_internal_portlet_",
                            "ClientExtensionEntryPortlet_",
                            serviceContext.getCompanyId(), "_",
                            CETUtil.normalizeExternalReferenceCodeForPortletId(
                                    jsonObject.getString("externalReferenceCode"))));
        }
    }

    protected void _addOrUpdateDataDefinitions(
            ServiceContext serviceContext,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        List<DDMStructure> ddmStructures =
                _ddmStructureLocalService.getStructures(
                        serviceContext.getScopeGroupId());

        for (DDMStructure ddmStructure : ddmStructures) {
            stringUtilReplaceValues.put(
                    "DDM_STRUCTURE_ID:" + ddmStructure.getStructureKey(),
                    String.valueOf(ddmStructure.getStructureId()));
        }

        Set<String> resourcePaths = _servletContext.getResourcePaths(
                "/site-initializer/data-definitions");

        if (SetUtil.isEmpty(resourcePaths)) {
            return;
        }

        DataDefinitionResource.Builder dataDefinitionResourceBuilder =
                _dataDefinitionResourceFactory.create();

        DataDefinitionResource dataDefinitionResource =
                dataDefinitionResourceBuilder.user(
                        serviceContext.fetchUser()
                ).build();

        for (String resourcePath : resourcePaths) {
            String json = _replace(
                    SiteInitializerUtil.read(resourcePath, _servletContext),
                    stringUtilReplaceValues);

            DataDefinition dataDefinition = DataDefinition.toDTO(json);

            if (dataDefinition == null) {
                _log.error(
                        "Unable to transform data definition from JSON: " + json);

                continue;
            }

            try {
                DataDefinition existingDataDefinition =
                        dataDefinitionResource.
                                getSiteDataDefinitionByContentTypeByDataDefinitionKey(
                                        serviceContext.getScopeGroupId(),
                                        dataDefinition.getContentType(),
                                        dataDefinition.getDataDefinitionKey());

                dataDefinition = dataDefinitionResource.putDataDefinition(
                        existingDataDefinition.getId(), dataDefinition);
            }
            catch (NoSuchStructureException noSuchStructureException) {
                dataDefinition =
                        dataDefinitionResource.postSiteDataDefinitionByContentType(
                                serviceContext.getScopeGroupId(),
                                dataDefinition.getContentType(), dataDefinition);
            }

            stringUtilReplaceValues.put(
                    "DATA_DEFINITION_ID:" + dataDefinition.getDataDefinitionKey(),
                    String.valueOf(dataDefinition.getId()));
        }
    }

    protected void _addOrUpdateDDMStructures(
            ServiceContext serviceContext,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        Set<String> resourcePaths = _servletContext.getResourcePaths(
                "/site-initializer/ddm-structures");

        if (SetUtil.isEmpty(resourcePaths)) {
            return;
        }

        for (String resourcePath : resourcePaths) {
            _defaultDDMStructureHelper.addOrUpdateDDMStructures(
                    serviceContext.getUserId(), serviceContext.getScopeGroupId(),
                    _portal.getClassNameId(JournalArticle.class), _classLoader,
                    resourcePath, serviceContext);
        }

        List<DDMStructure> ddmStructures =
                _ddmStructureLocalService.getStructures(
                        serviceContext.getScopeGroupId());

        for (DDMStructure ddmStructure : ddmStructures) {
            stringUtilReplaceValues.put(
                    "DDM_STRUCTURE_ID:" + ddmStructure.getStructureKey(),
                    String.valueOf(ddmStructure.getStructureId()));
        }
    }

    protected void _addOrUpdateDDMTemplates(
            ServiceContext serviceContext,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        List<DDMTemplate> ddmTemplates =
                _ddmTemplateLocalService.getDDMTemplates(
                        QueryUtil.ALL_POS, QueryUtil.ALL_POS);

        for (DDMTemplate ddmTemplate : ddmTemplates) {
            TemplateEntry templateEntry =
                    _templateEntryLocalService.fetchTemplateEntryByDDMTemplateId(
                            ddmTemplate.getTemplateId());

            if (templateEntry != null) {
                stringUtilReplaceValues.put(
                        "TEMPLATE_ENTRY_ID:" +
                                ddmTemplate.getName(LocaleUtil.getSiteDefault()),
                        String.valueOf(templateEntry.getTemplateEntryId()));
            }

            stringUtilReplaceValues.put(
                    "DDM_TEMPLATE_ID:" +
                            ddmTemplate.getName(LocaleUtil.getSiteDefault()),
                    String.valueOf(ddmTemplate.getTemplateId()));
        }

        Enumeration<URL> enumeration = _bundle.findEntries(
                "/site-initializer/ddm-templates", "ddm-template.json", true);

        if (enumeration == null) {
            return;
        }

        while (enumeration.hasMoreElements()) {
            URL url = enumeration.nextElement();

            JSONObject jsonObject = _jsonFactory.createJSONObject(
                    _replace(URLUtil.toString(url), stringUtilReplaceValues));

            long resourceClassNameId = _portal.getClassNameId(
                    jsonObject.getString(
                            "resourceClassName", JournalArticle.class.getName()));

            long ddmStructureId = 0;

            String ddmStructureKey = jsonObject.getString("ddmStructureKey");

            if (Validator.isNotNull(ddmStructureKey)) {
                DDMStructure ddmStructure =
                        _ddmStructureLocalService.fetchStructure(
                                serviceContext.getScopeGroupId(), resourceClassNameId,
                                ddmStructureKey);

                ddmStructureId = ddmStructure.getStructureId();
            }

            DDMTemplate ddmTemplate = _ddmTemplateLocalService.fetchTemplate(
                    serviceContext.getScopeGroupId(),
                    _portal.getClassNameId(
                            jsonObject.getString(
                                    "className", DDMStructure.class.getName())),
                    jsonObject.getString("ddmTemplateKey"));

            if (ddmTemplate == null) {
                ddmTemplate = _ddmTemplateLocalService.addTemplate(
                        serviceContext.getUserId(),
                        serviceContext.getScopeGroupId(),
                        _portal.getClassNameId(
                                jsonObject.getString(
                                        "className", DDMStructure.class.getName())),
                        ddmStructureId, resourceClassNameId,
                        jsonObject.getString("ddmTemplateKey"),
                        HashMapBuilder.put(
                                LocaleUtil.getSiteDefault(),
                                jsonObject.getString("name")
                        ).build(),
                        null, DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY, null,
                        TemplateConstants.LANG_TYPE_FTL,
                        SiteInitializerUtil.read(_bundle, "ddm-template.ftl", url),
                        false, false, null, null, serviceContext);

                if (Objects.equals(
                        jsonObject.getString("className"),
                        TemplateEntry.class.getName())) {

                    TemplateEntry templateEntry =
                            _templateEntryLocalService.addTemplateEntry(
                                    serviceContext.getUserId(),
                                    serviceContext.getScopeGroupId(),
                                    ddmTemplate.getTemplateId(),
                                    jsonObject.getString("infoItemClassName"),
                                    jsonObject.getString("infoItemKey"),
                                    serviceContext);

                    String templateEntryIdKey =
                            "TEMPLATE_ENTRY_ID:" +
                                    ddmTemplate.getName(LocaleUtil.getSiteDefault());
                    String templateEntryIdValue = String.valueOf(
                            templateEntry.getTemplateEntryId());

                    stringUtilReplaceValues.put(
                            templateEntryIdKey, templateEntryIdValue);
                }
            }
            else {
                _ddmTemplateLocalService.updateTemplate(
                        serviceContext.getUserId(), ddmTemplate.getTemplateId(),
                        ddmStructureId,
                        HashMapBuilder.put(
                                LocaleUtil.getSiteDefault(),
                                jsonObject.getString("name")
                        ).build(),
                        null, DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY, null,
                        TemplateConstants.LANG_TYPE_FTL,
                        SiteInitializerUtil.read(_bundle, "ddm-template.ftl", url),
                        false, false, null, null, serviceContext);
            }

            stringUtilReplaceValues.put(
                    "DDM_TEMPLATE_ID:" +
                            ddmTemplate.getName(LocaleUtil.getSiteDefault()),
                    String.valueOf(ddmTemplate.getTemplateId()));
        }
    }

    protected Long _addOrUpdateDocumentFolder(
            Long documentFolderId, long groupId, String resourcePath,
            ServiceContext serviceContext)
            throws Exception {

        DocumentFolderResource.Builder documentFolderResourceBuilder =
                _documentFolderResourceFactory.create();

        DocumentFolderResource documentFolderResource =
                documentFolderResourceBuilder.user(
                        serviceContext.fetchUser()
                ).build();

        DocumentFolder documentFolder = null;

        resourcePath = resourcePath.substring(0, resourcePath.length() - 1);

        String json = SiteInitializerUtil.read(
                resourcePath + ".metadata.json", _servletContext);

        if (json != null) {
            documentFolder = DocumentFolder.toDTO(json);
        }
        else {
            documentFolder = DocumentFolder.toDTO(
                    JSONUtil.put(
                            "name", FileUtil.getShortFileName(resourcePath)
                    ).put(
                            "viewableBy", "Anyone"
                    ).toString());
        }

        Page<DocumentFolder> documentFoldersPage =
                documentFolderResource.getSiteDocumentFoldersPage(
                        groupId, true, null, null,
                        documentFolderResource.toFilter(
                                StringBundler.concat(
                                        "name eq '", documentFolder.getName(), "'")),
                        null, null);

        DocumentFolder existingDocumentFolder =
                documentFoldersPage.fetchFirstItem();

        if (existingDocumentFolder == null) {
            if (documentFolderId != null) {
                documentFolder =
                        documentFolderResource.postDocumentFolderDocumentFolder(
                                documentFolderId, documentFolder);
            }
            else {
                documentFolder = documentFolderResource.postSiteDocumentFolder(
                        groupId, documentFolder);
            }
        }
        else {
            documentFolder = documentFolderResource.putDocumentFolder(
                    existingDocumentFolder.getId(), documentFolder);
        }

        return documentFolder.getId();
    }

    protected void _addOrUpdateDocuments(
            Long documentFolderId, long groupId, String parentResourcePath,
            ServiceContext serviceContext,
            SiteNavigationMenuItemSettingsBuilder
                    siteNavigationMenuItemSettingsBuilder,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        Set<String> resourcePaths = _servletContext.getResourcePaths(
                parentResourcePath);

        if (SetUtil.isEmpty(resourcePaths)) {
            return;
        }

        DocumentResource.Builder documentResourceBuilder =
                _documentResourceFactory.create();

        DocumentResource documentResource = documentResourceBuilder.user(
                serviceContext.fetchUser()
        ).build();

        for (String resourcePath : resourcePaths) {
            if (resourcePath.endsWith("/")) {
                _addOrUpdateDocuments(
                        _addOrUpdateDocumentFolder(
                                documentFolderId, groupId, resourcePath,
                                serviceContext),
                        groupId, resourcePath, serviceContext,
                        siteNavigationMenuItemSettingsBuilder,
                        stringUtilReplaceValues);

                continue;
            }

            if (resourcePath.endsWith(".gitkeep") ||
                    resourcePath.endsWith(".metadata.json")) {

                continue;
            }

            String fileName = FileUtil.getShortFileName(resourcePath);

            URL url = _servletContext.getResource(resourcePath);

            URLConnection urlConnection = url.openConnection();

            Map<String, String> values = new HashMap<>();

            String json = SiteInitializerUtil.read(
                    resourcePath + ".metadata.json", _servletContext);

            if (json != null) {
                values = Collections.singletonMap("document", json);
            }
            else {
                values = Collections.singletonMap(
                        "document",
                        JSONUtil.put(
                                "viewableBy", "Anyone"
                        ).toString());
            }

            Document document = null;

            if (documentFolderId != null) {
                Page<Document> documentsPage =
                        documentResource.getDocumentFolderDocumentsPage(
                                documentFolderId, false, null, null,
                                documentResource.toFilter(
                                        StringBundler.concat("title eq '", fileName, "'")),
                                null, null);

                Document existingDocument = documentsPage.fetchFirstItem();

                if (existingDocument == null) {
                    document = documentResource.postDocumentFolderDocument(
                            documentFolderId,
                            MultipartBody.of(
                                    Collections.singletonMap(
                                            "file",
                                            new BinaryFile(
                                                    MimeTypesUtil.getContentType(fileName),
                                                    fileName, urlConnection.getInputStream(),
                                                    urlConnection.getContentLength())),
                                    __ -> _objectMapper, values));
                }
                else {
                    document = documentResource.putDocument(
                            existingDocument.getId(),
                            MultipartBody.of(
                                    Collections.singletonMap(
                                            "file",
                                            new BinaryFile(
                                                    MimeTypesUtil.getContentType(fileName),
                                                    fileName, urlConnection.getInputStream(),
                                                    urlConnection.getContentLength())),
                                    __ -> _objectMapper, values));
                }
            }
            else {
                Page<Document> documentsPage =
                        documentResource.getSiteDocumentsPage(
                                groupId, false, null, null,
                                documentResource.toFilter(
                                        StringBundler.concat("title eq '", fileName, "'")),
                                null, null);

                Document existingDocument = documentsPage.fetchFirstItem();

                if (existingDocument == null) {
                    document = documentResource.postSiteDocument(
                            groupId,
                            MultipartBody.of(
                                    Collections.singletonMap(
                                            "file",
                                            new BinaryFile(
                                                    MimeTypesUtil.getContentType(fileName),
                                                    fileName, urlConnection.getInputStream(),
                                                    urlConnection.getContentLength())),
                                    __ -> _objectMapper, values));
                }
                else {
                    document = documentResource.putDocument(
                            existingDocument.getId(),
                            MultipartBody.of(
                                    Collections.singletonMap(
                                            "file",
                                            new BinaryFile(
                                                    MimeTypesUtil.getContentType(fileName),
                                                    fileName, urlConnection.getInputStream(),
                                                    urlConnection.getContentLength())),
                                    __ -> _objectMapper, values));
                }
            }

            String key = resourcePath;

            FileEntry fileEntry = DLAppLocalServiceUtil.getFileEntry(
                    document.getId());

            stringUtilReplaceValues.put(
                    "DOCUMENT_FILE_ENTRY_ID:" + key,
                    String.valueOf(fileEntry.getFileEntryId()));

            JSONObject jsonObject = _jsonFactory.createJSONObject(
                    _jsonFactory.looseSerialize(fileEntry));

            jsonObject.put("alt", StringPool.BLANK);

            stringUtilReplaceValues.put(
                    "DOCUMENT_JSON:" + key, jsonObject.toString());

            stringUtilReplaceValues.put(
                    "DOCUMENT_URL:" + key,
                    _dlURLHelper.getPreviewURL(
                            fileEntry, fileEntry.getFileVersion(), null,
                            StringPool.BLANK, false, false));

            long fileEntryTypeId = 0;

            if (fileEntry.getModel() instanceof DLFileEntry) {
                DLFileEntry dlFileEntry = (DLFileEntry)fileEntry.getModel();

                DLFileEntryType dlFileEntryType =
                        dlFileEntry.getDLFileEntryType();

                fileEntryTypeId = dlFileEntryType.getFileEntryTypeId();
            }

            String fileEntryTypeIdString = String.valueOf(fileEntryTypeId);

            siteNavigationMenuItemSettingsBuilder.put(
                    key,
                    new SiteNavigationMenuItemSetting() {
                        {
                            className = FileEntry.class.getName();
                            classPK = String.valueOf(fileEntry.getFileEntryId());
                            classTypeId = fileEntryTypeIdString;
                            title = fileEntry.getTitle();
                            type = ResourceActionsUtil.getModelResource(
                                    serviceContext.getLocale(),
                                    FileEntry.class.getName());
                        }
                    });
        }
    }

    protected void _addOrUpdateDocuments(
            ServiceContext serviceContext,
            SiteNavigationMenuItemSettingsBuilder
                    siteNavigationMenuItemSettingsBuilder,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        Group group = _groupLocalService.getCompanyGroup(
                serviceContext.getCompanyId());

        _addOrUpdateDocuments(
                null, group.getGroupId(), "/site-initializer/documents/company",
                serviceContext, siteNavigationMenuItemSettingsBuilder,
                stringUtilReplaceValues);

        _addOrUpdateDocuments(
                null, serviceContext.getScopeGroupId(),
                "/site-initializer/documents/group", serviceContext,
                siteNavigationMenuItemSettingsBuilder, stringUtilReplaceValues);
    }

    protected void _addOrUpdateExpandoColumns(ServiceContext serviceContext)
            throws Exception {

        String json = SiteInitializerUtil.read(
                "/site-initializer/expando-columns.json", _servletContext);

        if (json == null) {
            return;
        }

        JSONArray jsonArray = _jsonFactory.createJSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            ExpandoBridge expandoBridge =
                    ExpandoBridgeFactoryUtil.getExpandoBridge(
                            serviceContext.getCompanyId(),
                            jsonObject.getString("modelResource"));

            if (expandoBridge == null) {
                continue;
            }

            if (expandoBridge.getAttribute(jsonObject.getString("name")) !=
                    null) {

                expandoBridge.setAttributeDefault(
                        jsonObject.getString("name"),
                        _getExpandoAttributeValue(jsonObject));
            }
            else {
                expandoBridge.addAttribute(
                        jsonObject.getString("name"), jsonObject.getInt("dataType"),
                        _getExpandoAttributeValue(jsonObject));
            }

            if (jsonObject.has("properties")) {
                UnicodeProperties unicodeProperties = new UnicodeProperties(
                        true);

                JSONObject propertiesJSONObject = jsonObject.getJSONObject(
                        "properties");

                Map<String, Object> map = propertiesJSONObject.toMap();

                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    unicodeProperties.setProperty(
                            TextFormatter.format(entry.getKey(), TextFormatter.K),
                            String.valueOf(entry.getValue()));
                }

                expandoBridge.setAttributeProperties(
                        jsonObject.getString("name"), unicodeProperties);
            }
        }
    }

    protected void _addOrUpdateJournalArticles(
            Long documentFolderId, String parentResourcePath,
            ServiceContext serviceContext,
            SiteNavigationMenuItemSettingsBuilder
                    siteNavigationMenuItemSettingsBuilder,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        Set<String> resourcePaths = _servletContext.getResourcePaths(
                parentResourcePath);

        if (SetUtil.isEmpty(resourcePaths)) {
            return;
        }

        for (String resourcePath : resourcePaths) {
            parentResourcePath = resourcePath.substring(
                    0, resourcePath.length() - 1);

            if (resourcePath.endsWith("/")) {
                _addOrUpdateJournalArticles(
                        _addOrUpdateStructuredContentFolders(
                                documentFolderId, parentResourcePath, serviceContext),
                        resourcePath, serviceContext,
                        siteNavigationMenuItemSettingsBuilder,
                        stringUtilReplaceValues);

                continue;
            }

            if (resourcePath.endsWith(".gitkeep") ||
                    resourcePath.endsWith(".metadata.json") ||
                    resourcePath.endsWith(".xml")) {

                continue;
            }

            long journalFolderId =
                    JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID;

            if (documentFolderId != null) {
                journalFolderId = documentFolderId;
            }

            String json = SiteInitializerUtil.read(
                    resourcePath, _servletContext);

            JSONObject jsonObject = _jsonFactory.createJSONObject(json);

            String articleId = jsonObject.getString("articleId");

            if (Validator.isNull(articleId)) {
                articleId = FileUtil.stripExtension(
                        FileUtil.getShortFileName(resourcePath));
            }

            Map<Locale, String> titleMap = Collections.singletonMap(
                    LocaleUtil.getSiteDefault(), jsonObject.getString("name"));

            String ddmStructureKey = jsonObject.getString("ddmStructureKey");

            DDMStructure ddmStructure = _ddmStructureLocalService.getStructure(
                    serviceContext.getScopeGroupId(),
                    _portal.getClassNameId(JournalArticle.class), ddmStructureKey,
                    true);

            Calendar calendar = CalendarFactoryUtil.getCalendar(
                    serviceContext.getTimeZone());

            serviceContext.setAssetCategoryIds(
                    _getAssetCategoryIds(
                            serviceContext.getScopeGroupId(),
                            JSONUtil.toStringArray(
                                    jsonObject.getJSONArray("assetCategoryERCs"))));
            serviceContext.setAssetTagNames(
                    JSONUtil.toStringArray(
                            jsonObject.getJSONArray("assetTagNames")));

            JournalArticle journalArticle =
                    _journalArticleLocalService.fetchArticle(
                            serviceContext.getScopeGroupId(), articleId);

            if (journalArticle == null) {
                journalArticle = _journalArticleLocalService.addArticle(
                        null, serviceContext.getUserId(),
                        serviceContext.getScopeGroupId(), journalFolderId,
                        JournalArticleConstants.CLASS_NAME_ID_DEFAULT, 0, articleId,
                        false, 1, titleMap, null, titleMap,
                        _replace(
                                SiteInitializerUtil.read(
                                        _replace(resourcePath, ".json", ".xml"),
                                        _servletContext),
                                stringUtilReplaceValues),
                        ddmStructure.getStructureId(),
                        jsonObject.getString("ddmTemplateKey"), null,
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE), 0, 0, 0, 0, 0, true, 0, 0, 0,
                        0, 0, true, true, false, 0, 0, null, null, null, null,
                        serviceContext);
            }
            else {
                journalArticle = _journalArticleLocalService.updateArticle(
                        serviceContext.getUserId(),
                        serviceContext.getScopeGroupId(), journalFolderId,
                        articleId, journalArticle.getVersion(), titleMap, null,
                        titleMap,
                        _replace(
                                SiteInitializerUtil.read(
                                        _replace(resourcePath, ".json", ".xml"),
                                        _servletContext),
                                stringUtilReplaceValues),
                        jsonObject.getString("ddmTemplateKey"), null,
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE), 0, 0, 0, 0, 0, true, 0, 0, 0,
                        0, 0, true, true, false, 0, 0, null, null, null, null,
                        serviceContext);
            }

            JournalArticle finalJournalArticle = journalArticle;

            serviceContext.setAssetCategoryIds(null);
            serviceContext.setAssetTagNames(null);

            siteNavigationMenuItemSettingsBuilder.put(
                    resourcePath,
                    new SiteNavigationMenuItemSetting() {
                        {
                            className = JournalArticle.class.getName();
                            classPK = String.valueOf(
                                    finalJournalArticle.getResourcePrimKey());
                            classTypeId = String.valueOf(
                                    ddmStructure.getStructureId());
                            title = finalJournalArticle.getTitle(
                                    serviceContext.getLocale());
                            type = ResourceActionsUtil.getModelResource(
                                    serviceContext.getLocale(),
                                    JournalArticle.class.getName());
                        }
                    });
        }
    }

    protected void _addOrUpdateJournalArticles(
            ServiceContext serviceContext,
            SiteNavigationMenuItemSettingsBuilder
                    siteNavigationMenuItemSettingsBuilder,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        _addOrUpdateJournalArticles(
                null, "/site-initializer/journal-articles", serviceContext,
                siteNavigationMenuItemSettingsBuilder, stringUtilReplaceValues);
    }

    protected KnowledgeBaseArticle _addOrUpdateKnowledgeBaseArticle(
            boolean folder, JSONObject jsonObject,
            long parentKnowledgeBaseObjectId, ServiceContext serviceContext)
            throws Exception {

        KnowledgeBaseArticleResource.Builder
                knowledgeBaseArticleResourceBuilder =
                _knowledgeBaseArticleResourceFactory.create();

        KnowledgeBaseArticleResource knowledgeBaseArticleResource =
                knowledgeBaseArticleResourceBuilder.user(
                        serviceContext.fetchUser()
                ).build();

        KnowledgeBaseArticle knowledgeBaseArticle = KnowledgeBaseArticle.toDTO(
                jsonObject.toString());

        if (!folder) {
            knowledgeBaseArticle.setParentKnowledgeBaseArticleId(
                    parentKnowledgeBaseObjectId);
        }
        else {
            knowledgeBaseArticle.setParentKnowledgeBaseFolderId(
                    parentKnowledgeBaseObjectId);
        }

        return knowledgeBaseArticleResource.
                putSiteKnowledgeBaseArticleByExternalReferenceCode(
                        serviceContext.getScopeGroupId(),
                        knowledgeBaseArticle.getExternalReferenceCode(),
                        knowledgeBaseArticle);
    }

    protected void _addOrUpdateKnowledgeBaseArticle(
            boolean folder, JSONObject jsonObject,
            long parentKnowledgeBaseObjectId, String resourcePath,
            ServiceContext serviceContext)
            throws Exception {

        KnowledgeBaseArticle knowledgeBaseArticle =
                _addOrUpdateKnowledgeBaseArticle(
                        folder, jsonObject, parentKnowledgeBaseObjectId,
                        serviceContext);

        _addOrKnowledgeBaseObjects(
                false, knowledgeBaseArticle.getId(), resourcePath, serviceContext);
    }

    protected void _addOrUpdateKnowledgeBaseArticles(
            ServiceContext serviceContext)
            throws Exception {

        _addOrKnowledgeBaseObjects(
                true, 0, "/site-initializer/knowledge-base-articles",
                serviceContext);
    }

    protected KnowledgeBaseFolder _addOrUpdateKnowledgeBaseFolder(
            JSONObject jsonObject, long parentKnowledgeBaseObjectId,
            ServiceContext serviceContext)
            throws Exception {

        KnowledgeBaseFolderResource.Builder knowledgeBaseFolderResourceBuilder =
                _knowledgeBaseFolderResourceFactory.create();

        KnowledgeBaseFolderResource knowledgeBaseFolderResource =
                knowledgeBaseFolderResourceBuilder.httpServletRequest(
                        serviceContext.getRequest()
                ).user(
                        serviceContext.fetchUser()
                ).build();

        KnowledgeBaseFolder knowledgeBaseFolder = KnowledgeBaseFolder.toDTO(
                jsonObject.toString());

        knowledgeBaseFolder.setParentKnowledgeBaseFolderId(
                parentKnowledgeBaseObjectId);

        return knowledgeBaseFolderResource.
                putSiteKnowledgeBaseFolderByExternalReferenceCode(
                        serviceContext.getScopeGroupId(),
                        knowledgeBaseFolder.getExternalReferenceCode(),
                        knowledgeBaseFolder);
    }

    protected void _addOrUpdateKnowledgeBaseFolder(
            JSONObject jsonObject, long parentKnowledgeBaseObjectId,
            String resourcePath, ServiceContext serviceContext)
            throws Exception {

        KnowledgeBaseFolder knowledgeBaseFolder =
                _addOrUpdateKnowledgeBaseFolder(
                        jsonObject, parentKnowledgeBaseObjectId, serviceContext);

        _addOrKnowledgeBaseObjects(
                true, knowledgeBaseFolder.getId(), resourcePath, serviceContext);
    }

    protected Map<String, Layout> _addOrUpdateLayout(
            long parentLayoutId, String parentResourcePath,
            ServiceContext serviceContext,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        JSONObject pageJSONObject = _jsonFactory.createJSONObject(
                _replace(
                        SiteInitializerUtil.read(
                                parentResourcePath + "page.json", _servletContext),
                        stringUtilReplaceValues));

        Map<Locale, String> nameMap = new HashMap<>(
                SiteInitializerUtil.toMap(pageJSONObject.getString("name_i18n")));

        Locale siteDefaultLocale = _portal.getSiteDefaultLocale(
                serviceContext.getScopeGroupId());

        if (!nameMap.containsKey(siteDefaultLocale)) {
            nameMap.put(siteDefaultLocale, pageJSONObject.getString("name"));
        }

        String type = StringUtil.toLowerCase(pageJSONObject.getString("type"));

        if (Objects.equals(type, "link_to_layout")) {
            type = LayoutConstants.TYPE_LINK_TO_LAYOUT;
        }
        else if (Objects.equals(type, "url")) {
            type = LayoutConstants.TYPE_URL;
        }
        else if (Objects.equals(type, "widget")) {
            type = LayoutConstants.TYPE_PORTLET;
        }

        Map<Locale, String> friendlyURLMap = new HashMap<>(
                SiteInitializerUtil.toMap(
                        pageJSONObject.getString("friendlyURL_i18n")));

        if (!friendlyURLMap.containsKey(siteDefaultLocale)) {
            friendlyURLMap.put(
                    siteDefaultLocale, pageJSONObject.getString("friendlyURL"));
        }

        UnicodeProperties unicodeProperties = new UnicodeProperties(true);

        JSONArray typeSettingsJSONArray = pageJSONObject.getJSONArray(
                "typeSettings");

        if (typeSettingsJSONArray != null) {
            for (int i = 0; i < typeSettingsJSONArray.length(); i++) {
                JSONObject propertyJSONObject =
                        typeSettingsJSONArray.getJSONObject(i);

                unicodeProperties.put(
                        propertyJSONObject.getString("key"),
                        propertyJSONObject.getString("value"));
            }
        }

        Layout layout = _layoutLocalService.fetchLayoutByFriendlyURL(
                serviceContext.getScopeGroupId(),
                pageJSONObject.getBoolean("private"),
                pageJSONObject.getString("friendlyURL"));

        if ((layout != null) && !Objects.equals(layout.getType(), type)) {
            _layoutLocalService.deleteLayout(layout);

            layout = null;
        }

        if (layout != null) {
            _layoutLocalService.updateLayout(
                    serviceContext.getScopeGroupId(), layout.isPrivateLayout(),
                    layout.getLayoutId(), parentLayoutId, nameMap,
                    SiteInitializerUtil.toMap(
                            pageJSONObject.getString("title_i18n")),
                    SiteInitializerUtil.toMap(
                            pageJSONObject.getString("description_i18n")),
                    SiteInitializerUtil.toMap(
                            pageJSONObject.getString("keywords_i18n")),
                    SiteInitializerUtil.toMap(
                            pageJSONObject.getString("robots_i18n")),
                    type, pageJSONObject.getBoolean("hidden"),
                    layout.getFriendlyURLMap(), layout.getIconImage(), null,
                    layout.getStyleBookEntryId(),
                    pageJSONObject.getLong("faviconFileEntryId"),
                    layout.getMasterLayoutPlid(), serviceContext);
            _layoutLocalService.updateLayout(
                    serviceContext.getScopeGroupId(), layout.isPrivateLayout(),
                    layout.getLayoutId(), unicodeProperties.toString());
        }
        else {
            layout = _layoutLocalService.addLayout(
                    serviceContext.getUserId(), serviceContext.getScopeGroupId(),
                    pageJSONObject.getBoolean("private"), parentLayoutId, nameMap,
                    SiteInitializerUtil.toMap(
                            pageJSONObject.getString("title_i18n")),
                    SiteInitializerUtil.toMap(
                            pageJSONObject.getString("description_i18n")),
                    SiteInitializerUtil.toMap(
                            pageJSONObject.getString("keywords_i18n")),
                    SiteInitializerUtil.toMap(
                            pageJSONObject.getString("robots_i18n")),
                    type, unicodeProperties.toString(),
                    pageJSONObject.getBoolean("hidden"),
                    pageJSONObject.getBoolean("system"), friendlyURLMap,
                    serviceContext);
        }

        _setResourcePermissions(
                layout.getCompanyId(), layout.getModelClassName(),
                pageJSONObject.getJSONArray("permissions"),
                String.valueOf(layout.getPlid()));

        if (pageJSONObject.has("priority")) {
            layout = _layoutLocalService.updatePriority(
                    layout.getPlid(), pageJSONObject.getInt("priority"));
        }

        stringUtilReplaceValues.put(
                "LAYOUT_ID:" + layout.getName(LocaleUtil.getSiteDefault()),
                String.valueOf(layout.getLayoutId()));

        Map<String, Layout> layoutsMap = HashMapBuilder.<String, Layout>put(
                parentResourcePath, layout
        ).build();

        String layoutTemplateId = StringUtil.toLowerCase(
                pageJSONObject.getString("layoutTemplateId"));

        if (Validator.isNotNull(layoutTemplateId)) {
            LayoutTypePortlet layoutTypePortlet =
                    (LayoutTypePortlet)layout.getLayoutType();

            layoutTypePortlet.setLayoutTemplateId(0, layoutTemplateId, false);
        }

        Set<String> resourcePaths = _servletContext.getResourcePaths(
                parentResourcePath);

        if (SetUtil.isEmpty(resourcePaths)) {
            return layoutsMap;
        }

        Set<String> sortedResourcePaths = new TreeSet<>(
                new NaturalOrderStringComparator());

        sortedResourcePaths.addAll(resourcePaths);

        resourcePaths = sortedResourcePaths;

        for (String resourcePath : resourcePaths) {
            if (resourcePath.endsWith("/")) {
                layoutsMap.putAll(
                        _addOrUpdateLayout(
                                layout.getLayoutId(), resourcePath, serviceContext,
                                stringUtilReplaceValues));
            }
        }

        return layoutsMap;
    }

    protected void _addOrUpdateLayoutContent(
            Layout layout, String resourcePath, long segmentsExperienceId,
            ServiceContext serviceContext,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        JSONObject pageJSONObject = _jsonFactory.createJSONObject(
                SiteInitializerUtil.read(
                        resourcePath + "page.json", _servletContext));

        String type = StringUtil.toLowerCase(pageJSONObject.getString("type"));

        if (Objects.equals(type, "url")) {
            return;
        }
        else if (Objects.equals(type, "widget")) {
            type = LayoutConstants.TYPE_PORTLET;
        }

        String json = SiteInitializerUtil.read(
                resourcePath + "page-definition.json", _servletContext);

        if (json == null) {
            return;
        }

        json = _replace(
                _replace(json, serviceContext), stringUtilReplaceValues);

        JSONObject pageDefinitionJSONObject = _jsonFactory.createJSONObject(
                json);

        Layout draftLayout = layout.fetchDraftLayout();

        if (Objects.equals(type, LayoutConstants.TYPE_COLLECTION) ||
                Objects.equals(type, LayoutConstants.TYPE_CONTENT)) { // ||
                //Objects.equals(type, LayoutConstants.TYPE_UTILITY)) {
                //TODO: looks like UTILITY is not part of the Q1 2024 release?

            JSONObject pageElementJSONObject =
                    pageDefinitionJSONObject.getJSONObject("pageElement");

            if ((pageElementJSONObject != null) &&
                    Objects.equals(
                            pageElementJSONObject.getString("type"), "Root")) {

                JSONArray jsonArray = pageElementJSONObject.getJSONArray(
                        "pageElements");

                if (!JSONUtil.isEmpty(jsonArray)) {
                    LayoutPageTemplateStructure layoutPageTemplateStructure =
                            _layoutPageTemplateStructureLocalService.
                                    fetchLayoutPageTemplateStructure(
                                            draftLayout.getGroupId(),
                                            draftLayout.getPlid());

                    LayoutStructure layoutStructure = new LayoutStructure();

                    layoutStructure.addRootLayoutStructureItem();

                    if (segmentsExperienceId == 0) {
                        segmentsExperienceId =
                                _segmentsExperienceLocalService.
                                        fetchDefaultSegmentsExperienceId(
                                                draftLayout.getPlid());
                    }

                    if (Validator.isNull(
                            layoutPageTemplateStructure.getData(
                                    segmentsExperienceId))) {

                        _layoutPageTemplateStructureRelLocalService.
                                addLayoutPageTemplateStructureRel(
                                        serviceContext.getUserId(),
                                        serviceContext.getScopeGroupId(),
                                        layoutPageTemplateStructure.
                                                getLayoutPageTemplateStructureId(),
                                        segmentsExperienceId,
                                        layoutStructure.toString(), serviceContext);
                    }
                    else {
                        _layoutPageTemplateStructureRelLocalService.
                                updateLayoutPageTemplateStructureRel(
                                        layoutPageTemplateStructure.
                                                getLayoutPageTemplateStructureId(),
                                        segmentsExperienceId,
                                        layoutStructure.toString());
                        _portletPreferencesLocalService.
                                deletePortletPreferences(
                                        0, PortletKeys.PREFS_OWNER_TYPE_LAYOUT,
                                        draftLayout.getPlid());
                    }

                    for (int i = 0; i < jsonArray.length(); i++) {
                        _layoutsImporter.importPageElement(
                                draftLayout, layoutStructure,
                                layoutStructure.getMainItemId(),
                                jsonArray.getString(i), i, true,
                                segmentsExperienceId);
                    }
                }
            }
        }

        if (Objects.equals(type, LayoutConstants.TYPE_COLLECTION)) {
            UnicodeProperties unicodeProperties =
                    draftLayout.getTypeSettingsProperties();

            Object[] typeSettings = JSONUtil.toObjectArray(
                    pageJSONObject.getJSONArray("typeSettings"));

            for (Object typeSetting : typeSettings) {
                JSONObject typeSettingJSONObject = (JSONObject)typeSetting;

                String key = typeSettingJSONObject.getString("key");
                String value = typeSettingJSONObject.getString("value");

                unicodeProperties.put(
                        key, _replace(value, stringUtilReplaceValues));
            }

            draftLayout = _layoutLocalService.updateLayout(
                    serviceContext.getScopeGroupId(), draftLayout.isPrivateLayout(),
                    draftLayout.getLayoutId(), unicodeProperties.toString());
        }

        if (Objects.equals(type, LayoutConstants.TYPE_COLLECTION) ||
                Objects.equals(type, LayoutConstants.TYPE_CONTENT)) { //||
                //Objects.equals(type, LayoutConstants.TYPE_UTILITY)) {
                // TODO: Looks like UTILITY is not part fo the 2024 Q1 release

            JSONObject settingsJSONObject =
                    pageDefinitionJSONObject.getJSONObject("settings");

            if (settingsJSONObject != null) {
                draftLayout = _updateDraftLayout(
                        draftLayout, settingsJSONObject);
            }

            layout = _layoutCopyHelper.copyLayoutContent(draftLayout, layout);

            _layoutLocalService.updateStatus(
                    layout.getUserId(), draftLayout.getPlid(),
                    WorkflowConstants.STATUS_APPROVED, serviceContext);
            _layoutLocalService.updateStatus(
                    layout.getUserId(), layout.getPlid(),
                    WorkflowConstants.STATUS_APPROVED, serviceContext);
        }
    }

    protected Map<String, Layout> _addOrUpdateLayouts(
            ServiceContext serviceContext,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        Set<String> resourcePaths = _servletContext.getResourcePaths(
                "/site-initializer/layouts");

        if (SetUtil.isEmpty(resourcePaths)) {
            return new HashMap<>();
        }

        Map<String, Layout> layoutsMap = new HashMap<>();

        List<Layout> layouts = _layoutLocalService.getLayouts(
                serviceContext.getScopeGroupId(), QueryUtil.ALL_POS,
                QueryUtil.ALL_POS, null);

        for (Layout layout : layouts) {
            stringUtilReplaceValues.put(
                    "LAYOUT_ID:" + layout.getName(LocaleUtil.getSiteDefault()),
                    String.valueOf(layout.getLayoutId()));
        }

        Set<String> sortedResourcePaths = new TreeSet<>(
                new NaturalOrderStringComparator());

        sortedResourcePaths.addAll(resourcePaths);

        resourcePaths = sortedResourcePaths;

        for (String resourcePath : resourcePaths) {
            if (resourcePath.endsWith("/")) {
                layoutsMap.putAll(
                        _addOrUpdateLayout(
                                LayoutConstants.DEFAULT_PARENT_LAYOUT_ID, resourcePath,
                                serviceContext, stringUtilReplaceValues));
            }
        }

        return layoutsMap;
    }

    protected void _addOrUpdateLayoutsContent(
            Map<String, Layout> layouts, ServiceContext serviceContext,
            Map<String, SiteNavigationMenuItemSetting>
                    siteNavigationMenuItemSettings,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        for (Map.Entry<String, Layout> entry : layouts.entrySet()) {
            _addOrUpdateLayoutContent(
                    entry.getValue(), entry.getKey(), 0, serviceContext,
                    stringUtilReplaceValues);
        }

        _addOrUpdateSiteNavigationMenus(
                serviceContext, siteNavigationMenuItemSettings,
                stringUtilReplaceValues);
    }

    protected void _addOrUpdateListTypeDefinitions(
            ServiceContext serviceContext,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        Set<String> resourcePaths = _servletContext.getResourcePaths(
                "/site-initializer/list-type-definitions");

        if (SetUtil.isEmpty(resourcePaths)) {
            return;
        }

        ListTypeDefinitionResource.Builder listTypeDefinitionResourceBuilder =
                _listTypeDefinitionResourceFactory.create();

        ListTypeDefinitionResource listTypeDefinitionResource =
                listTypeDefinitionResourceBuilder.user(
                        serviceContext.fetchUser()
                ).build();

        for (String resourcePath : resourcePaths) {
            if (resourcePath.endsWith(".list-type-entries.json")) {
                continue;
            }

            String json = SiteInitializerUtil.read(
                    resourcePath, _servletContext);

            ListTypeDefinition listTypeDefinition = ListTypeDefinition.toDTO(
                    json);

            if (listTypeDefinition == null) {
                _log.error(
                        "Unable to transform list type definition from JSON: " +
                                json);

                continue;
            }

            ListTypeDefinition existingListTypeDefinition =
                    listTypeDefinitionResource.
                            getListTypeDefinitionByExternalReferenceCode(
                                    listTypeDefinition.getExternalReferenceCode());

            if (existingListTypeDefinition == null) {
                listTypeDefinition =
                        listTypeDefinitionResource.postListTypeDefinition(
                                listTypeDefinition);
            }
            else {
                listTypeDefinition =
                        listTypeDefinitionResource.patchListTypeDefinition(
                                existingListTypeDefinition.getId(), listTypeDefinition);
            }

            stringUtilReplaceValues.put(
                    "LIST_TYPE_DEFINITION_ID:" + listTypeDefinition.getName(),
                    String.valueOf(listTypeDefinition.getId()));

            String listTypeEntriesJSON = SiteInitializerUtil.read(
                    _replace(resourcePath, ".json", ".list-type-entries.json"),
                    _servletContext);

            if (listTypeEntriesJSON == null) {
                continue;
            }

            JSONArray jsonArray = _jsonFactory.createJSONArray(
                    listTypeEntriesJSON);

            ListTypeEntryResource.Builder listTypeEntryResourceBuilder =
                    _listTypeEntryResourceFactory.create();

            ListTypeEntryResource listTypeEntryResource =
                    listTypeEntryResourceBuilder.user(
                            serviceContext.fetchUser()
                    ).build();

            for (int i = 0; i < jsonArray.length(); i++) {
                ListTypeEntry listTypeEntry = ListTypeEntry.toDTO(
                        String.valueOf(jsonArray.getJSONObject(i)));

                com.liferay.list.type.model.ListTypeEntry
                        serviceBuilderListTypeEntry =
                        _listTypeEntryLocalService.fetchListTypeEntry(
                                listTypeDefinition.getId(), listTypeEntry.getKey());

                if (serviceBuilderListTypeEntry == null) {
                    listTypeEntryResource.postListTypeDefinitionListTypeEntry(
                            listTypeDefinition.getId(), listTypeEntry);
                }
                else {
                    listTypeEntryResource.putListTypeEntry(
                            serviceBuilderListTypeEntry.getListTypeEntryId(),
                            listTypeEntry);
                }
            }
        }
    }

    protected void _addOrUpdateNotificationTemplate(
            String resourcePath, ServiceContext serviceContext,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        String json = SiteInitializerUtil.read(
                resourcePath + "notification-template.json", _servletContext);

        if (json == null) {
            return;
        }

        JSONObject bodyJSONObject = _jsonFactory.createJSONObject();

        Enumeration<URL> enumeration = _bundle.findEntries(
                resourcePath, "*.html", true);

        if (enumeration == null) {
            return;
        }

        while (enumeration.hasMoreElements()) {
            URL url = enumeration.nextElement();

            bodyJSONObject.put(
                    FileUtil.getShortFileName(
                            FileUtil.stripExtension(url.getPath())),
                    _replace(
                            _replace(URLUtil.toString(url), serviceContext),
                            stringUtilReplaceValues));
        }

        JSONObject notificationTemplateJSONObject =
                _jsonFactory.createJSONObject(json);

        notificationTemplateJSONObject.put("body", bodyJSONObject);

        NotificationTemplate notificationTemplate = NotificationTemplate.toDTO(
                notificationTemplateJSONObject.toString());

        NotificationTemplateResource.Builder
                notificationTemplateResourceBuilder =
                _notificationTemplateResourceFactory.create();

        NotificationTemplateResource notificationTemplateResource =
                notificationTemplateResourceBuilder.user(
                        serviceContext.fetchUser()
                ).build();

        NotificationTemplate existingNotificationTemplate =
                notificationTemplateResource.
                        getNotificationTemplateByExternalReferenceCode(
                                notificationTemplate.getExternalReferenceCode());

        if (existingNotificationTemplate == null) {
            notificationTemplate =
                    notificationTemplateResource.postNotificationTemplate(
                            notificationTemplate);
        }
        else {
            notificationTemplate =
                    notificationTemplateResource.
                            putNotificationTemplateByExternalReferenceCode(
                                    notificationTemplate.getExternalReferenceCode(),
                                    notificationTemplate);
        }

        json = SiteInitializerUtil.read(
                resourcePath + "notification-template.object-actions.json",
                _servletContext);

        if (json == null) {
            return;
        }

        json = _replace(json, stringUtilReplaceValues);

        JSONArray jsonArray = _jsonFactory.createJSONArray(json);

        Map<String, Long> parametersMap = HashMapBuilder.put(
                "notificationTemplateId", notificationTemplate.getId()
        ).build();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            _objectActionLocalService.addOrUpdateObjectAction(
                    jsonObject.getString("externalReferenceCode"), 0,
                    serviceContext.getUserId(),
                    jsonObject.getLong("objectDefinitionId"),
                    jsonObject.getBoolean("active"),
                    jsonObject.getString("conditionExpression"),
                    jsonObject.getString("description"),
                    SiteInitializerUtil.toMap(jsonObject.getString("errorMessage")),
                    SiteInitializerUtil.toMap(jsonObject.getString("label")),
                    jsonObject.getString("name"),
                    jsonObject.getString("objectActionExecutorKey"),
                    jsonObject.getString("objectActionTriggerKey"),
                    ObjectActionUtil.toParametersUnicodeProperties(parametersMap),
                    jsonObject.getBoolean("system"));
        }
    }

    protected void _addOrUpdateNotificationTemplates(
            ServiceContext serviceContext,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        Set<String> resourcePaths = _servletContext.getResourcePaths(
                "/site-initializer/notification-templates");

        if (SetUtil.isEmpty(resourcePaths)) {
            return;
        }

        for (String resourcePath : resourcePaths) {
            _addOrUpdateNotificationTemplate(
                    resourcePath, serviceContext, stringUtilReplaceValues);
        }
    }

    protected void _addOrUpdateObjectEntries(
            ServiceContext serviceContext,
            SiteNavigationMenuItemSettingsBuilder
                    siteNavigationMenuItemSettingsBuilder,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        Set<String> resourcePaths = _servletContext.getResourcePaths(
                "/site-initializer/object-entries");

        if (SetUtil.isEmpty(resourcePaths)) {
            return;
        }

        Set<String> sortedResourcePaths = new TreeSet<>(
                new NaturalOrderStringComparator());

        sortedResourcePaths.addAll(resourcePaths);

        resourcePaths = sortedResourcePaths;

        for (String resourcePath : resourcePaths) {
            String json = SiteInitializerUtil.read(
                    resourcePath, _servletContext);

            if (json == null) {
                continue;
            }

            json = _replace(json, stringUtilReplaceValues);

            JSONObject jsonObject = _jsonFactory.createJSONObject(json);

            com.liferay.object.model.ObjectDefinition objectDefinition =
                    _objectDefinitionLocalService.fetchObjectDefinition(
                            serviceContext.getCompanyId(),
                            "C_" + jsonObject.getString("objectDefinitionName"));

            if (objectDefinition == null) {
                continue;
            }

            JSONArray jsonArray = jsonObject.getJSONArray("object-entries");

            if (JSONUtil.isEmpty(jsonArray)) {
                continue;
            }

            DefaultDTOConverterContext defaultDTOConverterContext =
                    new DefaultDTOConverterContext(
                            false, null, null, null, null, LocaleUtil.getSiteDefault(),
                            null, serviceContext.fetchUser());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objectEntryJSONObject = jsonArray.getJSONObject(i);

                String externalReferenceCode = objectEntryJSONObject.getString(
                        "externalReferenceCode");

                ObjectEntry objectEntry = ObjectEntry.toDTO(
                        JSONUtil.toString(objectEntryJSONObject));

                objectEntry = _objectEntryManager.updateObjectEntry(
                        serviceContext.getCompanyId(), defaultDTOConverterContext,
                        externalReferenceCode, objectDefinition, objectEntry,
                        String.valueOf(serviceContext.getScopeGroupId()));

                if (Validator.isNotNull(externalReferenceCode)) {
                    stringUtilReplaceValues.put(
                            StringBundler.concat(
                                    objectDefinition.getShortName(), "#",
                                    externalReferenceCode),
                            String.valueOf(objectEntry.getId()));
                }

                String objectEntrySiteInitializerKey =
                        objectEntryJSONObject.getString(
                                "objectEntrySiteInitializerKey");

                if (Validator.isNull(objectEntrySiteInitializerKey)) {
                    continue;
                }

                com.liferay.object.model.ObjectEntry serviceBuilderObjectEntry =
                        _objectEntryLocalService.getObjectEntry(
                                objectEntry.getId());

                siteNavigationMenuItemSettingsBuilder.put(
                        objectEntrySiteInitializerKey,
                        new SiteNavigationMenuItemSetting() {
                            {
                                className =
                                        serviceBuilderObjectEntry.getModelClassName();
                                classPK = String.valueOf(
                                        serviceBuilderObjectEntry.getObjectEntryId());
                                title = StringBundler.concat(
                                        objectDefinition.getName(), StringPool.SPACE,
                                        serviceBuilderObjectEntry.getObjectEntryId());
                            }
                        });
            }
        }
    }

    protected void _addOrUpdateObjectFields(
            ServiceContext serviceContext,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        Set<String> resourcePaths = _servletContext.getResourcePaths(
                "/site-initializer/object-fields");

        if (SetUtil.isEmpty(resourcePaths)) {
            return;
        }

        ObjectFieldResource.Builder objectFieldResourceBuilder =
                _objectFieldResourceFactory.create();

        ObjectFieldResource objectFieldResource =
                objectFieldResourceBuilder.user(
                        serviceContext.fetchUser()
                ).build();

        for (String resourcePath : resourcePaths) {
            String json = SiteInitializerUtil.read(
                    resourcePath, _servletContext);

            json = _replace(json, stringUtilReplaceValues);

            JSONObject jsonObject = _jsonFactory.createJSONObject(json);

            JSONArray jsonArray = jsonObject.getJSONArray("object-fields");

            if (JSONUtil.isEmpty(jsonArray)) {
                continue;
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objectFieldJSONObject = jsonArray.getJSONObject(i);

                ObjectField objectField = ObjectField.toDTO(
                        JSONUtil.toString(objectFieldJSONObject));

                if (objectField == null) {
                    _log.error(
                            "Unable to transform object field from JSON: " + json);

                    continue;
                }

                com.liferay.object.model.ObjectField existingObjectField =
                        _objectFieldLocalService.fetchObjectField(
                                jsonObject.getLong("objectDefinitionId"),
                                objectField.getName());

                if (existingObjectField == null) {
                    objectFieldResource.postObjectDefinitionObjectField(
                            jsonObject.getLong("objectDefinitionId"), objectField);
                }
                else {
                    objectFieldResource.putObjectField(
                            existingObjectField.getObjectFieldId(), objectField);
                }
            }
        }
    }

    protected void _addOrUpdateObjectRelationships(
            ServiceContext serviceContext,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        Set<String> resourcePaths = _servletContext.getResourcePaths(
                "/site-initializer/object-relationships");

        if (SetUtil.isEmpty(resourcePaths)) {
            return;
        }

        ObjectRelationshipResource.Builder objectRelationshipResourceBuilder =
                _objectRelationshipResourceFactory.create();

        ObjectRelationshipResource objectRelationshipResource =
                objectRelationshipResourceBuilder.user(
                        serviceContext.fetchUser()
                ).build();

        for (String resourcePath : resourcePaths) {
            String json = SiteInitializerUtil.read(
                    resourcePath, _servletContext);

            json = _replace(json, stringUtilReplaceValues);

            ObjectRelationship objectRelationship = ObjectRelationship.toDTO(
                    json);

            if (objectRelationship == null) {
                _log.error(
                        "Unable to transform object relationship from JSON: " +
                                json);

                continue;
            }

            com.liferay.object.model.ObjectRelationship
                    existingObjectRelationship =
                    _objectRelationshipLocalService.
                            fetchObjectRelationshipByObjectDefinitionId1(
                                    objectRelationship.getObjectDefinitionId1(),
                                    objectRelationship.getName());

            if (existingObjectRelationship == null) {
                objectRelationshipResource.
                        postObjectDefinitionObjectRelationship(
                                objectRelationship.getObjectDefinitionId1(),
                                objectRelationship);
            }
            else {
                objectRelationshipResource.putObjectRelationship(
                        existingObjectRelationship.getObjectRelationshipId(),
                        objectRelationship);
            }
        }
    }

    protected void _addOrUpdateOrganization(
            JSONObject jsonObject, Organization parentOrganization,
            ServiceContext serviceContext)
            throws Exception {

        Organization organization = Organization.toDTO(jsonObject.toString());

        if (organization == null) {
            _log.error(
                    "Unable to transform organization from JSON: " + jsonObject);

            return;
        }

        organization.setParentOrganization(parentOrganization);

        OrganizationResource.Builder organizationResourceBuilder =
                _organizationResourceFactory.create();

        OrganizationResource organizationResource =
                organizationResourceBuilder.user(
                        serviceContext.fetchUser()
                ).httpServletRequest(
                        serviceContext.getRequest()
                ).build();

        organization =
                organizationResource.putOrganizationByExternalReferenceCode(
                        organization.getExternalReferenceCode(), organization);

        JSONArray jsonArray = jsonObject.getJSONArray("childOrganizations");

        if (JSONUtil.isEmpty(jsonArray)) {
            return;
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            _addOrUpdateOrganization(
                    jsonArray.getJSONObject(i), organization, serviceContext);
        }
    }

    protected void _addOrUpdateOrganizations(ServiceContext serviceContext)
            throws Exception {

        String json = SiteInitializerUtil.read(
                "/site-initializer/organizations.json", _servletContext);

        if (json == null) {
            return;
        }

        JSONArray jsonArray = JSONFactoryUtil.createJSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            _addOrUpdateOrganization(
                    jsonArray.getJSONObject(i), null, serviceContext);
        }
    }

    protected void _addOrUpdateResourcePermissions(
            ServiceContext serviceContext,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        String json = SiteInitializerUtil.read(
                "/site-initializer/resource-permissions.json", _servletContext);

        if (json == null) {
            return;
        }

        List<LayoutPageTemplateEntry> layoutPageTemplateEntries =
                _layoutPageTemplateEntryLocalService.getLayoutPageTemplateEntries(
                        serviceContext.getScopeGroupId());

        for (LayoutPageTemplateEntry layoutPageTemplateEntry :
                layoutPageTemplateEntries) {

            stringUtilReplaceValues.put(
                    "LAYOUT_PAGE_TEMPLATE_ENTRY_ID:" +
                            layoutPageTemplateEntry.getName(),
                    String.valueOf(
                            layoutPageTemplateEntry.getLayoutPageTemplateEntryId()));
        }

        JSONArray jsonArray = _jsonFactory.createJSONArray(
                _replace(json, stringUtilReplaceValues));

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String[] actionIds = ArrayUtil.toStringArray(
                    jsonObject.getJSONArray("actionIds"));
            String[] resourceActionIds = TransformUtil.transformToArray(
                    _resourceActionLocalService.getResourceActions(
                            jsonObject.getString("resourceName")),
                    ResourceAction -> ResourceAction.getActionId(), String.class);

            if (!ArrayUtil.containsAll(resourceActionIds, actionIds)) {
                if (_log.isWarnEnabled()) {
                    _log.warn(
                            StringBundler.concat(
                                    "No resource action found with resourceName ",
                                    jsonObject.getString("resourceName"),
                                    " with the actionIds: ",
                                    ArrayUtil.toString(actionIds, "")));
                }

                continue;
            }

            Role role = _roleLocalService.fetchRole(
                    serviceContext.getCompanyId(),
                    jsonObject.getString("roleName"));

            if (role == null) {
                if (_log.isWarnEnabled()) {
                    _log.warn(
                            "No role found with name " +
                                    jsonObject.getString("roleName"));
                }

                continue;
            }

            int scope = jsonObject.getInt("scope");

            if (scope == ResourceConstants.SCOPE_COMPANY) {
                jsonObject.put(
                        "primKey", String.valueOf(serviceContext.getCompanyId()));
            }
            else if (scope == ResourceConstants.SCOPE_GROUP) {
                jsonObject.put(
                        "primKey",
                        String.valueOf(serviceContext.getScopeGroupId()));
            }

            _resourcePermissionLocalService.setResourcePermissions(
                    serviceContext.getCompanyId(),
                    jsonObject.getString("resourceName"), scope,
                    jsonObject.getString("primKey"), role.getRoleId(), actionIds);
        }
    }

    protected void _addOrUpdateRoles(
            ServiceContext serviceContext,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        List<Role> roles = _roleLocalService.getRoles(
                serviceContext.getCompanyId());

        for (Role role : roles) {
            stringUtilReplaceValues.put(
                    "ROLE_ID:" + role.getName(), String.valueOf(role.getRoleId()));
        }

        String json = SiteInitializerUtil.read(
                "/site-initializer/roles.json", _servletContext);

        if (json == null) {
            return;
        }

        JSONArray jsonArray = _jsonFactory.createJSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Role role = _roleLocalService.fetchRole(
                    serviceContext.getCompanyId(), jsonObject.getString("name"));

            if (role == null) {
                if (jsonObject.getInt("type") == RoleConstants.TYPE_ACCOUNT) {
                    com.liferay.account.model.AccountRole accountRole =
                            _accountRoleLocalService.addAccountRole(
                                    serviceContext.getUserId(),
                                    AccountConstants.ACCOUNT_ENTRY_ID_DEFAULT,
                                    jsonObject.getString("name"),
                                    SiteInitializerUtil.toMap(
                                            jsonObject.getString("name_i18n")),
                                    SiteInitializerUtil.toMap(
                                            jsonObject.getString("description")));

                    role = accountRole.getRole();
                }
                else {
                    role = _roleLocalService.addRole(
                            serviceContext.getUserId(), null, 0,
                            jsonObject.getString("name"),
                            SiteInitializerUtil.toMap(
                                    jsonObject.getString("name_i18n")),
                            SiteInitializerUtil.toMap(
                                    jsonObject.getString("description")),
                            jsonObject.getInt("type"),
                            jsonObject.getString("subtype"), serviceContext);
                }
            }
            else {
                role = _roleLocalService.updateRole(
                        role.getRoleId(), jsonObject.getString("name"),
                        SiteInitializerUtil.toMap(
                                jsonObject.getString("name_i18n")),
                        SiteInitializerUtil.toMap(
                                jsonObject.getString("description")),
                        jsonObject.getString("subtype"), serviceContext);
            }

            stringUtilReplaceValues.put(
                    "ROLE_ID:" + role.getName(), String.valueOf(role.getRoleId()));

            JSONArray actionsJSONArray = jsonObject.getJSONArray("actions");

            if (JSONUtil.isEmpty(actionsJSONArray) || (role == null)) {
                continue;
            }

            for (int j = 0; j < actionsJSONArray.length(); j++) {
                JSONObject actionsJSONObject = actionsJSONArray.getJSONObject(
                        j);

                String resource = actionsJSONObject.getString("resource");
                int scope = actionsJSONObject.getInt("scope");
                String actionId = actionsJSONObject.getString("actionId");

                if (scope == ResourceConstants.SCOPE_COMPANY) {
                    _resourcePermissionLocalService.addResourcePermission(
                            serviceContext.getCompanyId(), resource, scope,
                            String.valueOf(role.getCompanyId()), role.getRoleId(),
                            actionId);
                }
                else if (scope == ResourceConstants.SCOPE_GROUP) {
                    _resourcePermissionLocalService.removeResourcePermissions(
                            serviceContext.getCompanyId(), resource,
                            ResourceConstants.SCOPE_GROUP, role.getRoleId(),
                            actionId);

                    _resourcePermissionLocalService.addResourcePermission(
                            serviceContext.getCompanyId(), resource,
                            ResourceConstants.SCOPE_GROUP,
                            String.valueOf(serviceContext.getScopeGroupId()),
                            role.getRoleId(), actionId);
                }
                else if (scope == ResourceConstants.SCOPE_GROUP_TEMPLATE) {
                    _resourcePermissionLocalService.addResourcePermission(
                            serviceContext.getCompanyId(), resource,
                            ResourceConstants.SCOPE_GROUP_TEMPLATE,
                            String.valueOf(GroupConstants.DEFAULT_PARENT_GROUP_ID),
                            role.getRoleId(), actionId);
                }
            }
        }
    }

    protected void _addOrUpdateSAPEntries(ServiceContext serviceContext)
            throws Exception {

        String json = SiteInitializerUtil.read(
                "/site-initializer/sap-entries.json", _servletContext);

        if (json == null) {
            return;
        }

        JSONArray jsonArray = _jsonFactory.createJSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            SAPEntry sapEntry = _sapEntryLocalService.fetchSAPEntry(
                    serviceContext.getCompanyId(), jsonObject.getString("name"));

            if (sapEntry == null) {
                _sapEntryLocalService.addSAPEntry(
                        serviceContext.getUserId(),
                        StringUtil.merge(
                                JSONUtil.toStringArray(
                                        jsonObject.getJSONArray(
                                                "allowedServiceSignatures")),
                                StringPool.NEW_LINE),
                        jsonObject.getBoolean("defaultSAPEntry", true),
                        jsonObject.getBoolean("enabled", true),
                        jsonObject.getString("name"),
                        SiteInitializerUtil.toMap(
                                jsonObject.getString("title_i18n")),
                        serviceContext);
            }
            else {
                _sapEntryLocalService.updateSAPEntry(
                        sapEntry.getSapEntryId(),
                        StringUtil.merge(
                                JSONUtil.toStringArray(
                                        jsonObject.getJSONArray(
                                                "allowedServiceSignatures")),
                                StringPool.NEW_LINE),
                        jsonObject.getBoolean("defaultSAPEntry", true),
                        jsonObject.getBoolean("enabled", true),
                        jsonObject.getString("name"),
                        SiteInitializerUtil.toMap(
                                jsonObject.getString("title_i18n")),
                        serviceContext);
            }
        }
    }

    protected void _addOrUpdateSegmentsEntries(
            ServiceContext serviceContext,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        String json = SiteInitializerUtil.read(
                "/site-initializer/segments-entries.json", _servletContext);

        if (json == null) {
            return;
        }

        JSONArray jsonArray = _jsonFactory.createJSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            SegmentsEntry segmentsEntry =
                    _segmentsEntryLocalService.fetchSegmentsEntry(
                            serviceContext.getScopeGroupId(),
                            jsonObject.getString("segmentsEntryKey"));

            if (segmentsEntry == null) {
                segmentsEntry = _segmentsEntryLocalService.addSegmentsEntry(
                        jsonObject.getString("segmentsEntryKey"),
                        SiteInitializerUtil.toMap(
                                jsonObject.getString("name_i18n")),
                        null, jsonObject.getBoolean("active", true),
                        jsonObject.getString("criteria"), serviceContext);
            }
            else {
                segmentsEntry = _segmentsEntryLocalService.updateSegmentsEntry(
                        segmentsEntry.getSegmentsEntryId(),
                        jsonObject.getString("segmentsEntryKey"),
                        SiteInitializerUtil.toMap(
                                jsonObject.getString("name_i18n")),
                        null, jsonObject.getBoolean("active", true),
                        jsonObject.getString("criteria"), serviceContext);
            }

            stringUtilReplaceValues.put(
                    "SEGMENTS_ENTRY_ID:" + segmentsEntry.getSegmentsEntryKey(),
                    String.valueOf(segmentsEntry.getSegmentsEntryId()));
        }
    }

    protected void _addOrUpdateSiteNavigationMenu(
            JSONObject jsonObject, ServiceContext serviceContext,
            Map<String, SiteNavigationMenuItemSetting>
                    siteNavigationMenuItemSettings,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        SiteNavigationMenu siteNavigationMenu =
                _siteNavigationMenuLocalService.fetchSiteNavigationMenuByName(
                        serviceContext.getScopeGroupId(), jsonObject.getString("name"));

        if (siteNavigationMenu == null) {
            siteNavigationMenu =
                    _siteNavigationMenuLocalService.addSiteNavigationMenu(
                            serviceContext.getUserId(),
                            serviceContext.getScopeGroupId(),
                            jsonObject.getString("name"), jsonObject.getInt("typeSite"),
                            serviceContext);
        }
        else {
            _siteNavigationMenuLocalService.updateSiteNavigationMenu(
                    serviceContext.getUserId(),
                    siteNavigationMenu.getSiteNavigationMenuId(),
                    jsonObject.getInt("typeSite"), jsonObject.getBoolean("auto"),
                    serviceContext);
        }

        _addOrUpdateSiteNavigationMenuItems(
                jsonObject, siteNavigationMenu, 0, serviceContext,
                siteNavigationMenuItemSettings, stringUtilReplaceValues);
    }

    protected void _addOrUpdateSiteNavigationMenuItems(
            JSONObject jsonObject, SiteNavigationMenu siteNavigationMenu,
            long parentSiteNavigationMenuItemId, ServiceContext serviceContext,
            Map<String, SiteNavigationMenuItemSetting>
                    siteNavigationMenuItemSettings,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        for (Object object :
                JSONUtil.toObjectArray(jsonObject.getJSONArray("menuItems"))) {

            JSONObject menuItemJSONObject = (JSONObject)object;

            String type = menuItemJSONObject.getString("type");

            String typeSettings = null;

            if (type.equals(SiteNavigationMenuItemTypeConstants.LAYOUT)) {
                Layout layout = _layoutLocalService.fetchLayoutByFriendlyURL(
                        serviceContext.getScopeGroupId(),
                        menuItemJSONObject.getBoolean("privateLayout"),
                        menuItemJSONObject.getString("friendlyURL"));

                if (layout == null) {
                    if (_log.isWarnEnabled()) {
                        _log.warn(
                                "No layout found with friendly URL " +
                                        menuItemJSONObject.getString("friendlyURL"));
                    }

                    continue;
                }

                SiteNavigationMenuItemType siteNavigationMenuItemType =
                        _siteNavigationMenuItemTypeRegistry.
                                getSiteNavigationMenuItemType(
                                        SiteNavigationMenuItemTypeConstants.LAYOUT);

                typeSettings =
                        siteNavigationMenuItemType.getTypeSettingsFromLayout(
                                layout);
            }
            else if (type.equals(SiteNavigationMenuItemTypeConstants.NODE)) {
                UnicodePropertiesBuilder.UnicodePropertiesWrapper
                        unicodePropertiesWrapper =
                        _getNavigationMenuItemUnicodePropertiesWrapper(
                                menuItemJSONObject);

                if (unicodePropertiesWrapper == null) {
                    continue;
                }

                typeSettings = unicodePropertiesWrapper.put(
                        "title", menuItemJSONObject.getString("title")
                ).buildString();
            }
            else if (type.equals(SiteNavigationMenuItemTypeConstants.URL)) {
                UnicodePropertiesBuilder.UnicodePropertiesWrapper
                        unicodePropertiesWrapper =
                        _getNavigationMenuItemUnicodePropertiesWrapper(
                                menuItemJSONObject);

                if (unicodePropertiesWrapper == null) {
                    continue;
                }

                typeSettings = unicodePropertiesWrapper.put(
                        "url", menuItemJSONObject.getString("url")
                ).put(
                        "useNewTab", menuItemJSONObject.getString("useNewTab")
                ).buildString();
            }
            else if (type.equals("display-page")) {
                String key = menuItemJSONObject.getString("key");

                if (Validator.isNull(key)) {
                    continue;
                }

                SiteNavigationMenuItemSetting siteNavigationMenuItemSetting =
                        siteNavigationMenuItemSettings.get(key);

                if (siteNavigationMenuItemSetting == null) {
                    continue;
                }

                type = siteNavigationMenuItemSetting.className;

                typeSettings = UnicodePropertiesBuilder.create(
                        true
                ).put(
                        "className", siteNavigationMenuItemSetting.className
                ).put(
                        "classNameId",
                        String.valueOf(
                                _portal.getClassNameId(
                                        siteNavigationMenuItemSetting.className))
                ).put(
                        "classPK",
                        String.valueOf(siteNavigationMenuItemSetting.classPK)
                ).put(
                        "classTypeId", siteNavigationMenuItemSetting.classTypeId
                ).put(
                        "title", siteNavigationMenuItemSetting.title
                ).put(
                        "type", siteNavigationMenuItemSetting.type
                ).buildString();
            }

            SiteNavigationMenuItem siteNavigationMenuItem =
                    _siteNavigationMenuItemLocalService.
                            addOrUpdateSiteNavigationMenuItem(
                                    menuItemJSONObject.getString("externalReferenceCode"),
                                    serviceContext.getUserId(),
                                    serviceContext.getScopeGroupId(),
                                    siteNavigationMenu.getSiteNavigationMenuId(),
                                    parentSiteNavigationMenuItemId, type, typeSettings,
                                    serviceContext);

            stringUtilReplaceValues.put(
                    "SITE_NAVIGATION_MENU_ITEM_ID:" +
                            siteNavigationMenuItem.getExternalReferenceCode(),
                    String.valueOf(
                            siteNavigationMenuItem.getSiteNavigationMenuItemId()));

            _addOrUpdateSiteNavigationMenuItems(
                    menuItemJSONObject, siteNavigationMenu,
                    siteNavigationMenuItem.getSiteNavigationMenuItemId(),
                    serviceContext, siteNavigationMenuItemSettings,
                    stringUtilReplaceValues);
        }
    }

    protected void _addOrUpdateSiteNavigationMenus(
            ServiceContext serviceContext,
            Map<String, SiteNavigationMenuItemSetting>
                    siteNavigationMenuItemSettings,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        String json = SiteInitializerUtil.read(
                "/site-initializer/site-navigation-menus.json", _servletContext);

        if (json == null) {
            return;
        }

        JSONArray jsonArray = _jsonFactory.createJSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            _addOrUpdateSiteNavigationMenu(
                    jsonArray.getJSONObject(i), serviceContext,
                    siteNavigationMenuItemSettings, stringUtilReplaceValues);
        }
    }

    protected Long _addOrUpdateStructuredContentFolders(
            Long documentFolderId, String parentResourcePath,
            ServiceContext serviceContext)
            throws Exception {

        StructuredContentFolderResource.Builder
                structuredContentFolderResourceBuilder =
                _structuredContentFolderResourceFactory.create();

        StructuredContentFolderResource structuredContentFolderResource =
                structuredContentFolderResourceBuilder.user(
                        serviceContext.fetchUser()
                ).build();

        String json = SiteInitializerUtil.read(
                parentResourcePath + ".metadata.json", _servletContext);

        if (json == null) {
            json = JSONUtil.put(
                    "name", FileUtil.getShortFileName(parentResourcePath)
            ).toString();
        }

        StructuredContentFolder structuredContentFolder =
                StructuredContentFolder.toDTO(json);

        structuredContentFolder.setParentStructuredContentFolderId(
                documentFolderId);

        structuredContentFolder =
                structuredContentFolderResource.
                        putSiteStructuredContentFolderByExternalReferenceCode(
                                serviceContext.getScopeGroupId(),
                                structuredContentFolder.getExternalReferenceCode(),
                                structuredContentFolder);

        return structuredContentFolder.getId();
    }

    protected void _addOrUpdateSXPBlueprint(
            ServiceContext serviceContext,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        OSBSiteInitializer osbSiteInitializer =
                _osbSiteInitializerSnapshot.get();

        if (osbSiteInitializer == null) {
            return;
        }

        osbSiteInitializer.addOrUpdateSXPBlueprint(
                serviceContext, _servletContext, stringUtilReplaceValues);
    }

    protected TaxonomyCategory _addOrUpdateTaxonomyCategoryTaxonomyCategory(
            String parentTaxonomyCategoryId, ServiceContext serviceContext,
            TaxonomyCategory taxonomyCategory)
            throws Exception {

        TaxonomyCategoryResource.Builder taxonomyCategoryResourceBuilder =
                _taxonomyCategoryResourceFactory.create();

        TaxonomyCategoryResource taxonomyCategoryResource =
                taxonomyCategoryResourceBuilder.user(
                        serviceContext.fetchUser()
                ).build();

        Page<TaxonomyCategory> taxonomyCategoryPage =
                taxonomyCategoryResource.getTaxonomyCategoryTaxonomyCategoriesPage(
                        parentTaxonomyCategoryId, "", null,
                        taxonomyCategoryResource.toFilter(
                                StringBundler.concat(
                                        "name eq '", taxonomyCategory.getName(), "'")),
                        null, null);

        TaxonomyCategory existingTaxonomyCategory =
                taxonomyCategoryPage.fetchFirstItem();

        if (existingTaxonomyCategory == null) {
            taxonomyCategory =
                    taxonomyCategoryResource.postTaxonomyCategoryTaxonomyCategory(
                            parentTaxonomyCategoryId, taxonomyCategory);
        }
        else {
            taxonomyCategory = taxonomyCategoryResource.patchTaxonomyCategory(
                    existingTaxonomyCategory.getId(), taxonomyCategory);
        }

        return taxonomyCategory;
    }

    protected void _addOrUpdateTaxonomyVocabularies(
            long groupId, String parentResourcePath,
            ServiceContext serviceContext,
            SiteNavigationMenuItemSettingsBuilder
                    siteNavigationMenuItemSettingsBuilder,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        Set<String> resourcePaths = _servletContext.getResourcePaths(
                parentResourcePath);

        if (SetUtil.isEmpty(resourcePaths)) {
            return;
        }

        TaxonomyVocabularyResource.Builder taxonomyVocabularyResourceBuilder =
                _taxonomyVocabularyResourceFactory.create();

        TaxonomyVocabularyResource taxonomyVocabularyResource =
                taxonomyVocabularyResourceBuilder.user(
                        serviceContext.fetchUser()
                ).build();

        for (String resourcePath : resourcePaths) {
            if (resourcePath.endsWith("/")) {
                continue;
            }

            String json = SiteInitializerUtil.read(
                    resourcePath, _servletContext);

            TaxonomyVocabulary taxonomyVocabulary = TaxonomyVocabulary.toDTO(
                    json);

            if (taxonomyVocabulary == null) {
                _log.error(
                        "Unable to transform taxonomy vocabulary from JSON: " +
                                json);

                continue;
            }

            Page<TaxonomyVocabulary> taxonomyVocabularyPage =
                    taxonomyVocabularyResource.getSiteTaxonomyVocabulariesPage(
                            groupId, "", null,
                            taxonomyVocabularyResource.toFilter(
                                    StringBundler.concat(
                                            "name eq '", taxonomyVocabulary.getName(), "'")),
                            null, null);

            TaxonomyVocabulary existingTaxonomyVocabulary =
                    taxonomyVocabularyPage.fetchFirstItem();

            if (existingTaxonomyVocabulary == null) {
                taxonomyVocabulary =
                        taxonomyVocabularyResource.postSiteTaxonomyVocabulary(
                                groupId, taxonomyVocabulary);
            }
            else {
                taxonomyVocabulary =
                        taxonomyVocabularyResource.patchTaxonomyVocabulary(
                                existingTaxonomyVocabulary.getId(), taxonomyVocabulary);
            }

            stringUtilReplaceValues.put(
                    "TAXONOMY_VOCABULARY_ID:" + taxonomyVocabulary.getName(),
                    String.valueOf(taxonomyVocabulary.getId()));

            _addTaxonomyCategories(
                    StringUtil.replaceLast(resourcePath, ".json", "/"), null,
                    serviceContext, siteNavigationMenuItemSettingsBuilder,
                    stringUtilReplaceValues, taxonomyVocabulary.getId());
        }
    }

    protected void _addOrUpdateTaxonomyVocabularies(
            ServiceContext serviceContext,
            SiteNavigationMenuItemSettingsBuilder
                    siteNavigationMenuItemSettingsBuilder,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        Group group = _groupLocalService.getCompanyGroup(
                serviceContext.getCompanyId());

        _addOrUpdateTaxonomyVocabularies(
                group.getGroupId(),
                "/site-initializer/taxonomy-vocabularies/company", serviceContext,
                siteNavigationMenuItemSettingsBuilder, stringUtilReplaceValues);

        _addOrUpdateTaxonomyVocabularies(
                serviceContext.getScopeGroupId(),
                "/site-initializer/taxonomy-vocabularies/group", serviceContext,
                siteNavigationMenuItemSettingsBuilder, stringUtilReplaceValues);
    }

    protected TaxonomyCategory _addOrUpdateTaxonomyVocabularyTaxonomyCategory(
            ServiceContext serviceContext, TaxonomyCategory taxonomyCategory,
            long vocabularyId)
            throws Exception {

        TaxonomyCategoryResource.Builder taxonomyCategoryResourceBuilder =
                _taxonomyCategoryResourceFactory.create();

        TaxonomyCategoryResource taxonomyCategoryResource =
                taxonomyCategoryResourceBuilder.user(
                        serviceContext.fetchUser()
                ).build();

        Page<TaxonomyCategory> taxonomyCategoryPage =
                taxonomyCategoryResource.
                        getTaxonomyVocabularyTaxonomyCategoriesPage(
                                vocabularyId, null, "", null,
                                taxonomyCategoryResource.toFilter(
                                        StringBundler.concat(
                                                "name eq '", taxonomyCategory.getName(), "'")),
                                null, null);

        TaxonomyCategory existingTaxonomyCategory =
                taxonomyCategoryPage.fetchFirstItem();

        if (existingTaxonomyCategory == null) {
            taxonomyCategory =
                    taxonomyCategoryResource.postTaxonomyVocabularyTaxonomyCategory(
                            vocabularyId, taxonomyCategory);
        }
        else {
            taxonomyCategory = taxonomyCategoryResource.patchTaxonomyCategory(
                    existingTaxonomyCategory.getId(), taxonomyCategory);
        }

        return taxonomyCategory;
    }

    protected void _addOrUpdateUserGroups(ServiceContext serviceContext)
            throws Exception {

        String json = SiteInitializerUtil.read(
                "/site-initializer/user-groups.json", _servletContext);

        if (json == null) {
            return;
        }

        JSONArray jsonArray = _jsonFactory.createJSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            UserGroup userGroup = _userGroupLocalService.addOrUpdateUserGroup(
                    jsonObject.getString("externalReferenceCode"),
                    serviceContext.getUserId(), serviceContext.getCompanyId(),
                    jsonObject.getString("name"),
                    jsonObject.getString("description"), serviceContext);

            _userGroupLocalService.addGroupUserGroup(
                    serviceContext.getScopeGroupId(), userGroup);
        }
    }

    protected void _addPortletSettings(ServiceContext serviceContext)
            throws Exception {

        CommerceSiteInitializer commerceSiteInitializer =
                _commerceSiteInitializerSnapshot.get();

        if (commerceSiteInitializer == null) {
            return;
        }

        commerceSiteInitializer.addPortletSettings(
                _classLoader, serviceContext, _servletContext);
    }

    protected void _addRolesAssignments(ServiceContext serviceContext)
            throws Exception {

        String json = SiteInitializerUtil.read(
                "/site-initializer/roles-assignments.json", _servletContext);

        if (json == null) {
            return;
        }

        JSONArray jsonArray = _jsonFactory.createJSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            JSONArray groupsJSONArray = jsonObject.getJSONArray("groups");

            if (JSONUtil.isEmpty(groupsJSONArray)) {
                continue;
            }

            List<Long> groupIds = new ArrayList<>();

            for (int j = 0; j < groupsJSONArray.length(); j++) {
                JSONObject groupJSONObject = groupsJSONArray.getJSONObject(j);

                String groupType = groupJSONObject.getString("groupType");

                if (StringUtil.equals(groupType, "Organization")) {
                    com.liferay.portal.kernel.model.Organization organization =
                            _organizationLocalService.fetchOrganization(
                                    serviceContext.getCompanyId(),
                                    groupJSONObject.getString("groupName"));

                    if (organization == null) {
                        if (_log.isWarnEnabled()) {
                            _log.warn(
                                    "No organization found with name " +
                                            groupJSONObject.getString("groupName"));
                        }

                        continue;
                    }

                    Group group = _groupLocalService.getOrganizationGroup(
                            serviceContext.getCompanyId(),
                            organization.getOrganizationId());

                    groupIds.add(group.getGroupId());
                }
                else if (StringUtil.equals(groupType, "Site")) {
                    groupIds.add(serviceContext.getScopeGroupId());
                }
                else if (StringUtil.equals(groupType, "User")) {
                    User user = _userLocalService.fetchUserByScreenName(
                            serviceContext.getCompanyId(),
                            groupJSONObject.getString("groupName"));

                    if (user == null) {
                        if (_log.isWarnEnabled()) {
                            _log.warn(
                                    "No user found with screen name " +
                                            groupJSONObject.getString("groupName"));
                        }

                        continue;
                    }

                    Group group = _groupLocalService.getUserGroup(
                            serviceContext.getCompanyId(), user.getUserId());

                    groupIds.add(group.getGroupId());
                }
                else if (StringUtil.equals(groupType, "UserGroups")) {
                    UserGroup userGroup = _userGroupLocalService.fetchUserGroup(
                            serviceContext.getCompanyId(),
                            groupJSONObject.getString("groupName"));

                    if (userGroup == null) {
                        if (_log.isWarnEnabled()) {
                            _log.warn(
                                    "No user group found with name " +
                                            groupJSONObject.getString("groupName"));
                        }

                        continue;
                    }

                    Group group = _groupLocalService.getUserGroupGroup(
                            serviceContext.getCompanyId(),
                            userGroup.getUserGroupId());

                    groupIds.add(group.getGroupId());
                }
            }

            if (ListUtil.isEmpty(groupIds)) {
                continue;
            }

            Role role = _roleLocalService.fetchRole(
                    serviceContext.getCompanyId(),
                    jsonObject.getString("roleName"));

            if (role == null) {
                if (_log.isWarnEnabled()) {
                    if (_log.isWarnEnabled()) {
                        _log.warn(
                                "No role found with name " +
                                        jsonObject.getString("roleName"));
                    }
                }

                continue;
            }

            _groupLocalService.setRoleGroups(
                    role.getRoleId(), ArrayUtil.toLongArray(groupIds));
        }
    }

    protected void _addSegmentsExperiences(
            ServiceContext serviceContext,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        Set<String> parentResourcePaths = _servletContext.getResourcePaths(
                "/site-initializer/segments-experiences");

        if (SetUtil.isEmpty(parentResourcePaths)) {
            return;
        }

        for (String parentResourcePath : parentResourcePaths) {
            String json = SiteInitializerUtil.read(
                    parentResourcePath + "segments-experiences.json",
                    _servletContext);

            if (json == null) {
                return;
            }

            json = _replace(json, stringUtilReplaceValues);

            JSONObject jsonObject = _jsonFactory.createJSONObject(json);

            Layout layout = _layoutLocalService.getLayoutByFriendlyURL(
                    serviceContext.getScopeGroupId(), false,
                    jsonObject.getString("friendlyURL"));

            Layout draftLayout = layout.fetchDraftLayout();

            UnicodeProperties unicodeProperties = new UnicodeProperties(true);

            JSONObject propertiesJSONObject = jsonObject.getJSONObject(
                    "typeSettings");

            if (propertiesJSONObject != null) {
                Map<String, String> map = JSONUtil.toStringMap(
                        propertiesJSONObject);

                unicodeProperties.putAll(map);
            }

            SegmentsExperience segmentsExperience =
                    _segmentsExperienceLocalService.appendSegmentsExperience(
                            serviceContext.getUserId(),
                            serviceContext.getScopeGroupId(),
                            jsonObject.getLong("segmentsEntryId"),
                            draftLayout.getClassPK(),
                            SiteInitializerUtil.toMap(
                                    jsonObject.getString("name_i18n")),
                            jsonObject.getBoolean("active", true), unicodeProperties,
                            serviceContext);

            Set<String> resourcePaths = _servletContext.getResourcePaths(
                    parentResourcePath);

            for (String resourcePath : resourcePaths) {
                if (resourcePath.endsWith("/")) {
                    _addOrUpdateLayoutContent(
                            layout, resourcePath,
                            segmentsExperience.getSegmentsExperienceId(),
                            serviceContext, stringUtilReplaceValues);
                }
            }
        }
    }

    protected void _addSiteConfiguration(ServiceContext serviceContext)
            throws Exception {

        String resourcePath = "site-initializer/site-configuration.json";

        String json = SiteInitializerUtil.read(resourcePath, _servletContext);

        if (json == null) {
            return;
        }

        Group group = _groupLocalService.getGroup(
                serviceContext.getScopeGroupId());

        JSONObject jsonObject = _jsonFactory.createJSONObject(json);

        group.setType(jsonObject.getInt("typeSite"));
        group.setManualMembership(jsonObject.getBoolean("manualMembership"));
        group.setMembershipRestriction(
                jsonObject.getInt("membershipRestriction"));

        _groupLocalService.updateGroup(group);
    }

    protected void _addSiteSettings(ServiceContext serviceContext)
            throws Exception {

        String json = SiteInitializerUtil.read(
                "/site-initializer/site-settings.json", _servletContext);

        if (json == null) {
            return;
        }

        JSONArray jsonArray = _jsonFactory.createJSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Dictionary<String, Object> properties = new HashMapDictionary<>();

            JSONObject propertiesJSONObject = jsonObject.getJSONObject(
                    "properties");

            Iterator<String> iterator = propertiesJSONObject.keys();

            while (iterator.hasNext()) {
                String key = iterator.next();

                properties.put(key, propertiesJSONObject.getString(key));
            }

            _configurationProvider.saveGroupConfiguration(
                    serviceContext.getScopeGroupId(), jsonObject.getString("pid"),
                    properties);
        }
    }

    protected void _addStyleBookEntries(ServiceContext serviceContext)
            throws Exception {

        Enumeration<URL> enumeration = _bundle.findEntries(
                "/site-initializer/style-books", StringPool.STAR, true);

        if (enumeration == null) {
            return;
        }

        ZipWriter zipWriter = _zipWriterFactory.getZipWriter();

        while (enumeration.hasMoreElements()) {
            URL url = enumeration.nextElement();

            String fileName = url.getFile();

            if (fileName.endsWith("/")) {
                continue;
            }

            try (InputStream inputStream = url.openStream()) {
                zipWriter.addEntry(
                        _removeFirst(fileName, "/site-initializer/style-books/"),
                        inputStream);
            }
        }

        _styleBookEntryZipProcessor.importStyleBookEntries(
                serviceContext.getUserId(), serviceContext.getScopeGroupId(),
                zipWriter.getFile(), true);
    }

    protected void _addTaxonomyCategories(
            String parentResourcePath, String parentTaxonomyCategoryId,
            ServiceContext serviceContext,
            SiteNavigationMenuItemSettingsBuilder
                    siteNavigationMenuItemSettingsBuilder,
            Map<String, String> stringUtilReplaceValues,
            long taxonomyVocabularyId)
            throws Exception {

        Set<String> resourcePaths = _servletContext.getResourcePaths(
                parentResourcePath);

        if (SetUtil.isEmpty(resourcePaths)) {
            return;
        }

        for (String resourcePath : resourcePaths) {
            if (resourcePath.endsWith("/")) {
                continue;
            }

            String json = SiteInitializerUtil.read(
                    resourcePath, _servletContext);

            json = _replace(
                    _replace(json, serviceContext), stringUtilReplaceValues);

            TaxonomyCategory taxonomyCategory = TaxonomyCategory.toDTO(json);

            if (taxonomyCategory == null) {
                _log.error(
                        "Unable to transform taxonomy category from JSON: " + json);

                continue;
            }

            if (parentTaxonomyCategoryId == null) {
                taxonomyCategory =
                        _addOrUpdateTaxonomyVocabularyTaxonomyCategory(
                                serviceContext, taxonomyCategory, taxonomyVocabularyId);
            }
            else {
                taxonomyCategory = _addOrUpdateTaxonomyCategoryTaxonomyCategory(
                        parentTaxonomyCategoryId, serviceContext, taxonomyCategory);
            }

            TaxonomyCategory finalTaxonomyCategory = taxonomyCategory;

            String key = resourcePath;

            stringUtilReplaceValues.put(
                    "TAXONOMY_CATEGORY_ID:" + key,
                    String.valueOf(finalTaxonomyCategory.getId()));

            siteNavigationMenuItemSettingsBuilder.put(
                    resourcePath,
                    new SiteNavigationMenuItemSetting() {
                        {
                            className = AssetCategory.class.getName();
                            classPK = finalTaxonomyCategory.getId();
                            title = finalTaxonomyCategory.getName();
                        }
                    });

            _addTaxonomyCategories(
                    StringUtil.replaceLast(resourcePath, ".json", "/"),
                    taxonomyCategory.getId(), serviceContext,
                    siteNavigationMenuItemSettingsBuilder, stringUtilReplaceValues,
                    taxonomyVocabularyId);
        }
    }

    protected void _addUserAccounts(ServiceContext serviceContext)
            throws Exception {

        String json = SiteInitializerUtil.read(
                "/site-initializer/user-accounts.json", _servletContext);

        if (json == null) {
            return;
        }

        UserAccountResource.Builder userAccountResourceBuilder =
                _userAccountResourceFactory.create();

        UserAccountResource userAccountResource =
                userAccountResourceBuilder.user(
                        serviceContext.fetchUser()
                ).httpServletRequest(
                        serviceContext.getRequest()
                ).build();

        JSONArray jsonArray = _jsonFactory.createJSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            JSONArray accountBriefsJSONArray = jsonObject.getJSONArray(
                    "accountBriefs");

            if (JSONUtil.isEmpty(accountBriefsJSONArray)) {
                continue;
            }

            List<Group> oldGroups = new ArrayList<>();

            int j = 0;
            long userId = 0;

            UserAccount userAccount = UserAccount.toDTO(
                    String.valueOf(jsonObject));

            User user = _userLocalService.fetchUserByEmailAddress(
                    serviceContext.getCompanyId(), userAccount.getEmailAddress());

            if (user == null) {
                JSONObject accountBriefsJSONObject =
                        accountBriefsJSONArray.getJSONObject(j);

                userAccount =
                        userAccountResource.putUserAccountByExternalReferenceCode(
                                jsonObject.getString("externalReferenceCode"),
                                userAccount);

                userAccountResource.
                        postAccountByExternalReferenceCodeUserAccountByExternalReferenceCode(
                                accountBriefsJSONObject.getString(
                                        "externalReferenceCode"),
                                userAccount.getExternalReferenceCode());

                j++;

                _associateUserAccounts(
                        accountBriefsJSONObject,
                        jsonObject.getString("emailAddress"), serviceContext);

                userId = userAccount.getId();
            }
            else {
                userId = user.getUserId();

                oldGroups = user.getSiteGroups();
            }

            oldGroups.add(serviceContext.getScopeGroup());

            _userLocalService.updateGroups(
                    userId, ListUtil.toLongArray(oldGroups, GroupModel::getGroupId),
                    serviceContext);

            if (jsonObject.has("organizationBriefs")) {
                _addOrganizationUser(
                        jsonObject.getJSONArray("organizationBriefs"),
                        serviceContext, userId);
            }

            for (; j < accountBriefsJSONArray.length(); j++) {
                JSONObject accountBriefsJSONObject =
                        accountBriefsJSONArray.getJSONObject(j);

                userAccountResource.
                        postAccountUserAccountByExternalReferenceCodeByEmailAddress(
                                accountBriefsJSONObject.getString(
                                        "externalReferenceCode"),
                                userAccount.getEmailAddress());

                _associateUserAccounts(
                        accountBriefsJSONObject,
                        jsonObject.getString("emailAddress"), serviceContext);
            }

            userAccount = userAccountResource.getUserAccountByEmailAddress(
                    userAccount.getEmailAddress());

            userAccount.setStatus(UserAccount.Status.INACTIVE);

            userAccountResource.patchUserAccount(
                    userAccount.getId(), userAccount);
        }
    }

    protected void _addUserRoles(ServiceContext serviceContext) throws Exception {
        String json = SiteInitializerUtil.read(
                "/site-initializer/user-roles.json", _servletContext);

        if (json == null) {
            return;
        }

        JSONArray jsonArray = _jsonFactory.createJSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            JSONArray rolesJSONArray = jsonObject.getJSONArray("roles");

            if (JSONUtil.isEmpty(rolesJSONArray)) {
                continue;
            }

            List<Role> roles = new ArrayList<>();

            for (int j = 0; j < rolesJSONArray.length(); j++) {
                roles.add(
                        _roleLocalService.getRole(
                                serviceContext.getCompanyId(),
                                rolesJSONArray.getString(j)));
            }

            if (ListUtil.isNotEmpty(roles)) {
                User user = _userLocalService.fetchUserByEmailAddress(
                        serviceContext.getCompanyId(),
                        jsonObject.getString("emailAddress"));

                _roleLocalService.addUserRoles(user.getUserId(), roles);
            }
        }
    }

    protected void _addWorkflowDefinitions(
            ServiceContext serviceContext,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        Set<String> resourcePaths = _servletContext.getResourcePaths(
                "/site-initializer/workflow-definitions");

        if (SetUtil.isEmpty(resourcePaths)) {
            return;
        }

        WorkflowDefinitionResource.Builder workflowDefinitionResourceBuilder =
                _workflowDefinitionResourceFactory.create();

        WorkflowDefinitionResource workflowDefinitionResource =
                workflowDefinitionResourceBuilder.user(
                        serviceContext.fetchUser()
                ).build();

        for (String resourcePath : resourcePaths) {
            JSONObject workflowDefinitionJSONObject =
                    _jsonFactory.createJSONObject(
                            SiteInitializerUtil.read(
                                    resourcePath + "workflow-definition.json",
                                    _servletContext));

            workflowDefinitionJSONObject.put(
                    "content",
                    _replace(
                            SiteInitializerUtil.read(
                                    resourcePath + "workflow-definition.xml",
                                    _servletContext),
                            stringUtilReplaceValues));

            WorkflowDefinition workflowDefinition =
                    workflowDefinitionResource.postWorkflowDefinitionDeploy(
                            WorkflowDefinition.toDTO(
                                    workflowDefinitionJSONObject.toString()));

            String propertiesJSON = SiteInitializerUtil.read(
                    resourcePath + "workflow-definition.properties.json",
                    _servletContext);

            if (propertiesJSON == null) {
                continue;
            }

            JSONArray propertiesJSONArray = _jsonFactory.createJSONArray(
                    propertiesJSON);

            for (int i = 0; i < propertiesJSONArray.length(); i++) {
                JSONObject propertiesJSONObject =
                        propertiesJSONArray.getJSONObject(i);

                long groupId = 0;

                if (StringUtil.equals(
                        propertiesJSONObject.getString("scope"), "site")) {

                    groupId = serviceContext.getScopeGroupId();
                }

                String className = propertiesJSONObject.getString("className");

                if (StringUtil.equals(
                        className,
                        com.liferay.object.model.ObjectDefinition.class.
                                getName())) {

                    com.liferay.object.model.ObjectDefinition objectDefinition =
                            _objectDefinitionLocalService.fetchObjectDefinition(
                                    serviceContext.getCompanyId(),
                                    propertiesJSONObject.getString("assetName"));

                    if (objectDefinition == null) {
                        continue;
                    }

                    className = StringBundler.concat(
                            className, "#",
                            objectDefinition.getObjectDefinitionId());
                }

                long typePK = 0;

                CommerceSiteInitializer commerceSiteInitializer =
                        _commerceSiteInitializerSnapshot.get();

                if ((commerceSiteInitializer != null) &&
                        StringUtil.equals(
                                className,
                                commerceSiteInitializer.getCommerceOrderClassName())) {

                    groupId = commerceSiteInitializer.getCommerceChannelGroupId(
                            groupId);

                    typePK = propertiesJSONObject.getLong("typePK");
                }

                _workflowDefinitionLinkLocalService.
                        updateWorkflowDefinitionLink(
                                serviceContext.getUserId(),
                                serviceContext.getCompanyId(), groupId, className, 0,
                                typePK,
                                StringBundler.concat(
                                        workflowDefinition.getName(), "@",
                                        workflowDefinition.getVersion()));
            }
        }
    }

    protected void _associateUserAccounts(
            JSONObject accountBriefsJSONObject, String emailAddress,
            ServiceContext serviceContext)
            throws Exception {

        if (!accountBriefsJSONObject.has("roleBriefs")) {
            return;
        }

        JSONArray jsonArray = accountBriefsJSONObject.getJSONArray(
                "roleBriefs");

        if (JSONUtil.isEmpty(jsonArray)) {
            return;
        }

        AccountRoleResource.Builder builder =
                _accountRoleResourceFactory.create();

        AccountRoleResource accountRoleResource = builder.user(
                serviceContext.fetchUser()
        ).build();

        for (int i = 0; i < jsonArray.length(); i++) {
            Page<AccountRole> accountRolePage =
                    accountRoleResource.
                            getAccountAccountRolesByExternalReferenceCodePage(
                                    accountBriefsJSONObject.getString(
                                            "externalReferenceCode"),
                                    null,
                                    accountRoleResource.toFilter(
                                            StringBundler.concat(
                                                    "name eq '", jsonArray.getString(i), "'")),
                                    null, null);

            AccountRole accountRole = accountRolePage.fetchFirstItem();

            if (accountRole == null) {
                continue;
            }

            accountRoleResource.
                    postAccountByExternalReferenceCodeAccountRoleUserAccountByEmailAddress(
                            accountBriefsJSONObject.getString("externalReferenceCode"),
                            accountRole.getId(), emailAddress);
        }
    }

    protected long[] _getAssetCategoryIds(
            long groupId, String[] externalReferenceCodes) {

        List<Long> assetCategoryIds = new ArrayList<>();

        for (String externalReferenceCode : externalReferenceCodes) {
            AssetCategory assetCategory =
                    _assetCategoryLocalService.
                            fetchAssetCategoryByExternalReferenceCode(
                                    externalReferenceCode, groupId);

            if (assetCategory != null) {
                assetCategoryIds.add(assetCategory.getCategoryId());
            }
        }

        return ArrayUtil.toLongArray(assetCategoryIds);
    }

    protected Map<String, String> _getClassNameIdStringUtilReplaceValues() {
        Map<String, String> map = new HashMap<>();

        Class<?>[] classes = {DDMStructure.class, JournalArticle.class};

        for (Class<?> clazz : classes) {
            map.put(
                    "CLASS_NAME_ID:" + clazz.getName(),
                    String.valueOf(_portal.getClassNameId(clazz)));
        }

        return map;
    }

    protected Serializable _getExpandoAttributeValue(JSONObject jsonObject)
            throws Exception {

        if (jsonObject.getInt("dataType") == ExpandoColumnConstants.BOOLEAN) {
            return jsonObject.getBoolean("defaultValue");
        }
        else if (jsonObject.getInt("dataType") == ExpandoColumnConstants.DATE) {
            if (Validator.isNull(jsonObject.getString("defaultValue"))) {
                return null;
            }

            DateFormat dateFormat = DateUtil.getISOFormat(
                    jsonObject.getString("defaultValue"));

            return dateFormat.parse(jsonObject.getString("defaultValue"));
        }
        else if (jsonObject.getInt("dataType") ==
                ExpandoColumnConstants.DOUBLE) {

            return jsonObject.getDouble("defaultValue");
        }
        else if (jsonObject.getInt("dataType") ==
                ExpandoColumnConstants.DOUBLE_ARRAY) {

            return JSONUtil.toDoubleArray(
                    jsonObject.getJSONArray("defaultValue"));
        }
        else if (jsonObject.getInt("dataType") ==
                ExpandoColumnConstants.FLOAT) {

            return jsonObject.getDouble("defaultValue");
        }
        else if (jsonObject.getInt("dataType") ==
                ExpandoColumnConstants.FLOAT_ARRAY) {

            return JSONUtil.toFloatArray(
                    jsonObject.getJSONArray("defaultValue"));
        }
        else if (jsonObject.getInt("dataType") ==
                ExpandoColumnConstants.INTEGER) {

            return jsonObject.getInt("defaultValue");
        }
        else if (jsonObject.getInt("dataType") ==
                ExpandoColumnConstants.INTEGER_ARRAY) {

            return JSONUtil.toIntegerArray(
                    jsonObject.getJSONArray("defaultValue"));
        }
        else if (jsonObject.getInt("dataType") == ExpandoColumnConstants.LONG) {
            return jsonObject.getLong("defaultValue");
        }
        else if (jsonObject.getInt("dataType") ==
                ExpandoColumnConstants.LONG_ARRAY) {

            return JSONUtil.toLongArray(
                    jsonObject.getJSONArray("defaultValue"));
        }
        else if (jsonObject.getInt("dataType") ==
                ExpandoColumnConstants.NUMBER) {

            return jsonObject.getDouble("defaultValue");
        }
        else if (jsonObject.getInt("dataType") ==
                ExpandoColumnConstants.NUMBER_ARRAY) {

            return JSONUtil.toIntegerArray(
                    jsonObject.getJSONArray("defaultValue"));
        }
        else if (jsonObject.getInt("dataType") ==
                ExpandoColumnConstants.STRING) {

            return jsonObject.getString("defaultValue");
        }
        else if (jsonObject.getInt("dataType") ==
                ExpandoColumnConstants.STRING_ARRAY) {

            return JSONUtil.toStringArray(
                    jsonObject.getJSONArray("defaultValue"));
        }

        return (Serializable)jsonObject.get("defaultValue");
    }

    protected UnicodePropertiesBuilder.UnicodePropertiesWrapper
    _getNavigationMenuItemUnicodePropertiesWrapper(
            JSONObject menuItemJSONObject) {

        JSONObject nameI18nJSONObject = menuItemJSONObject.getJSONObject(
                "name_i18n");

        if (nameI18nJSONObject == null) {
            if (_log.isWarnEnabled()) {
                _log.warn("Missing \"name_i18n\" in " + menuItemJSONObject);
            }

            return null;
        }

        UnicodePropertiesBuilder.UnicodePropertiesWrapper
                unicodePropertiesWrapper = UnicodePropertiesBuilder.put(
                "defaultLanguageId",
                LocaleUtil.toLanguageId(LocaleUtil.getDefault()));

        for (String key : nameI18nJSONObject.keySet()) {
            unicodePropertiesWrapper.put(
                    "name_" + key, nameI18nJSONObject.getString(key));
        }

        return unicodePropertiesWrapper;
    }

    protected Map<String, String> _getReleaseInfoStringUtilReplaceValues() {
        Map<String, String> map = new HashMap<>();

        Object[] entries = {
                "BUILD_DATE", ReleaseInfo.getBuildDate(), "BUILD_NUMBER",
                ReleaseInfo.getBuildNumber(), "CODE_NAME",
                ReleaseInfo.getCodeName(), "NAME", ReleaseInfo.getName(),
                "PARENT_BUILD_NUMBER", ReleaseInfo.getParentBuildNumber(),
                "RELEASE_INFO",
                _replace(
                        ReleaseInfo.getReleaseInfo(), StringPool.OPEN_PARENTHESIS,
                        "<br>("),
                "SERVER_INFO", ReleaseInfo.getServerInfo(), "VENDOR",
                ReleaseInfo.getVendor(), "VERSION", ReleaseInfo.getVersion(),
                "VERSION_DISPLAY_NAME", ReleaseInfo.getVersionDisplayName()
        };

        for (int i = 0; i < entries.length; i += 2) {
            String entryKey = String.valueOf(entries[i]);
            String entryValue = String.valueOf(entries[i + 1]);

            map.put("RELEASE_INFO:" + entryKey, entryValue);
        }

        return map;
    }

    protected String _getThemeId(
            long companyId, String defaultThemeId, String themeName) {

        List<Theme> themes = ListUtil.filter(
                _themeLocalService.getThemes(companyId),
                theme -> Objects.equals(theme.getName(), themeName));

        if (ListUtil.isNotEmpty(themes)) {
            Theme theme = themes.get(0);

            return theme.getThemeId();
        }

        return defaultThemeId;
    }

    protected void _invoke(UnsafeRunnable<Exception> unsafeRunnable)
            throws Exception {

        long startTime = System.currentTimeMillis();

        unsafeRunnable.run();

        if (_log.isInfoEnabled()) {
            Thread thread = Thread.currentThread();

            StackTraceElement stackTraceElement = thread.getStackTrace()[2];

            _log.info(
                    StringBundler.concat(
                            "Invoking line ", stackTraceElement.getLineNumber(),
                            " took ", System.currentTimeMillis() - startTime, " ms"));
        }
    }

    protected <T> T _invoke(UnsafeSupplier<T, Exception> unsafeSupplier)
            throws Exception {

        long startTime = System.currentTimeMillis();

        T t = unsafeSupplier.get();

        if (_log.isInfoEnabled()) {
            Thread thread = Thread.currentThread();

            StackTraceElement stackTraceElement = thread.getStackTrace()[2];

            _log.info(
                    StringBundler.concat(
                            "Invoking line ", stackTraceElement.getLineNumber(), " in ",
                            System.currentTimeMillis() - startTime, " ms"));
        }

        return t;
    }

    protected void _publishObjectDefinitions(
            List<Long> objectDefinitinIds, ServiceContext serviceContext)
            throws Exception {

        if (ListUtil.isEmpty(objectDefinitinIds)) {
            return;
        }

        ObjectDefinitionResource.Builder builder =
                _objectDefinitionResourceFactory.create();

        ObjectDefinitionResource objectDefinitionResource = builder.user(
                serviceContext.fetchUser()
        ).build();

        for (Long objectDefinitionId : objectDefinitinIds) {
            objectDefinitionResource.postObjectDefinitionPublish(
                    objectDefinitionId);
        }
    }

    protected String _removeFirst(String s, String oldSub) {
        int index = s.indexOf(oldSub);

        return s.substring(index + oldSub.length());
    }

    protected String _replace(
            String s, Map<String, String> stringUtilReplaceValues) {

        HashMap<String, String> aggregatedStringUtilReplaceValues =
                HashMapBuilder.putAll(
                        _classNameIdStringUtilReplaceValues
                ).putAll(
                        _releaseInfoStringUtilReplaceValues
                ).putAll(
                        stringUtilReplaceValues
                ).build();

        s = StringUtil.replace(
                s, "\"[#", "#]\"", aggregatedStringUtilReplaceValues);

        return StringUtil.replace(
                s, "[$", "$]", aggregatedStringUtilReplaceValues);
    }

    protected String _replace(String s, ServiceContext serviceContext)
            throws Exception {

        Group group = serviceContext.getScopeGroup();

        return StringUtil.replace(
                s,
                new String[] {
                        "[$COMPANY_ID$]", "[$GROUP_FRIENDLY_URL$]", "[$GROUP_ID$]",
                        "[$GROUP_KEY$]", "[$PORTAL_URL$]"
                },
                new String[] {
                        String.valueOf(group.getCompanyId()), group.getFriendlyURL(),
                        String.valueOf(serviceContext.getScopeGroupId()),
                        group.getGroupKey(), serviceContext.getPortalURL()
                });
    }

    protected String _replace(String s, String oldSub, String newSub) {
        return StringUtil.replace(s, oldSub, newSub);
    }

    protected void _setDefaultLayoutUtilityPageEntries(
            ServiceContext serviceContext)
            throws Exception {

        String json = SiteInitializerUtil.read(
                "/site-initializer/layout-utility-page-entries" +
                        "/default-utility-page-entries.json",
                _servletContext);

        if (json == null) {
            return;
        }

        JSONObject jsonObject = JSONFactoryUtil.createJSONObject(json);

        Iterator<String> iterator = jsonObject.keys();

        while (iterator.hasNext()) {
            String type = iterator.next();

            String name = jsonObject.getString(type);

            LayoutUtilityPageEntry layoutUtilityPageEntry =
                    _layoutUtilityPageEntryLocalService.fetchLayoutUtilityPageEntry(
                            serviceContext.getScopeGroupId(), name,
                            LayoutUtilityPageEntryTypeConverter.convertToInternalValue(
                                    type));

            if (layoutUtilityPageEntry != null) {
                _layoutUtilityPageEntryLocalService.
                        setDefaultLayoutUtilityPageEntry(
                                layoutUtilityPageEntry.getLayoutUtilityPageEntryId());
            }
        }
    }

    protected void _setPLOEntries(ServiceContext serviceContext)
            throws Exception {

        String json = SiteInitializerUtil.read(
                "/site-initializer/plo-entries.json", _servletContext);

        if (json == null) {
            return;
        }

        JSONArray jsonArray = JSONFactoryUtil.createJSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            _ploEntryLocalService.setPLOEntries(
                    serviceContext.getCompanyId(), serviceContext.getUserId(),
                    jsonObject.getString("key"),
                    SiteInitializerUtil.toMap(jsonObject.getString("value")));
        }
    }

    protected void _setResourcePermissions(
            long companyId, String name, JSONArray permissionsJSONArray,
            String primKey)
            throws Exception {

        if (permissionsJSONArray == null) {
            return;
        }

        for (int i = 0; i < permissionsJSONArray.length(); i++) {
            JSONObject permissionsJSONObject =
                    permissionsJSONArray.getJSONObject(i);

            int scope = permissionsJSONObject.getInt("scope");

            String roleName = permissionsJSONObject.getString("roleName");

            Role role = _roleLocalService.getRole(companyId, roleName);

            String[] actionIds = new String[0];

            JSONArray actionIdsJSONArray = permissionsJSONObject.getJSONArray(
                    "actionIds");

            if (actionIdsJSONArray != null) {
                for (int j = 0; j < actionIdsJSONArray.length(); j++) {
                    actionIds = ArrayUtil.append(
                            actionIds, actionIdsJSONArray.getString(j));
                }
            }

            _resourcePermissionLocalService.setResourcePermissions(
                    companyId, name, scope, primKey, role.getRoleId(), actionIds);
        }
    }

    protected Layout _updateDraftLayout(
            Layout draftLayout, JSONObject settingsJSONObject)
            throws Exception {

        UnicodeProperties unicodeProperties =
                draftLayout.getTypeSettingsProperties();

        Set<Map.Entry<String, String>> set = unicodeProperties.entrySet();

        set.removeIf(
                entry -> StringUtil.startsWith(entry.getKey(), "lfr-theme:"));

        JSONObject themeSettingsJSONObject = settingsJSONObject.getJSONObject(
                "themeSettings");

        if (themeSettingsJSONObject != null) {
            for (String key : themeSettingsJSONObject.keySet()) {
                unicodeProperties.put(
                        key, themeSettingsJSONObject.getString(key));
            }

            draftLayout = _layoutLocalService.updateLayout(
                    draftLayout.getGroupId(), draftLayout.isPrivateLayout(),
                    draftLayout.getLayoutId(), unicodeProperties.toString());

            draftLayout.setTypeSettingsProperties(unicodeProperties);
        }

        draftLayout = _layoutLocalService.updateLookAndFeel(
                draftLayout.getGroupId(), draftLayout.isPrivateLayout(),
                draftLayout.getLayoutId(),
                _getThemeId(
                        draftLayout.getCompanyId(), draftLayout.getThemeId(),
                        settingsJSONObject.getString("themeName")),
                settingsJSONObject.getString(
                        "colorSchemeName", draftLayout.getColorSchemeId()),
                settingsJSONObject.getString("css", draftLayout.getCss()));

        JSONObject masterPageJSONObject = settingsJSONObject.getJSONObject(
                "masterPage");

        if (masterPageJSONObject != null) {
            LayoutPageTemplateEntry layoutPageTemplateEntry =
                    _layoutPageTemplateEntryLocalService.
                            fetchLayoutPageTemplateEntry(
                                    draftLayout.getGroupId(),
                                    masterPageJSONObject.getString("key"));

            if (layoutPageTemplateEntry != null) {
                draftLayout = _layoutLocalService.updateMasterLayoutPlid(
                        draftLayout.getGroupId(), draftLayout.isPrivateLayout(),
                        draftLayout.getLayoutId(),
                        layoutPageTemplateEntry.getPlid());
            }
        }

        return draftLayout;
    }

    protected void _updateGroupSiteInitializerKey(long groupId) throws Exception {
        if (!FeatureFlagManagerUtil.isEnabled("LPS-165482")) {
            return;
        }

        Group group = _groupLocalService.getGroup(groupId);

        UnicodeProperties typeSettingsUnicodeProperties =
                group.getTypeSettingsProperties();

        typeSettingsUnicodeProperties.setProperty(
                "siteInitializerKey", getKey());

        _groupLocalService.updateGroup(
                group.getGroupId(), typeSettingsUnicodeProperties.toString());
    }

    protected void _updateLayoutSet(
            boolean privateLayout, ServiceContext serviceContext,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        LayoutSet layoutSet = _layoutSetLocalService.getLayoutSet(
                serviceContext.getScopeGroupId(), privateLayout);

        String resourcePath = "/site-initializer/layout-set";

        if (privateLayout) {
            resourcePath += "/private";
        }
        else {
            resourcePath += "/public";
        }

        String metadataJSON = SiteInitializerUtil.read(
                resourcePath + "/metadata.json", _servletContext);

        JSONObject metadataJSONObject = _jsonFactory.createJSONObject(
                (metadataJSON == null) ? "{}" : metadataJSON);

        String css = _replace(
                SiteInitializerUtil.read(
                        resourcePath + "/css.css", _servletContext),
                stringUtilReplaceValues);

        _layoutSetLocalService.updateLookAndFeel(
                serviceContext.getScopeGroupId(), privateLayout,
                _getThemeId(
                        serviceContext.getCompanyId(), StringPool.BLANK,
                        metadataJSONObject.getString("themeName")),
                layoutSet.getColorSchemeId(), css);

        URL url = _servletContext.getResource(resourcePath + "/logo.png");

        if (url != null) {
            _layoutSetLocalService.updateLogo(
                    serviceContext.getScopeGroupId(), privateLayout, true,
                    URLUtil.toByteArray(url));
        }

        JSONObject settingsJSONObject = metadataJSONObject.getJSONObject(
                "settings");

        if (settingsJSONObject == null) {
            return;
        }

        String js = SiteInitializerUtil.read(
                resourcePath + "/js.js", _servletContext);

        if (Validator.isNotNull(js)) {
            settingsJSONObject.put("javascript", js);
        }

        UnicodeProperties unicodeProperties = layoutSet.getSettingsProperties();

        for (String key : settingsJSONObject.keySet()) {
            unicodeProperties.put(key, settingsJSONObject.getString(key));
        }

        _layoutSetLocalService.updateSettings(
                serviceContext.getScopeGroupId(), privateLayout,
                unicodeProperties.toString());
    }

    protected void _updateLayoutSets(
            ServiceContext serviceContext,
            Map<String, String> stringUtilReplaceValues)
            throws Exception {

        _updateLayoutSet(false, serviceContext, stringUtilReplaceValues);
        _updateLayoutSet(true, serviceContext, stringUtilReplaceValues);
    }

    protected static final Log _log = LogFactoryUtil.getLog(
            BundleSiteInitializer.class);

    protected static final Snapshot<CommerceSiteInitializer>
            _commerceSiteInitializerSnapshot = new Snapshot<>(
            BundleSiteInitializer.class, CommerceSiteInitializer.class);
    protected static final ObjectMapper _objectMapper = new ObjectMapper();
    protected static final Snapshot<OSBSiteInitializer>
            _osbSiteInitializerSnapshot = new Snapshot<>(
            BundleSiteInitializer.class, OSBSiteInitializer.class);

    @Reference
    protected AccountEntryLocalService _accountEntryLocalService;

    @Reference
    protected AccountEntryOrganizationRelLocalService
            _accountEntryOrganizationRelLocalService;

    @Reference
    protected AccountGroupLocalService _accountGroupLocalService;

    @Reference
    protected AccountGroupRelService _accountGroupRelService;

    @Reference
    protected AccountResource.Factory _accountResourceFactory;

    @Reference
    protected AccountRoleLocalService _accountRoleLocalService;

    @Reference
    protected AccountRoleResource.Factory _accountRoleResourceFactory;

    @Reference
    protected ArchivedSettingsFactory _archivedSettingsFactory;

    @Reference
    protected AssetCategoryLocalService _assetCategoryLocalService;

    @Reference
    protected AssetListEntryLocalService _assetListEntryLocalService;

    protected BundleContext _bundleContext;

    protected Bundle _bundle;

    @Reference
    protected CETManager _cetManager;

    protected ClassLoader _classLoader;

    protected Map<String, String> _classNameIdStringUtilReplaceValues;

    @Reference
    protected ClientExtensionEntryLocalService _clientExtensionEntryLocalService;

    @Reference
    protected ConfigurationProvider _configurationProvider;

    @Reference
    protected DataDefinitionResource.Factory _dataDefinitionResourceFactory;

    @Reference
    protected DDMStructureLocalService _ddmStructureLocalService;

    @Reference
    protected DDMTemplateLocalService _ddmTemplateLocalService;

    @Reference
    protected DefaultDDMStructureHelper _defaultDDMStructureHelper;

    @Reference
    protected DLURLHelper _dlURLHelper;

    @Reference
    protected DocumentFolderResource.Factory _documentFolderResourceFactory;

    @Reference
    protected DocumentResource.Factory _documentResourceFactory;

    @Reference
    protected ExpandoValueLocalService _expandoValueLocalService;

    @Reference
    protected FragmentsImporter _fragmentsImporter;

    @Reference
    protected GroupLocalService _groupLocalService;

    @Reference
    protected JournalArticleLocalService _journalArticleLocalService;

    @Reference
    protected JSONFactory _jsonFactory;

    @Reference
    protected KnowledgeBaseArticleResource.Factory
            _knowledgeBaseArticleResourceFactory;

    @Reference
    protected KnowledgeBaseFolderResource.Factory
            _knowledgeBaseFolderResourceFactory;

    @Reference
    protected LayoutCopyHelper _layoutCopyHelper;

    @Reference
    protected LayoutLocalService _layoutLocalService;

    @Reference
    protected LayoutPageTemplateEntryLocalService
            _layoutPageTemplateEntryLocalService;

    @Reference
    protected LayoutPageTemplateStructureLocalService
            _layoutPageTemplateStructureLocalService;

    @Reference
    protected LayoutPageTemplateStructureRelLocalService
            _layoutPageTemplateStructureRelLocalService;

    @Reference
    protected LayoutSetLocalService _layoutSetLocalService;

    @Reference
    protected LayoutsImporter _layoutsImporter;

    @Reference
    protected LayoutUtilityPageEntryLocalService
            _layoutUtilityPageEntryLocalService;

    @Reference
    protected ListTypeDefinitionResource _listTypeDefinitionResource;

    @Reference
    protected ListTypeDefinitionResource.Factory
            _listTypeDefinitionResourceFactory;

    @Reference
    protected ListTypeEntryLocalService _listTypeEntryLocalService;

    @Reference
    protected ListTypeEntryResource _listTypeEntryResource;

    @Reference
    protected ListTypeEntryResource.Factory _listTypeEntryResourceFactory;

    @Reference
    protected NotificationTemplateResource.Factory
            _notificationTemplateResourceFactory;

    @Reference
    protected ObjectActionLocalService _objectActionLocalService;

    @Reference
    protected ObjectDefinitionLocalService _objectDefinitionLocalService;

    @Reference
    protected ObjectDefinitionResource.Factory _objectDefinitionResourceFactory;

    @Reference
    protected ObjectEntryLocalService _objectEntryLocalService;

    @Reference(target = "(object.entry.manager.storage.type=default)")
    protected ObjectEntryManager _objectEntryManager;

    @Reference
    protected ObjectFieldLocalService _objectFieldLocalService;

    @Reference
    protected ObjectFieldResource.Factory _objectFieldResourceFactory;

    @Reference
    protected ObjectRelationshipLocalService _objectRelationshipLocalService;

    @Reference
    protected ObjectRelationshipResource.Factory
            _objectRelationshipResourceFactory;

    @Reference
    protected OrganizationLocalService _organizationLocalService;

    @Reference
    protected OrganizationResource.Factory _organizationResourceFactory;

    @Reference
    protected PLOEntryLocalService _ploEntryLocalService;

    @Reference
    protected Portal _portal;

    @Reference
    protected PortletPreferencesLocalService _portletPreferencesLocalService;

    protected Map<String, String> _releaseInfoStringUtilReplaceValues;

    @Reference
    protected ResourceActionLocalService _resourceActionLocalService;

    @Reference
    protected ResourcePermissionLocalService _resourcePermissionLocalService;

    @Reference
    protected RoleLocalService _roleLocalService;

    @Reference
    protected SAPEntryLocalService _sapEntryLocalService;

    @Reference
    protected SegmentsEntryLocalService _segmentsEntryLocalService;

    @Reference
    protected SegmentsExperienceLocalService _segmentsExperienceLocalService;

    protected ServletContext _servletContext;

    @Reference
    protected SiteNavigationMenuItemLocalService
            _siteNavigationMenuItemLocalService;

    @Reference
    protected SiteNavigationMenuItemTypeRegistry
            _siteNavigationMenuItemTypeRegistry;

    @Reference
    protected SiteNavigationMenuLocalService _siteNavigationMenuLocalService;

    @Reference
    protected StructuredContentFolderResource.Factory
            _structuredContentFolderResourceFactory;

    @Reference
    protected StyleBookEntryZipProcessor _styleBookEntryZipProcessor;

    @Reference
    protected TaxonomyCategoryResource.Factory _taxonomyCategoryResourceFactory;

    @Reference
    protected TaxonomyVocabularyResource.Factory
            _taxonomyVocabularyResourceFactory;

    @Reference
    protected TemplateEntryLocalService _templateEntryLocalService;

    @Reference
    protected ThemeLocalService _themeLocalService;

    @Reference
    protected UserAccountResource.Factory _userAccountResourceFactory;

    @Reference
    protected UserGroupLocalService _userGroupLocalService;

    @Reference
    protected UserLocalService _userLocalService;

    @Reference
    protected WorkflowDefinitionLinkLocalService
            _workflowDefinitionLinkLocalService;

    @Reference
    protected WorkflowDefinitionResource.Factory
            _workflowDefinitionResourceFactory;

    @Reference
    protected ZipWriterFactory _zipWriterFactory;

    protected class SiteNavigationMenuItemSetting {

        public String className;
        public String classPK;
        public String classTypeId = StringPool.BLANK;
        public String title;
        public String type = StringPool.BLANK;

    }

    protected class SiteNavigationMenuItemSettingsBuilder {

        public Map<String, SiteNavigationMenuItemSetting> build() {
            return _siteNavigationMenuItemSettings;
        }

        public void put(
                String key,
                SiteNavigationMenuItemSetting siteNavigationMenuItemSetting) {

            _siteNavigationMenuItemSettings.put(
                    key, siteNavigationMenuItemSetting);
        }

        protected Map<String, SiteNavigationMenuItemSetting>
                _siteNavigationMenuItemSettings = new HashMap<>();

    }

    @Activate
    public void activate(BundleContext bundleContext) {

        _bundleContext = bundleContext;
        _bundle = bundleContext.getBundle();

    }

}
