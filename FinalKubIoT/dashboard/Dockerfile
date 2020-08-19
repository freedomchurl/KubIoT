FROM nginx
EXPOSE 8080
COPY ./vue-server.conf /etc/nginx/conf.d/
COPY ./dist /var/www/vuejs/dist
WORKDIR /
RUN service nginx restart
