# Project: Social media blog API

## Project Description

I completed this project as part of Revature's pre-training program. It's a backend for a hypothetical social media app that manages users’ accounts and any messages that they submit to the application. This project uses a JDBC connection to access data from a database, which enables use of SQL crud operations in a DAO class in java code. This backend delivers the data needed to display information for accounts and messages, such as processing actions like logins, registrations, message creations, message updates, and message deletions.

## Technologies Used

- Java
- SQL queries
- DAO Design Pattern
- Java Controller and Service classes

## Features

The API allows a user to

1. create a new Account on the endpoint POST localhost:8080/register. The request body contains a representation of a JSON Account, but does not contain an accountId. If all the required conditions are met, the response status is 200 OK, and the new account is persisted to the database.

2. verify login on the endpoint POST localhost:8080/login. The request body will contain a JSON representation of an Account. If the username and password provided exist together in the database, the response body contains a JSON representation of that account.

3. create new messages. This can be done by making an HTTP POST request at localhost:8080/messages with the message details in the request body.

4. retrieve all messages from the database using a get request at the /messages endpoint.

5. retrieve a message by its message id by making a get request to the /messages/{id} endpoint.

6. delete a message by its message id by issuing a delete request to the /messages/{id} endpoint.

7. update a message text by message id by using a patch request at the /messages/{id} endpoint

8. retrieve all messages posted by a particular user (account id) by issuing a get request to /accounts/{id}/messages

#### TODO list

Add functionality to retrieve a list of all user accounts in the database.

## Getting Started

To clone this project use the following command and url:

git clone https://github.com/MarkBahr/MarkBahr-pep-project.git

If you change the type of database, you'll have to have a jdbc driver for that specific sql flavor (ie PosgreSQL, MySQL, etc).
