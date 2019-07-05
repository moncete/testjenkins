pipeline {

    agent any

    stages {
        stage ("Clone ansible_devops") {
            steps {
                checkout(
                    [$class: 'GitSCM', 
                    branches: [[name: '*/master']], 
                    doGenerateSubmoduleConfigurations: false, 
                    extensions: [
                        [$class: 'RelativeTargetDirectory', 
                         relativeTargetDir: 'ansible-devops'], 
                        [$class: 'CleanBeforeCheckout']], 
                        submoduleCfg: [], 
                        userRemoteConfigs: [
                            [credentialsId: 'bitbu', url: 'https://aqdes-stash.cm.es/stash/scm/arqdvo/ansible-devops.git']
                        ]
                    ]
                )
            }
        }

        stage ("Clone Filebeat") {
            steps {
                checkout(
                    [$class: 'GitSCM', 
                    branches: [[name: '*/master']], 
                    doGenerateSubmoduleConfigurations: false, 
                    extensions: [
                        [$class: 'RelativeTargetDirectory', 
                         relativeTargetDir: 'filebeat'], 
                        [$class: 'CleanBeforeCheckout']], 
                        submoduleCfg: [], 
                        userRemoteConfigs: [
                            [credentialsId: 'bitbu', url: 'https://aqdes-stash.cm.es/stash/scm/arqdvo/trazas-aws-devops.git']
                        ]
                    ]
                )
            }
        }
        
        stage ("Intalacion Telegraf") {
            steps {
                
                sh '''
                    cd ansible_devops
                '''

                ansiblePlaybook (
                    become: true, 
                    becomeUser: 'SV00967', 
                    colorized: true, 
                    credentialsId: '343c97f1-a663-4d78-94ca-5ed66a5fb039', 
                    disableHostKeyChecking: true,
                    limit: "${params.host}",
                    extras: 'IMAGE_VERSION=1.10 ENTORNO=EPD SERVICE=telegraf action=start', 
                    inventory: "inventory/${params.ENTORNO}", 
                    playbook: 'pb-devops-infra.yml'
                )

                sh '''
                    cd ansible-devops
                    cat ansible.cfg
                    ls -l ansible.cfg
                ''' 
            }
        }

        stage ("Instalcion de Filebeat") {
            steps {
                sh '''
                    cd filebeat
                    cat ansible.cfg
                    ls -l ansible.cfg
                '''
                ansiblePlaybook (
                    become: true, 
                    becomeUser: 'SV00967', 
                    colorized: true, 
                    credentialsId: '343c97f1-a663-4d78-94ca-5ed66a5fb039', 
                    disableHostKeyChecking: true,
                    limit: "${params.host}",
                    extras: 'IMAGE_VERSION=1.10 ENTORNO=EPD SERVICE=telegraf action=start', 
                    inventory: "inventory/filebeat/container/filebeat-${params.ENTORNO}", 
                    playbook: 'pb-devops-infra.yml'
                )
            }
        }
    }
}
