MovieWeb allows users to:
- check if any movie has won Oscar in Best Picture category since 1927 to 2010
- rate movies (1-5)
- browse rated movies
- get top 10 rated movies sorted by box office

Solution:  
My application isn't very complex, so I decided to use three layer architecture. Part of
data is taken from external API. For such call I'm using RestTemplate. As this API
is quite limited (missing batch API) I am using Spring's TaskExecutor and CompletableFuture
to make subsequent calls concurrent. Other part of data is taken from CSV file. 
Nominated movies data is imported into DB as it is much faster to take it from there.
Database schema and import of data is handled via liquibase. Checks for JWT is 
done with Spring security.