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
