# 🏠 Háztartási Költség- és Jövedelem Kalkulátor (Full-Stack)

Modern, reszponzív háztartási pénzügyi menedzser és kalkulátor webalkalmazás, amely segít átlátni a háztartás bevételeit, fix rezsiköltségeit, változó kiadásait, valamint okos 50/30/20 arányelemzést, vésztartalék-számítást és interaktív „Mi lenne, ha...?” forgatókönyv-szimulációt nyújt.

---

## 🛠️ Alkalmazott Technológiák (Szakmai Stack)

- **Adatbázis**: **PostgreSQL 18** (DataGrip / pgAdmin / Docker támogatással)
- **Backend**: **Java 21 / Spring Boot 3** (Spring Web, Spring Data JPA / Hibernate, Bean Validation, Springdoc OpenAPI Swagger)
- **Frontend**: **Angular 18+ (TypeScript)** (Standalone komponensek, Signals, Chart.js adatvizualizáció, Glassmorphism & Dark/Light téma)

---

## 📂 Projekt Felépítése

```
d:/Háztartás/
├── database/
│   └── init.sql                 # PostgreSQL séma és magyar háztartási mintaadatok
├── backend/                     # Spring Boot 3 REST API
│   ├── pom.xml
│   ├── mvnw.cmd / mvnw.ps1      # Maven Wrapper
│   └── src/main/java/hu/haztartas/
│       ├── controller/          # REST API végpontok (/api/dashboard, /api/calculations stb.)
│       ├── service/             # Pénzügyi kalkulációs és szimulációs motor
│       ├── repository/          # Spring Data JPA adattárak
│       ├── entity/              # JPA entitások (Category, Income, Expense, SavingsGoal)
│       └── dto/                 # Adatátviteli modellek
└── frontend/                    # Angular 18+ Kliens
    ├── src/app/
    │   ├── features/
    │   │   ├── dashboard/       # Fő irányítópult, KPI kártyák és Chart.js grafikonok
    │   │   ├── calculator/      # Cash Flow, 50/30/20 & Vésztartalék kalkulátor
    │   │   ├── simulator/       # "Mi lenne, ha...?" interaktív forgatókönyv tervező
    │   │   ├── incomes/         # Bevételek listázása és kezelése
    │   │   ├── expenses/        # Kiadások (Fix vs. Változó) és keretfigyelő
    │   │   └── goals/           # Megtakarítási célok és befizetések
    │   └── styles.css           # Modern CSS dizájn rendszer
```

---

## 🚀 Gyors Indítási Útmutató

### 1. Lépés: PostgreSQL Adatbázis létrehozása DataGrip-ben

1. Nyisd meg a **DataGrip** programot.
2. Kattints az **Új adatforrás hozzáadása** (`+` -> **Data Source** -> **PostgreSQL**) gombra.
3. Add meg a kapcsolódási adatokat:
   - **Host**: `localhost`
   - **Port**: `5432`
   - **User**: `postgres`
   - **Password**: *(a telepítéskor megadott jelszavad)*
4. Kattints a **Test Connection** gombra. Ha zöld, nyomj **OK**-t.
5. Hozz létre egy új adatbázist `haztartas_db` néven:
   ```sql
   CREATE DATABASE haztartas_db;
   ```
6. *(Opcionális)*: Nyisd meg és futtasd le a `database/init.sql` fájlt a kezdő kategóriák és mintatételek betöltéséhez (a Spring Boot induláskor automatikusan is inicializálja, ha üres).

---

### 2. Lépés: Spring Boot Backend Indítása

Nyiss egy terminált a `d:\Háztartás\backend` mappában:

```powershell
powershell -ExecutionPolicy Bypass -File .\mvnw.ps1 spring-boot:run
```
*(vagy sima CMD-ben: `mvnw.cmd spring-boot:run`)*

- A REST API a `http://localhost:8080` címen fog futni.
- **Swagger / OpenAPI Dokumentáció**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

### 3. Lépés: Angular Frontend Indítása

Nyiss egy másik terminált a `d:\Háztartás\frontend` mappában:

```powershell
npm start
```

Nyisd meg a böngészőben: **[http://localhost:4200](http://localhost:4200)**

---

## ✨ Fő Funkciók

1. **📊 Pénzügyi Áttekintő Irányítópult**:
   - Valós idejű KPI kártyák (Havi bevétel, Fix kiadás, Változó kiadás, Megtakarítási ráta %).
   - **Költségmegoszlás kördiagram** és **Havi trendek (Bevétel vs. Kiadás)** interaktív Chart.js diagramokkal.
   - Kategória költségkeret (Budget limit) sávok túllépési riasztással.
2. **🧮 Okos Pénzügyi Kalkulátorok**:
   - **50 / 30 / 20 Aranyszabály elemzés**: Szükségletek (50%), Vágyak (30%), Megtakarítás (20%) összevetése a valós adatokkal és szöveges tanácsadással.
   - **Vésztartalék (Emergency Fund) Kalkulátor**: 3, 6 és 12 havi biztonsági alap felépítési ideje a havi megtakarítási kapacitás alapján.
   - **Egyéni Aránytervező**: Interaktív csúszkákkal azonnal tesztelhető a kívánt arány.
3. **🔮 „Mi lenne, ha...?” (What-If) Szimulátor**:
   - Valós idejű sandbox szimuláció csúszkákkal: Fizetésváltozás, rezsiár-drágulás, spórolás, új autóhitel, vagy váratlan egyszeri kár.
   - Jelenlegi vs. szimulált állapot összehasonlító táblázat és **éves vagyoni hatás (+/- Ft)** kalkuláció.
4. **💰 Bevételek és 🛒 Kiadások Menedzser**:
   - Fix (lakbér, rezsi, hitel, előfizetés) és változó (élelmiszer, üzemanyag, szórakozás) tételek rögzítése.
5. **🎯 Megtakarítási Célok Modul**:
   - Célösszegek, céldátumok, előrehaladási sávok és azonnali befizetés jóváírása.
