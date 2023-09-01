mvn clean
mvn package
docker build -t fransiciliano/vetlens:vetlens-backend .
docker push fransiciliano/vetlens:vetlens-backend