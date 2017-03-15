#### База данных
Схема базы данных:


````
CREATE TABLE users (
     id bigint NOT NULL AUTO_INCREMENT,
     username varchar(50) NOT NULL unique,
     password varchar(60) NOT NULL,
     full_name varchar(50) NOT NULL,
     PRIMARY KEY (id)
) DEFAULT CHARACTER SET utf8;


CREATE TABLE contacts(
     id bigint NOT NULL AUTO_INCREMENT,
     first_name varchar(50) NOT NULL,
     middle_name varchar(50) NOT NULL,
     last_name varchar(50) NOT NULL,
     mobile_phone_number varchar(13) NOT NULL,
     home_phone_number varchar(13),
     email varchar(50),
     address varchar(50),
     user_id bigint NOT NULL,
     PRIMARY KEY (id),
     FOREIGN KEY (user_id) REFERENCES users(id)
) DEFAULT CHARACTER SET utf8;
````




#### Конфигурация
Пример файла конфигурации (`demo.properties`):


````
# Active storage. Choose one of: json/mysql
storage = json


# MySQL storage config
storage.mysql.url = 127.0.0.1:3306/phonebook
storage.mysql.url.postfix = ?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&autoReconnect=true&useSSL=false&useLegacyDatetimeCode=false
storage.mysql.username = root
storage.mysql.password = root


# JSON storage config
storage.json.path = ./
````


* `storage` — это свойство определяет тип хранилища. 
Mожет принимать значения `json` или `mysql` 
* `storage.mysql.url` может принимать значения в следующих в форматах:
`jdbc:mysql://127.0.0.1:3306/phonebook` и `127.0.0.1:3306/phonebook`. 
В любом случае будет использоваться MySQL JDBC-драйвер.
* `storage.mysql.url.postfix` фактически конкатенируется с `storage.mysql.url` 
и определяет дополнительные параметры подключения к БД. Значение устанвленное в `demo.properties`
установленно на основании ошибок с часовым поясом при запуске под Windows и на основании отладочных сообщений 
JDBC-драйвера.

#### Cборка
В репозитории присутствует `maven-wrapper` и скрипты запуска `nvnw` и `nvmw.cmd`, 
что позволяет собрать проект следующим вызовом:


`./nvmw package` или `mvnw.cmd package` для Windows


#### Запуск
`demo.properties` по умолчанию является валидной конфигурацией, так как использует 
файл-хранилище которое будет создано в локальном каталоге.
Поэтому можно запустить приложение сразу с использованием этого файла:


`java -jar -Dlardi.conf=./demo.properties ./target/phone-book-0.1.0.jar`


`java -jar -Dlardi.conf=./demo.properties .\target\phone-book-0.1.0.jar` (для Windows)


#### Demo
При первом запуске создается demo-пользователь с _логином_ "**demo**" и _паролем_ "**demo**".
Для данного пользователя генерируются demo-контакты.


![screenshot](https://goo.gl/vArVrG)
