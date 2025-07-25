pipeline {
  agent {
    kubernetes {
      yaml '''
        apiVersion: v1
        kind: Pod
        spec:
          containers:
          - name: maven
            image: maven:3.9.6-eclipse-temurin-17-alpine
            command:
            - cat
            tty: true
            volumeMounts:
            - mountPath: "/root/.m2"
              name: "volume-0"
              readOnly: false
            - mountPath: "/root/.m2repo"
              name: "volume-1"
              readOnly: false
          volumes:
          - name: "volume-0"
            secret:
              secretName: "m2-settings"
          - name: "volume-1"
            persistentVolumeClaim:
              claimName: "maven-repo"
              readOnly: false
        '''
    }
  }
  stages {
    stage('Clone') {
      steps {
        container('maven') {
          git branch: 'main', changelog: false, poll: false, url: 'https://github.com/erwanjouan/spring-boot-batch.git'
        }
      }
    }
    stage('Build') {
      steps {
        container('maven') {
          sh 'mvn package'
        }
      }
    }
    stage('Run') {
      environment {
        SPRING_PROFILES_ACTIVE = 'dev'
      }
      steps {
        container('maven') {
          sh 'java -jar target*.jar'
        }
      }
    }
  }
}