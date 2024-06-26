const swaggerAutogen = require('swagger-autogen')()

const outputFile = './swagger_output.json'
const endpointsFiles = ['./server.js']

const doc = {
    info: {
        version: "1.0.0",
        title: "Cosmic API",
        description: "API for Books",
    },
    host: "api.nytuo.fr",
    basePath: "/",
    schemes: ['http', 'https'],
    consumes: ['application/json'],
    produces: ['application/json']
}


swaggerAutogen(outputFile, endpointsFiles, doc)