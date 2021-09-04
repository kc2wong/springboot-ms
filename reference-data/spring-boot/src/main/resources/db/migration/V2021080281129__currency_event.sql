CREATE TABLE `evt_currency` (
  `record_id` char(36) NOT NULL,
  `domain_model_id` varchar(100) NOT NULL,
  `domain_model_version` bigint NOT NULL,
  `event_type` varchar(20) NOT NULL,
  `additional_info` varchar(1024) NULL,
  `created_by` varchar(36) NOT NULL,
  `created_datetime` timestamp(6) NOT NULL,
  `updated_by` varchar(36) NOT NULL,
  `updated_datetime` timestamp(6) NOT NULL,
  PRIMARY KEY (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
