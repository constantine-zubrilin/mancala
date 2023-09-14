FROM node:20 AS build
WORKDIR /usr/src/app
COPY package*.json ./
RUN yarn install
COPY . .
#RUN npm install -g serve
# RUN npm run build
EXPOSE 3000

CMD [ "yarn", "run", "start", "--verbose", "--trace-deprecation"]
#CMD [ "npm", "run", "start", "--verbose", "--trace-deprecation"]
#CMD ["npx", "serve", "-s", "public"]

#FROM node:18-alpine AS deps
#ENV NODE_ENV=development
#
#WORKDIR /app
#
#COPY packages packages
#COPY package*.json ./
#
#RUN --mount=type=cache,target=/root/.yarn YARN_CACHE_FOLDER=/root/.yarn yarn workspace @martech/careers install --frozen-lockfile
#
#FROM node:18-alpine AS build
#
#ENV NODE_ENV=production
#
#WORKDIR /app
#
#COPY --from=deps /app/node_modules ./node_modules
#COPY package*.json .
#RUN yarn workspace @martech/careers build
#
#FROM node:18-alpine AS runner
#
#WORKDIR /app
#
#COPY --from=build /app/services/careers ./services/careers
#COPY package*.json yarn.lock .
#RUN --mount=type=cache,target=/root/.yarn YARN_CACHE_FOLDER=/root/.yarn yarn workspace @martech/careers install --frozen-lockfile
#
## Running the app
#CMD [ "yarn", "run", "run:careers" ]
#
#EXPOSE 3000