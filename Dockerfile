FROM tomcat:8.0.20-jre8
ADD target/ifood-test-0.0.1-SNAPSHOT.war ifood-test-0.0.1-SNAPSHOT.war
EXPOSE 4011
