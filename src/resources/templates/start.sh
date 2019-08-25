cd bin

java -Xmx512m -DstarterIgniteSecureKey={{secureKey}} -DdbUrl={{DB_URL}} -DdbName={{DB_NAME}} -DdbUser={{DB_USER}} -DdbPassword={{DB_PASSWORD}} -DadminUser={{adminUser}} -DadminPassword={{adminPassword}} -Dspring.application.name={{appName}} -DservicePort={{servicePort}} -DserviceHost={{serviceHost}} -DadminServiceURL=https://{{adminServiceHost}}:{{adminServicePort}} -jar admin-0.8-SNAPSHOT-exec.jar & disown
~                                                                                              