#!/bin/sh
gnome-terminal --tab -e "tail -f s" --tab -e "java -jar client/target/Client-1.0-SNAPSHOT-jar-with-dependencies.jar client1.xml"
sleep 2
gnome-terminal --tab -e "java -jar client/target/Client-1.0-SNAPSHOT-jar-with-dependencies.jar client2.xml"
sleep 2
gnome-terminal --tab -e "java -jar client/target/Client-1.0-SNAPSHOT-jar-with-dependencies.jar client3.xml"
sleep 2