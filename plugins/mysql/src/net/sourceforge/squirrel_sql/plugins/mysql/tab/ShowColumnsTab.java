package net.sourceforge.squirrel_sql.plugins.mysql.tab;
/*
 * Copyright (C) 2003 Colin Bell
 * colbell@users.sourceforge.net
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
import net.sourceforge.squirrel_sql.fw.util.StringManager;
import net.sourceforge.squirrel_sql.fw.util.StringManagerFactory;
import net.sourceforge.squirrel_sql.fw.util.log.ILogger;
import net.sourceforge.squirrel_sql.fw.util.log.LoggerController;
/**
 * This tab will display the results of an "SHOW FULL COLUMNS" command over
 * the currently selected tables.
 *
 * @author <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
public class ShowColumnsTab extends BaseSQLTab
{
	/** Logger for this class. */
	private final static ILogger s_log =
		LoggerController.createLogger(ShowColumnsTab.class);

	/** Internationalized strings for this class. */
	private static final StringManager s_stringMgr =
		StringManagerFactory.getStringManager(ShowColumnsTab.class);

	public ShowColumnsTab()
	{
		super(s_stringMgr.getString("ShowColumnsTab.title"),
				s_stringMgr.getString("ShowColumnsTab.hint"));
	}

	protected String getSQL()
	{
		final String tbl = getDatabaseObjectInfo().getQualifiedName();
		return "show full columns from " + tbl;
	}
}
