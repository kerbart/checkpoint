# Ilet server


Partie serveur du projet "Infirmiers Liberaux En Tournée".

## Prerequis 
- java 1.8
- Maven 3

## Installation

Pour builder le serveur et lancer les tests :

`mvn clean install`

## Serveur

Pour lancer le serveur : 

`mvn jetty:run`

## Documentation des APIs

Les APIs sont disponibles au endpoint `http://localhost:8080/ilet/api/`

La documentation des APIs est ici `http://localhost:8080/ilet/swagger/index.html`

## configuration

Vous pouvez utiliser un base de donnée différente (celle par defaut est H2 in memory). il suffit pour ça de dupliquer le fichier  `application-ilet.properties` et de le rendre disponible dans le classpath au lancement sur serveur.

