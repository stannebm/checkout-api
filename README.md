# StAnne Checkout API (Cybersource/FPX)

This project integrates FPX and Cybersource API according to the [official documentation](https://fpxexchange.myclear.org.my:8443/MerchantIntegrationKit/)

FPX is a payment gateway that provides online banking services to internet businesses.

https://paynet.my/personal-fpx.html

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

## Deployment

run `make && make release`

## Requirements

FPX certificate and merchant exchange key must be available at path `/etc/fpx`.

FPX is configured [here](/src/stanne/fpx/core.clj)

Cybersource is configured at the profile level:

https://ebc2test.cybersource.com/ebc2/app/Home

CyberSource's environment variables must be present at `/etc/checkout-api/env`

## UAT test

In UAT test, only "SBI Bank A" is supported, and the username/password is 1234/1234
