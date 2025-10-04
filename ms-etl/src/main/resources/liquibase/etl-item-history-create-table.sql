--liquibase formatted sql
--changeset sum:etl-item-create-table

DROP TABLE IF EXISTS [main-report].dbo.[etl_item_history];

CREATE TABLE [main-report].dbo.[etl_item_history] (
    [id] [bigint] IDENTITY(1,1) NOT NULL,
    [business_date] [date] NULL,
    [job_name] [varchar](255) NOT NULL UNIQUE,
    [job_type] [varchar](20) NULL,
    [status] [varchar](30) NULL,
    [start_time] [datetime2](7) NULL,
    [end_time] [datetime2](7) NULL,
    [duration] [bigint] NULL,
    [message] [varchar](MAX) NULL,
    [created_at] [datetime2](7) NULL,
    [updated_at] [datetime2](7) NULL,
    CONSTRAINT [PK_ETL_ITEM_HISTORY] PRIMARY KEY CLUSTERED([id] ASC) ON [PRIMARY]
) ON [PRIMARY];