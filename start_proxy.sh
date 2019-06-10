#!/bin/bash
cloud_proxy_exe=/c/progs/cloud_sql_proxy_x64.exe
new_db_name=mapdecorator:europe-west3:map-decorator-db
old_db_name=mapdecorator:europe-west4:mapdecorator-db
port_number=3306


# cloud_proxy needs a service account key to connect to database.
# Most recently it's found at C:\Users\letownia\mapDecorator-dfe83fa7f774.json
# and set with environment variable
$cloud_proxy_exe -instances=$new_db_name=tcp:$port_number