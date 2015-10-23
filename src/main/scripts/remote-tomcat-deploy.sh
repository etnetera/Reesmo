#!/bin/bash
# Script will copy Tremapp WAR and deploy it to tomcat

function usage() {
    echo "Deploys Tremapp WAR to given server"
    echo ""
    echo "Options:"
    echo "  -s  server where WAR will be deployed (i.e. myserver.mydomain.com)"
    echo "  -w  path to WAR to be deployed (i.e. target/tremapp-0.0.1-SNAPSHOT.war)"
    echo "  -u  (optional) user which will connect to server (defaults to actual user)"
    echo "  -v  (optional) verbose mode"
    echo "  -h  (optional) prints help (this screen)"
}

# default arguments
WAR_LOCATION=""
VERBOSITY=0
TARGET_SERVER=""
TARGET_USER=$USER

# parse arguments
while getopts ":hvw:s:u:" opt; do
    case $opt in
        h)
            usage
            exit 0;
            ;;
        v)
            VERBOSITY=$((VERBOSITY+1))
            ;;
        w)
            WAR_LOCATION="$OPTARG"
            ;;
        s)
            TARGET_SERVER="$OPTARG"
            ;;
        u)
            TARGET_USER="$OPTARG"
            ;;
        \?)
            echo "Invalid option: -$OPTARG" >&2
            ;;
    esac
done

if [ "$VERBOSITY" -gt 0 ]; then
	echo "TARGET_SERVER = $TARGET_SERVER"
    echo "TARGET_USER = $TARGET_USER"
    echo "WAR_LOCATION = $WAR_LOCATION"
fi

if [ ! -f "$WAR_LOCATION" ]; then
    echo "ERROR: WAR file '$WAR_LOCATION' not found!" >&2
    exit 1
fi



rsync -e ssh ${WAR_LOCATION} ${TARGET_USER}@${TARGET_SERVER}:/home/${TARGET_USER}/tremapp.war
if [ $? -ne 0 ]; then
	echo "ERROR Unable to copy WAR ${WAR_LOCATION} to server ${TARGET_SERVER}.";
	exit 1;
else
	echo "================================================================================"
	echo "                         DEPLOYING TREMAPP                                      "
	echo "================================================================================"
ssh ${TARGET_USER}@${TARGET_SERVER} << "ENDSSH"
#!/bin/bash
NEW_BUILD="/home/$TARGET_USER/tremapp.war"
TOMCAT_WEBAPPS_DIR="/opt/tomcat/webapps"
# must be writable
TOMCAT_APP_DIR="$TOMCAT_WEBAPPS_DIR/ROOT"
# must be writable
BACKUP_DIR="/opt/tomcat/backup"
# must be writable
TOMCAT_APP_WAR="$TOMCAT_WEBAPPS_DIR/tremapp.war"
TOMCAT_SERVICE_NAME="tremapp"
DATE=$(date +%Y-%m-%d_%H-%M-%S)

# make sure backup dir exists
if [[ ! -d "$BACKUP_DIR" ]]
then
  echo "Backup directory $BACKUP_DIR does not exist, I am creating one"
  mkdir -p "$BACKUP_DIR"
fi

echo "=> Stopping Tomcat"
sudo /sbin/service ${TOMCAT_SERVICE_NAME} stop
sleep 10

# move old war file, append date and copy new war file
if [[ -e ${TOMCAT_APP_WAR} ]]
then
  echo "=> Backing up old WAR"
  mv -b ${TOMCAT_APP_WAR} "$BACKUP_DIR/tremapp"_$DATE.war
fi

echo "=> Copying new WAR file"
cp ${NEW_BUILD} ${TOMCAT_APP_WAR} || exit 1
rm ${NEW_BUILD} || exit 1
chmod u+rwx ${TOMCAT_APP_WAR} || exit 1
chmod g+rwx ${TOMCAT_APP_WAR} || exit 1
chgrp optwrite ${TOMCAT_APP_WAR} || exit 1

# delete work dir
echo "=> Removing old deployed application files"
sudo rm -rf "$TOMCAT_APP_DIR" || exit 1

echo "=> Writing deploy datetime"
echo `date +"%Y-%m-%d %T %z %A"` > "$TOMCAT_WEBAPPS_DIR/DEPLOYED_AT"

echo "=> Starting Tomcat"
sudo /sbin/service ${TOMCAT_SERVICE_NAME} start || exit 1

# delete backups older than 7 days
echo "=> Cleaning up backups older than 7 days"
find "$BACKUP_DIR" -name "*.war" -type f -mtime +7 -delete || exit 1
ENDSSH

if [ $? -ne 0 ]; then
	echo "ERROR Deploy of new version to server ${TARGET_SERVER} failed.";
	exit 1;
else
	echo "================================================================================"
	echo "                            TREMAPP DEPLOYED                                    "
	echo "================================================================================"
	exit 0;
fi

fi
