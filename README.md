#Customer Manager service
Tutorial on how to build a REST api with Spray, Akka and ReactiveMongo

Article: <a href="http://danielasfregola.com/2015/03/16/how-to-integrate-reactivemongo-in-your-akka-spray-application/" target="_blank">How to Integrate ReactiveMongo in your Akka Spray Application</a>

## How to run the service
Clone the repository:
```
> git clone https://github.com/DanielaSfregola/quiz-management-service.git
```

Get to the `tutorial-2` folder:
```
> cd tutorial-2
```

Run the service:
```
> sbt run
```

The service runs on port 5000 by default.

## Usage
Quiz entity:
```
case class Quiz(id: String, question: String, correctAnswer: String)
```
Question entity:
```
case class Question(id: String, question: String)
```
Answer entity:
```
case class Answer(answer: String)
```

### Run the Mongo Database

Run MongoDB on windows

```
D:\Mongodb\bin\mongod.exe --dbpath d:\mongodb\data
```

### Create a User
```
curl -v -H "Content-Type: application/json" \
     -X POST http://localhost:5000/quizzes \
     -d '{"id": "test", "question": "is scala cool?", "correctAnswer": "YES!"}'
     
curl -v -H "Content-Type: application/json" \
     -X POST http://127.0.0.1:5000/user \
     -d '{"city": "Amsterdam", "role": "customer", "username": "testpost01", "streetAddress": "Bloemgracht 10", "firstname": "Ziggy", "emailAddress": "test@reactivecountry.nl", "surname": "Stardust", "postalCode": "10023"}'
```

### Delete a quiz
```
curl -v -X DELETE http://localhost:5000/quizzes/test
```

### Get a user by Id
```
curl http://127.0.0.1:5000/customer/53d7be30242b692a1138ac82
```

### Get a user by UserName
```
curl -v http://localhost:5000/questions/test
```

### Answer a question
```
curl -v -H "Content-Type: application/json" \
     -X PUT http://localhost:5000/questions/test \
     -d '{ "answer": "YES!" }'
```
