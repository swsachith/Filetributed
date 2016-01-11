
# Filetributed
 Step 1 <br />
How to Build:<br />
1. go to Filetributed folder<br />
2. run "mvn clean install -DskipTests" ( if you want to run the tests remove the -DskipTests part)<br />
3. run command "$ sudo chmod +x run.sh"<br />
4. run command "$ sudo chmod +x runDebugserver.sh"<br /><br />

Step 2<br />
1. go to bootstrap_server/ using terminal<br />
2. compile P2PRegistry.c using the command $ gcc P2PRegistry.c<br />
3. run the bootstrap server using the command $ ./a.out 9889<br /><br />

Step 3<br />
1. on a different terminal, go to project root<br />
2. execute shell script using the command "$ sh runDebugserver.sh 9000" (use the  port number you want to up the debug server)<br /><br />

Step 4<br />
 1. on a different terminal, go to project root.<br />
 2. execute shell script using the command "$ sh run.sh 3" (use the number of nodes you want to run, upto 10)<br />


