# OnlineShoppingApplication
Online Shopping Application with microservices in Spring Boot

##keycloak
sudo docker run -p 8180:8080 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin quay.io/keycloak/keycloak:13.0.0

##RabbitMQ
sudo docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management

##vault
vault server -dev
