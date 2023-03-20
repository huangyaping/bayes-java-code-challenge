# Bayes Java Dota Challlenge

This is the [task](TASK.md).

## Summary of the solution
* Events are parsed using a readable pattern and converted into corresponding DotaEvent classes, which makes the code maintainable and extensible.
* Batch insert is enabled by configuring spring.jpa.properties.hibernate.jdbc.batch_size=10.
* The customized queries were conducted by native SQL. 
* Test coverage is implemented for all REST APIs.
