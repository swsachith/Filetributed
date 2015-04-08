#!/bin/sh
max=$1
for i in `seq 1 $max`
do
    gnome-terminal --tab -e "tail -f s" --tab -e "java -jar client/target/Client-1.0-SNAPSHOT-jar-with-dependencies.jar client"$i".xml"
    sleep 3
    echo "Node" $i "started with configuration client"$i".xml"
done