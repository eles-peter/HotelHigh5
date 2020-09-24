#!/bin/sh
set -x
set -e
set -v
#INIT PROJECTS RELATED INFO
#------------- UPDATE ALWAYS CORRESPONDINGLY!
# pom.xml Artifact id must be 'project'
#remote_address=hotels-demo.progmasters.hu
remote_address=18.216.85.81
frontend_source_location=./hotel/dist/hotel/*
backend_source_location=./target/project-1.0-SNAPSHOT.jar
frontend_remote_location=/home/ubuntu/hotel/frontend
backend_remote_location=/home/ubuntu/hotel
pem_file_full_path=elespeter.pem
ssh_options='StrictHostKeyChecking=accept-new'

chmod 400 $pem_file_full_path

#COPY LOCAL FILES TO SERVER
scp  -o $ssh_options -i $pem_file_full_path -r $frontend_source_location ubuntu@$remote_address:$frontend_remote_location
scp  -o $ssh_options -i $pem_file_full_path $backend_source_location ubuntu@$remote_address:$backend_remote_location/project-1.0-SNAPSHOT.jar.new

#UPDATE .JAR WITH NEW, AND RESTART
ssh  -o $ssh_options -i $pem_file_full_path ubuntu@$remote_address './shutdown.sh; mv project-1.0-SNAPSHOT.jar.new project.jar; ./start.sh'
