FROM openjdk:17
MAINTAINER Wevioo <fgbd@wevioo.com>

VOLUME /tmp

# Variable definition
ARG JAR_FILE
ARG PORT

# Install wait-for-it
#ADD ./wait-for-it.sh /app/citoyennete/wait-for-it.sh
ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /app/fgbd/wait-for-it.sh
RUN chmod +x /app/fgbd/wait-for-it.sh

# Install JAR file
ADD ${JAR_FILE} /app/fgbd/fgbd-cloud-config.jar

# Install Running file
ADD ./start.sh /usr/local/bin/start.sh
RUN chmod +x /usr/local/bin/start.sh
#RUN dos2unix /usr/local/bin/start.sh

# Start Application
CMD ["/bin/bash", "/usr/local/bin/start.sh"]

EXPOSE ${PORT}