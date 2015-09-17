#!/bin/bash
numclicks=1
numtrackbacks=1
#IPs: Uppsala, Beijing, Oslo, Paris, Cairo, New deli, Bankok, Buenos Aires, New York, San Francisco
declare -a ips=("82.196.191.164" "58.133.61.3" "95.34.207.113" "78.237.248.114" "41.130.106.246" "203.176.113.112" "58.9.51.161" "181.30.241.246" "150.210.231.30" "208.113.83.165")

for i in `seq 1 1000`
do
	r=$(jot -r 1 0 3)
	ipNr=$(jot -r 1 0 9)
	ip=${ips[$ipNr]}
	#echo 'r='$r
	if [ "$r" -lt 3 ]
	then
		echo 'Send click' $numclicks 'with ip' $ip
		curl "http://localhost:9170/rgtv/hercules/click?uuid=123456788&ip=$ip&adgroupid=123&valid=true"
		numclicks=$(($numclicks + 1))
	else
		echo 'Send Trackback' $numtrackbacks 'with ip' $ip
		curl "http://localhost:9170/rgtv/hercules/trackback?uuid=123456788&ip=$ip&adgroupid=123&valid=true"
		numtrackbacks=$(($numtrackbacks + 1))
	fi

	#Randomly sleep 0-3 seconds.
	echo 'Sleep' $r 's'
	sleep $r
done