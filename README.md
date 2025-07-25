# Ci on Kind (Kubernetes in Docker)

- Requires packages
  - helm
  - ansible
  - python3-pip
  - kind
  - Docker

Usage:
```
make deploy
```

## Maven settings

https://help.sonatype.com/en/maven-repositories.html

## Kaniko

https://docs.cloudbees.com/docs/cloudbees-ci-kb/latest/cloudbees-ci-on-modern-cloud-platforms/what-you-need-to-know-when-using-kaniko-from-kubernetes-jenkins-agents

## Deployment

https://gbengaoni.com/blog/Kubernetes-CI-CD-with-Helm-and-Jenkins

## notes

All services are in the same namespace unless DNS does not work

```
kubectl run -it alpine --image alpine
kubectl run -it myimage --image docker-registry-integ:5000/my-custom-image:latest -n ci
```

https://medium.com/@nash.checkin/kind-kubernetes-in-docker-pulling-image-from-private-insecure-registry-2711706629cb
# kind-cicd
