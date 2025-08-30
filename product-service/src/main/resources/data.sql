-- CLEAN TABLE
DELETE FROM product;

-- ADD PRODUCTS TO TEST THE APPLICATION
INSERT INTO product (name, category, price, description, sku_code, quantity, created_at, updated_at)
VALUES
    ('T-Shirt Basic', 'SHIRTS', 49.99, 'Klasyczny bawełniany T-shirt w kolorze białym', 'SH-TSB-001', 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Buty sportowe', 'SHOES', 249.99, 'Wygodne buty do biegania z amortyzacją', 'SH-SPO-001', 45, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Kurtka zimowa', 'JACKETS', 299.99, 'Ciepła kurtka na zimę z wodoodpornym wykończeniem', 'JK-WIN-001', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Smartfon XYZ', 'ELECTRONICS', 1999.99, 'Najnowszy model smartfona z procesorem 8-rdzeniowym', 'EL-PHO-001', 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Laptop ProBook', 'COMPUTERS', 3499.99, 'Laptop do zastosowań biznesowych z dyskiem SSD', 'CO-LPT-001', 15, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Bestseller 2023', 'BOOKS', 39.99, 'Najpopularniejsza powieść roku 2023', 'BO-FIC-001', 200, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Sofa narożna', 'FURNITURE', 1299.99, 'Elegancka sofa narożna do salonu', 'FU-SOF-001', 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Klocki konstrukcyjne', 'TOYS', 129.99, 'Zestaw klocków dla dzieci 7-12 lat', 'TO-BLO-001', 50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Zestaw przypraw', 'GROCERIES', 59.99, 'Komplet 12 podstawowych przypraw kuchennych', 'GR-SPI-001', 80, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Krem nawilżający', 'BEAUTY', 89.99, 'Intensywnie nawilżający krem do twarzy', 'BE-CRE-001', 60, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Piłka do koszykówki', 'SPORTS', 149.99, 'Profesjonalna piłka do koszykówki', 'SP-BAS-001', 40, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Olej silnikowy', 'AUTOMOTIVE', 79.99, 'Syntetyczny olej silnikowy 5W-30', 'AU-OIL-001', 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Słuchawki bezprzewodowe', 'MUSIC', 399.99, 'Słuchawki z aktywną redukcją szumów', 'MU-HEA-001', 25, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Film akcji na Blu-ray', 'MOVIES', 99.99, 'Najnowszy hit kinowy w jakości 4K', 'MO-ACT-001', 70, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Zestaw narzędzi ogrodowych', 'GARDEN', 159.99, 'Podstawowe narzędzia do pielęgnacji ogrodu', 'GA-TOO-001', 35, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Karma dla psa', 'PETS', 119.99, 'Premium karma sucha dla psów dorosłych', 'PE-DOG-001', 90, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);