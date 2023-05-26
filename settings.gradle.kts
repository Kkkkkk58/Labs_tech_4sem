rootProject.name = "Kkkkkk58"

include("banks")
include("banks.console")
include("cats:service")
findProject(":cats:service")?.name = "service"
include("cats:data-access")
findProject(":cats:data-access")?.name = "data-access"
include("cats:presentation")
findProject(":cats:presentation")?.name = "presentation"
include("cats:common")
findProject(":cats:common")?.name = "common"
include("cats-microservices")
include("cats-microservices:cats-service")
findProject(":cats-microservices:cats-service")?.name = "cats-service"
include("cats-microservices:cat-owners-service")
findProject(":cats-microservices:cat-owners-service")?.name = "cat-owners-service"
include("cats-microservices:interface-service")
findProject(":cats-microservices:interface-service")?.name = "interface-service"
include("cats-microservices:common")
findProject(":cats-microservices:common")?.name = "common"
include("cats-microservices:users-service")
findProject(":cats-microservices:users-service")?.name = "users-service"
include("cats-microservices:jpa-entities")
findProject(":cats-microservices:jpa-entities")?.name = "jpa-entities"
include("cats-microservices:jpa-entities")
findProject(":cats-microservices:jpa-entities")?.name = "jpa-entities"
include("cats-microservices:discovery-server")
findProject(":cats-microservices:discovery-server")?.name = "discovery-server"
include("cats-microservices:api-gateway")
findProject(":cats-microservices:api-gateway")?.name = "api-gateway"
include("cats-microservices:utils")
findProject(":cats-microservices:utils")?.name = "utils"
