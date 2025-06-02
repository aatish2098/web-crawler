# Webcrawler Project

This repository contains a Spring Boot backend and a Swing-based UI for crawling web pages and storing results in MongoDB.

## Prerequisites

* **Java 21+** installed and on your `PATH`
* **Gradle Wrapper** (included in the repo: `gradlew`, `gradlew.bat`, `gradle/`)

> By default, the Mongo URI is defined in `src/main/resources/application.properties`:
>
> ```properties
> spring.data.mongodb.uri=mongodb+srv://<user>:<pass>@cluster0…/webcrawlerdb
> ```
>
> You can override it at runtime via command‑line.

## Building the Project

From the project root:

```bash
./gradlew clean bootJar uiFatJar
```

* **`bootJar`** produces the backend fat jar:
  `build/libs/webcrawler-0.0.1-SNAPSHOT.jar`
* **`uiFatJar`** produces the UI fat jar:
  `build/libs/webcrawler-ui-0.0.1-SNAPSHOT.jar`

## Running via Gradle

### 1. Backend (Spring Boot)

```bash
# runs the application using the built-in main class
./gradlew bootRun
```

If you want to point to a different MongoDB URI:

```bash
./gradlew bootRun --args='--spring.data.mongodb.uri="mongodb+srv://<user>:<pass>@…/webcrawlerdb"'
```

### 2. UI (Swing Client)

```bash
./gradlew runUI
```

This launches the Swing client, which connects to `http://localhost:8080` by default.

## Running the JARs Directly

### Backend JAR

```bash
java -jar build/libs/webcrawler-0.0.1-SNAPSHOT.jar
```

Override the Mongo URI:

```bash
java -jar build/libs/webcrawler-0.0.1-SNAPSHOT.jar \
  --spring.data.mongodb.uri="mongodb+srv://<user>:<pass>@…/webcrawlerdb"
```

### UI JAR

```bash
java -jar build/libs/webcrawler-ui-0.0.1-SNAPSHOT.jar
```
