FROM node:12-alpine
EXPOSE 7878
COPY /PushServer /PushServer
WORKDIR /PushServer
RUN npm install
#RUN npm install -g forever
ENTRYPOINT ["node" ,"app.js"]
