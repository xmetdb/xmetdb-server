# CLAUDE.md

See **[AGENTS.md](./AGENTS.md)** for full project guidance — it is the single
source of truth for this repository and everything there applies to Claude Code.

## Quick reference

- **What**: XMetDB (Xenobiotics Metabolism Database) — legacy Java 6 / Restlet
  2.0-M6 REST app + FreeMarker web UI, `version 1.0.1-SNAPSHOT`. Prebuilt
  downloads: <https://www.ideaconsult.net/downloads/xmetdb/>.
- **Modules**: `xmet-beans` (POJOs + `Resources.java` path/config constants) →
  `xmet-db` (REST resources, routers, SQL query classes, schema) → `xmet-war`
  (builds `xmetdb.war`: `web.xml`, `XMETApplication` routing, templates, static
  assets, integration tests).
- **Build**: `mvn package -P xmet` (the `xmet` profile is active by default).
- **Run locally**: `mvn tomcat7:run -P xmet` → <http://localhost:8081/xmetdb>.
- **Tests**: JUnit 4 + DbUnit **integration** tests needing a live MySQL
  (`xmetdb-test`, `aalocal_test`, `ambit2-xmetdb-test`, user `guest`/`guest`);
  see `README.md` / `INSTALL.txt` for the GRANT setup. Surefire is skipped in the
  root POM but runs in `xmet-db` and `xmet-war`.
- **Config**: Maven resource filtering; `${...}` values come from the `xmet`
  profile in the root `pom.xml` and land in
  `xmet-war/.../config/xmetdb.properties` and `xmet-db/.../conf/xmetdb.pref`.
  Edit the profile, not the filtered templates. Don't commit real secrets.
- **Routing** is defined in code in `XMETApplication.createInboundRoot()`, not in
  `web.xml` or annotations.

## Expectations for changes

- Maintenance mode: minimal, localized edits; match each file's existing style.
- Stay at Java 6 language level; do not add or upgrade dependencies unasked.
- SQL changes need a new `upgrade_*.sql` and a `version` table bump under
  `xmet-db/src/main/resources/org/xmetdb/xmet/protocol/db/sql/`.
- Branch off `master` before committing; commit/push only when asked.
- If a failure is really unreachable Maven repos or missing MySQL, say so.
