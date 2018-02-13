# Shoppyng App


## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

#### 1. Mysql client

Create a database that is named `shoppyngApp`

** /!\ Database updates with Liquibase** when we launch the app

### 2. Node.js

We use Node to run a development web server and build the project.

Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

    npm install

We use npm scripts and Webpack as our build system.


Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    ./mvnw
    npm start


Npm is also used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in [package.json](package.json). You can also run `npm update` and `npm install` to manage dependencies.
Add the `help` flag on any command to see how you can use it. For example, `npm help update`.

The `npm run` command will list all of the scripts available to run for this project.

### 3. Insert Data in database
 launch `sql/insert_product.sql` in database 'shoppyngApp'

### 4. Sign in ShoppyngApp website 
user / password =  user / user


## 5. Testing

To launch your application's tests, run:

    ./mvnw clean test

### 6. Client tests

Unit tests are run by [Karma][] and written with [Jasmine][]. They're located in [src/test/javascript/](src/test/javascript/) and can be run with:

    npm test

## TODO
- add DTO for all entities
- fix userResource Test
- add jasmine test to adding product to a shopping cart

This application was generated using JHipster 4.8.2, you can find documentation and help at [http://www.jhipster.tech/documentation-archive/v4.8.2](http://www.jhipster.tech/documentation-archive/v4.8.2).