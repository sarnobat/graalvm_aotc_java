#FROM alpine
FROM ubuntu
CMD ["echo", "Hello StackOverflow!"]


# COPY helloworld.go /
#COPY helloworld.py /
ENV DEBIAN_FRONTEND=noninteractive
ENV SHELL=/bin/zsh
ADD .zshrc .zshrc
RUN apt update
# RUN apt install -y golang
RUN apt install -y zsh
RUN apt install -y git
RUN apt install -y bat
RUN apt install -y grc
RUN apt install -y vim
RUN apt install -y inetutils-ping
RUN ln -sr /media/sarnobat/git/computers.git ~/
RUN ln -sr /media/sarnobat/git/computers.git/docker/.zshrc ~/


CMD [ "source /root/.zshrc", "zsh"]
# CMD ["/bin/zsh"]
# CMD ["-f","/dev/null"]
# CMD ["go", "run","helloworld.go"]
# ENTRYPOINT ["source", ".zshrc"]

# Mounting via Dockerfile
# This simply cannot be done. Docker images are designed to be portable. Host mounts are host specific. Thus if you are able to specify a host mount at build time, it will make the image non-portable across machine that don't have this mount folder. Thus this is why this option is not available.
# You can use docker compose to help the user not choose the mount folder. 
