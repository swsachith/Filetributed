
# Filetributed
step 1
How to Build:
1. go to Filetributed folder
2. run "mvn clean install -DskipTests" ( if you want to run the tests remove the -DskipTests part)

step 2
 go to bootstrap_server/ using terminal
 compile P2PRegistry.c using the command $ gcc P2PRegistry.c
 run the bootstrap server using the command $ ./a.out 9889

step 3
  on a different terminal, go to project root.
  execute shell script using the command $ sh run.sh


