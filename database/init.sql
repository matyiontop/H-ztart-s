-- ==============================================================================
-- Háztartási Költség- és Jövedelem Kalkulátor - Adatbázis Inicializáló Szkript
-- Használat: Futtatható DataGrip-ben, pgAdmin-ban vagy psql parancssorból
-- ==============================================================================

-- 1. Adatbázis létrehozása (ha még nem létezne)
-- CREATE DATABASE haztartas_db WITH OWNER postgres ENCODING 'UTF8';

-- Táblák eldobása (tiszta újrainicializáláshoz, ha szükséges)
-- DROP TABLE IF EXISTS savings_goals CASCADE;
-- DROP TABLE IF EXISTS expenses CASCADE;
-- DROP TABLE IF EXISTS incomes CASCADE;
-- DROP TABLE IF EXISTS categories CASCADE;

-- 2. Kategóriák tábla
CREATE TABLE IF NOT EXISTS categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(20) NOT NULL,            -- 'INCOME' vagy 'EXPENSE'
    is_fixed BOOLEAN DEFAULT FALSE,       -- Fix / Rendszeres tétel-e (pl. Rezsi, Lakbér vs. Bevásárlás)
    icon VARCHAR(50) DEFAULT 'wallet',     -- Ikon azonosító (pl. home, zap, shopping-cart, dollar)
    color VARCHAR(20) DEFAULT '#4f46e5',   -- Megjelenítési színkód
    monthly_budget_limit NUMERIC(14, 2) DEFAULT 0, -- Opcionális havi keretösszeg
    priority VARCHAR(20) DEFAULT 'NEEDS', -- 'NEEDS' (Szükséglet 50%), 'WANTS' (Vágyak 30%), 'SAVINGS' (Megtakarítás 20%)
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 3. Bevételek tábla
CREATE TABLE IF NOT EXISTS incomes (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    amount NUMERIC(14, 2) NOT NULL,
    frequency VARCHAR(30) NOT NULL DEFAULT 'MONTHLY', -- 'MONTHLY', 'ONETIME', 'YEARLY', 'BIWEEKLY'
    category_id BIGINT REFERENCES categories(id) ON DELETE SET NULL,
    received_date DATE NOT NULL,
    description TEXT,
    is_recurring BOOLEAN DEFAULT TRUE,
    duration_months INT DEFAULT 1,                -- 1 = egyszeri, 2, 3.. = X hónapig, NULL/0 = állandó
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 4. Kiadások tábla
CREATE TABLE IF NOT EXISTS expenses (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    amount NUMERIC(14, 2) NOT NULL,
    is_fixed BOOLEAN NOT NULL DEFAULT FALSE,
    category_id BIGINT REFERENCES categories(id) ON DELETE SET NULL,
    expense_date DATE NOT NULL,
    due_day_of_month INT DEFAULT 10,
    priority VARCHAR(20) DEFAULT 'NEEDS', -- 'NEEDS', 'WANTS', 'SAVINGS'
    description TEXT,
    is_recurring BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 5. Aktuális Vagyon & Számlaegyenleg tábla
CREATE TABLE IF NOT EXISTS account_balance (
    id BIGSERIAL PRIMARY KEY,
    balance NUMERIC(14, 2) NOT NULL DEFAULT 0,
    bank_amount NUMERIC(14, 2) DEFAULT 0,
    cash_amount NUMERIC(14, 2) DEFAULT 0,
    note VARCHAR(255),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================================================
-- Alapadatok betöltése (Tipikus magyar háztartási kategóriák és mintatételek)
-- ==============================================================================

-- Kategóriák: JÖVEDELMEK
INSERT INTO categories (name, type, is_fixed, icon, color, monthly_budget_limit, priority)
SELECT 'Főállású Fizetés', 'INCOME', TRUE, 'briefcase', '#10b981', 0, 'NEEDS'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Főállású Fizetés');

INSERT INTO categories (name, type, is_fixed, icon, color, monthly_budget_limit, priority)
SELECT 'Bónusz & Prémium', 'INCOME', FALSE, 'gift', '#06b6d4', 0, 'WANTS'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Bónusz & Prémium');

INSERT INTO categories (name, type, is_fixed, icon, color, monthly_budget_limit, priority)
SELECT 'Mellékállás / Szabadúszó', 'INCOME', FALSE, 'laptop', '#3b82f6', 0, 'NEEDS'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Mellékállás / Szabadúszó');

INSERT INTO categories (name, type, is_fixed, icon, color, monthly_budget_limit, priority)
SELECT 'Bérbeadás / Passzív Jövedelem', 'INCOME', TRUE, 'home', '#8b5cf6', 0, 'NEEDS'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Bérbeadás / Passzív Jövedelem');

INSERT INTO categories (name, type, is_fixed, icon, color, monthly_budget_limit, priority)
SELECT 'Egyéb Bevétel', 'INCOME', FALSE, 'plus-circle', '#14b8a6', 0, 'WANTS'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Egyéb Bevétel');

-- Kategóriák: FIX KIADÁSOK (Szükségletek 50%)
INSERT INTO categories (name, type, is_fixed, icon, color, monthly_budget_limit, priority)
SELECT 'Lakbér / Lakáshitel Törlesztő', 'EXPENSE', TRUE, 'home', '#ef4444', 180000, 'NEEDS'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Lakbér / Lakáshitel Törlesztő');

INSERT INTO categories (name, type, is_fixed, icon, color, monthly_budget_limit, priority)
SELECT 'Rezsi - Áram & Gáz & Fűtés', 'EXPENSE', TRUE, 'zap', '#f97316', 45000, 'NEEDS'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Rezsi - Áram & Gáz & Fűtés');

INSERT INTO categories (name, type, is_fixed, icon, color, monthly_budget_limit, priority)
SELECT 'Rezsi - Víz, Csatorna, Szemét', 'EXPENSE', TRUE, 'droplet', '#0ea5e9', 15000, 'NEEDS'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Rezsi - Víz, Csatorna, Szemét');

INSERT INTO categories (name, type, is_fixed, icon, color, monthly_budget_limit, priority)
SELECT 'Közös költség', 'EXPENSE', TRUE, 'building', '#eab308', 18000, 'NEEDS'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Közös költség');

INSERT INTO categories (name, type, is_fixed, icon, color, monthly_budget_limit, priority)
SELECT 'Internet & Mobil előfizetés', 'EXPENSE', TRUE, 'wifi', '#6366f1', 16000, 'NEEDS'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Internet & Mobil előfizetés');

INSERT INTO categories (name, type, is_fixed, icon, color, monthly_budget_limit, priority)
SELECT 'Biztosítások (Lakás, Casco, Élet)', 'EXPENSE', TRUE, 'shield-check', '#8b5cf6', 22000, 'NEEDS'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Biztosítások (Lakás, Casco, Élet)');

-- Kategóriák: VÁLTOZÓ KIADÁSOK (Szükségletek & Igények)
INSERT INTO categories (name, type, is_fixed, icon, color, monthly_budget_limit, priority)
SELECT 'Élelmiszer & Napi Bevásárlás', 'EXPENSE', FALSE, 'shopping-cart', '#f59e0b', 140000, 'NEEDS'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Élelmiszer & Napi Bevásárlás');

INSERT INTO categories (name, type, is_fixed, icon, color, monthly_budget_limit, priority)
SELECT 'Közlekedés & Benzin & Bérlet', 'EXPENSE', FALSE, 'car', '#3b82f6', 45000, 'NEEDS'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Közlekedés & Benzin & Bérlet');

INSERT INTO categories (name, type, is_fixed, icon, color, monthly_budget_limit, priority)
SELECT 'Egészségügy & Gyógyszertár', 'EXPENSE', FALSE, 'activity', '#ec4899', 20000, 'NEEDS'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Egészségügy & Gyógyszertár');

INSERT INTO categories (name, type, is_fixed, icon, color, monthly_budget_limit, priority)
SELECT 'Szórakozás & Kikapcsolódás', 'EXPENSE', FALSE, 'film', '#a855f7', 35000, 'WANTS'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Szórakozás & Kikapcsolódás');

INSERT INTO categories (name, type, is_fixed, icon, color, monthly_budget_limit, priority)
SELECT 'Étterem & Kávézó & Rendelés', 'EXPENSE', FALSE, 'coffee', '#d97706', 30000, 'WANTS'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Étterem & Kávézó & Rendelés');

INSERT INTO categories (name, type, is_fixed, icon, color, monthly_budget_limit, priority)
SELECT 'Előfizetések (Netflix, Spotify, Cloud)', 'EXPENSE', TRUE, 'tv', '#06b6d4', 12000, 'WANTS'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Előfizetések (Netflix, Spotify, Cloud)');

INSERT INTO categories (name, type, is_fixed, icon, color, monthly_budget_limit, priority)
SELECT 'Ruházkodás & Szépségápolás', 'EXPENSE', FALSE, 'tag', '#fb7185', 25000, 'WANTS'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Ruházkodás & Szépségápolás');

INSERT INTO categories (name, type, is_fixed, icon, color, monthly_budget_limit, priority)
SELECT 'Egyéb Változó Kiadás', 'EXPENSE', FALSE, 'credit-card', '#64748b', 20000, 'WANTS'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Egyéb Változó Kiadás');

-- Mintatételek betöltése (ha üres az adatbázis)
INSERT INTO incomes (title, amount, frequency, category_id, received_date, description, is_recurring)
SELECT 'Havi munkabér (Nettó fizetés)', 550000, 'MONTHLY', (SELECT id FROM categories WHERE name = 'Főállású Fizetés' LIMIT 1), CURRENT_DATE, 'Elsődleges jövedelem', TRUE
WHERE NOT EXISTS (SELECT 1 FROM incomes WHERE title = 'Havi munkabér (Nettó fizetés)');

INSERT INTO incomes (title, amount, frequency, category_id, received_date, description, is_recurring)
SELECT 'Szabadúszó projekt bevétel', 85000, 'MONTHLY', (SELECT id FROM categories WHERE name = 'Mellékállás / Szabadúszó' LIMIT 1), CURRENT_DATE, 'Kiegészítő webes feladatok', FALSE
WHERE NOT EXISTS (SELECT 1 FROM incomes WHERE title = 'Szabadúszó projekt bevétel');

-- Mintakiadások
INSERT INTO expenses (title, amount, is_fixed, category_id, expense_date, due_day_of_month, priority, description, is_recurring)
SELECT 'Albérleti díj / Hitel', 175000, TRUE, (SELECT id FROM categories WHERE name = 'Lakbér / Lakáshitel Törlesztő' LIMIT 1), CURRENT_DATE, 5, 'NEEDS', 'Havi lakhatás', TRUE
WHERE NOT EXISTS (SELECT 1 FROM expenses WHERE title = 'Albérleti díj / Hitel');

INSERT INTO expenses (title, amount, is_fixed, category_id, expense_date, due_day_of_month, priority, description, is_recurring)
SELECT 'Áram és gázszámla', 38000, TRUE, (SELECT id FROM categories WHERE name = 'Rezsi - Áram & Gáz & Fűtés' LIMIT 1), CURRENT_DATE, 12, 'NEEDS', 'Havi átalány', TRUE
WHERE NOT EXISTS (SELECT 1 FROM expenses WHERE title = 'Áram és gázszámla');

INSERT INTO expenses (title, amount, is_fixed, category_id, expense_date, due_day_of_month, priority, description, is_recurring)
SELECT 'Optikai Internet és Mobil', 14500, TRUE, (SELECT id FROM categories WHERE name = 'Internet & Mobil előfizetés' LIMIT 1), CURRENT_DATE, 15, 'NEEDS', 'Korlátlan net csomag', TRUE
WHERE NOT EXISTS (SELECT 1 FROM expenses WHERE title = 'Optikai Internet és Mobil');

INSERT INTO expenses (title, amount, is_fixed, category_id, expense_date, due_day_of_month, priority, description, is_recurring)
SELECT 'Heti nagybevásárlások', 125000, FALSE, (SELECT id FROM categories WHERE name = 'Élelmiszer & Napi Bevásárlás' LIMIT 1), CURRENT_DATE, 1, 'NEEDS', 'Élelmiszer és háztartási cikkek', TRUE
WHERE NOT EXISTS (SELECT 1 FROM expenses WHERE title = 'Heti nagybevásárlások');

INSERT INTO expenses (title, amount, is_fixed, category_id, expense_date, due_day_of_month, priority, description, is_recurring)
SELECT 'Üzemanyag / BKK bérlet', 38000, FALSE, (SELECT id FROM categories WHERE name = 'Közlekedés & Benzin & Bérlet' LIMIT 1), CURRENT_DATE, 1, 'NEEDS', 'Havi közlekedési költség', TRUE
WHERE NOT EXISTS (SELECT 1 FROM expenses WHERE title = 'Üzemanyag / BKK bérlet');

INSERT INTO expenses (title, amount, is_fixed, category_id, expense_date, due_day_of_month, priority, description, is_recurring)
SELECT 'Streaming szolgáltatások (Netflix, Spotify)', 9500, TRUE, (SELECT id FROM categories WHERE name = 'Előfizetések (Netflix, Spotify, Cloud)' LIMIT 1), CURRENT_DATE, 8, 'WANTS', 'Családi csomagok', TRUE
WHERE NOT EXISTS (SELECT 1 FROM expenses WHERE title = 'Streaming szolgáltatások (Netflix, Spotify)');

-- Mintacélok
INSERT INTO savings_goals (name, target_amount, current_amount, target_date, color, icon, notes)
SELECT '6 Havi Vésztartalék Alap', 1800000, 750000, CURRENT_DATE + INTERVAL '12 months', '#10b981', 'shield', 'Váratlan kiadásokra és biztonságra'
WHERE NOT EXISTS (SELECT 1 FROM savings_goals WHERE name = '6 Havi Vésztartalék Alap');

INSERT INTO savings_goals (name, target_amount, current_amount, target_date, color, icon, notes)
SELECT 'Nyári Utazás / Pihenés', 450000, 180000, CURRENT_DATE + INTERVAL '6 months', '#3b82f6', 'plane', 'Nyaralási büdzsé'
WHERE NOT EXISTS (SELECT 1 FROM savings_goals WHERE name = 'Nyári Utazás / Pihenés');
