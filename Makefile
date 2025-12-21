install:
	python3 -m venv ./.venv && \
	source ./.venv/bin/activate && \
	python -m pip install -r requirements.txt && \
	ansible-playbook ansible-helm/cicd.yml --extra-vars="mode=deploy"

uninstall:
	python3 -m venv ./.venv && \
	source ./.venv/bin/activate && \
	ansible-playbook ansible-helm/cicd.yml --extra-vars="mode=destroy"
