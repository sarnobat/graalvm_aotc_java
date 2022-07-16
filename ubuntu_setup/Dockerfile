#FROM alpine
FROM ubuntu
CMD ["echo", "Hello StackOverflow!"]


# COPY helloworld.go /
#COPY helloworld.py /
ENV DEBIAN_FRONTEND=noninteractive
ADD .zshrc .zshrc
RUN apt update
# RUN apt install -y golang
RUN apt install -y zsh
CMD [ "source /root/.zshrc", "zsh"]
# CMD ["/bin/zsh"]
# CMD ["-f","/dev/null"]
# CMD ["go", "run","helloworld.go"]
# ENTRYPOINT ["source", ".zshrc"]
