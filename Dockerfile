################################
# Runtime environment variable args:
# GENEGRAPH_URL_HTTP:<url of http graphql endpoint of genegraph server>
#   if not provided, defaults to http://localhost:8888/api
# GENEGRAPH_URL_WS:<url of http graphql endpoint of genegraph server>
#   if not provided, defaults to ws://localhost:8888/ws
#
# UI server is exposed on container port 8080, can bind with -p <local-port>:8080
################################

FROM ubuntu:20.04

ENV DEBIAN_FRONTEND=noninteractive
RUN apt update
# clojure comes with openjdk 11
RUN apt install -y --no-install-recommends npm clojure

#RUN mkdir /app
COPY src /app/src
COPY public /app/public
COPY shadow-cljs.edn /app
COPY package.json /app

WORKDIR /app

RUN npm install yarn
ENV PATH="$PATH:/app/node_modules/.bin/"

# install npm deps from package.json
RUN yarn install
# install cljs deps from shadow-cljs.edn
RUN yarn shadow-cljs compile


MAINTAINER Tristan Nelson <thnelson@geisinger.edu>
EXPOSE 8080
CMD ["yarn", "watch"]
