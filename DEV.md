# checkout-api Development

## Run

```sh
# Terminal
cd checkout-api
#sdk use java 17.0.10-zulu
#sdk use maven 3.9.6
mvn -Dspring-boot.run.profiles=dev spring-boot:run

# IDE Eclipse en VM Arguments
-Dspring.profiles.active=dev
-Duser.timezone=-03:00

# Docker Compose
docker-compose up -d --build

# Docker (--add-host localhost:127.0.0.1)
docker run --rm -v $PWD/config:/app/config --net=host -e SPRING_PROFILES_ACTIVE=dev local/checkout-api:dev
```

## Build

```sh
cd checkout-api

docker build --rm -t local/checkout-api:demo .
```

## Crear entregable (release)

Configurar usuario del repositorio **Nexus** en el archivo `~/.m2/settings.xml`.

- <https://maven.apache.org/guides/mini/guide-encryption.html>

### Hacer release con maven-release-plugin

Debe contar con la clave para el repositorio indicado en tag `<distributionManagement></distributionManagement>`

```sh
# NOTA: Si no se crea el branch release, los cambios se realizan en master.
# 1 - Crear el branch release
git checkout -b release/1.0.0

# 2 - Ejecuta los cambios en el branch y crea el tag
mvn -Darguments="-DskipTests" release:clean release:prepare release:perform
```

## Configuración de entorno

Es recomendable contar con las herramientas indicadas

- [IDE](https://www.eclipse.org/downloads/)
- [Java 17 o >](https://www.oracle.com/technetwork/es/java/javase/downloads/index.html)
- [Maven](https://maven.apache.org/index.html)  Se puede utilizar el existente en Eclipse IDE.
- [Spring Tool Suite](https://spring.io/tools/sts/all) En mi caso (janusky@gmail.com) lo instalé desde Marketplace de eclipse.

## Clean Up

```sh
docker-compose down -v
```