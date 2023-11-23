# Use an official lightweight Node.js image as a parent image
FROM node:14-alpine

# Set the working directory in the container
WORKDIR /usr/src/app

# Install Windows build tools (needed for some npm packages on Windows)
RUN apk add --no-cache --virtual .gyp \
        python \
        make \
        g++ \
    && npm install -g node-gyp \
    && apk del .gyp

# Copy package.json and package-lock.json to the working directory
COPY package*.json ./

# Install app dependencies
RUN npm install --omit=dev

# Copy the application code to the container
COPY . .

# Copy tensorflow.dll to the required location
COPY node_modules/@tensorflow/tfjs-node/deps/lib/tensorflow.dll node_modules/@tensorflow/tfjs-node/lib/napi-v7/

# Expose the port on which your app will run
EXPOSE 5000

# Define the command to run your application
CMD ["node", "src/server.js"]
