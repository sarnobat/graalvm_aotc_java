sudo docker rm --force $(sudo docker ps --all --quiet)
sudo docker build --tag intro/v1 .
sudo docker images
sudo docker run --name myimage --detach --tty --interactive intro/v1
docker exec -it myimage bash
sudo docker ps --all
