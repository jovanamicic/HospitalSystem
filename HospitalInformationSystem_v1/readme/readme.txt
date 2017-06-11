**Pokretanje aplikacije**

- Otovoriti MySQL Workbench
- Create New Schema
- Name: hospital_v1 -> Apply
- Otvoriti projekat
- Otvoriti folder src/main/resources -> application.properties
- Za prvo pokretanje pokrenuti sa spring.jpa.hibernate.ddl-auto= create
- Svaki sledeci put spring.jpa.hibernate.ddl-auto= update
- Desni klik na projekat -> Run as Spring Boot
- Otvoriti MySQL Workbench
- File -> New Query tab
- Kopirati fajl init.sql iz foldera database iz projekta
- Pustiti skriptu da popuni bazu podataka inicijalnim podacima