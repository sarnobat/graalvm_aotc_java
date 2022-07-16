#FROM alpine
FROM ubuntu
CMD ["echo", "Hello StackOverflow!"]


# COPY helloworld.go /
COPY helloworld.py /
ENV DEBIAN_FRONTEND=noninteractive
RUN apt update
# RUN apt install -y golang
RUN apt install -y python3
CMD ["/bin/python3","helloworld.py"]
# CMD ["-f","/dev/null"]
# CMD ["go", "run","helloworld.go"]
# ENTRYPOINT ["tail"]
