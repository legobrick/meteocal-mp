CREATE SCHEMA weathercal;

CREATE TABLE calendar ( 
	id                   bigint UNSIGNED NOT NULL  AUTO_INCREMENT,
	is_public            bool  NOT NULL  ,
	CONSTRAINT pk_calendar PRIMARY KEY ( id )
 ) engine=InnoDB;

ALTER TABLE calendar MODIFY is_public bool  NOT NULL   COMMENT 'visibility of the calendar';

CREATE TABLE event ( 
	id                   bigint UNSIGNED NOT NULL  AUTO_INCREMENT,
	place_latitude       decimal(9,6)    ,
	place_longitude      decimal(9,6)    ,
	is_public            bool  NOT NULL  ,
	when                 datetime  NOT NULL  ,
	timezone             varchar(100)  NOT NULL  ,
	place_description    varchar(500)  NOT NULL  ,
	CONSTRAINT pk_event PRIMARY KEY ( id )
 ) engine=InnoDB;

CREATE TABLE user ( 
	email                varchar(320)  NOT NULL  ,
	first_name           varchar(100)  NOT NULL  ,
	last_name            varchar(100)  NOT NULL  ,
	password             varchar(128)  NOT NULL  ,
	CONSTRAINT pk_user PRIMARY KEY ( email )
 ) engine=InnoDB;

ALTER TABLE user MODIFY email varchar(320)  NOT NULL   COMMENT 'RFC 3696 end of page 6';

ALTER TABLE user MODIFY password varchar(128)  NOT NULL   COMMENT 'SHA-512';

CREATE TABLE user_has_calendar ( 
	id_user              varchar(320)  NOT NULL  ,
	id_calendar          bigint UNSIGNED NOT NULL  ,
	CONSTRAINT pk_user_has_calendar PRIMARY KEY ( id_user, id_calendar )
 ) engine=InnoDB;

CREATE INDEX idx_user_has_calendar ON user_has_calendar ( id_calendar );

CREATE INDEX idx_user_has_calendar_0 ON user_has_calendar ( id_user );

CREATE TABLE weather_constraint ( 
	id                   bigint UNSIGNED NOT NULL  AUTO_INCREMENT,
	temperature          decimal(5,2)  NOT NULL  ,
	is_temperature_lower_than bool  NOT NULL  ,
	id_event             bigint UNSIGNED NOT NULL  ,
	CONSTRAINT pk_weather_constraint PRIMARY KEY ( id )
 ) engine=InnoDB;

CREATE INDEX idx_weather_constraint ON weather_constraint ( id_event );

CREATE TABLE weather_state_constraint ( 
	id                   bigint UNSIGNED NOT NULL  AUTO_INCREMENT,
	weather_state        varchar(50)  NOT NULL  ,
	id_weather_constraint bigint UNSIGNED NOT NULL  ,
	CONSTRAINT pk_weather_state_constraint PRIMARY KEY ( id )
 ) engine=InnoDB;

CREATE INDEX idx_weather_state_constraint ON weather_state_constraint ( id_weather_constraint );

CREATE TABLE owner ( 
	id_user              bigint UNSIGNED NOT NULL  ,
	id_event             bigint UNSIGNED NOT NULL  ,
	CONSTRAINT pk_owner PRIMARY KEY ( id_user, id_event )
 ) engine=InnoDB;

CREATE INDEX idx_owner ON owner ( id_user );

CREATE INDEX idx_owner ON owner ( id_event );

ALTER TABLE owner COMMENT 'ownage of event implies participation to it';

CREATE TABLE participation ( 
	id_user              int UNSIGNED NOT NULL  ,
	id_event             bigint UNSIGNED NOT NULL  ,
	availability         varchar(50)  NOT NULL  ,
	CONSTRAINT pk_participation PRIMARY KEY ( id_user, id_event )
 ) engine=InnoDB;

CREATE INDEX idx_participation ON participation ( id_user );

CREATE INDEX idx_participation ON participation ( id_event );

ALTER TABLE owner ADD CONSTRAINT fk_owner_user FOREIGN KEY ( id_user ) REFERENCES user( email ) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE owner ADD CONSTRAINT fk_owner_event FOREIGN KEY ( id_event ) REFERENCES event( id ) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE participation ADD CONSTRAINT fk_participation_user FOREIGN KEY ( id_user ) REFERENCES user( email ) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE participation ADD CONSTRAINT fk_participation_event FOREIGN KEY ( id_event ) REFERENCES event( id ) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE user_has_calendar ADD CONSTRAINT fk_user_has_calendar_calendar FOREIGN KEY ( id_calendar ) REFERENCES calendar( id ) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE user_has_calendar ADD CONSTRAINT fk_user_has_calendar_user FOREIGN KEY ( id_user ) REFERENCES user( email ) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE weather_constraint ADD CONSTRAINT fk_weather_constraint_event FOREIGN KEY ( id_event ) REFERENCES event( id ) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE weather_state_constraint ADD CONSTRAINT fk_weather_state_constraint FOREIGN KEY ( id_weather_constraint ) REFERENCES weather_constraint( id ) ON DELETE CASCADE ON UPDATE CASCADE;

