#!/bin/sh

set -ex

mvn -f C:/Users/okr/Downloads/fgbd_backend/fgdb-cloud-config/pom.xml               -Pdocker-dev -PdockerImage docker:build
mvn -f C:/Users/okr/Downloads/fgbd_backend/fgdb-cloud-discovery/pom.xml            -Pdocker-dev -PdockerImage docker:build
mvn -f C:/Users/okr/Downloads/fgbd_backend/fgdb-cloud-gateway/pom.xml            -Pdocker-dev -PdockerImage docker:build
mvn -f C:/Users/okr/Downloads/fgbd_backend/fgdb-services/fgdb-extract-batch/pom.xml  -Pdocker-dev -PdockerImage docker:build
