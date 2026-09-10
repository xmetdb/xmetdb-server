# AGENTS.md

Guidance for AI coding agents working in this repository. Applies to the whole
`xmetdb-server` tree.

## What this is

**XMetDB** — the Xenobiotics Metabolism Database. A Java REST web application and
web UI for curating and querying metabolism observations (protocols /
observations, sites of metabolism, enzymes, chemical structures). Public site:
<http://www.xmetdb.org>. Developed by Ideaconsult Ltd. on behalf of xmetdb.org.
Prebuilt WARs, SQL scripts, and release archives:
<https://www.ideaconsult.net/downloads/xmetdb/>.

This is a **legacy codebase** (inception 2012, `version 1.0.1-SNAPSHOT`, Java 6,
Restlet 2.0-M6). Match the surrounding style; do not modernize opportunistically.

## Module layout

Multi-module Maven build (`pom.xml` at root, packaging `pom`):

| Module | Artifact | Packaging | Contains |
|---|---|---|---|
| `xmet-beans` | `org.xmetdb:xmet-beans` | jar | Plain POJOs and shared constants. `Resources.java` holds every REST path segment and the `Config` enum of property keys. `XMETDBRoles`, `PublishedStatus`, `AtomUncertainty`, `ProductAmount`. |
| `xmet-db` | `org.xmetdb:xmet-db` | jar | The bulk of the code: Restlet resources, routers, DB query/update classes, reporters, and the SQL schema under `src/main/resources/org/xmetdb/xmet/protocol/db/sql/`. Depends on `xmet-beans`. |
| `xmet-war` | `org.xmetdb:xmet-war` → `xmetdb.war` | war | Web layer: `web.xml`, Restlet `XMETApplication` / `XMETRESTComponent`, cookie auth wiring, FreeMarker templates and static assets under `src/main/webapp/`, and the HTTP integration tests. Depends on `xmet-db` (plus its `tests` classifier jar). |

`xmet-war` builds the final `xmetdb.war` (`finalName` = `xmetdb`).

## Build & run

```bash
mvn package -P xmet          # build all modules + war. -P xmet is active by default.
mvn install -P xmet          # if another module needs the freshly built jars
mvn tomcat7:run -P xmet      # run xmet-war locally (tomcat7-maven-plugin, port 8081, context /xmetdb)
```

- README says `mvn tomcat:run`; the configured plugin is **tomcat7-maven-plugin**
  (`xmet-war/pom.xml`), so the real goal is `tomcat7:run`. There is also legacy
  `pluginManagement` for the old codehaus `tomcat-maven-plugin`.
- Compiler is pinned to **source/target 1.6**. Do not use Java 7+ syntax
  (diamond operator, try-with-resources, `String` switch, etc.).
- `project.build.sourceEncoding` is UTF-8.
- Dependencies resolve from `nexus.ideaconsult.net` and
  `ambit.uni-plovdiv.bg:8083` (key versions: `ambit 2.4.11`, `restnet 1.0.0`,
  `restlet 2.0-M6`, `opentox-client 1.0.2`). Several declared repositories are
  plain `http` and may be unreachable; a clean build can fail purely on
  dependency resolution. That is an environment problem, not a code problem.

## Tests

- JUnit **4.8.1** + **DbUnit 2.4.8**. They are **integration tests that require a
  live MySQL** on `localhost:3306` — there is no in-memory fallback.
- Surefire is `<skip>true</skip>` in the **root** `pom.xml` and re-enabled
  (`skip=false`) in `xmet-db` and `xmet-war`. A plain `mvn test` from the root
  therefore still runs the module tests.
- Required databases / user (see `README.md` and `INSTALL.txt` for the exact
  GRANT statements):
  - `xmetdb-test` — application schema for tests (`DbUnitTest` auto-creates /
    re-creates tables from the bundled SQL when the `version` table is missing or
    stale).
  - `aalocal_test` — local users/roles DB used by `xmet-war` `ResourceTest`.
  - `ambit2-xmetdb-test` — chemical structures DB.
  - MySQL user `guest` / password `guest`, plus `EXECUTE` grants on the stored
    procedures (`createProtocolVersion`, `createProtocolCopy`, `deleteProtocol`,
    `findByProperty`).
- `xmet-db` test base classes: `DbUnitTest` (connection + schema bootstrap),
  `CRUDTest`, `QueryTest`. `xmet-war` test base: `ResourceTest` (starts an
  embedded `XMETRESTComponent` on port 8181, HTTP+HTTPS, with
  `xmet_protected=false` and `TESTAUTHZ=true`).
- When you cannot run the DB-backed tests, say so explicitly rather than
  implying they passed.

## Configuration

- All runtime config is Maven **resource filtering**. `src/main/resources` is
  filtered in every module; `${...}` placeholders are supplied by the **`xmet`
  profile** in the root `pom.xml` (DB host/name/user/password, attachment dir,
  Ambit service URL, mail/SMTP, `xmet.secret`, CORS `xmet.allowed.origins`, test
  DB coordinates, etc.).
- Two template files carry those values into the artifacts:
  - `xmet-war/src/main/resources/config/xmetdb.properties` — application settings
    read by `XMETApplication` (`config/xmetdb.properties`).
  - `xmet-db/src/main/resources/conf/xmetdb.pref` — MySQL/JDBC + REST settings
    (`conf/xmetdb.pref`), also read by the tests.
- After deployment these live at
  `{tomcat}/webapps/xmetdb/WEB-INF/classes/config/xmetdb.properties` and
  `.../conf/xmetdb.pref`. `INSTALL.txt` is the operator-facing setup guide.
- To change credentials for local work, edit the `xmet` profile properties in the
  root `pom.xml` (or pass `-D` overrides), **not** the filtered template files.
- HTTP vs HTTPS `web.xml` is selected by the `http` (default) / `https` profiles
  in `xmet-war/pom.xml`.
- Do not commit real secrets, mail passwords, or DB passwords. `xmet.secret` and
  the placeholder mail credentials in the profile are dev defaults — treat any
  real values as sensitive.

## Architecture notes

- **Routing is entirely in code**, not annotations or `web.xml`.
  `web.xml` hands everything to Restlet's `ServerServlet`;
  `XMETApplication.createInboundRoot()` (in `xmet-war`) builds the full router
  tree and attaches every resource. To add or change an endpoint you edit that
  method plus the relevant `*Router` in `xmet-db`.
- **Path constants** live in `xmet-beans` `Resources.java` (e.g. `/protocol`,
  `/catalog`, `/chemical`, `/dataset`, `/myaccount`, `/admin`, `/task`). Reuse
  them; don't hard-code path strings.
- **Naming conventions** in `xmet-db`:
  - `*Resource` — Restlet `ServerResource` handling an HTTP resource.
  - `*Router` — a `MyRouter` subtree of attachments.
  - `Read* / Create* / Update* / Delete*` — modbcum query objects
    (`IQueryRetrieval` / `IQueryUpdate`) that build parameterized SQL; these are
    where actual SQL lives. Always use `QueryParam` binding, never string
    concatenation of user input.
  - `*Reporter` — serializes result rows to a representation (HTML via FreeMarker,
    JSON, URI, RDF). RDF writer is Jena or StAX per `rdf.writer` config.
  - `Callable*` — long-running work executed as a background `Task`
    (`/task/{id}` resource).
- **Auth/authz**: cookie-based (`CookieAuthenticator`, cookie name `xmetdb`,
  secret from `xmet.secret`). Users/roles in the `xmet_users` MySQL DB via
  `DBVerifier` / `DbEnroller`. Roles come from `DBRoles` / `XMETDBRoles`
  (`admin`, `curator`, `xmetdb_user`). `xmet_protected=false` and context param
  `TESTAUTHZ=true` disable enforcement (used by tests). Authorizers:
  `ProtocolAuthorizer`, `UserAuthorizer`, `SimpleRoleAndMethodAuthorizer`
  (GET open, write methods role-gated).
- **Views**: FreeMarker templates in
  `xmet-war/src/main/webapp/WEB-INF/templates/` (`*.ftl`), with context help
  files under `templates/help/` — see `xmet-war/README.md` for the help-page
  format (`helptitle` / `helpcontent` / `#keys` divs, `chelp` links).
- **Chemistry**: structure storage and structure/substructure search are
  delegated to an external **Ambit** service (`xmet_ambit_service`, an
  `ambit2.war` with a matching `ambit2-xmetdb` DB). If it is not configured,
  query-by-structure is simply unavailable.
- **CORS**: `OriginFilter` wraps the whole router; allowed origins from
  `xmet.allowed.origins` (`*` in the dev profile).

## Working agreements

- Keep changes minimal and localized; this app is in maintenance mode.
- Stay within Java 6 language level and the existing library versions. Do not add
  or bump dependencies without being asked.
- Follow the existing formatting in each file (tabs, brace style vary by file —
  match the file you are editing).
- SQL schema changes go in
  `xmet-db/src/main/resources/org/xmetdb/xmet/protocol/db/sql/` and need a
  matching `upgrade_*.sql` plus a bump of the `version` table (see the existing
  `upgrade_2_*.sql` files and `DbCreateDatabase` / `DBVersion`).
- `git` default branch is `master`. Create a branch before committing; only
  commit or push when the user asks.
- If a build or test failure is caused by unreachable Maven repositories or a
  missing MySQL, report that plainly instead of trying to work around it.
