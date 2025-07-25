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
          - name: kaniko
            image: gcr.io/kaniko-project/executor:debug
            command:
            - sleep
            args:
            - 99d
          - name: helm
            image: lachlanevenson/k8s-helm:v3.1.1
            command:
            - cat
            tty: true
          - name: python
            image: python
            command:
            - cat
            tty: true
          volumes:
          - name: "volume-0"
            secret:
              secretName: "m2-settings"
          - name: "volume-1"
            persistentVolumeClaim:
              claimName: "maven-repo"
              readOnly: false
          - name: "volume-2"
            secret:
              secretName: "kube-config"
        '''
    }
  }
  stages {
    stage('Clone') {
      steps {
        container('maven') {
          git branch: 'main', changelog: false, poll: false, url: 'https://github.com/erwanjouan/spring-batch-101.git'
        }
      }
    }
    stage('Build-Jar-file') {
      steps {
        container('maven') {
          sh 'mvn package'
        }
      }
    }
    stage('Build-Docker-Image') {
      steps {
        container('kaniko') {
          sh '/kaniko/executor --insecure-registry docker-registry-integ:5000 --context `pwd` --dockerfile "deployment/Dockerfile" --destination "docker-registry-integ:5000/my-custom-image"'
        }
      }
    }
    stage('Python') {
      steps {
        container('python') {
          sh 'pip install kubernetes'
          sh 'python -u deployment/task_executor.py "Kubernetes"'
        }
      }
    }
  }
}