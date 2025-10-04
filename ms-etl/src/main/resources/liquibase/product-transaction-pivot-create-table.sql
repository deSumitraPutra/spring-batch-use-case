--liquibase formatted sql
--changeset sum:product-create-table

DROP TABLE IF EXISTS [main-report].dbo.[stg_product_transaction];

CREATE TABLE [main-report].dbo.[stg_product_transaction] (
    [id] [bigint] IDENTITY(1,1) NOT NULL,
    [transaction_id] [bigint] NOT NULL,
    [product_id] [bigint] NOT NULL,
    [product_count] [int] NOT NULL,
    CONSTRAINT [PK_STG_PRODUCT_TRANSACTION] PRIMARY KEY CLUSTERED([id] ASC) ON [PRIMARY]
) ON [PRIMARY];