# Game Sales Project
This project was done by Allen Tan Kuan Boon for the technical test provided by Vanguard Software. 
This project use:
-Maven
-Spring Boot 2.7.18
-Java 1.8
-Spring Data JPA
-Mysql DB

## Installation
1. Download the source code.
2. Open any IDE (Eclipse/Spring Tool Suite 4)
3. Import existing maven projects -> select the download source code -> click ok
4. The IDE should auto download dependencies stated in pom.xml file.
5. Once build success, run Project as Spring Boot App.

## My SQL Tables
### 1. game_sales table
CREATE TABLE `game_sales` (
  `id` int NOT NULL,
  `game_no` int NOT NULL,
  `game_name` varchar(20) DEFAULT NULL,
  `game_code` varchar(5) DEFAULT NULL,
  `type` int NOT NULL,
  `cost_price` decimal(5,2) DEFAULT NULL,
  `tax` int DEFAULT NULL,
  `sale_price` decimal(5,2) DEFAULT NULL,
  `date_of_sale` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_date_of_sale` (`date_of_sale`)
)
**REFERENCE** :
[gamesales/images
/game_sales_table.JPG](https://github.com/AllenTan941028/Test/blob/main/gamesales/images/game_sales_table.JPG)

### 2. import_progress table
CREATE TABLE `import_progress` (
  `id` int NOT NULL AUTO_INCREMENT,
  `total_records` int NOT NULL,
  `processed_records` int NOT NULL,
  `start_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `process_time` varchar(50) DEFAULT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  `status` varchar(20) NOT NULL,
  `message` text,
  `invalid_records` int NOT NULL,
  PRIMARY KEY (`id`)
)
**REFERENCE** :
[gamesales/images
/import_progress_table.JPG
](https://github.com/AllenTan941028/Test/blob/main/gamesales/images/import_progress_table.JPG)

## REST API Endpoints
1. **Import CSV endpoint**
   **Full url** - localhost:8080/game/import
   **Parameter**: file - [Your csv file]
   ([gamesales/images/Import_EndPoint.JPG](https://github.com/AllenTan941028/Test/blob/main/gamesales/images/Import_EndPoint.JPG))

2. **Get Game Sales endpoint**
   **Full url** - localhost:8080/game/getGameSales
   **Parameters**: fromDate (yyyy-MM-dd), toDate (yyyy-MM-dd), salePrice, page, size.
   ([gamesales/images/GameSales_EndPoint.JPG](https://github.com/AllenTan941028/Test/blob/main/gamesales/images/GameSales_EndPoint.JPG))

3. **Get Total Sales endpoint**
   **Full url** - localhost:8080/game/getTotalSales
   **Parameters**: fromDate (yyyy-MM-dd), toDate (yyyy-MM-dd), displayTotalSales (1/0), game_no.
   ([gamesales/images
/TotalSales_EndPoint.JPG](https://github.com/AllenTan941028/Test/blob/main/gamesales/images/TotalSales_EndPoint.JPG))
