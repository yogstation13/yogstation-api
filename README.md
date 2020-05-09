# Yogstation API

*NOTE: This project is still a work in progress and is not live in any way.*

This repo contains the source code for the Yogstation API used for our Webmin panel and Hub. 

## Running

In production the following environment variables must be set

```
xenforo.host=<Xenforo location>

jwt.secret=<Randomly generated secret - should be a secure length>

spring.datasource.url=<database URL>
spring.datasource.driverClassName=<database driver>
spring.datasource.username=<database username>
spring.datasource.password=<database password>
```

Sample values can be found in https://github.com/yogstation13/yogstation-api/blob/master/src/main/resources/application-development.properties

## Development

A profile has been pre-configured for development purposes, this profile should never be enabled in a production system. To enable this profile the following environment variables must be set

```
spring.profiles.active=development
```

This profile has a few features
 - Sets up default environment variables.
 - Configures an embedded database with sample data.
 - Enables a simple login system that allows developers to test without needing a local instance of Xenforo.
 
