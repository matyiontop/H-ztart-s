# 🏠 Háztartási Költség- és Jövedelem Kalkulátor (Full-Stack)

Modern, reszponzív háztartási pénzügyi menedzser és kalkulátor webalkalmazás. Segít átlátni a háztartás bevételeit, fix rezsiköltségeit és változó kiadásait, kiszámítja a hónap hátralévő napjaira jutó **napi elkölthető keretet**, megjeleníti a **valós vs. kalkulált egyenleg idővonalat (12 hónapos prognózissal)**, valamint interaktív **„Mi lenne, ha...?” forgatókönyv-szimulációt** nyújt.

---

## 🛠️ Alkalmazott Technológiák

- **Backend**: **Java 21 / Spring Boot 3.3.4**
  - Spring Web MVC, Spring Data JPA / Hibernate, Bean Validation
  - Springdoc OpenAPI 2.5.0 (Swagger UI & API Docs)
  - Maven Wrapper (`mvnw.cmd` / `mvnw.ps1`), automatikus indító parancsfájl (`start.bat`)
- **Frontend**: **Angular 18 (TypeScript)**
  - Standalone komponensek, Angular Signals & Effects
  - Chart.js adatvizualizáció (kördiagram, görbevonalas egyenlegpálya diagram)
  - Swiss Minimalist fekete-fehér monokróm dizájn, Nappali / Éjjeli (Dark/Light) téma kapcsoló
- **Adatbázis**: **PostgreSQL 16+ / 18**
  - Docker Compose támogatás (PostgreSQL + Adminer felület)
  - Automatikus kezdőadat-betöltés (`DataInitializer` és `database/init.sql`)

---

## 📂 Projekt Felépítése

```
d:/Háztartás/
├── docker-compose.yml           # PostgreSQL 16 és Adminer adatbázis konténerek
├── database/
│   └── init.sql                 # PostgreSQL séma és kezdő mintaadatok
├── backend/                     # Spring Boot 3 REST API
│   ├── start.bat                # Egykattintásos indító szkript (Java és JAR automatikus kezeléssel)
│   ├── pom.xml                  # Maven függőségek
│   ├── mvnw.cmd / mvnw.ps1      # Maven Wrapper
│   └── src/main/
│       ├── resources/
│       │   └── application.yml  # Adatbázis és Swagger konfiguráció
│       └── java/hu/haztartas/
│           ├── controller/      # REST API vezérlők (/api/dashboard, /api/balance, /api/calculations stb.)
│           ├── service/         # Üzleti logika, egyenleg-trajektória és szimulációs motor
│           ├── repository/      # Spring Data JPA adattárak
│           ├── entity/          # JPA entitások (Income, Expense, AccountBalance, Category stb.)
│           ├── dto/             # Adatátviteli modellek
│           └── config/          # CORS, Hibakezelés és DataInitializer
└── frontend/                    # Angular 18 Kliens
    ├── src/app/
    │   ├── core/
    │   │   ├── models/          # TypeScript interfészek és típusok
    │   │   └── services/        # HaztartasApiService (HTTP hívások, Signals állapotkezelés)
    │   ├── layout/
    │   │   └── navbar/          # Felső navigációs sáv, téma- és státuszjelző
    │   ├── shared/
    │   │   └── pipes/           # Pénzformázó Pipe (HufCurrencyPipe)
    │   ├── features/
    │   │   ├── dashboard/       # Fő áttekintés, vagyonkezelő, kördiagram & egyenlegpálya
    │   │   ├── calculator/      # Havi & napi keret kalkulátor
    │   │   ├── incomes/         # Bevételek listázása és rögzítése (időtartam-kezeléssel)
    │   │   ├── expenses/        # Kiadások (Fix vs. Változó) és szűrés
    │   │   └── simulator/       # "Mi lenne, ha...?" interaktív forgatókönyv-szimuláció
    │   ├── app.routes.ts        # Kliens oldali útvonalak
    │   └── styles.css           # Globális dizájn rendszer (Dark/Light téma változók)
    └── package.json
```

---

## ✨ Fő Funkciók

### 1. 📊 Áttekintő Irányítópult (Dashboard)
- **Vagyon & Egyenleg nyilvántartás**: Aktuális likvid vagyon követése **Bankszámla** és **Készpénz** felbontásban, becsült hó végi egyenleggel és gyors szerkesztési lehetőséggel.
- **Valós idejű KPI kártyák**: Havi összes bevétel, Fix kötelezettségek/rezsi, Változó kiadások, Havi megtakarítási ráta (%).
- **📈 Kalkulált vs. Valós Egyenleg Idővonal Diagram (Trajectory Chart)**:
  - Vonaldiagram a múltbeli tényleges egyenlegek (zöld vonal) és a jövőbeli 12 hónapos kalkulált vagyonpálya (lila vonal) követésére.
  - Automatikus eltérés-számítás (Valós vs. Kalkulált egyenleg különbsége).
- **🍩 Költségmegoszlás kördiagram**: Kategóriánkénti kiadásbontás Chart.js diagrammal.
- **Gyorsműveletek & Legutóbbi tételek**: Bevételek és kiadások azonnali rögzítése vagy törlése.

### 2. 🧮 Napi Keret & Kalkulátor
- **Napi megmaradó keret**: Kiszámítja, hogy a hónap hátralévő napjaira mennyi pénz költhető naponta a tényleges vagyonból, illetve a havi nettó megtakarításból.
- **Egyéni tervező modul**: Gyors kalkuláció egyéni havi jövedelem (pl. diákmunka, zsebpénz) és fix kiadások megadásával a tervezhető napi költőpénz meghatározására.
- **Cash-flow összesítő**: Havi és évesített pénzáramlás kimutatás.

### 3. 💵 Bevételek Menedzsment
- Bevételek rögzítése kategóriával és időtartam-kezeléssel:
  - **Egyszeri tétel** (1 alkalom)
  - **Határozott idejű tétel** (pl. 3 hónapos megbízás, féléves ösztöndíj)
  - **Állandó havi tétel** (rendszeres fizetés, bérbeadás)
- Összesített havi bevétel összegző és közvetlen törlési lehetőség.

### 4. 🛒 Kiadások Menedzsment
- Kiadások felvitele és csoportosítása: **Fix költségek** (lakbér, rezsi, előfizetés) vs. **Változó kiadások** (bevásárlás, szórakozás, utazás).
- Gyors szűrés: `Összes` / `Fix kiadások` / `Változó kiadások`.
- Prioritási szintek és esedékességi napok nyilvántartása.

### 5. 🔮 „Mi lenne, ha...?” (What-If) Szimulátor
- Valós idejű pénzügyi sandbox csúszkákkal:
  - Jövedelemváltozás (±%)
  - Fix kiadások változása (±%)
  - Változó kiadások változása (±%)
  - Új rendszeres havi tétel (pl. új hitel, bérlet)
  - Egyszeri váratlan kiadás (pl. háztartási kár, szerviz)
- **Gyors forgatókönyv-sablonok (Presetek)**: Rezsiár-emelkedés (+20%), Fizetésemelés (+15%), Spórolás (-15%), Autóhitel, Váratlan kár.
- **Összehasonlító elemzés**: Jelenlegi állapot vs. Szimulált eredmény, havi megtakarítási változás és **éves vagyoni hatás (+/- Ft)** kimutatás.

---

## 🚀 Gyors Indítási Útmutató

### 1. Lépés: Adatbázis Indítása

#### 1. opció: Docker Compose használatával (Ajánlott)
A projekt gyökerében futtasd:
```bash
docker compose up -d
```
- **PostgreSQL**: `localhost:5432` (adatbázis: `haztartas_db`, felhasználó: `postgres`, jelszó: `postgrespassword`)
- **Adminer webes adatbáziskezelő**: [http://localhost:8081](http://localhost:8081)

#### 2. opció: Lokális PostgreSQL / DataGrip
1. Hozz létre egy adatbázist `haztartas_db` néven:
   ```sql
   CREATE DATABASE haztartas_db;
   ```
2. *(Opcionális)*: Lefuttathatod a `database/init.sql` szkriptet (ha az adatbázis üres, a Spring Boot backend induláskor automatikusan inicializálja az alapértelmezett kategóriákat és mintatételeket).

---

### 2. Lépés: Backend Indítása

#### Egyszerű indítás (Windows):
Kattints duplán a `backend/start.bat` fájlra, vagy futtasd terminálból:
```cmd
backend\start.bat
```
*(A parancsfájl automatikusan megkeresi a telepített Java környezetet, szükség esetén lefordítja a JAR állományt a Maven Wrapperrel, és elindítja a szervert.)*

#### Manuális indítás Maven Wrapperrel:
```powershell
cd backend
powershell -ExecutionPolicy Bypass -File .\mvnw.ps1 spring-boot:run
```
*(vagy parancssorban: `mvnw.cmd spring-boot:run`)*

- **REST API elérhetőség**: `http://localhost:8080`
- **Swagger UI dokumentáció**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON specifikáció**: [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

---

### 3. Lépés: Frontend Indítása

Nyiss egy terminált a `frontend` mappában:
```powershell
cd frontend
npm install
npm start
```

Nyisd meg a böngészőben: **[http://localhost:4200](http://localhost:4200)**

---

## 📡 REST API Végpontok Áttekintése

| Végpont | Metódus | Leírás |
|---|---|---|
| `/api/dashboard` | `GET` | Teljes összefoglaló adatok (KPI-k, egyenleg, trendek, kategóriák, legutóbbi tételek) |
| `/api/balance` | `GET`, `PUT` | Aktuális vagyon és egyenleg lekérdezése / frissítése (Bank + Készpénz) |
| `/api/balance/trajectory` | `GET` | Valós és kalkulált 12 hónapos egyenleg idővonal lekérése |
| `/api/incomes` | `GET`, `POST`, `DELETE` | Bevételek lekérése, felvitele és törlése |
| `/api/expenses` | `GET`, `POST`, `DELETE` | Kiadások lekérése, felvitele és törlése |
| `/api/categories` | `GET`, `POST`, `DELETE` | Kategóriák kezelése |
| `/api/calculations/cash-flow` | `GET` | Havi és éves cash-flow összefoglaló |
| `/api/calculations/50-30-20` | `GET` | 50/30/20 szabály alapú pénzügyi felosztás elemzése |
| `/api/calculations/emergency-fund`| `GET` | Vésztartalék kalkuláció (3, 6, 12 havi célok) |
| `/api/calculations/simulate` | `POST` | Forgatókönyv-szimuláció futtatása a megadott paraméterekkel |
| `/api/savings-goals` | `GET`, `POST`, `DELETE` | Megtakarítási célok lekérése, rögzítése és törlése |
| `/api/savings-goals/{id}/deposit` | `POST` | Befizetés rögzítése egy megtakarítási célhoz |

---

## ⚙️ Konfiguráció

A backend beállításai a `backend/src/main/resources/application.yml` fájlban találhatók, illetve környezeti változókkal is felülírhatók:

| Változó | Alapértelmezett érték | Leírás |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/haztartas_db` | PostgreSQL JDBC kapcsolat URL |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | Adatbázis felhasználónév |
| `SPRING_DATASOURCE_PASSWORD` | `admin` *(vagy Dockerben: `postgrespassword`)* | Adatbázis jelszó |
| `server.port` | `8080` | Backend HTTP port |
