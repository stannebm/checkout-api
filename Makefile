build:
	lein uberjar

release:
	scp target/stanne-0.0.1-snapshot-standalone.jar	root@arch:/root/fpx-api.jar
	ssh arch "systemctl restart fpx-api"

create-service:
	scp etc/systemd/system/fpx-api.service root@arch:/etc/systemd/system/fpx-api.service

status:
	ssh arch "systemctl status fpx-api"

log:
	ssh arch "journalctl -u fpx-api -f"
