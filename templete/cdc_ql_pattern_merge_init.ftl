add jar hdfs://10.10.10.160:9000/Library/hiveudf-1.0.jar;

CREATE EXTERNAL TABLE IF NOT EXISTS #<TableNameHead>_#<TableName> (
    #<InterfaceDef>,
    OPER_DTM string,
    EIUD_TYPE string
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY "#<Delimeter>"
LOCATION '#<URL>/SERVICES_IAS/#<SiteName>/RAW/#<TableNameHead>_#<TableName>';

dfs -rm #<URL>/SERVICES_IAS/#<SiteName>/RAW/#<TableNameHead>_#<TableName>/*;

dfs -mkdir #<URL>/#outputPath#;

LOAD DATA INPATH '#<URL>/#inputPath#/part*' OVERWRITE INTO TABLE #<TableNameHead>_#<TableName>;
