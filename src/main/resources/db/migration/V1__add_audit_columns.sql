--Code Reference: Interview:
--With Flyway, Spring automatically tracks which scripts have already run and won't execute them again. 
--This is the approach most enterprise teams use for WebFlux/R2DBC projects.
ALTER TABLE NOTIFICATION
ADD (
    CREATED_BY VARCHAR2(100),
    CREATED_ON TIMESTAMP,
    UPDATED_BY VARCHAR2(100),
    UPDATED_ON TIMESTAMP
);

SELECT COLUMN_NAME
FROM USER_TAB_COLUMNS
WHERE TABLE_NAME = 'NOTIFICATION';