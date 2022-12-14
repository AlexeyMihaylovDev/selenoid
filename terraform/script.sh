#!/bin/bash
#upgrade yum
sudo yum update -y


#install docker
sudo amazon-linux-extras install docker -y
sudo yum install docker
sudo service docker start
sudo usermod -a -G docker ec2-user


#install git
sudo yum install git -y

#install docker-compose
sudo curl -L https://github.com/docker/compose/releases/download/1.22.0/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose
sudo curl -L https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
docker-compose version


wget https://github.com/aerokube/cm/releases/download/1.8.1/cm_linux_amd64

chmod +x cm_linux_amd64
./cm_linux_amd64 selenoid start --vnc
./cm_linux_amd64 selenoid-ui start