# Projekt aplikacji Doodle

## Opis projektu:
Aplikacja służy do umawiania spotkań. <br> 
Dowolny użytkownik może utworzyć spotkanie,
staje się on wówczas jego adminem tzn. ma prawo ustawić lub edytować jego nazwę, opis oraz 
podać możliwe terminy. Goście (pozostali użytkownicy) mający dostęp do danego spotkania,
mogą jedynie zadeklarować swoją obecność w danym terminie lub też jej brak.

# Plan działań:
* Wybór frameworków
* Ustalenie schemtu bazy danych
* Ustalenie modelu aplikacji
* Implementacja szkieletu aplikacji
* Połączenie z bazą danych
* Dodanie UI (Połączenie z Java FX)
* Przeprowadzenie Unit Testów
* Ustalenie GUI
* Implementacja GUI
* 


## Technologie i Frameworki
### Frameworki:

JavaFX <br>
JPA (Hibernate) <br>
PostgreSQL <br>

### Model Diagram
![db_diagram.png](db_diagram.png)

## Uruchomienie aplikacji
W celu uruchomienia aplikacji konieczne jest pobranie PostegreSQL. Polecamy przy okazji pobrać
pgAdmina (może być także docker jednak nie zalecamy go przy systemie Windows). W razie problemów odsyłamy do [poradnika](https://www.youtube.com/watch?v=0n41UTkOBb0&ab_channel=GeekyScript).<br>

W celu uruchomienia aplikacji musimy w pliku application.properties zmodyfikować linijki:
spring.datasource.username,
spring.datasource.password<br>
podając odpowiadnio nazwę bazy danych i hasło.<br><br>
Jeśli korzystasz z pgAdmin to najprowdopodobniej będzie to wyglgądać następująco:
```properties
spring.datasource.username=postgres
spring.datasource.password="twoje hasło"
```
 Następnie powinniśy być w stanie odpalić projekt.

## Podstawowe jednostki:

### UserEntity
```java
@Entity
@Table(name = "users")
public class UserEntity {
    ...
    @Column(unique=true)
    private String nickname;

    @OneToMany(mappedBy = "creator")
    private List<EventEntity> events;

    @OneToMany(mappedBy = "voter")
    private List<VoteEntity> votes;
    ...
}
```
W początkowym rozwoju aplikacji zakładamy, że będziemy identyfikować
użytkownika po jego nazwie

### EventEntity
```java
@Entity
@Table(name = "events")
public class EventEntity {
    ...
    private String name;

    private String description;

    private Date finalDate;

    private String location;

    @OneToMany(mappedBy = "event")
    private List<ProposalEntity> eventProposals;

    @ManyToOne
    private UserEntity creator;

    @CreatedDate
    private Date created;
    ...
}
```
Modyfikować dane wydarzenie, tzn. zmieniać nazwę, opis, dodawać nowe
propozycje terminów może tylko creator (admin).<br>
FinalDate jest to termin, który został ostatecznie wybrany jako
termin wydarzenia przez admina.

### ProposalEntity
```java
@Entity
@Table(name="proposals")
public class ProposalEntity {
    ...
    private Date startDate;
    private Date endDate;

    @ManyToOne
    private EventEntity event;

    @CreatedDate
    private Date created;

    @OneToMany(mappedBy = "proposal")
    private List<VoteEntity> votes;
    ...
}
```
Klasa ta odpowiada za propozycje terminów wydarzenia danego eventu

### VoteEntity
```java
@Entity
@Table(name = "votes")
public class VoteEntity {
    ...
    @ManyToOne
    private UserEntity voter;

    @ManyToOne
    private ProposalEntity proposal;

    @CreatedDate
    private Date created;

    private String state;
    ...
}
```
Stan informuje nas o decyzji użytkownika. Wyróżniamy 3 podstawowe stany: present, absent, pending.<br>
Stan pending otrzyma uczestnik, który wypelnił swoje preferencje przed dodaniem nowej propozycji terminu.





# Pytania:
* Czy admin moze zaprosic jakies osoby?? - czy przechowywać gdzieś informacje
o wszystkich zaproszonych uczesnitakch?<br>
* testing container? <br>

# Do dodania:
add dto + ro<br>
add validation pipe<br>
add dto + validations<br>
add logging interceptor<br>
add http error filter<br>
testing CRUD METHODS????<br>
add swagger api doc <br>

auth??? oauth2.0?? + decorators & guards~~~~<br>