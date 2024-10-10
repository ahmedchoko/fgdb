FROM openjdk:17
MAINTAINER Wevioo <fgbd@wevioo.com>
VOLUME /tmp
ARG JAR_FILE
ARG PORT

# Set the working directory
WORKDIR /app/fgbd

# Install wait-for-it
ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /app/fgbd/wait-for-it.sh
RUN chmod +x /app/fgbd/wait-for-it.sh

ADD ${JAR_FILE} /app/fgbd/fgbd-extract-batch.jar

# Install Running file
ADD ./start.sh /usr/local/bin/start.sh
RUN chmod +x /usr/local/bin/start.sh
#RUN dos2unix /usr/local/bin/start.sh

# Start Application
CMD ["/bin/bash", "/usr/local/bin/start.sh"]

EXPOSE ${PORT}
