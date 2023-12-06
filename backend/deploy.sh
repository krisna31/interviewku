# auth artifact registry https://cloud.google.com/artifact-registry/docs/docker/pushing-and-pulling
gcloud auth configure-docker asia-southeast1-docker.pkg.dev

# build docker image no cache
docker build --no-cache -t interviewku:0.0.1 .

# build docker image
# docker build -t interviewku:0.0.1 .

# list docker image
docker images

# run docker image
docker run -d -p 5000:5000 interviewku:0.0.1

# push docker image
docker tag interviewku:0.0.1 asia-southeast1-docker.pkg.dev/$PROJECT/$FOLDER/interviewku:0.0.1

docker push asia-southeast1-docker.pkg.dev/$PROJECT/$FOLDER/interviewku:0.0.1

gcloud run deploy interviewku-api \
  --image=$IMAGE_URL \
  --allow-unauthenticated \
  --port=5000 \
  --service-account=$SERVICE_ACCOUNT \
  --cpu=1 \
  --memory=1Gi \
  --max-instances=3 \
  --set-env-vars=APP_ENV=$APP_ENV--set-env-vars='BASE_URL=$URL_APPLICATION_CHANGE_ME'--set-env-vars=AUDIO_SERVICE=false--set-env-vars=AUDIO_BUCKET_NAME=$BUCKET_NAME--set-env-vars=GOOGLE_SERVICE_ACCOUNT_KEY=false--set-env-vars=SMTP_HOST=$SMTP_HOST--set-env-vars=SMTP_PORT=$SMTP_PORT--set-env-vars=SMTP_USER=$SMTP_USER--set-env-vars=SMTP_PASSWORD=$SMTP_PASSWORD--set-env-vars=HOST=0.0.0.0--set-env-vars=PGUSER=$PGUSER--set-env-vars=PGPASSWORD=$PGPASSWORD--set-env-vars=PGDATABASE=$PGDATABASE--set-env-vars=PGHOST=$PGHOST--set-env-vars=PGPORT=$PGPORT--set-env-vars=ACCESS_TOKEN_KEY=$ACCESS_TOKEN_KEY--set-env-vars=REFRESH_TOKEN_KEY=$REFRESH_TOKEN_KEY--set-env-vars=ACCESS_TOKEN_AGE=$ACCESS_TOKEN_AGE \
  --cpu-boost \
  --region=$REGION \
  --project=$PROJECT