DROP TABLE IF EXISTS contacts;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id        BIGINT      NOT NULL AUTO_INCREMENT,
  username  VARCHAR(50) NOT NULL UNIQUE,
  password  VARCHAR(60) NOT NULL,
  full_name VARCHAR(50) NOT NULL,
  PRIMARY KEY (id)
)
  DEFAULT CHARACTER SET utf8;


CREATE TABLE contacts (
  id                  BIGINT      NOT NULL AUTO_INCREMENT,
  first_name          VARCHAR(50) NOT NULL,
  middle_name         VARCHAR(50) NOT NULL,
  last_name           VARCHAR(50) NOT NULL,
  mobile_phone_number VARCHAR(13) NOT NULL,
  home_phone_number   VARCHAR(13),
  email               VARCHAR(50),
  address             VARCHAR(50),
  user_id             BIGINT      NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users (id)
)
  DEFAULT CHARACTER SET utf8;