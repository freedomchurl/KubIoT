FROM mysql
EXPOSE 3306
COPY ./db.sh /
WORKDIR /
RUN ["service", "mysqld", "start"]
RUN ["sh", "db.sh"]


