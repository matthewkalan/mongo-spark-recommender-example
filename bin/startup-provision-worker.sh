#!/bin/bash

# Assuming root user is installing when run by Databricks 

# download Mongodb Cloud Manager Automation agent
curl -OL https://cloud.mongodb.com/download/agent/automation/mongodb-mms-automation-agent-2.7.3.1679-1.linux_x86_64.tar.gz

tar -xvf mongodb-mms-automation-agent-2.7.3.1679-1.linux_x86_64.tar.gz

cd mongodb-mms-automation-agent-2.7.3.1679-1.linux_x86_64

# Configure the agent to a specific Cloud Manager group
sed -i -- 's/mmsGroupId=/mmsGroupId=57439a27e4b058d8b2bdde3f/' local.config
sed -i -- 's/mmsApiKey=/mmsApiKey=607f268c5e4c4567bec8971bc59b9044/' local.config

mkdir /var/lib/mongodb-mms-automation
mkdir /var/log/mongodb-mms-automation
mkdir -p /data

chown `whoami` /var/lib/mongodb-mms-automation
chown `whoami` /var/log/mongodb-mms-automation
chown `whoami` /data

nohup ./mongodb-mms-automation-agent --config=local.config >> /var/log/mongodb-mms-automation/automation-agent.log 2>&1 &
