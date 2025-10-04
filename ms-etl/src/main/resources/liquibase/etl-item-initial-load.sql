--liquibase formatted sql
--changeset sum:etl-item-initial-load

INSERT INTO etl_item (job_name, job_type, status, created_at)
VALUES('JOB_PRODUCT_MIGRATION', 'STAGING_DATA', 'NOT_STARTED', SYSDATETIMEOFFSET());
INSERT INTO etl_item (job_name, job_type, status, created_at)
VALUES('JOB_TRANSACTION_MIGRATION', 'STAGING_DATA', 'NOT_STARTED', SYSDATETIMEOFFSET());
INSERT INTO etl_item (job_name, job_type, status, created_at)
VALUES('JOB_ORDER_BACKUP', 'HISTORICAL_BACKUP', 'NOT_STARTED', SYSDATETIMEOFFSET());
INSERT INTO etl_item (job_name, job_type, status, created_at)
VALUES('JOB_TRANSACTION_BACKUP', 'HISTORICAL_BACKUP', 'NOT_STARTED', SYSDATETIMEOFFSET());