# U-Fund:  ___ Society for the Wellbeing of Endangered Nestlings ___
# Modify this document to expand any and all sections that are applicable for a better understanding from your users/testers/collaborators (remove this comment and other instructions areas for your FINAL release)

An online U-Fund system built in Java 17=> and ___ Angular 17 and Spring and Maven 3.9.6 ___
  
## Team

- Alex Lee
- Abdelouakil Bouharrat 
- Umaima Nisar
- Katie Bippus
- Tyler Knox


## Prerequisites

- Java 11=>17 (Make sure to have correct JAVA_HOME setup in your environment)
- Maven
- Angular 17
- Spring


## How to run it

1. Clone the repository and go to the root directory.
2. Execute `mvn compile exec:java`
3. Open in your browser `http://localhost:8080/ufund`
4. To Put a need into the cupboard, use `curl -v -X POST -H 'Content-Type: application/json' 'http://localhost:8080/ufund' -d '{"name": "<need name>", "quantity": <number>}'`
5. To list all needs in the cupdoard, use `curl -v -X GET 'http://localhost:8080/ufund'`
6. To look for a specific need in the cupboard using its ID, use `curl -v -X GET 'http://localhost:8080/ufund/<id number>'`
7. To look up a need by its name, or get a list of all needs with a specific string, use `curl -v -X GET 'http://localhost:8080/ufund/?name=<string to look for>'`
8. To update a need's attributes, use `curl -X PUT -H 'Content-Type:application/json' 'http://localhost:8080/ufund' -d '{"id": <id number for need to update>, "cost": <new cost>, "type": "<new type>", "quantity": <new quantity>}'`

## Known bugs and disclaimers
(It may be the case that your implementation is not perfect.)

Document any known bug or nuisance.
If any shortcomings, make clear what these are and where they are located.

## How to test it

The Maven build script provides hooks for run unit tests and generate code coverage
reports in HTML.

To run tests on all tiers together do this:

1. Execute `mvn clean test jacoco:report`
2. Open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/index.html`

To run tests on a single tier do this:

1. Execute `mvn clean test-compile surefire:test@tier jacoco:report@tier` where `tier` is one of `controller`, `model`, `persistence`
2. Open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/{controller, model, persistence}/index.html`

To run tests on all the tiers in isolation do this:

1. Execute `mvn exec:exec@tests-and-coverage`
2. To view the Controller tier tests open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/model/index.html`
3. To view the Model tier tests open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/model/index.html`
4. To view the Persistence tier tests open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/model/index.html`

*(Consider using `mvn clean verify` to attest you have reached the target threshold for coverage)
  
  
## How to generate the Design documentation PDF

1. Access the `PROJECT_DOCS_HOME/` directory
2. Execute `mvn exec:exec@docs`
3. The generated PDF will be in `PROJECT_DOCS_HOME/` directory


## How to setup/run/test program 
1. Tester, first obtain the Acceptance Test plan
2. IP address of target machine running the app
3. Execute ________
4. ...
5. ...

## License

MIT License

See LICENSE for details.
