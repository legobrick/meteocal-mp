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
In order to make the authorization and authentication system provided by Glassfish work, the configuration of a **Realm** is needed.

* To perform this operation you must start the server and open the *Glassfish Admin Console*.
* From there, edit the *service-config* Configuration and, in particular, add a new *Realm* in the *Security* section.* Set n arbitrary name for the realm and associate it to the JDBCRealm Class name.
* Set the class-specific properties as follows:

| Parameter                     | Value    |
|------------------------------:|:--------:|
| JAAS Context                  | mpRealm  |
| User Table                    | user_    |
| User Name Column              | username |
| Password Column               | password |
| Group Table                   | group_   |
| Group Name Column             | name     |
| Password Encryption Algorithm | SHA-512  |

Optionally, you can use the command-line tool, issuing the next command:

```
$ asadmin create-auth-realm --classname com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm --property 'datasource-jndi=java\:app/jdbc/pgResource:digestrealm-password-enc-algorithm=SHA-512:group-name-column=name:group-table=group_:jaas-context=mpRealm:password-column=password:user-name-column=username:user-table=user_' mpRealm
```
