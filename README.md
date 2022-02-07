### Simple CRUD application for storing files of specified types.

Requirements: Java 8+ installed on machine. </br>Mind that application uses <b>h2 in-memory database</b> to store files.
Restart of application means that database state will be lost.

1. clone repo
2. navigate to cloned directory
3. execute `./mvnw install -U`
4. execute `java -jar target/simple-file-server-1.0.0.jar`
5. visit `localhost:8080/swagger-ui/index.html#/` to explore API