-- How to rename database without an error

USE master;
GO

ALTER DATABASE ejemplosjdbc SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
GO

ALTER DATABASE ejemplosjdbc MODIFY NAME = ejemplosjdbc_bcup;
GO

ALTER DATABASE ejemplosjdbc_bcup SET MULTI_USER;
GO
