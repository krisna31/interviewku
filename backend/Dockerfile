# Use an official Node.js image as a parent image
FROM node:18.16.1

# Set the working directory in the container
WORKDIR /app

# Copy package.json and package-lock.json to the working directory
COPY package*.json ./

# Install app dependencies
RUN npm install --production

# Copy the application code to the container
COPY . .

# Expose the port on which your app will run
EXPOSE 5000

# Define the command to run your application
CMD ["npm", "start"]
