FROM jenkinsci/blueocean



ENV JENKINS_USER admin
ENV JENKINS_PASS admin

# Skip the initial setup wizard
ENV JAVA_OPTS  -Djenkins.install.runSetupWizard=false
#ENV -Dhttp.auth.preference=basic
ENV -Djdk.http.auth.tunneling.disabledSchemes=

USER root
#Numero de procesos de ejecucion
COPY executors.groovy /usr/share/jenkins/ref/init.groovy.d/

#Creacion del usaurio Admin
COPY default-user.groovy /usr/share/jenkins/ref/init.groovy.d/

RUN chown -R jenkins.jenkins /usr/share/jenkins/ref/init.groovy.d

USER jenkins
