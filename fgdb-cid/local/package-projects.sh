#!/bin/sh

set -ex
mvn -f C:/Users/okr/Downloads/fgbd_backend/fgdb-cloud-config/pom.xml               clean package -Plocal -PdockerImage -DskipTests -Dmaven.test.skip=true
mvn -f C:/Users/okr/Downloads/fgbd_backend/fgdb-cloud-discovery/pom.xml            clean package -Plocal -PdockerImage -DskipTests -Dmaven.test.skip=true
mvn -f C:/Users/okr/Downloads/fgbd_backend/fgdb-cloud-gateway/pom.xml            clean package -Plocal -PdockerImage -DskipTests -Dmaven.test.skip=true
mvn -f C:/Users/okr/Downloads/fgbd_backend/fgdb-services/fgdb-extract-batch/pom.xml         clean package -Plocal -PdockerImage -DskipTests -Dmaven.test.skip=true
