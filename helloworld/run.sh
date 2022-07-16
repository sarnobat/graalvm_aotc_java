sudo docker rm --force $(sudo docker ps --all --quiet)
sudo docker image rm --force $(sudo docker images --quiet)
sudo docker images
sudo docker build --label mylabel --tag intro/v1 .
sudo docker images
sudo docker run --name mycontainer intro/v1
sudo docker run --name mycontainer --detach --tty --interactive intro/v1
sudo docker exec -it mycontainer bash
sudo docker rm mycontainer
#sudo docker image rm myimage
sudo docker ps --all
