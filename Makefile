build: clean
	lein uberjar

lint:
	lein kibit

release:
	scp .env.prod root@arch:/etc/checkout-api/env
	scp target/checkout-api-0.0.1-snapshot-standalone.jar	root@arch:/root/checkout-api.jar
	ssh arch "systemctl restart checkout-api"

create-service:
	scp etc/systemd/system/checkout-api.service root@arch:/etc/systemd/system/checkout-api.service
	ssh arch "systemctl start checkout-api"
	ssh arch "systemctl enable checkout-api"

status:
	ssh arch "systemctl status checkout-api"

log:
	ssh arch "journalctl -u checkout-api -f"

pull-production-logs:
	rm -f logs/prod/*
	scp -r root@arch:/logs/* logs/prod/

# https://stackoverflow.com/questions/44246924/clojure-tools-namespace-refresh-fails-with-no-namespace-foo
clean:
	lein clean

repl:
	lein repl
