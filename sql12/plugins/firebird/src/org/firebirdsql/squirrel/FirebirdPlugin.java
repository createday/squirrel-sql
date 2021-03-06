package org.firebirdsql.squirrel;

import javax.swing.JMenu;

import net.sourceforge.squirrel_sql.client.IApplication;
import net.sourceforge.squirrel_sql.client.action.ActionCollection;
import net.sourceforge.squirrel_sql.client.gui.session.ObjectTreeInternalFrame;
import net.sourceforge.squirrel_sql.client.gui.session.SQLInternalFrame;
import net.sourceforge.squirrel_sql.client.plugin.DefaultSessionPlugin;
import net.sourceforge.squirrel_sql.client.plugin.IPluginResourcesFactory;
import net.sourceforge.squirrel_sql.client.plugin.PluginException;
import net.sourceforge.squirrel_sql.client.plugin.PluginResourcesFactory;
import net.sourceforge.squirrel_sql.client.plugin.PluginSessionCallback;
import net.sourceforge.squirrel_sql.client.session.IObjectTreeAPI;
import net.sourceforge.squirrel_sql.client.session.ISession;
import net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.ObjectTreePanel;
import net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.expanders.TableWithChildNodesExpander;
import net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.tabs.DatabaseObjectInfoTab;
import net.sourceforge.squirrel_sql.client.session.mainpanel.sqltab.AdditionalSQLTab;
import net.sourceforge.squirrel_sql.fw.dialects.DialectFactory;
import net.sourceforge.squirrel_sql.fw.gui.GUIUtils;
import net.sourceforge.squirrel_sql.fw.sql.DatabaseObjectType;
import net.sourceforge.squirrel_sql.fw.resources.IResources;
import net.sourceforge.squirrel_sql.fw.util.StringManager;
import net.sourceforge.squirrel_sql.fw.util.StringManagerFactory;

import org.firebirdsql.squirrel.act.ActivateIndexAction;
import org.firebirdsql.squirrel.act.DeactivateIndexAction;
import org.firebirdsql.squirrel.exp.AllIndexesParentExpander;
import org.firebirdsql.squirrel.exp.DatabaseExpander;
import org.firebirdsql.squirrel.exp.FirebirdTableIndexExtractorImpl;
import org.firebirdsql.squirrel.exp.FirebirdTableTriggerExtractorImpl;
import org.firebirdsql.squirrel.tab.DomainDetailsTab;
import org.firebirdsql.squirrel.tab.GeneratorDetailsTab;
import org.firebirdsql.squirrel.tab.IndexInfoTab;
import org.firebirdsql.squirrel.tab.ProcedureSourceTab;
import org.firebirdsql.squirrel.tab.TriggerDetailsTab;
import org.firebirdsql.squirrel.tab.TriggerSourceTab;
import org.firebirdsql.squirrel.tab.ViewSourceTab;

public class FirebirdPlugin extends DefaultSessionPlugin {

	private static final StringManager s_stringMgr =
		StringManagerFactory.getStringManager(FirebirdPlugin.class);

    /** API for the Obejct Tree. */
    private IObjectTreeAPI _treeAPI;

 	private IResources _resources;

	private IPluginResourcesFactory _resourcesFactory = new PluginResourcesFactory();
	/**
	 * @param resourcesFactory the resourcesFactory to set
	 */
	public void setResourcesFactory(IPluginResourcesFactory resourcesFactory)
	{
		_resourcesFactory = resourcesFactory;
	}
    
	public interface IMenuResourceKeys
	{
//		String CHECK_TABLE = "checktable";
		String FIREBIRD = "firebird";
	}
	
	
	/** Firebird menu. */
	private JMenu _firebirdMenu;

    /**
     * Return the internal name of this plugin.
     *
     * @return  the internal name of this plugin.
     */
    public String getInternalName()
    {
        return "firebird";
    }

    /**
     * Return the descriptive name of this plugin.
     *
     * @return  the descriptive name of this plugin.
     */
    public String getDescriptiveName()
    {
        return "Firebird Plugin";
    }

    /**
     * Returns the current version of this plugin.
     *
     * @return  the current version of this plugin.
     */
    public String getVersion()
    {
        return "0.02";
    }

    /**
     * Returns the authors name.
     *
     * @return  the authors name.
     */
    public String getAuthor()
    {
        return "Roman Rokytskyy";
    }

    /**
     * @see net.sourceforge.squirrel_sql.client.plugin.IPlugin#getChangeLogFileName()
     */
    public String getChangeLogFileName()
    {
        return "changes.txt";
    }

    /**
     * @see net.sourceforge.squirrel_sql.client.plugin.IPlugin#getHelpFileName()
     */
    public String getHelpFileName()
    {
        return "doc/readme.html";
    }

    /**
     * @see net.sourceforge.squirrel_sql.client.plugin.IPlugin#getLicenceFileName()
     */
    public String getLicenceFileName()
    {
        return "licence.txt";
    }

	/**
	 * Load this plugin.
	 *
	 * @param	app	 Application API.
	 */
	public synchronized void load(IApplication app) throws PluginException
	{
		super.load(app);
		_resources = _resourcesFactory.createResource(getClass().getName(), this);
	}

	/**
	 * Initialize this plugin.
	 */
	public synchronized void initialize() throws PluginException
	{
		super.initialize();
		final IApplication app = getApplication();
		final ActionCollection coll = app.getActionCollection();

		coll.add(new ActivateIndexAction(app, _resources, this));
		coll.add(new DeactivateIndexAction(app, _resources, this));

		_firebirdMenu = createFirebirdMenu();
		app.addToMenu(IApplication.IMenuIDs.SESSION_MENU, _firebirdMenu);
        super.registerSessionMenu(_firebirdMenu);
	}

    /**
     * Application is shutting down so save preferences.
     */
    public void unload()
    {
        super.unload();
    }

   public boolean allowsSessionStartedInBackground()
   {
      return true;
   }

   /**
    * Session has been started. If this is an Oracle session then
    * register an extra expander for the Schema nodes to show
    * Oracle Packages.
    *
    * @param   session     Session that has started.
    *
    * @return  <TT>true</TT> if session is Oracle in which case this plugin
    *                          is interested in it.
    */
   public PluginSessionCallback sessionStarted(final ISession session)
   {
       if (!isPluginSession(session)) {
           return null;
       }
       GUIUtils.processOnSwingEventThread(new Runnable() {
           public void run() {
               updateTreeApi(session);
           }
       });
       return new PluginSessionCallback()
       {
           public void sqlInternalFrameOpened(SQLInternalFrame sqlInternalFrame, ISession sess)
           {
           }

           public void objectTreeInternalFrameOpened(ObjectTreeInternalFrame objectTreeInternalFrame, ISession sess)
           {
           }

          @Override
          public void objectTreeInSQLTabOpened(ObjectTreePanel objectTreePanel)
          {
          }

          @Override
          public void additionalSQLTabOpened(AdditionalSQLTab additionalSQLTab)
          {
          }
       };
   }

    @Override
    protected boolean isPluginSession(ISession session) {
        return DialectFactory.isFirebird(session.getMetaData());
    }
    
    private void updateTreeApi(ISession session) {
        _treeAPI = session.getSessionInternalFrame().getObjectTreeAPI();

        // Tabs to add to the database node.
        _treeAPI.addDetailTab(DatabaseObjectType.SEQUENCE, new DatabaseObjectInfoTab());
        _treeAPI.addDetailTab(DatabaseObjectType.TRIGGER, new DatabaseObjectInfoTab());
        _treeAPI.addDetailTab(DatabaseObjectType.TRIGGER_TYPE_DBO, new DatabaseObjectInfoTab());
        _treeAPI.addDetailTab(DatabaseObjectType.INDEX, new DatabaseObjectInfoTab());
        _treeAPI.addDetailTab(DatabaseObjectType.INDEX, new IndexInfoTab());

        // Expanders.
        _treeAPI.addExpander(IObjectTypes.INDEX_PARENT, new AllIndexesParentExpander());

        _treeAPI.addExpander(DatabaseObjectType.SESSION, new DatabaseExpander(this));

        TableWithChildNodesExpander tableExp = new TableWithChildNodesExpander();
        tableExp.setTableTriggerExtractor(new FirebirdTableTriggerExtractorImpl());
        tableExp.setTableIndexExtractor(new FirebirdTableIndexExtractorImpl());
        _treeAPI.addExpander(DatabaseObjectType.TABLE, tableExp);
        
        
        _treeAPI.addDetailTab(DatabaseObjectType.SEQUENCE, new GeneratorDetailsTab());
        _treeAPI.addDetailTab(DatabaseObjectType.DATATYPE, new DomainDetailsTab());
        _treeAPI.addDetailTab(DatabaseObjectType.TRIGGER, new TriggerDetailsTab());
        // i18n[firebird.showTrigger=Show trigger source]
        _treeAPI.addDetailTab(DatabaseObjectType.TRIGGER, 
                new TriggerSourceTab(s_stringMgr.getString("firebird.showTrigger")));
        // i18n[firebird.showProcedureSource=Show procedure source]
        _treeAPI.addDetailTab(DatabaseObjectType.PROCEDURE, 
                new ProcedureSourceTab(s_stringMgr.getString("firebird.showProcedureSource")));
        // i18n[firebird.showView=Show view source]
        _treeAPI.addDetailTab(DatabaseObjectType.VIEW, 
                new ViewSourceTab(s_stringMgr.getString("firebird.showView")));


        final ActionCollection coll = getApplication().getActionCollection();
        _treeAPI.addToPopup(DatabaseObjectType.INDEX, coll.get(ActivateIndexAction.class));
        _treeAPI.addToPopup(DatabaseObjectType.INDEX, coll.get(DeactivateIndexAction.class));        
    }
    
	/**
	 * Create menu containing all Firebird actions.
	 *
	 * @return	The menu object.
	 */
	private JMenu createFirebirdMenu()
	{
		final IApplication app = getApplication();
		final ActionCollection coll = app.getActionCollection();

		final JMenu firebirdMenu = _resources.createMenu(IMenuResourceKeys.FIREBIRD);

		_resources.addToMenu(coll.get(ActivateIndexAction.class), firebirdMenu);
		_resources.addToMenu(coll.get(DeactivateIndexAction.class), firebirdMenu);

		return firebirdMenu;
	}

}
