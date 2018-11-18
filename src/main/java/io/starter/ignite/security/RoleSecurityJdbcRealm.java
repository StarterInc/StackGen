package io.starter.ignite.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.JdbcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.starter.ignite.security.dao.ConnectionFactory;
import io.starter.ignite.util.SystemConstants;

/**
 * Sub-class of JdbcRealm that defines the Data Source the JdbcRealm should use
 * 
 * The configuration specified in web.xml will cause an object of this class to
 * be injected into the SecurityManager
 * 
 * @author John McMahon Copyright 2013 Starter Inc., all rights reserved.
 * 
 */
public class RoleSecurityJdbcRealm
		extends io.starter.ignite.security.JdbcRealm {

	protected static final Logger			logger								= LoggerFactory
			.getLogger(RoleSecurityJdbcRealm.class);

	protected static final java.lang.String	DEFAULT_AUTHENTICATION_QUERY		= "select password, id from user where username = ?";
	protected static final java.lang.String	DEFAULT_SALTED_AUTHENTICATION_QUERY	= "select password, password_salt from user where username = ?";
	protected static final java.lang.String	USER_ID_QUERY						= "select id from user where username = ?";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.shiro.realm.AuthenticatingRealm#
	 * getAuthenticationCacheKey(
	 * org.apache.shiro.authc.AuthenticationToken)
	 */
	@Override
	protected Object getAuthenticationCacheKey(AuthenticationToken token) {
		Object ret = super.getAuthenticationCacheKey(token);
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.shiro.realm.AuthenticatingRealm#
	 * getAuthenticationCacheKey(
	 * org.apache.shiro.subject.PrincipalCollection)
	 */
	@Override
	protected Object getAuthenticationCacheKey(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		Object ret = super.getAuthenticationCacheKey(principals);
		return ret;
	}

	public RoleSecurityJdbcRealm() throws NamingException, SQLException {
		super();
		logger.debug("Initializing RoleSecurityJdbcRealm");
		setAuthenticationQuery(DEFAULT_AUTHENTICATION_QUERY);
		setPermissionsLookupEnabled(true);
		setAuthorizationCachingEnabled(true); // ok needs to be reset
		initializeDB();

		logger.debug("Done Initializing RoleSecurityJdbcRealm");
	}

	public boolean clearCacheForAllActiveUsers() {

		Iterator itx = cachedPermissions.keySet().iterator();
		while (itx.hasNext()) {
			Object o = itx.next();
			cachedPermissions.remove(o);

		}
		return cachedPermissions.isEmpty();
	}

	public boolean clearCacheForPrincipalCollection(PrincipalCollection p) {
		clearCachedAuthorizationInfo(p);
		clearCachedAuthenticationInfo(p);
		String username = (String) getAvailablePrincipal(p);

		return clearPrincipalCacheForUser(username);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.shiro.realm.jdbc.JdbcRealm#
	 * doGetAuthenticationInfo(org.apache
	 * .shiro.authc.AuthenticationToken)
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (conn.isClosed())
				initializeDB();

		} catch (Exception e) {
			try {
				initializeDB();
			} catch (Exception ex) {
				logger.error("RoleSecurityJDBCRealm could not re-init DB.", ex);
			}
		} finally {
			JdbcUtils.closeConnection(conn);
			conn = null;
		}

		return super.doGetAuthenticationInfo(token);
	}

	@Override
	public boolean isPermitted(PrincipalCollection principals, Permission permission) {
		AuthorizationInfo info = getAuthorizationInfo(principals);
		return isPermitted(permission, info);
	}

	private boolean isPermitted(Permission permission, AuthorizationInfo info) {
		Collection<Permission> perms = getPermissions(info);

		if (perms != null && !perms.isEmpty()) {
			if (perms.contains(permission))
				return true;

			for (Permission perm : perms) {
				if (perm.toString().equals(permission.toString()))
					return true;

				if (perm.implies(permission)) {
					return true;
				}
			}
		}
		return false;
	}

	private Collection<Permission> getPermissions(AuthorizationInfo info) {
		Set<Permission> permissions = new HashSet<Permission>();

		if (info != null) {
			Collection<Permission> perms = info.getObjectPermissions();
			if (!CollectionUtils.isEmpty(perms)) {
				permissions.addAll(perms);
			}
			perms = resolvePermissions(info.getStringPermissions());
			if (!CollectionUtils.isEmpty(perms)) {
				permissions.addAll(perms);
			}

			perms = resolveRolePermissions(info.getRoles());
			if (!CollectionUtils.isEmpty(perms)) {
				permissions.addAll(perms);
			}
		}

		if (permissions.isEmpty()) {
			return Collections.emptySet();
		} else {
			return Collections.unmodifiableSet(permissions);
		}
	}

	private Collection<Permission> resolveRolePermissions(Collection<String> roleNames) {
		Collection<Permission> perms = Collections.emptySet();
		RolePermissionResolver resolver = getRolePermissionResolver();
		if (resolver != null && !CollectionUtils.isEmpty(roleNames)) {
			perms = new LinkedHashSet<Permission>(roleNames.size());
			for (String roleName : roleNames) {
				Collection<Permission> resolved = resolver
						.resolvePermissionsInRole(roleName);
				if (!CollectionUtils.isEmpty(resolved)) {
					perms.addAll(resolved);
				}
			}
		}
		return perms;
	}

	private Collection<Permission> resolvePermissions(Collection<String> stringPerms) {
		Collection<Permission> perms = Collections.emptySet();
		PermissionResolver resolver = getPermissionResolver();
		if (resolver != null && !CollectionUtils.isEmpty(stringPerms)) {
			perms = new LinkedHashSet<Permission>(stringPerms.size());
			for (String strPermission : stringPerms) {
				Permission permission = getPermissionResolver()
						.resolvePermission(strPermission);
				perms.add(permission);
			}
		}
		return perms;
	}

	/*
	 * @see
	 * org.apache.shiro.realm.jdbc.JdbcRealm#
	 * doGetAuthorizationInfo(org.apache
	 * .shiro.subject.PrincipalCollection)
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub

		// null usernames are invalid
		if (principals == null) {
			throw new AuthorizationException(
					"RoleSecurityJdbcRealm.doGetAuthorizationInfo: PrincipalCollection method argument cannot be null.");
		}

		String username = (String) getAvailablePrincipal(principals);

		Connection conn = null;
		Set<String> roleNames = null;
		Set<String> permissions = null;
		try {
			conn = dataSource.getConnection();
			// Retrieve roles and permissions from database

			int userid = getUserIdFromUserName(conn, username);

			roleNames = getRoleNamesForUser(conn, String.valueOf(userid));
			if (permissionsLookupEnabled && roleNames.size() > 0) {
				permissions = getPermissions(conn, String
						.valueOf(userid), roleNames);
			}
			conn.close();
			conn = null;
		} catch (Exception e) {
			final String message = "RoleSecurityJdbcRealm.doGetAuthorizationInfo: There was a SQL error while authorizing user ["
					+ username + "]";

			JdbcUtils.closeConnection(conn);
			// Rethrow any SQL exceptions wrapped so they dont need to
			// be
			// defined in throws
			throw new RuntimeException(e);
		} finally {
			JdbcUtils.closeConnection(conn);
			conn = null;
		}

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
		info.setStringPermissions(permissions);
		return info;
		// return super.doGetAuthorizationInfo(arg0);
	}

	private int getUserIdFromUserName(Connection conn, String username) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int uid = -1;
		try {
			ps = conn.prepareStatement(USER_ID_QUERY);
			ps.setString(1, username);

			// Execute query
			rs = ps.executeQuery();
			rs.next();
			uid = rs.getInt(1);

		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(ps);
		}
		return uid;
	}

	/*
	 * this JDBC Realm needs a nice strong DB connection
	 * 
	 * @throws NamingException
	 * 
	 * @throws SQLException
	 */
	private void initializeDB() throws NamingException, SQLException {
		// get the DataSource that JSecurity's JdbcRealm
		// should use to find the user's password
		// using the provided username
		// see context.xml for this DataSource's properties
		InitialContext ic;
		DataSource dataSource;
		String DATABASE_CHOICE = SystemConstants.JNDI_DB_LOOKUP_STRING;

		if (System.getProperty("PARAM1") != null) {
			if (System.getProperty("PARAM1").equalsIgnoreCase("production")) {
				// use default production DB
			} else if (System.getProperty("PARAM1")
					.equalsIgnoreCase("staging")) {

				DATABASE_CHOICE = SystemConstants.JNDI_DB_LOOKUP_STRING
						+ "_staging";
			}
		}

		logger.debug("RoleSecurityJdbcRealm: initializing dataSource: "
				+ DATABASE_CHOICE);

		try {
			ic = new InitialContext();
			dataSource = (DataSource) ic.lookup(DATABASE_CHOICE);
			java.sql.Connection c = dataSource.getConnection();
			setDataSource(dataSource);
			c.close();

		} catch (Exception e) {
			logger.warn("RoleSecurityJdbcRealm.initializeDB() Falling back to non-JNDI "
					+ "ConnectionFactory connection:"
					+ e.getLocalizedMessage());
			dataSource = ConnectionFactory.getDataSource();
			java.sql.Connection c = dataSource.getConnection();
			setDataSource(dataSource);
			c.close();

		}
		logger.debug(" Datasource set OK!");
	}

	public boolean clearPrincipalCacheForUser(String username) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (conn.isClosed())
				initializeDB();

			int userid = getUserIdFromUserName(conn, username);
			cachedPermissions.remove(String.valueOf(userid));

		} catch (Exception e) {
			try {
				initializeDB();
			} catch (Exception ex) {
				logger.error("RoleSecurityJDBCRealm could not re-init DB.", ex);
			}
		} finally {
			JdbcUtils.closeConnection(conn);
			conn = null;
		}

		return true;

	}

}
