rootProject.name = "Kkkkkk58"

include("banks")
include("banks.console")
include("cats:service")
findProject(":cats:service")?.name = "service"
include("cats:data-access")
findProject(":cats:data-access")?.name = "data-access"
include("cats:presentation")
findProject(":cats:presentation")?.name = "presentation"
