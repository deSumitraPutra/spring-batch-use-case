--liquibase formatted sql
--changeset sum:product-create-table

DROP TABLE IF EXISTS [main-report].dbo.[stg_product];

CREATE TABLE [main-report].dbo.[stg_product] (
    [id] [bigint] IDENTITY(1,1) NOT NULL,
    [code] [varchar](10) NULL,
    [name] [varchar](10) NULL,
    [description] [varchar](255) NULL,
    [price] [numeric](21,0) NULL,
    [created_at] [datetime2](7) NULL,
    CONSTRAINT [PK_STG_PRODUCT] PRIMARY KEY CLUSTERED([id] ASC) ON [PRIMARY]
) ON [PRIMARY];