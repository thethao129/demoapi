FROM tomcat:8.0.51-jre8-alpine
RUN rm -rf /usr/local/tomcat/webapps/*
#ENV dbDriver=com.mysql.Driver dbConnectionUrl=jdbc:mysql://localhost:3306/demo_api dbUserName=root dbPassword=123456
COPY ./target/demoApi.war /usr/local/tomcat/webapps/demoApi.war
CMD ["catalina.sh","run"]