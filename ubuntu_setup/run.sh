set -e
set -o pipefail

cat <<EOF
/Applications/Docker.app/Contents/MacOS/Docker
or
/Applications/Docker.app/Contents/MacOS/com.docker.backend -watchdog -native-api
EOF
sudo docker rm --force $(sudo docker ps --all --quiet) || echo "no existing container to delete"
sudo docker image rm --force $(sudo docker images --quiet) || echo "no existing image to delete"
sudo docker images
sudo docker build --label mylabel --tag mytag .
sudo docker images
# sudo docker run --name mycontainer intro/v1
sudo docker run --name mycontainer --detach --tty --interactive mytag zsh
sudo docker exec -it mycontainer zsh
sudo docker stop mycontainer
sudo docker rm mycontainer
#sudo docker image rm myimage
sudo docker ps --all
