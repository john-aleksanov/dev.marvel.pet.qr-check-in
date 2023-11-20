## Overview

This project showcases a simple check-in application that allows students (system users) to register class attendance using QR codes.
It provides two main APIs:
- Retrieve current activities for a given QR code (effectively, classroom ID)
- 'Check in' for a selected activity

Current activity is determined as an activity that has started or is about to start 15 minutes from NOW in a given classroom
(identified by a classroom ID).

## Tech stack

- Java 17
- Spring Boot 3 and Spring 6
- In-memory H2 DB or Postgres DB (configurable via profile)
- Flyway DB versioning framework

## Notes & Limitations

This project serves as a basic example to demonstrate a check-in implementation. Due to its simplicity, several
shortcuts and hardcoding have been implemented. In a production setting, the following improvements would be made:

1. **Testing**: Integration, E2E and NFR tests would be added.
2. **Observability**: Logging and performance metrics would be added.
3. **Persistent store**: At present, an in-memory data structure is used to save check-ins. An RDBMS or a No-SQL DB (like DDB) would be used.
4. **Localization**: Spring's `MessageSource` would be used for message resolution.
5. **Error Handling**: More robust error handling mechanisms would be in place.
6. **API first**: an OpenAPI description of the services' APIs would be provided.
7. Etc., etc.