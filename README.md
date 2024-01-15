## Projekt aplikacji Doodle

### Opis projektu:

Projekt zakłada stworzenie aplikacji w stylu Doodle, dedykowanej do organizacji wydarzeń i spotkań. 
Główną funkcjonalnością będzie umożliwienie użytkownikom tworzenia wydarzeń z wieloma opcjami terminowymi. 
Zaproszeni uczestnicy będą mieli możliwość głosowania na preferowane terminy, co pozwoli ustalić ostateczną i 
odpowiednią dla wszystkich datę wydarzenia.

Najważeniejsze feature'y:
* tworzenie wydarzeń 
* rejestracja i logowanie użytkowników
* zapisywanie użytkowników na wydarzenia
* głosowanie na preferowane terminy
* wybór ostatecznego terminu wydarzenia
* zarządzanie wydarzeniami - edycja i usuwanie (tylko dla twórcy wydarzenia)

### Technologie

* PostgreSQL - baza danych
* Spring Boot, JPA (Hibernate) - backend
* React - frontend

### Schemat bazy danych 

<img src="assets/db_diagram.png" alt="db_diagram.jpg" height="450px">

### Uruchomienie aplikacji

Mając Dockera, aplikację można uruchomić jedną komendą. Wystarczy wejść do folderu projektowego 
(w miejscu, gdzie znajduje się plik **docker-compose.yaml**) i uruchomić komendę:

```
docker-compose up -d 
```

Domyślnie aplikacja będzie dostępna na następujących portach:

* Baza danych *PostgreSQL* : 5432
* Backend *Spring Boot* : 8080
* Frontend *React* : 3000

Aplikacje można uruchomić również częściami w następującej kolejności:

1. Baza danych:
   ```
   docker-compose up -d db
   ```

2. Backend:
   ```
   docker-compose up -d backend
   ```

3. Frontend:
   ```
   docker-compose up -d frontend
   ```

### Testowanie backendu

Importujemy folder *backend* do Intellij: File -> Open.. Czekamy, aż Gradle pobierze wszystkie zależności i 
zbuduje projekt. Aby przetestować działanie aplikacji korzystamy z test runnera (zielona strzałka obok nazwy klasy testowej).

