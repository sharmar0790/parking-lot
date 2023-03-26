DROP TABLE IF EXISTS parking_spots;

CREATE TABLE parking_spots (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  vehicle_type VARCHAR(20) NOT NULL,
  spot_number INT DEFAULT 0 NOT NULL
);

DROP TABLE IF EXISTS parking_ticket;
CREATE SEQUENCE parking_ticket_sequence START with 1 INCREMENT by 1;

CREATE TABLE parking_ticket (
  id INT8 PRIMARY KEY NOT NULL,
  entry_date_time DATETIME NOT NULL,
  exit_date_time DATETIME NULL,
  vehicle_type VARCHAR(10) NOT NULL,
  spot_number VARCHAR(16) NOT NULL,
  location VARCHAR(16) NOT NULL,
  ticket_number VARCHAR(16) NOT NULL
);