--liquibase formatted sql
--changeset sum:product-create-table

DROP TABLE IF EXISTS [main-report].dbo.[stg_transaction];

CREATE TABLE [main-report].dbo.[stg_transaction] (
    [id] [bigint] IDENTITY(1,1) NOT NULL,
    [identifier] [varchar](30) NULL,
    [status] [varchar](30) NULL,
    [created_at] [datetime2](7) NULL,
    [updated_at] [datetime2](7) NULL,
    CONSTRAINT [PK_STG_TRANSACTION] PRIMARY KEY CLUSTERED([id] ASC) ON [PRIMARY]
) ON [PRIMARY];