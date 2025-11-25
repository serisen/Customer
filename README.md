
## DECISIONS:

1. The uniqueness of the customer is checked with social security number.
2. A sequence generator is used  for the customer number and account
3. The photos of the customer should be kept in a storage like S3 storage system in order. So the urls of the photos should be stored in DB.
   MinIO is used to use in local development, as it is lightweight and does not need AWS credentials
4. Sendgrid library is added for the notification emails. An API key is created in sendgrid, emails are sent via sendgrid as SMTP server is needed to send emails
5. Emails are sent asynchronusly, kafka might be used for email sending as well.
6. When the request is sent, after validations, the files are uploaded then the request is sent to Kafka topic, the consumer reads from the topic
  and creates the customer and the account and information email is sent the customer

### Endpoints
API:
http://localhost:8080/api/v1/onboard

Swagger:
http://localhost:8080/swagger-ui/index.html

MinIO Console UI:
http://localhost:9001/browser/customer-bucket
Object url: http://localhost:9000/customer-bucket/<key>
 
Kafka-UI:
http://localhost:8090/

## TODO

1. Account service should be implemented separately, it should generate IBAN as well.
2. There should be an option for the customers to be notified, via SMS or email or phone. Customers who choose to be notified by SMS should be notified via SMS
3. Event based notification service should be implemented, notifications should be sent to a Kafka topic for instance, 
   then SMS/email consumers should read from topic and send notifications
4. The requests that are stuck in error should be sent to DLT to be operated and to be sent to the customer for the information
5. Nationality might be kept as country code
6. Security should be implemented
7. Templates for different mails should be implemented
7. UT coverage can be increased, currently it is around 85%
8. DB scripts should be added to file, table and sequence and later db changes
9. Onboarding status might be added to track the process step by step
10. Different profile files should be prepared for the upper environments


### Curl command for integration testing
Add Customer:
curl -X POST http://localhost:8080/api/v1/onboard \
-H "Content-Type: multipart/form-data" \
-F "firstName=Ser" \
-F "lastName=Ser" \
-F "gender=FEMALE" \
-F "dateOfBirth=1987-01-01" \
-F "phoneNumber=+3145555" \
-F "email=serser@serexample.com" \
-F "nationality=NL" \
-F "residentialAddress=1111AK 111" \
-F "socialSecurityNumber=111" \
-F "idPhoto=@/pathTo/customer-onboarding-app/src/test/resources/passport.jpg" \
-F "photo=@/pathTo/customer-onboarding-app/src/test/resources/photo.jpg"


### DB model:
CREATE SEQUENCE customer_number_seq
START 1000000000
INCREMENT 1;

CREATE SEQUENCE account_number_seq
START 1
INCREMENT 1;

GRANT USAGE, SELECT ON SEQUENCE customer_number_seq TO myuser;
GRANT USAGE, SELECT ON SEQUENCE account_number_seq TO myuser;