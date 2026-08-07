# &#129443; Mammoth Facts

A Java EE 8 multi-module EAR application that serves fascinating facts about woolly mammoths, with a JPA/PostgreSQL backend REST API and a JSF dark-mode frontend.

[![Contribute](https://img.shields.io/badge/Dev%20Spaces-Contribute-blue?logo=redhat&logoColor=white)](https://devspaces.apps.cluster-bqq9p.bqq9p.sandbox400.opentlc.com/#https://github.com/shirodkar/mammoth-ear)

## Architecture

```
mammoth-ear.ear
├── backend-api.war    — JAX-RS REST API + JPA entities
├── frontend.war       — JSF 2.3 web UI
└── lib/               — shared dependencies
```

| Layer | Technology |
|-------|-----------|
| Runtime | JBoss EAP 7.4 |
| API | JAX-RS 2.1 |
| Persistence | JPA 2.2 / Hibernate |
| Frontend | JSF 2.3 / Facelets |
| Database | PostgreSQL 15 |
| Build | Maven (multi-module) |
| Java | 11 |

## Getting Started

### Prerequisites

- Java 11
- Maven 3.8+
- PostgreSQL 15 (or use Dev Spaces — see badge above)

### Build

```bash
mvn clean install
```

### Run Tests

```bash
mvn clean verify
```

### Dev Spaces Workflow

Click the **Contribute** badge above to launch a preconfigured workspace with PostgreSQL, JBoss EAP 7.4, and the MTA extension. Then run the devfile tasks:

1. **init-eap7** — Downloads the PostgreSQL driver, configures the datasource, and starts EAP 7
2. **build** — `mvn clean install -DskipTests`
3. **deploy-eap7** — Deploys the EAR to JBoss EAP 7 via CLI

## Project Structure

```
mammoth-ear/
├── backend-api/          — REST API module (WAR)
│   └── src/main/java/
│       ├── api/          — JAX-RS resource and application
│       ├── model/        — JPA entity (MammothFact)
│       └── service/      — Business logic (MammothFactService)
├── frontend/             — JSF frontend module (WAR)
│   └── src/main/webapp/
│       ├── index.xhtml   — Main page (woolly mammoth theme)
│       └── WEB-INF/      — JSF and servlet config
├── ear/                  — EAR packaging module
├── init-db.sql           — Database seed script
├── devfile.yaml          — OpenShift Dev Spaces configuration
└── pom.xml               — Parent POM
```

## API

```
GET /backend/api/mammoths          — All mammoth facts
GET /backend/api/mammoths/{id}     — Single fact by ID
GET /backend/api/mammoths/random   — Random fact
```
