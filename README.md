## Projekt aplikacji Doodle

### Opis projektu:

Projekt zakłada stworzenie aplikacji w stylu Doodle, dedykowanej do organizacji wydarzeń i spotkań. 
Główną funkcjonalnością będzie umożliwienie użytkownikom tworzenia wydarzeń z wieloma opcjami terminowymi. 
Zaproszeni uczestnicy będą mieli możliwość głosowania na preferowane terminy, co pozwoli ustalić ostateczną i 
odpowiednią dla wszystkich datę wydarzenia.

### Plan działań:
* Zaimplementowanie gui umozliwiajacego wyswietlanie wydarzeń stworzonych przez konkretnego użytkownika
* Zaimplementowanie gui pozwalającego na przeglądanie dostępnych opcji terminowych dla danego wydarzenia 
* Stworzenie w gui możliwości dodawania nowych wydarzeń przez użytkownika (początkowo niezalogowanego)
* Zapewnienie prostego formularza umożliwiajacego okreslenie nazwy wydarzenia, jego opisu oraz opcji terminowych
* Zapewnienie gui do zaznaczania preferowanych opcji terminowych przez użytkownika na konkretne wydarzenie
* możliwość oddania głosu (vote) na konkretne wydarzenie przez użytkownika (początkowo niezalogowanego) w gui
* Autoryzacja i atuentykacja na backendzie - jesli bedzie czas
* Dodanie testów dla frontendu

### Technologie

Spring Boot, JPA (Hibernate), PostreSQL - backend
JavaFX - frontend

### Schemat bazy danych

![db_diagram.jpg](assets/db_diagram.png)

### Uruchomienie aplikacji

#### Baza danych

Aby uruchomic aplikację należy najpierw postawić bazę danych. Proponujemy jeden z dwóch możliwych sposobów:

1) Użycie dokeryzowanej bazy danych:

    Pobieramy obraz postgresa na lokalną maszynę:
    
    ```
    docker pull postgres
    ```
    
    Następnie uruchamiamy kontener wpisując odpowiednią nazwę użytkownika i hasło (może być ta co w przykładzie):
    
    ```
    docker run -d --name postgres-1 -e POSTGRES_PASSWORD=password -e POSTGRES_USER=username -p 5432:5432 postgres
    ```
    
    Możemy sprawdzić czy kontener działa poprawnie
    
    ```
    docker ps
    ```
    
    Należy pamiętać, aby w pliku application.properties zmienić dane logowania do bazy danych na te, które podaliśmy przy uruchamianiu kontenera.
    
    ```properties
    spring.datasource.username=postgres
    spring.datasource.password="twoje hasło"
    ```
    
    Jeśli występują problemy, kierujemy do [oficjalnej dokumentacji](https://www.docker.com/blog/how-to-use-the-postgres-docker-official-image/) lub skorzystanie z drugiego sposobu

<br>

2) Pobranie PostgreSQL i użycie PgAdmina

    Pobieramy PostgreSQL oraz PgAdmina ze strony [oficjalnej strony](https://www.postgresql.org/download/).
    
    Analogicznie należy pamiętać o zmianie danych logowania w pliku application.properties.
    
    W razie problemów odsyłamy do [poradnika](https://www.youtube.com/watch?v=0n41UTkOBb0&ab_channel=GeekyScript)

#### Backend

Importujemy folder backend do Intellij: File -> Open.. Czekamy, aż Gradle pobierze wszystkie zależności i 
zbuduje projekt. Aby uruchomić aplikację korzystamy z polecenia "Run" na klasie CombatSquadApplication (zielona strzałka)

Aby przetestować działanie aplikacji korzystamy z test runnera (zielona strzałka obok nazwy klasy testowej)

#### Frontend

Analogicznie tak jak wyżej, tylko importujemy folder frontend. 
Aby uruchomić aplikację korzystamy z polecenia "Run" na klasie Main (zielona strzałka).

#### Frontend w  ReactJS

Aby włączyć projekt należy wejść do folderu frontend-react, a następnie wpisać :
```
npm install
```
Pozwoli to załadować wszystkie potrzebne biblioteki. Następnie wpisujemy:
```
npm start
```
Aplikacja powinna się uruchomić na porcie 3000.
Proszę wpisać w przeglądarce adres: http://localhost:3000/

Podstawowe routy:
* /login - logowanie
* /register - rejestracja
* /events - lista wydarzeń (dostępna po zalogowaniu) z możliwością zarządzania nimi
* /events/:id - szczegóły wydarzenia (dostępna dla wszystkich)
* /events/:id/edit - edycja wydarzenia (dostępna dla twórcy wydarzenia)
* /events/new- głosowanie na wydarzenie (dostępna dla wszystkich)

