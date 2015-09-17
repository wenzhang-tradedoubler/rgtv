#!/bin/bash
numclicks=1
numtrackbacks=1
for i in `seq 1 1000`
do
	r=$(jot -r 1 0 3)
	#echo 'r='$r
	if [ "$r" -lt 3 ]
	then
		echo 'Send click' $numclicks
		curl "http://localhost:9170/rgtv/hercules/click?uuid=123456788&ip=82.196.191.164&adgroupid=123&valid=true"
		numclicks=$(($numclicks + 1))
	else
		echo 'Send Trackback' $numtrackbacks
		curl "http://localhost:9170/rgtv/hercules/trackback?uuid=123456788&ip=82.196.191.164&adgroupid=123&valid=true"
		numtrackbacks=$(($numtrackbacks + 1))
	fi

	#Randomly sleep 0-3 seconds.
	echo 'Sleep' $r 's'
	sleep $r
done
