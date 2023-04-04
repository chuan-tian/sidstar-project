# Build
FROM node:16-alpine3.12 as builder

WORKDIR /app

COPY . ./

RUN npm install

RUN npm run build

# Run
FROM nginx:1.22-alpine

COPY --from=builder /app/build /usr/share/nginx/html

RUN rm /etc/nginx/conf.d/default.conf

COPY ./nginx.conf /etc/nginx/conf.d

EXPOSE 8080

CMD ["nginx", "-g", "daemon off;"]
