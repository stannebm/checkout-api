build: clean lint
	lein uberjar

lint:
	lein kibit

release:
	scp target/stanne-0.0.1-snapshot-standalone.jar	root@stanne:/root/fpx-api.jar
	ssh stanne "systemctl restart fpx-api"

create-service:
	scp etc/systemd/system/fpx-api.service root@stanne:/etc/systemd/system/fpx-api.service

status:
	ssh stanne "systemctl status fpx-api"

log:
	ssh stanne "journalctl -u fpx-api -f"

pull-production-logs:
	rm -f logs/prod/*
	scp -r root@stanne:/logs/* logs/prod/

# https://stackoverflow.com/questions/44246924/clojure-tools-namespace-refresh-fails-with-no-namespace-foo
clean:
	lein clean

repl:
	lein repl
