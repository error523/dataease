#!/bin/sh
chmod 777 D:\workspace\open-fork\dataease\rootPath\kettle\tmp_ds_90e00f7d_5300_49d0_b223_b8a1b3eed054.txt 
result=`curl --location-trusted -u root:1234 -H "label:1659493861940" -H "column_separator:|DE|" -H "columns:C_4e832b50f61c5e87ec9e7264e6466e1f,C_ad32e604e17467fc435538334fbddf3e,C_7b98c4625e21be7486555946c603f621,dataease_uuid" -H "merge_type: APPEND" -T D:\workspace\open-fork\dataease\rootPath\kettle\tmp_ds_90e00f7d_5300_49d0_b223_b8a1b3eed054.txt -XPUT http://10.1.2.90:8030/api/dataease/tmp_ds_90e00f7d_5300_49d0_b223_b8a1b3eed054/_stream_load`
rm -rf  D:\workspace\open-fork\dataease\rootPath\kettle\tmp_ds_90e00f7d_5300_49d0_b223_b8a1b3eed054.txt 
if [ $? -eq 0 ] ; then
  failstatus=$(echo $result | grep '"Status": "Fail"')
  if [ "x${failstatus}" != "x" ];then     echo $result
     exit 1
  fi
else
  echo $result
  exit 1
fi

