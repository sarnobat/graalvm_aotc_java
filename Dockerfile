#FROM alpine
FROM ubuntu
CMD ["echo", "Hello StackOverflow!"]


COPY gmail_list_messages.go /
ENV DEBIAN_FRONTEND=noninteractive
RUN apt update
RUN apt install -y golang
CMD ["-f","/dev/null"]
ENTRYPOINT ["tail"]
