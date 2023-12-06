# build docker image
docker build -t interviewku:0.0.1 .

# run docker image
docker run -d -p 8080:8080 interviewku:0.0.1