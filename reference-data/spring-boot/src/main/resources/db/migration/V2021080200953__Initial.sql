CREATE TABLE `currency` (
  `record_id` char(36) NOT NULL,
  `code` varchar(10) NOT NULL,
  `name` varchar(50) NOT NULL,
  `short_name` varchar(10) NOT NULL,
  `decimal_place` smallint NOT NULL,
  `created_by` varchar(36) NOT NULL,
  `created_datetime` timestamp(6) NOT NULL,
  `updated_by` varchar(36) NOT NULL,
  `updated_datetime` timestamp(6) NOT NULL,
  `version` bigint NOT NULL,
  PRIMARY KEY (`record_id`),
  UNIQUE INDEX (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `currency_locale` (
  `record_id` char(36) NOT NULL,
  `currency_id` char(36) NOT NULL,
  `locale` varchar(10) NOT NULL,
  `name` varchar(50) NOT NULL,
  `short_name` varchar(10) NOT NULL,
  `created_by` varchar(36) NOT NULL,
  `created_datetime` timestamp(6) NOT NULL,
  `updated_by` varchar(36) NOT NULL,
  `updated_datetime` timestamp(6) NOT NULL,
  `version` bigint NOT NULL,
  PRIMARY KEY (`record_id`),
  FOREIGN KEY (`currency_id`)
    REFERENCES `currency`(`record_id`)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  UNIQUE INDEX (`currency_id`, `locale`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO `currency` (`record_id`,`code`,`name`,`short_name`,`decimal_place`,`created_by`,`created_datetime`,`updated_by`,`updated_datetime`,`version`) VALUES ('402881857b5ea538017b5ea774570000','HKD','Hong Kong Dollar','HKD',2,'its-refdata','2021-08-19 21:43:57.781000','its-refdata','2021-08-19 21:43:57.815000',1);

INSERT INTO `currency_locale` (`record_id`,`currency_id`,`locale`,`name`,`short_name`,`created_by`,`created_datetime`,`updated_by`,`updated_datetime`,`version`) VALUES ('402881857b5ea538017b5ea7746a0001','402881857b5ea538017b5ea774570000','zh-Hant','港幣','港幣','its-refdata','2021-08-19 21:43:57.802000','its-refdata','2021-08-19 21:43:57.802000',0);
INSERT INTO `currency_locale` (`record_id`,`currency_id`,`locale`,`name`,`short_name`,`created_by`,`created_datetime`,`updated_by`,`updated_datetime`,`version`) VALUES ('402881857b5ea538017b5ea7746c0002','402881857b5ea538017b5ea774570000','zh-Hans','港币','港币','its-refdata','2021-08-19 21:43:57.804000','its-refdata','2021-08-19 21:43:57.804000',0);
