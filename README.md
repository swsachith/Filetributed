
# Filetributed
step 1 <br />
How to Build:<br />
1. go to Filetributed folder<br />
2. run "mvn clean install -DskipTests" ( if you want to run the tests remove the -DskipTests part)<br /><br />

step 2<br />
 go to bootstrap_server/ using terminal<br />
 compile P2PRegistry.c using the command $ gcc P2PRegistry.c<br />
 run the bootstrap server using the command $ ./a.out 9889<br /><br />

step 3<br />
  on a different terminal, go to project root.<br />
  execute shell script using the command $ sh run.sh<br />


