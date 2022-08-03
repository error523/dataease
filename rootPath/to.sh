#!/bin/sh
result=`curl --location-trusted -u root: -H "label:1659443537557" -H "column_separator:|DE|" -H "columns:C_4e832b50f61c5e87ec9e7264e6466e1f,C_ad32e604e17467fc435538334fbddf3e,C_7b98c4625e21be7486555946c603f621,dataease_uuid" -H "merge_type: APPEND" -T /home/dever/workspace/dataease/rootPath/tmp_ds_531350fa_f749_4b2c_b1eb_3b05c750ed89.txt -XPUT http://10.1.2.90:8030/api/dataease/tmp_ds_531350fa_f749_4b2c_b1eb_3b05c750ed89/_stream_load`
#rm -rf  /home/dever/workspace/dataease/rootPath/kettle/tmp_ds_531350fa_f749_4b2c_b1eb_3b05c750ed89.txt 
if [ $? -eq 0 ] ; then
  failstatus=$(echo $result | grep '"Status": "Fail"')
  if [ "x${failstatus}" != "x" ];then     echo $result
     exit 1
  fi
else
  echo $result
  exit 1
fi

