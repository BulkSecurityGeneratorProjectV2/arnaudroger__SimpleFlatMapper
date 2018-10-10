#!/bin/bash
set -e
sudo apt-get -y install oracle-java9-installer
wget https://www-eu.apache.org/dist/maven/maven-3/3.2.5/binaries/apache-maven-3.2.5-bin.zip
unzip -qq apache-maven-3.5.2-bin.zip
export M2_HOME=$PWD/apache-maven-3.5.2
export PATH=$M2_HOME/bin:$PATH

