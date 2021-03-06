/** 
 * Copyright (c) 2012 by JP Moresmau
 * This code is made available under the terms of the Eclipse Public License,
 * version 1.0 (EPL). See http://www.eclipse.org/legal/epl-v10.html
 */
package net.sf.eclipsefp.haskell.buildwrapper.usage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.eclipsefp.haskell.buildwrapper.BuildWrapperPlugin;
import net.sf.eclipsefp.haskell.buildwrapper.types.Module;
import net.sf.eclipsefp.haskell.buildwrapper.types.ReferenceLocation;
import net.sf.eclipsefp.haskell.buildwrapper.types.SearchResultLocation;
import net.sf.eclipsefp.haskell.buildwrapper.types.SymbolDef;
import net.sf.eclipsefp.haskell.buildwrapper.types.UsageResults;
import net.sf.eclipsefp.haskell.buildwrapper.util.BWText;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.json.JSONArray;
import org.json.JSONException;


/**
 * The DB side (sqlite) of usage queries
 * @author JP Moresmau
 *
 */
public class UsageDB {
	private Connection conn;

	
	public UsageDB(){
		// the db file is inside the project metadata folder
		IPath p=BuildWrapperPlugin.getDefault().getStateLocation().append("usage.db");
		File f=p.toFile();
		f.getParentFile().mkdirs();
		try {
			Class.forName("org.sqlite.JDBC");
			conn =
			      DriverManager.getConnection("jdbc:sqlite:"+f.getAbsolutePath());
			conn.setAutoCommit(true);
			// foreign key support
			try (Statement s=conn.createStatement()) {
				s.executeUpdate("PRAGMA foreign_keys = ON;");
			}
			// we do explicit commits for performance and coherence
			conn.setAutoCommit(false);
			setup();
		} catch (Exception e){
			BuildWrapperPlugin.logError(BWText.error_setup_db, e);
		}
	}
	
	public void close(){
		if (conn!=null){
			try {
				conn.close();
			} catch (SQLException sqle){
				BuildWrapperPlugin.logError(BWText.error_db, sqle);
			}
			conn=null;
		}
	}
	
	public void commit() throws SQLException{
		checkConnection();
		conn.commit();
	}
	
	public boolean isValid(){
		return conn!=null;
	}
	
	protected void checkConnection() throws SQLException{
		if (conn==null){
			throw new SQLException(BWText.error_no_db);
		}
	}
	
	/**
	 * create tables and indices if needed
	 * @throws SQLException
	 */
	protected void setup() throws SQLException{
		checkConnection();
		try (Statement s=conn.createStatement()) {
			s.execute("create table if not exists files (fileid INTEGER PRIMARY KEY ASC,project TEXT not null, name TEXT not null)");
			s.execute("create unique index if not exists filenames on files (project, name)");
			
			s.execute("create table if not exists modules (moduleid INTEGER PRIMARY KEY ASC,package TEXT not null, module TEXT not null,fileid INTEGER,location TEXT,foreign key (fileid) references files(fileid) on delete set null)");
			s.execute("create unique index if not exists modulenames on modules (package,module)");
			
			//s.execute("create index if not exists modulefiles on modules (fileid)");
			
			s.execute("create table if not exists module_usages (moduleid INTEGER not null,fileid INTEGER not null,section TEXT not null,location TEXT not null,foreign key (fileid) references files(fileid) on delete cascade,foreign key (moduleid) references modules(moduleid) on delete cascade)");
		
			s.execute("create table if not exists symbols(symbolid INTEGER PRIMARY KEY ASC,symbol TEXT,type INTEGER,moduleid INTEGER not null,foreign key (moduleid) references modules(moduleid) on delete set null)");
			s.execute("create index if not exists symbolnames on symbols (symbol,type,moduleid)");
			
			s.execute("create table if not exists symbol_defs(symbolid INTEGER not null,fileid INTEGER not null,section TEXT not null,location TEXT not null,foreign key (symbolid) references symbols(symbolid) on delete cascade,foreign key (fileid) references files(fileid) on delete cascade)");
			
			s.execute("create table if not exists symbol_usages(symbolid INTEGER not null,fileid INTEGER not null,section TEXT not null,location TEXT not null,foreign key (symbolid) references symbols(symbolid) on delete cascade,foreign key (fileid) references files(fileid) on delete cascade)");
			
			
		}
		conn.commit();
	}
	
	public long getFileID(IFile f) throws SQLException{
		checkConnection();
		Long fileID=null;
		try (PreparedStatement ps=conn.prepareStatement("select fileid from files where project=? and name=?")) {
			ps.setString(1, f.getProject().getName());
			ps.setString(2, f.getProjectRelativePath().toPortableString());
			try (ResultSet rs=ps.executeQuery()) {
				if (rs.next()){
					fileID=rs.getLong(1);
				}
			}
		}
		if (fileID==null){
			try (PreparedStatement ps=conn.prepareStatement("insert into files (project,name) values(?,?)")) {
				ps.setString(1, f.getProject().getName());
				ps.setString(2, f.getProjectRelativePath().toPortableString());
				ps.execute();
				try (ResultSet rs=ps.getGeneratedKeys()) {
					rs.next();
					fileID=rs.getLong(1);
				}
			 }
		}
		return fileID;
	}
	
	public void removeFile(IFile f) throws SQLException{
		checkConnection();
		Long fileID=null;
		try (PreparedStatement ps=conn.prepareStatement("select fileid from files where project=? and name=?")) {
			ps.setString(1, f.getProject().getName());
			ps.setString(2, f.getProjectRelativePath().toPortableString());
			try (ResultSet rs=ps.executeQuery()) {
				if (rs.next()){
					fileID=rs.getLong(1);
				}
			}
		}
		if (fileID!=null){
			try (PreparedStatement ps=conn.prepareStatement("delete from files where fileid=?")) {
				ps.setLong(1, fileID);
				ps.executeUpdate();
			}
		}
	}
	
	public void clearUsageInFile(long fileid) throws SQLException{
		checkConnection();
		try (PreparedStatement ps=conn.prepareStatement("delete from module_usages where fileid=?")) {
			ps.setLong(1, fileid);
			ps.executeUpdate();
		}
		try (PreparedStatement ps=conn.prepareStatement("delete from symbol_usages where fileid=?")) {
			ps.setLong(1, fileid);
			ps.executeUpdate();
		}
		try (PreparedStatement ps=conn.prepareStatement("delete from symbol_defs where fileid=?")) {
			ps.setLong(1, fileid);
			ps.executeUpdate();
		}
	}
	
	
	
	public void setModuleUsages(long fileid,Collection<ObjectUsage> usages) throws SQLException{
		checkConnection();
		try (PreparedStatement ps=conn.prepareStatement("insert into module_usages values(?,?,?,?)")) {
			for (ObjectUsage usg:usages){
				ps.setLong(1, usg.getObjectID());
				ps.setLong(2, fileid);
				ps.setString(3, usg.getSection());
				ps.setString(4, usg.getLocation());
				ps.addBatch();
			}
			ps.executeBatch();
		}
	}
	
	public void setSymbolUsages(long fileid,Collection<ObjectUsage> usages) throws SQLException{
		checkConnection();
		try (PreparedStatement ps=conn.prepareStatement("insert into symbol_usages values(?,?,?,?)")) {
			for (ObjectUsage usg:usages){
				ps.setLong(1, usg.getObjectID());
				ps.setLong(2, fileid);
				ps.setString(3, usg.getSection());
				ps.setString(4, usg.getLocation());
				ps.addBatch();
			}
			ps.executeBatch();
		}
	}
	
	public void setSymbolDefinitions(long fileid,Collection<ObjectUsage> usages) throws SQLException{
		checkConnection();
		try (PreparedStatement ps=conn.prepareStatement("insert into symbol_defs values(?,?,?,?)")) {
			for (ObjectUsage usg:usages){
				ps.setLong(1, usg.getObjectID());
				ps.setLong(2, fileid);
				ps.setString(3, usg.getSection());
				ps.setString(4, usg.getLocation());
				ps.addBatch();
			}
			ps.executeBatch();
		}
	}
	
	public long getModuleID(String pkg,String module,Long fileID,String loc) throws SQLException {
		checkConnection();
		Long moduleID=null;
		try (PreparedStatement ps=conn.prepareStatement("select moduleid from modules where package=? and module=?")) {
			ps.setString(1, pkg);
			ps.setString(2, module);
			try (ResultSet rs=ps.executeQuery()) {
				if (rs.next()){
					moduleID=rs.getLong(1);
				}
			}
		}
		if (moduleID==null){
			 try (PreparedStatement ps=conn.prepareStatement("insert into modules (package,module,fileid,location) values(?,?,?,?)")) {
				ps.setString(1, pkg);
				ps.setString(2, module);
				if (fileID!=null){
					ps.setLong(3, fileID);
				} else {
					ps.setNull(3, Types.NUMERIC);
				}
				if (loc!=null){
					ps.setString(4, loc);
				} else {
					ps.setNull(4, Types.VARCHAR);
				}
				ps.execute();
				try (ResultSet rs=ps.getGeneratedKeys()) {
					rs.next();
					moduleID=rs.getLong(1);
				}
			 }
		} else if (fileID!=null){
			try (PreparedStatement ps=conn.prepareStatement("update modules set fileid=?,location=? where moduleid=?")) {
				ps.setLong(1, fileID);
				if (loc!=null){
					ps.setString(2, loc);
				} else {
					ps.setNull(2, Types.VARCHAR);
				}
				ps.setLong(3, moduleID);
				ps.execute();
			}
		}
		return moduleID;
	}
	
	//,String section,String loc
	public long getSymbolID(long moduleid,String symbol,int type) throws SQLException {
		checkConnection();
		Long symbolID=null;
		try (PreparedStatement ps=conn.prepareStatement("select symbolid from symbols where moduleid=? and symbol=? and type=?")) {
			ps.setLong(1, moduleid);
			ps.setString(2, symbol);
			ps.setInt(3, type);
			try (ResultSet rs=ps.executeQuery()) {
				if (rs.next()){
					symbolID=rs.getLong(1);
				}
			}
		}
		if (symbolID==null){
			try (PreparedStatement ps=conn.prepareStatement("insert into symbols (symbol,type,moduleid) values(?,?,?)")) {
				ps.setString(1, symbol);
				ps.setInt(2, type);
				ps.setLong(3, moduleid);
				ps.execute();
				try (ResultSet rs=ps.getGeneratedKeys()) {
					rs.next();
					symbolID=rs.getLong(1);
				}
			 }
		} 
		/*if (loc!=null){
			 ps=conn.prepareStatement("insert into symbol_defs values(section=?,location=? where symbolid=?");
			 try {
				
				if (section!=null){
					ps.setString(1, section);
				} else {
					ps.setNull(1, Types.VARCHAR);
				}
				if (loc!=null){
					ps.setString(2, loc);
				} 
//				else {
//					ps.setNull(2, Types.VARCHAR);
//				}
				ps.setLong(3, symbolID);
				ps.execute();
			} finally {
				ps.close();
			}
		}*/
		return symbolID;
	}
	
	public List<Module> listLocalModules() throws SQLException {
		checkConnection();
		List<Module> ret=new ArrayList<>();
		try (PreparedStatement ps=conn.prepareStatement("select moduleid,package,module,fileid from modules where fileid is not null")) {
			try (ResultSet rs=ps.executeQuery()) {
				while (rs.next()){
					long moduleID=rs.getLong(1);
					String packageName=rs.getString(2);
					String moduleName=rs.getString(3);
					long fileid=rs.getLong(4);
					Module mod=new Module(moduleID,packageName,moduleName,fileid);
					ret.add(mod);
				}
			}
		}
		return ret;
	}
	
	public IFile getFile(Long fileid) throws SQLException {
		if (fileid==null){
			return null;
		}
		checkConnection();
		try (PreparedStatement ps=conn.prepareStatement("select project,name from files where fileid =?")) {
			ps.setLong(1, fileid);
			try (ResultSet rs=ps.executeQuery()) {
				if (rs.next()){
					
					String project=rs.getString(1);
					String name=rs.getString(2);
					IProject p=ResourcesPlugin.getWorkspace().getRoot().getProject(project);
					if (p!=null){
						return p.getFile(name);
					}
				}
			}
		}
		return null;
	}
	
	public boolean knowsProject(String project) throws SQLException{
		checkConnection();
		try (PreparedStatement ps=conn.prepareStatement("select project from files where project=?")) {
			ps.setString(1, project);
			try (ResultSet rs=ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	public UsageResults getModuleDefinitions(String pkg,String module,IProject p,boolean exact) throws SQLException {
		checkConnection();
		StringBuilder sb=new StringBuilder("select m.fileid,'module ' || module,m.location,1 from modules m");
		if (p!=null){
			sb.append(",files f");
		}
		if (exact){
			sb.append(" where m.module=?");
		} else {
			sb.append(" where m.module LIKE ? ESCAPE '\\'");
		}
		if (pkg!=null){
			sb.append(" and m.package=?");
		}
		if (p!=null){
			sb.append(" and f.fileid=m.fileid and f.project=?");
		}
		sb.append(" and m.location is not null");
		return getUsageResults(pkg, module, p, sb.toString());
	}
	
	public List<String> findModules(String pkg,String module,IProject p) throws SQLException {
		checkConnection();
		StringBuilder sb=new StringBuilder("select module from modules m");
		if (p!=null){
			sb.append(",files f");
		}
		sb.append(" where m.module LIKE ? ESCAPE '\\'");
		if (pkg!=null){
			sb.append(" and m.package=?");
		}
		if (p!=null){
			sb.append(" and f.fileid=m.fileid and f.project=?");
		}
		List<String> ret=new ArrayList<>();
		try (PreparedStatement ps=conn.prepareStatement(sb.toString())) {
			int ix=1;
			if (module!=null){
				ps.setString(ix++, module);
			}
			if (pkg!=null){
				ps.setString(ix++, pkg);
			}
			if (p!=null){
				ps.setString(ix++, p.getName());
			}

			try (ResultSet rs=ps.executeQuery()) {
				while (rs.next()){
					ret.add(rs.getString(1));
				}
			}
		}
		return ret;
	}
	
	public UsageResults getSymbolDefinitions(String pkg,String module,String symbol,int type,IProject p,boolean exact) throws SQLException {
		return getSymbols(pkg, module, symbol, type, p, exact, "symbol_defs");
	}
			
	public UsageResults getModuleReferences(String pkg,String module,IProject p,boolean exact) throws SQLException {
		checkConnection();
		StringBuilder sb=new StringBuilder("select mu.fileid,mu.section,mu.location,0 from module_usages mu, modules m");
		if (p!=null){
			sb.append(",files f");
		}
		sb.append(" where mu.moduleid=m.moduleid");
		if (exact){
			sb.append(" and m.module=?");
		} else {
			sb.append(" and m.module LIKE ? ESCAPE '\\'");
		}
		if (pkg!=null){
			sb.append(" and m.package=?");
		}
		if (p!=null){
			sb.append(" and f.fileid=mu.fileid and f.project=?");
		}
		return getUsageResults(pkg, module, p, sb.toString());	
	}
	
	public UsageResults getSymbolReferences(String pkg,String module,String symbol,int type,IProject p,boolean exact) throws SQLException {
		return getSymbols(pkg, module, symbol, type, p, exact, "symbol_usages");
	}
	
	private UsageResults getSymbols(String pkg,String module,String symbol,int type,IProject p,boolean exact,String table) throws SQLException {
		checkConnection();
		StringBuilder sb=new StringBuilder("select su.fileid,su.section,su.location,0 from "+table+" su,symbols s, modules m");
		if (p!=null){
			sb.append(",files f");
		}
		sb.append(" where s.symbolid=su.symbolid and s.moduleid=m.moduleid");
		if (module!=null){
			sb.append(" and m.module=?");
		}
		if (pkg!=null){
			sb.append(" and m.package=?");
		}
		if (p!=null){
			sb.append(" and f.fileid=su.fileid and f.project=?");
		}
		if (exact){
			sb.append(" and s.symbol=?");
		} else {
			sb.append(" and s.symbol LIKE ? ESCAPE '\\'");
		}
		if (type>0){
			sb.append(" and s.type=?");
		}
		return getUsageResults(pkg, module, p,symbol,type, sb.toString());	
	}
	
	private UsageResults getUsageResults(String pkg,String module,IProject p,String query) throws SQLException{
		try (PreparedStatement ps=conn.prepareStatement(query)) {
			int ix=1;
			if (module!=null){
				ps.setString(ix++, module);
			}
			if (pkg!=null){
				ps.setString(ix++, pkg);
			}
			if (p!=null){
				ps.setString(ix++, p.getName());
			}
			return getUsageResults(ps,query);
		}
	}
	
	private UsageResults getUsageResults(String pkg,String module,IProject p,String symbol,int type,String query) throws SQLException{
		try (PreparedStatement ps=conn.prepareStatement(query)) {
			int ix=1;
			if (module!=null){
				ps.setString(ix++, module);
			}
			if (pkg!=null){
				ps.setString(ix++, pkg);
			}
			if (p!=null){
				ps.setString(ix++, p.getName());
			}
			ps.setString(ix++,symbol);
			if (type>0){
				ps.setInt(ix++, type);
			}
			return getUsageResults(ps,query);
		}
	}
		
	private UsageResults getUsageResults(PreparedStatement ps,String query) throws SQLException{
		Map<Long,Map<String,Collection<SearchResultLocation>>> m=new HashMap<>();
	
		try (ResultSet rs=ps.executeQuery()) {
			while (rs.next()){
				long fileid=rs.getLong(1);
				Map<String,Collection<SearchResultLocation>> sections=m.get(fileid);
				if (sections==null){
					sections=new HashMap<>();
					m.put(fileid, sections);
				}
				String section=rs.getString(2);
				Collection<SearchResultLocation> locs=sections.get(section);
				if (locs==null){
					locs=new ArrayList<>();
					sections.put(section,locs);
				}
				//IProject p=ResourcesPlugin.getWorkspace().getRoot().getProject(project);
				//if (p!=null){
					//IFile f=p.getFile(name);
					String loc=rs.getString(3);
					boolean def=rs.getBoolean(4);
					try {
						SearchResultLocation l=new SearchResultLocation(null, new JSONArray(loc));
						l.setDefinition(def);
						locs.add(l);
						
					} catch (JSONException je){
						BuildWrapperPlugin.logError(je.getLocalizedMessage(), je);
					}
				//}
			}
		}

		UsageResults ret=new UsageResults();
		for (Long fileid:m.keySet()){
			IFile f=getFile(fileid);
			ret.put(f, m.get(fileid));
		}
		return ret;
	}
	
	/**
	 * unused for the moment, we use cached outline results for auto completion
	 * @param p
	 * @return
	 * @throws SQLException
	 */
	public List<SymbolDef> listDefinedSymbols(IProject p) throws SQLException{
		checkConnection();
		StringBuilder sb=new StringBuilder();
		sb.append("select m.module,s.symbol,s.type from modules m, symbols s,symbol_defs sd, files f where ");
		sb.append("f.project=? ");
		sb.append("and m.moduleid=s.moduleid ");
		sb.append("and f.fileid is not null ");
		sb.append("and f.fileid=m.fileid ");
		sb.append("and s.symbolid=sd.symbolid ");
		sb.append("and sd.location is not null");
		List<SymbolDef> ret=new ArrayList<>();
		try (PreparedStatement ps=conn.prepareStatement(sb.toString())) {
			ps.setString(1, p.getName());
			try (ResultSet rs=ps.executeQuery()) {
				while (rs.next()){
					String mod=rs.getString(1);
					String sym=rs.getString(2);
					int type=rs.getInt(3);
					SymbolDef sd=new SymbolDef(mod,sym, type);
					ret.add(sd);
				}
			}
		}
		return ret;
	}
	
	public Map<String,List<ReferenceLocation>> listReferencesInFile(IFile f)throws SQLException,JSONException{
		checkConnection();
		long fileid=getFileID(f);
		Map<String,List<ReferenceLocation>> ret=new HashMap<>();
		try (PreparedStatement ps=conn.prepareStatement("select mu.section,m.module,mu.location from module_usages mu,modules m where mu.fileid=? and mu.moduleid=m.moduleid")) {
			ps.setLong(1, fileid);
			try (ResultSet rs=ps.executeQuery()) {
				while(rs.next()){
					addReference(f,ret, rs.getString(1), rs.getString(2), rs.getString(3),true);
				}
			}
		}
		try (PreparedStatement ps=conn.prepareStatement("select su.section,m.module,s.symbol,su.location from symbol_usages su,modules m,symbols s where su.fileid=? and s.symbolid=su.symbolid and s.moduleid=m.moduleid")) {
			ps.setLong(1, fileid);
			try (ResultSet rs=ps.executeQuery()) {
				while(rs.next()){
					addReference(f,ret, rs.getString(1), rs.getString(2)+"."+rs.getString(3), rs.getString(4),false);
				}
			}
		}
		return ret;
	}
	
	private void addReference(IFile f,Map<String,List<ReferenceLocation>> m,String section,String name,String loc,boolean mod) throws JSONException{
		List<ReferenceLocation> s=m.get(section);
		if (s==null){
			s=new ArrayList<>();
			m.put(section, s);
		}
		ReferenceLocation rl=new ReferenceLocation(name, f, new JSONArray(loc));
		s.add(rl);
		rl.setModule(mod);
		
	}
}
