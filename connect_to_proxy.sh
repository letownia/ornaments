#!/bin/bash
mysqlExe=/c/progs/mysql-8.0.11-winx64/bin/mysql.exe

# Password is the same as in application.properties :
# i.e. spring.datasource.password=epodroznik
password=epodroznik

$mysqlExe -u root -p$password --host 127.0.0.1