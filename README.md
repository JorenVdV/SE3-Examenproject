# README #

SE3 examen project 2016/17, eerste zit, deel A.

### How do I get set up? ###

Om te compileer gebruik maven, na het installeren van de lokale dependency 'conveyorservice':
mvn install:install-file -Dpath=lib/conveyorservice-1.0.jar\
         -DgroupId=be.kdg.se3.proxy \
         -DartifactId=Converyorservice \
         -Dversion=1.0 -Dpackaging=jar

mvn clean compile test