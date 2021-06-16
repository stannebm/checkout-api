# stanne

FIXME

## Getting Started

1. Start the application: `lein run`
2. Go to [localhost:8080](http://localhost:8080/) to see: `Hello World!`
3. Read your app's source code at src/fpx_app/service.clj. Explore the docs of functions
   that define routes and responses.
4. Run your app's tests with `lein test`. Read the tests at test/fpx_app/service_test.clj.
5. Learn more! See the [Links section below](#links).

## Configuration

To configure logging see config/logback.xml. By default, the app logs to stdout and logs/.
To learn more about configuring Logback, read its [documentation](http://logback.qos.ch/documentation.html).

## Stacks

- JDK: Amazon Corretto-11.0.11.9.1
- Web framework: Pedestal
- Web server: Jetty
- Workflow/microframeworks: Integrant & Integrant REPL
- Encryption Provider: BouncyCastle
- Templating language: Hiccup

## Dev Workflow

1. run `lein repl`
2. execute `(go)`
3. Visit `localhost:8080`
4. In emacs, run `M-x: cider-connect-clj`
5. After changing a file, run `M-x: cider-ns-refresh`
6. Refresh `localhost:8080`
