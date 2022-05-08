package dev.mbo.ldap;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Hashtable;

public class Application {

    public static void main(final String... args) throws Exception {
        final Hashtable<String, String> connectionProperties = new Hashtable<>();

        connectionProperties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        connectionProperties.put(Context.PROVIDER_URL, "ldap://localhost:1389");
        connectionProperties.put(Context.SECURITY_AUTHENTICATION, "simple");
        connectionProperties.put(Context.SECURITY_PRINCIPAL, "cn=admin,dc=mbo,dc=dev");
        connectionProperties.put(Context.SECURITY_CREDENTIALS, "adminpassword");

        final DirContext context = new InitialDirContext(connectionProperties);
        System.out.println("connected");

        final String filter = "(&(objectClass=person)(cn=sample01))";
        final String[] attrIDs = {"cn"};
        final SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(attrIDs);
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        final NamingEnumeration<SearchResult> searchResults = context.search("dc=mbo,dc=dev", filter, searchControls);

        String commonName;
        String distinguishedName;
        if (searchResults.hasMore()) {
            System.out.println("---- found entry:");

            final SearchResult result = searchResults.next();
            Attributes attrs = result.getAttributes();

            distinguishedName = result.getNameInNamespace();
            System.out.println(distinguishedName);

            commonName = attrs.get("cn").toString();
            System.out.println(commonName);
        }

        context.close();
    }

}
