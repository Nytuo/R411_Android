FROM --platform=linux/amd64 node:18-alpine
RUN mkdir -p /home/node/app/node_modules && chown -R node:node /home/node/app
WORKDIR /home/node/app
COPY package*.json ./
USER node
RUN npm install
COPY --chown=node:node . .
EXPOSE 6906
CMD ["node","swagger.js"]
CMD ["node","server.js","--port","6906"]
