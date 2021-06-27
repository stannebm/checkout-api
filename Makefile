build: clean
	lein uberjar

lint:
	lein kibit

release:
	scp target/checkout-api-0.0.1-snapshot-standalone.jar	root@stanne:/root/checkout-api.jar
	ssh stanne "systemctl restart checkout-api"

create-service:
	scp etc/systemd/system/checkout-api.service root@stanne:/etc/systemd/system/checkout-api.service
	ssh stanne "systemctl start checkout-api"
	ssh stanne "systemctl enable checkout-api"

status:
	ssh stanne "systemctl status checkout-api"

log:
	ssh stanne "journalctl -u checkout-api -f"

pull-production-logs:
	rm -f logs/prod/*
	scp -r root@stanne:/logs/* logs/prod/

# https://stackoverflow.com/questions/44246924/clojure-tools-namespace-refresh-fails-with-no-namespace-foo
clean:
	lein clean

repl:
	lein repl
