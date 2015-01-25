WeatherCal Platorm installation guide
=======

# Required software
* [Java SE Development Kit 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html). If you want to use Java 7, switch the maven.compiler.source and maven.compiler.target version in your pom.xml from 1.8 to 1.7.
* [Glassfish Open Source Edition 4.1](https://glassfish.java.net/download.html)
* [PostgreSQL >= 9.1](http://www.postgresql.org/download/)


# PostgreSQL & DB Setup
* Set up postgresql for accepting Unix-socket and TCP/IP connections from localhost without password for the user postgres, creating it if does not exists with the next query:

```
CREATE ROLE postgres WITH SUPERUSER LOGIN UNENCRYPTED PASSWORD 'postgres';
```
* Enstablish a connection to the DBMS (psql, pgAdmin, *DBC && coding, ...) and issue the next query:

```
DROP DATABASE IF EXISTS weathercal;
CREATE DATABASE weathercal
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'it_IT.UTF-8'
       LC_CTYPE = 'it_IT.UTF-8'
       CONNECTION LIMIT = 100;
```
* Connect then to the weathercal database and create the schema for the application:

```
CREATE SCHEMA development
  AUTHORIZATION postgres;
```

# Glassfish setup
Create a clean domain or recycle an older one.
In order to make the authorization and authentication system provided by Glassfish work, the configuration of a **Realm** is needed.

* To perform this operation you must start the server and open the *Glassfish Admin Console*.
* From there, edit the *service-config* Configuration and, in particular, add a new *Realm* in the *Security* section.* Set n arbitrary name for the realm and associate it to the JDBCRealm Class name.
* Set the class-specific properties as follows:

| Parameter                     | Value                      |
|------------------------------:|:--------------------------:|
| JAAS Context                  | jdbcRealm                  |
| User Table                    | development.user_          |
| User Name Column              | email                      |
| Password Column               | password                   |
| Group Table                   | development.user_has_group |
| Group Name Column             | id_group                   |
| Password Encryption Algorithm | SHA-256                    |
| Encoding                      | Hex                        |
| Charset                       | UTF-8                      |

Optionally, you can use the command-line tool, issuing the next command:

```
$ asadmin create-auth-realm --classname com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm --property 'datasource-jndi=java\:app/jdbc/pgResource:digestrealm-password-enc-algorithm=SHA-256:group-name-column=id_group:group-table=development.user_has_group:jaas-context=jdbcRealm:password-column=password:user-name-column=email:user-table=development.user_:charset=UTF-8:encoding=Hex' mpRealm
```

Or, you can edit the <glassfish_home>/domains/<domain_name>/config/domain.xml file and add the next node as a child of the security-service node:

```
<auth-realm classname="com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm" name="mpRealm">
  <property name="jaas-context" description="null" value="jdbcRealm"></property>
  <property name="encoding" value="Hex"></property>
  <property name="charset" value="UTF-8"></property>
  <property name="password-column" description="null" value="password"></property>
  <property name="datasource-jndi" value="java:app/jdbc/pgResource"></property>
  <property name="group-table" value="development.user_has_group"></property>
  <property name="user-table" value="development.user_"></property>
  <property name="group-name-column" description="null" value="id_group"></property>
  <property name="password-encryption-algorithm" value="SHA-256"></property>
  <property name="user-name-column" description="null" value="email"></property>
</auth-realm>
```

* Install the postgresql JDBC connector on glassfish (by copying it under glassfish/libs/)

# Project deployment
Now open http://localhost:4848 or the glassfish admin console you configured and go to the Applications task using the left common tasks menu, click the "Deploy" button and select the WAR. Confirm all the default options and click "OK".
You are now able to select the deployed application under the "Applications" task on the left menu: click on if, go to the descriptor and click "Launch" in the bottom table, first row. Click on the non-SSL link and enjoy the application.
