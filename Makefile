# PROD := "arch"
PROD := "stanne"

build: clean
	lein uberjar

lint:
	lein kibit

release:
	scp .env.prod ${PROD}:/etc/checkout-api/env
	scp target/checkout-api-0.0.1-SNAPSHOT-standalone.jar \
		${PROD}:/root/checkout-api.jar
	ssh ${PROD} "systemctl restart checkout-api"

create-service:
	scp etc/systemd/system/checkout-api.service \
		${PROD}:/etc/systemd/system/checkout-api.service
	ssh ${PROD} "systemctl start checkout-api"
	ssh ${PROD} "systemctl enable checkout-api"

status:
	ssh ${PROD} "systemctl status checkout-api"

log:
	ssh ${PROD} "journalctl -u checkout-api -f"

pull-production-logs:
	rm -f logs/prod/*
	scp -r ${PROD}:/logs/* logs/prod/

# https://stackoverflow.com/questions/44246924/clojure-tools-namespace-refresh-fails-with-no-namespace-foo
clean:
	lein clean

repl:
	lein repl
