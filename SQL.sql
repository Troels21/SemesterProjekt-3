CREATE DATABASE listeDB;

CREATE TABLE LoginOplyninger (
username VARCHAR(255),
password VARCHAR(255),
salt VARCHAR(255),
doctor INT,
PRIMARY KEY(username)
);

CREATE TABLE patient(
patientID INT AUTO_INCREMENT,
cpr varchar(10),
PRIMARY KEY(patientID)
);

CREATE TABLE aftaler(
patientID INT,
timeStart DATETIME,
timeEnd DATETIME,
notat VARCHAR(255),
klinikID VARCHAR(5),
aftaleID INT AUTO_INCREMENT,
PRIMARY KEY(aftaleID),
FOREIGN KEY(patientID) REFERENCES patient(patientID)
);

CREATE TABLE malingstidspunkt(
timeStart DATETIME,
sessionID INT AUTO_INCREMENT,
markers VARCHAR(255),
comments VARCHAR(255),
patientID INT,
PRIMARY KEY(sessionID),
FOREIGN KEY(patientID) REFERENCES patient(patientID)
);

CREATE TABLE ekg(
maling DOUBLE,
malingID INT AUTO_INCREMENT,
sessionID INT,
patientID INT,
PRIMARY KEY(malingID),
FOREIGN KEY(sessionID) REFERENCES malingstidspunkt(sessionID),
FOREIGN KEY(patientID) REFERENCES patient(patientID)
);