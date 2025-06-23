For run the project in your system for the fist time follow those steps-
install xammp
Add jar file in project liraries
start mysql
create database "walletwatch"
create table :
CREATE TABLE user (
    id INT(11) NOT NULL AUTO_INCREMENT,
    username VARCHAR(50),
    password VARCHAR(50),
    PRIMARY KEY (id)
);
