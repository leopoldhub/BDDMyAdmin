# Tomcat Application

1. [Included dependencies](#included-dependencies)
2. [Configuration](#configuration)
   1. [Deployed](#deployed)
      1. [tomcat-users.xml](#tomcat-usersxml)
      2. [settings.xml](#settingsxml)
      3. [pom.xml](#pomxml)
   2. [Embedded](#embedded)
      1. [pom.xml](#pomxml)
   3. [web.xml](#webxml)
3. [Deploy and run](#deploy-and-run)
   1. [Deployment](#deployment)
   2. [Run](#run)

## Included dependencies

|Dependency         |Version    |Repository|
|-------------------|-----------|----------|
|tomcat-servlet-api |9.0.54     |[org.apache.tomcat.tomcat-servlet-api](https://mvnrepository.com/artifact/org.apache.tomcat/tomcat-servlet-api/9.0.54)|
|Postgresql         |42.3.0     |[org.postgresql](https://mvnrepository.com/artifact/org.postgresql/postgresql/42.3.0)|

## Configuration

### Embedded

#### tomcat-users.xml

Add roles and users in your tomcat configuration file `$TOMCAT/conf/tomcat-users.xml`

```xml
<tomcat-users xmlns="http://tomcat.apache.org/xml"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://tomcat.apache.org/xml tomcat-users.xsd"
              version="1.0">
   ...
   <role rolename="manager-gui"/>
   <role rolename="manager-script"/>
   <user username="admin" password="password" roles="manager-gui, manager-script"/>
   ...
</tomcat-users>
```

#### settings.xml
Modify your maven `settings.xml` file, generally located in `$HOME/.m2/settings.xml` and add your tomcat server configuration

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0  
 http://maven.apache.org/xsd/settings-1.0.0.xsd">
   ...
   <servers>
      <server>
         <id>TomcatServer</id>
         <username>admin</username>
         <password>password</password>
      </server>
   </servers>
   ...
</settings>
```

#### pom.xml

Replace the `http://localhost:8080/manager/text` by your own tomcat manager URL

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
   ...
   <build>
      <plugins>
         ...
         <plugin>
            <groupId>org.apache.tomcat.maven</groupId>
            <artifactId>tomcat7-maven-plugin</artifactId>
            <version>2.2</version>
            <configuration>
               <url>http://localhost:8080/manager/text</url>
               <server>TomcatServer</server>
               <path>/${project.artifactId}</path>
               <!--port is used only for run not deploy-->
               <port>9090</port>
            </configuration>
         </plugin>
         ...
      </plugins>
   </build>
   ...
</project>
```

### Deployed

#### pom.xml

Replace the `port` by your wanted port

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
   ...
   <build>
      <plugins>
         ...
         <plugin>
            <groupId>org.apache.tomcat.maven</groupId>
            <artifactId>tomcat7-maven-plugin</artifactId>
            <version>2.2</version>
            <configuration>
               <url>http://localhost:8080/manager/text</url>
               <server>TomcatServer</server>
               <path>/${project.artifactId}</path>
               <!--port is used only for run not deploy-->
               <port>9090</port>
            </configuration>
         </plugin>
         ...
      </plugins>
   </build>
   ...
</project>
```

### web.xml

Change the database settings

```xml
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
  http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">
    ...
      <!--Database parameters-->
      <context-param>
         <param-name>db.driverClassName</param-name>
         <param-value>org.postgresql.Driver</param-value>
      </context-param>
      <context-param>
         <param-name>db.url</param-name>
         <param-value>jdbc:postgresql://psqlserv/da2i</param-value>
      </context-param>
      <context-param>
         <param-name>db.username</param-name>
         <param-value>databaseusername</param-value>
      </context-param>
      <context-param>
         <param-name>db.password</param-name>
         <param-value>databasepassword</param-value>
      </context-param>
    ...
</web-app>
```

## Deploy and run

### Deployment

Run the command `mvn org.apache.tomcat.maven:tomcat7-maven-plugin:2.2:redeploy -f pom.xml` in your project directory.
Your application context path correspond to your `artifactId` (ex: `http://localhost:8080/myapp`)

### Run

Run the command `mvn org.apache.tomcat.maven:tomcat7-maven-plugin:2.2:run -f pom.xml` in your project directory.
Your application will run on the port precised in pom.xml file
Your application context path correspond to your `artifactId` (ex: `http://localhost:8080/myapp`)