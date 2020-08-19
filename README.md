Overview
========
This is a very basic spring-boot app that can store a list of numbers and return a permutation of those numbers using REST endpoints.

Running the app
==================
Run it using the `mvnw spring-boot:run` command line command or your favorite IDE.
The app requires Java 8

REST endpoints
==============
- Call `GET http://localhost:5000/store?numbers=<comman separated numbers>` endpoint to store a list of numbers. Response will contain id of the list of numbers 
- Call `GET ttp://localhost:5000/permutation?id=<id>` endpoint to get a random permutation of a list of numbers stored with provided id 
