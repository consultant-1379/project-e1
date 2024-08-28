FROM node:16-alpine
COPY frontend /frontend
EXPOSE 8080
WORKDIR /frontend
RUN npm install
ENTRYPOINT npm start
