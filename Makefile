install:
	ansible-playbook ansible-helm/cicd.yml --extra-vars="mode=deploy"

uninstall:
	ansible-playbook ansible-helm/cicd.yml --extra-vars="mode=destroy"
