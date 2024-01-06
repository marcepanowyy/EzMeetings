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

### Plan działań:
* dodanie dokumentacji API (Swagger)
* dodanie usuwania eventów / wybrania ostatecznego terminu na frontendzie

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


[//]: # (#### Baza danych)

[//]: # ()
[//]: # (Aby uruchomic aplikację należy najpierw postawić bazę danych. Proponujemy jeden z dwóch możliwych sposobów:)

[//]: # ()
[//]: # (1&#41; Użycie dokeryzowanej bazy danych:)

[//]: # ()
[//]: # (    Pobieramy obraz postgresa na lokalną maszynę:)

[//]: # (    )
[//]: # (    ```)

[//]: # (    docker pull postgres)

[//]: # (    ```)

[//]: # (    )
[//]: # (    Następnie uruchamiamy kontener wpisując odpowiednią nazwę użytkownika i hasło &#40;może być ta co w przykładzie&#41;:)

[//]: # (    )
[//]: # (    ```)

[//]: # (    docker run -d --name postgres -e POSTGRES_PASSWORD=password -e POSTGRES_USER=username -p 5432:5432 postgres)

[//]: # (    ```)

[//]: # (    )
[//]: # (    Możemy sprawdzić czy kontener działa poprawnie)

[//]: # (    )
[//]: # (    ```)

[//]: # (    docker ps)

[//]: # (    ```)

[//]: # (    )
[//]: # (    Należy pamiętać, aby w pliku application.properties zmienić dane logowania do bazy danych na te, które podaliśmy przy uruchamianiu kontenera.)

[//]: # (    )
[//]: # (    ```properties)

[//]: # (    spring.datasource.username=username)

[//]: # (    spring.datasource.password=password)

[//]: # (    ```)

[//]: # (    )
[//]: # (    Jeśli występują problemy, kierujemy do [oficjalnej dokumentacji]&#40;https://www.docker.com/blog/how-to-use-the-postgres-docker-official-image/&#41; lub skorzystanie z drugiego sposobu)

[//]: # ()
[//]: # ()
[//]: # (2&#41; Pobranie PostgreSQL i użycie PgAdmina)

[//]: # ()
[//]: # (    Pobieramy PostgreSQL oraz PgAdmina ze strony [oficjalnej strony]&#40;https://www.postgresql.org/download/&#41;.)

[//]: # (    )
[//]: # (    Analogicznie należy pamiętać o zmianie danych logowania w pliku application.properties.)

[//]: # (    )
[//]: # (    W razie problemów odsyłamy do [poradnika]&#40;https://www.youtube.com/watch?v=0n41UTkOBb0&ab_channel=GeekyScript&#41;)

[//]: # ()
[//]: # (#### Backend)

[//]: # ()
[//]: # (Importujemy folder *backend* do Intellij: File -> Open.. Czekamy, aż Gradle pobierze wszystkie zależności i )

[//]: # ()
[//]: # (zbuduje projekt. Aby uruchomić aplikację korzystamy z polecenia "Run" na klasie CombatSquadApplication &#40;zielona strzałka&#41;)

[//]: # ()
[//]: # ()
[//]: # (Aplikacja powinna się uruchomić na porcie 8080)

[//]: # ()
[//]: # ()
[//]: # (Aby przetestować działanie aplikacji korzystamy z test runnera &#40;zielona strzałka obok nazwy klasy testowej&#41;)

[//]: # ()
[//]: # (#### Frontend)

[//]: # ()
[//]: # (Aby uruchomić aplikację frontendową należy najpierw zainstalować node.js &#40;https://nodejs.org/en/&#41;)

[//]: # (Projekt został stworzony przy użyciu wersji 16.17.0 jednak będzie działać również na wyższych wersjach.)

[//]: # ()
[//]: # (Importujemy folder *frontend* do Webstorm lub VS Code: File -> Open.. )

[//]: # ()
[//]: # (Aby pobrać moduły node.js należy wpisać w terminalu:)

[//]: # (```)

[//]: # (npm install)

[//]: # (```)

[//]: # (Pozwoli nam to załadować wszystkie potrzebne biblioteki. Następnie wpisujemy:)

[//]: # (```)

[//]: # (npm start)

[//]: # (```)

[//]: # ()
[//]: # (Aplikacja powinna się uruchomić na porcie 3000.)

### Testowanie backendu

Importujemy folder *backend* do Intellij: File -> Open.. Czekamy, aż Gradle pobierze wszystkie zależności i 
zbuduje projekt. Aby przetestować działanie aplikacji korzystamy z test runnera (zielona strzałka obok nazwy klasy testowej).

