# The cash register

The final project of free EPAM course is *The cash register*

### Requirements

> There are roles: `cashier`, `senior cashier`, `commodity expert`.

> The commodity expert:

- create goods and indicate their quantity in the warehouse.

> The cashier:

- create order (check);
- add selected products by their code or by product name in the order;
- specify / change the quantity of a certain product or weight;
- close the order (check).

> The senior cashier:

- cancel check;
- cancel the goods in the receipt;
- create X and Z reports.

### Details
1. Java version 11
2. The webapp uses `Apache Tomcat 8.5`
3. Some config properties were changed for DataSource [Download configs](https://google.com)
4. The main database is `Postgresql`. You can initialize all tables with script `create-db-postgre.sql`.