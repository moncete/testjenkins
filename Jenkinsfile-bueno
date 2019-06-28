pipeline {
    
    agent any

    parameters { 
        //choice(name: 'ENTORNO', choices: ['EPD', 'EPI1', 'EP2'], description: 'Entornos previos') 
        text(name: 'Maquina', defaultValue: 'Nombre_Maquina', description:'Maquina donde corre el Servicio' )
        string(name: 'Servicio', defaultValue: 'Nombre_servicio', description: 'Servicio a reiniciar')
        choice(name: 'Accion', choices: ['Stop','Start','Restart'], description: 'Accion a Ejecutar')
    }

    stages{
        stage('Stop'){
            when{
                expression { params.Accion == 'Stop' || params.Accion == 'Restart' }
            }
            steps{
                withCredentials([sshUserPrivateKey(credentialsId: '343c97f1-a663-4d78-94ca-5ed66a5fb039', keyFileVariable: 'token', passphraseVariable: '', usernameVariable: 'user')]) {
                    sh "ssh -o StrictHostKeyChecking=no -p 2153 -i ${token} SV00967@${params.Maquina} docker ps -f name=${params.Servicio}; echo Adios"
                }
            }
        }
        stage('Start'){
            when{
                expression{ params.Accion == 'Start'|| params.Accion == 'Restart' }
            }
            steps{
                withCredentials([sshUserPrivateKey(credentialsId: '343c97f1-a663-4d78-94ca-5ed66a5fb039', keyFileVariable: 'token', passphraseVariable: '', usernameVariable: 'user')]) {
                    sh "ssh -o StrictHostKeyChecking=no -p 2153 -i ${token} SV00967@${params.Maquina} docker ps -f name=${params.Servicio}; echo Hola"
                }   
            }
        }
    }   
}


