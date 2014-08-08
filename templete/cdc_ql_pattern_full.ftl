add jar hdfs://10.10.10.160:9000/Library/hiveudf-1.0.jar;
#<AddEncryptJar>

CREATE EXTERNAL TABLE IF NOT EXISTS CDC_#<TableNameHead>_#<TableName> (
    #<InterfaceDef>
)
PARTITIONED BY(dt STRING)
ROW FORMAT DELIMITED FIELDS TERMINATED BY "#<Delimeter>"
LOCATION '#<URL>/SERVICES_IAS/#<SiteName>/CDC/CDC_#<TableNameHead>_#<TableName>';

LOAD DATA INPATH '#<URL>/#inputPath#' OVERWRITE INTO TABLE CDC_#<TableNameHead>_#<TableName> PARTITION (dt='#{today}');

CREATE EXTERNAL TABLE IF NOT EXISTS #<TableNameHead>_#<TableName> (
    #<InterfaceDef>,
    OPER_DTM string,
    EIUD_TYPE string
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY "#<Delimeter>"
LOCATION '#<URL>/SERVICES_IAS/#<SiteName>/RAW/#<TableNameHead>_#<TableName>';

FROM (
SELECT #<ColumnRefineList>,
       from_unixtime(unix_timestamp(), 'yyyy-MM-dd HH:mm:ss') OPER_DTM,
       'I' EIUD_TYPE
  FROM CDC_#<TableNameHead>_#<TableName> 
 WHERE dt='#{today}'
) TOT
INSERT OVERWRITE TABLE #<TableNameHead>_#<TableName>
SELECT #<ColumnList>,
       OPER_DTM,
       EIUD_TYPE
INSERT OVERWRITE TABLE IAS_PROCESS_LOG PARTITION (dt='#{today}', jobId='#<TableNameHead>_#<TableName>')
SELECT 'HDFS' JOB_TYPE,
       from_unixtime(unix_timestamp(), 'yyyy-MM-dd HH:mm:ss') START_DTTM,
       from_unixtime(unix_timestamp(), 'yyyy-MM-dd HH:mm:ss') END_DTTM,
       '{yesterday}' BASE_DT,
       COUNT(EIUD_TYPE) TOTAL_CNT,
       COUNT(EIUD_TYPE) PROCESS_CNT,
       0 UPDATE_CNT,
       COUNT(EIUD_TYPE) INSERT_CNT
;

CREATE EXTERNAL TABLE IF NOT EXISTS STG_#<TableNameHead>_#<TableName> (
    #<InterfaceDef>,
    OPER_DTM string
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY "#<Delimeter>"
LOCATION '#<URL>/SERVICES/#<SiteName>/STG/STG_#<TableNameHead>_#<TableName>';

INSERT OVERWRITE TABLE STG_#<TableNameHead>_#<TableName>
SELECT #<ColumnRefineList>,
       from_unixtime(unix_timestamp(), 'yyyy-MM-dd HH:mm:ss') OPER_DTM
  FROM CDC_#<TableName> 
 WHERE dt='#{today}'
;

dfs -mkdir #<URL>/#outputPath#;

dfs -cp #<URL>/SERVICES_IAS/#<SiteName>/STG/STG_#<TableNameHead>_#<TableName>/* #outputPath#;

