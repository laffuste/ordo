# ordo

Order validation system POC.

## Requirements

- An order has the following attributes, and these are stored as individual records in a file 
  - Order Id 
  - Order Qty 
  - Order Price 
  - Side – can be two values “Buy or Sell” 
  - Order Type – can be two values “LIMIT” or “MARKET”
- Each order is passed through a validator
  - NotionalValidator:
    - If OrderType is “LIMIT”, then validate if Qty*Price is < Configured Limit 
    - If OrderType is “MARKET”, then validate if Qty*DEFAULT_PRICE (100) < Configured Limit 
  - QtyValidator:  Validate if the Order Qty < 100,000 
  - TypeValidator: Validate if the Order is either “LIMIT” or “MARKET”


## Getting started

### Building and running the app

- From IDE: run `Application#main`.

- From jar:
  1. Execute tests and build a jar:
     ```bash
     mvn install
     # output will show the jar location (ordo\target)
     ```
  2. Run the app:
     ```bash
     java -jar <jar_with_dependencies_location>
     ```
  3. To run the app with user supplied properties (e.g. `my-conf.yaml`):
     ```bash 
       java -Dapp.config=my-conf.yaml -cp "ordo-1.0-SNAPSHOT-jar-with-dependencies.jar;." com.laffuste.ordo.Application
       # for linux, use "<jar-name>.jar:." (colon) instead of "<jar-name>.jar;." (semicolon, windows)
     ```
     `java -jar` ignores additional classpath, so it needs to be called the non-jar way by adding jar and conf in the classpath

### Order validation

:information_source: Execute when the app is running.

- Send a valid order:
  ```bash
  curl  --request POST --data '{"id": "123", "quantity": 100, "price": 1.1, "buy": true, "type": "LIMIT"}' localhost:1234/validate
  # expect empty array as response
  ```

- Send an invalid order:
  ```bash
  curl  --request POST --data '{"id": "123", "quantity": 10000000, "price": 11000, "buy": true, "type": "LIMIT"}' localhost:1234/validate
  # expect array with errors as response
  ```

## Development

- Configure lombok for idea:
  1. lombok plugin (recent ideas have it bundled)
  2. file > settings > Build, Execution, Deployment > Compiler > Annotation Processors > enable annotatin processing

Execute tests:
  ```bash
  mvn verify
  # integration tests execute in verify phase
  ```

## Design

### Assumptions

- main goal of the service is validation
- no low latency required (usage of objects not constrained)

### Design principles

- Hexagonal architecture: code is split into adapter, application and domain and dependencies flow in that direction. Dependencies in the opposite direction are prohibited.
- TDD
- Test pyramid: 38 Unit Tests (surefire plugin) and 6 Integration Tests (failsafe plugin)

### Technical choices

>:warning: Important Foreword: the code has been deliberately overengineered for the sake of discussion

- For the sake of conversation, the code has been deliberately overengineered. It hopefully is not to the point of obfuscating readability too much. Over-usage of patterns and inheritance is itself an anti-pattern. This repo might seem unnecessarily complicated at first.
- Hexagonal design allows for easier maintenance on the long term and prepares for potential complexity. However, in the requirements no inputs/outputs to the system have been specified. For the sake of having one, a simple, naive http server has been created with `SimpleController`.
- Avoided using frameworks like Spring to give a more raw design
- Attempted a design that minimises impact on code if requirements increase

#### Structure

- `properties` package:
  - properties loading has no external dependencies and could be extracted to a library. The mapping of properties to domain models (which is tied to validations or future modules) is done by dependency injection.
  - its configuration is modular (see `validation.Application`): currently it takes the default config file (`app.properties`) and merges it with any yaml or java props file in the classpath given by command line arg `app.config`
  - can be easily expanded with extra loaders (e.g. HttpFileLoader, FSFileLoader, ScpFileLoader...) or parsers (e.g. XMLPropertyParser, JsonPropertyParser...) and added to the programmatic configuration

- `validation` package:
  - subpackages (hexagonal):
    - adapter: i/o of the system (db access, web server, etc). In this case, a simple web server.
    - application: the business logic
    - domain: domain models, usually pojos
  - hexagonal architecture would allow to expand / modify the application entrypoints with no changes on the `application` package. A new entrypoint e.g. "FileWatcher" could be created and `OrderValidationUseCase` transparently injected.
  - adding Validators is done by linking the new ones in `OrderValidationService` (and adding the relevant tests).
  - adding new properties would require a change only in `OrdoValidationProperties` and `ValidationPropertiesMapper` (and the property itself)


#### Used patterns

| Pattern                                | Location                                                 | Necessary                                                                        | Comments                                                                                         |
|----------------------------------------|----------------------------------------------------------|----------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------|
| Hexagonal architecture                 | validation package                                       | No. At this simplicity level an old fashioned layered design would suffice.      | The hexagonal design has its adapters almost empty, but SimpleController shows how it would work |
| Chain of Responsibility / Filter Chain | BaseValidator                                            | No. A simple list iteration would have worked. Decorator could also have worked. |                                                                                                  |
| Notification                           | Validators                                               | Good practice.                                                                   | Fail early not a good practice when validating. Exception based validation is worse.             |
| Singleton + Dependency Injection       | Validators, Property Loaders/Parsers                     | Good practice.                                                                   | Effectively Singletons by DI, code is not enforcing it's creation via `getInstance()`            |
| Builder                                | Pojos (with lombok) and PropertyLoaderBuilder (manually) | Good practice.                                                                   |                                                                                                  |
| Strategy                               | Properties mapping method in PropertyLoaderBuilder       | Might be unnecessary if the app will not grow much.                              |                                                                                                  |
| AAA/BDD (tests)                        | All tests.                                               | Good practice.                                                                   |                                                                                                  |



#### Possible Improvements

- e2e tests: usually done in CI server, but could be done here by running `Application#main`. 2 tests (happy path and happy path with error) should be enough.
- low latency
- separate domain, application and adapter into maven modules (maven multi-module) so that not only the code has a dependency direction but each module's libraries are also isolated
- extract properties package as a lib
- validator could return an object instead of a list of strings, so to also inform of warning sor info messages
