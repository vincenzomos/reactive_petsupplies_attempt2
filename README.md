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
User entity:
```
case class Quiz(id: String, question: String, correctAnswer: String)
```

### Run the Mongo Database

Run MongoDB on windows

```
D:\Mongodb\bin\mongod.exe --dbpath d:\mongodb\data
```

### Create a User
```  
curl -v -H "Content-Type: application/json" \
     -X POST http://127.0.0.1:5000/user \
     -d '{"city": "Amsterdam", "role": "customer", "username": "testpost01", "streetAddress": "Bloemgracht 10", "firstname": "Ziggy", "emailAddress": "test@reactivecountry.nl", "surname": "Stardust", "postalCode": "10023"}'
```

### Delete a user
```
curl -v -X DELETE http://localhost:5000/user/testuser01
```

### Get a user by UserName
```
curl -v http://localhost:5000/user/testuser01
```

### Update a user
```
curl -v -H "Content-Type: application/json" \
     -X PUT http://localhost:5000/user/testuser01 \
     -d '{ "streetAddress": "Rozengracht 36" }'
```
