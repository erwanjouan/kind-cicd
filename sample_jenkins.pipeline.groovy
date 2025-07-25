podTemplate(containers: [
    containerTemplate(name: 'maven', image: 'maven:3.9.6-eclipse-temurin-17-alpine', command: 'sleep', args: '99d')
    ], volumes: [
    secretVolume(secretName: 'mvn-settings', mountPath: '/root/.m2'),
    persistentVolumeClaim(mountPath: '/root/.m2repo', claimName: 'maven-repo', readOnly: false)
    ]) {
  
    node(POD_LABEL) {
      stage('Build a Maven project') {
        git url:'https://github.com/spring-projects/spring-petclinic', branch: "main"
        container('maven') {
          sh '''
            mvn clean deploy \
                -DaltReleaseDeploymentRepository=nexus::http://my-nexus-repository-manager:8081/repository/maven-releases \
                -DaltSnapshotDeploymentRepository=nexus::http://my-nexus-repository-manager:8081/repository/maven-snapshots
            '''
        }
      }
    }
  }