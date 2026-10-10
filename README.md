# Online Voting System

Spring Boot 3 · Spring Security · Spring Data JPA · MySQL · JSP (JSTL)

## Screenshots

### Home Page
![Home Page](Image/Screenshot%202026-10-11%20002311.png)

### Voter Registration
![Voter Registration](Image/Screenshot%202026-10-11%20002321.png)

### Login
![Login](Image/Screenshot%202026-10-11%20002425.png)

### Election Page
![Election Page](Image/Screenshot%202026-10-11%20002449.png)

## Features
- Voter registration (18+ age check, unique email & Voter ID, BCrypt-hashed passwords)
- Login/logout with Spring Security, CSRF protection, role-based access (ADMIN / VOTER)
- Admin: add/delete candidates, open/close the election, view voter list & turnout
- One vote per voter, enforced in the service layer **and** by a DB unique constraint
- Vote counting with percentage bars; results hidden from voters until the election is closed
- Admin panel never reveals who voted for whom

## Run
1. Install JDK 17+, Maven, MySQL.
2. Edit DB credentials in `src/main/resources/application.properties` (database `voting_db` is created automatically).
3. `mvn spring-boot:run`
4. Open http://localhost:8080
5. Default admin: `admin@vote.com` / `admin123` (change in application.properties)

## Typical flow
1. Log in as admin → add at least two candidates → **Open Voting**
2. Register voters → log in → cast vote
3. Admin → **Close Voting & Publish Results** → everyone can view `/results`
