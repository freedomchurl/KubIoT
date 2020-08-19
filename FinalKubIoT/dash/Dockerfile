FROM node:12-alpine
EXPOSE 80
RUN npm install nginx
COPY /root/dist /var/www/vuejs/
COPY /etc/nginx/conf.d/vue-server.conf /etc/nginx/conf.d/
WORKDIR /
