#!/bin/bash
cloud_proxy_exe=/c/progs/cloud_sql_proxy_x64.exe
new_db_name=mapdecorator:europe-west3:map-decorator-db
old_db_name=mapdecorator:europe-west4:mapdecorator-db
port_number=3306

$cloud_proxy_exe -instances=$new_db_name=tcp:$port_number