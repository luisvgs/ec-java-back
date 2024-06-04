#### Java backend:
Note: the project requires Java 17 to run.
```sh
git clone https://github.com/luisvgs/ec-java-back.git
cd ec-java-back
./nvm install
```
### Alternatively, run the container with the following commands:
```sh
docker build -t ec-java-back .
docker run -e DATASOURCE_PASSWORD='Ecommerce123..!!' 
-e DATASOURCE_USERNAME='postgres.cakevkbiyvpubucopwum' 
-e DATASOURCE_URL='jdbc:postgresql://aws-0-us-west-1.pooler.supabase.com:5432/postgres' -p 8080:8080 --rm ec-java-back
```

### Deployment details:
- Deployed via: https://ec-java-back-production.up.railway.app
