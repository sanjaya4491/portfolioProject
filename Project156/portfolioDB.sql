use sdhakal;
-- Authors: Daniel Godoy and Sanjaya Dhakal

drop table if exists PortfolioAsset;
drop table if exists Portfolio;
drop table if exists Asset;
drop table if exists Email;
drop table if exists Person;
drop table if exists Address;
drop table if exists State;
drop table if exists Country;

create table Country (
countryId int not null primary key auto_increment,
country varchar (255) not null
);

create table State(
stateId int not null primary key auto_increment,
state varchar (255) not null,
countryId int not null,
foreign key (countryId) references Country(countryId)
);

create table Address(
addressId int not null primary key auto_increment,
street varchar(255) not null,
city varchar (255) not null,
zipCode varchar (255) not null,
stateId int not null,
foreign key (stateId) references State (stateId)
);

create table Person (
personId int not null primary key auto_increment,
personCode varchar (255) not null unique key,
firstName varchar (255) not null,
lastName varchar (255) not null,
brokerType varchar (255),
brokerSection varchar (255),
addressId int not null,
foreign key (addressId) references Address(addressId) on delete cascade
);

create table Email(
emailId int not null primary key auto_increment,
email varchar (255),
personId int not null,
foreign key (personId) references Person (personId) on delete cascade
);

create table Asset(
assetId int not null primary key auto_increment,
assetCode varchar (255) not null unique key,
assetType varchar(255) not null,
assetName varchar(255) not null,
apr double,
quarterlydividend double,
baseReturn double,
omegaMeasure double,
totalValue double,
stockSymbol varchar(255) unique key,
sharePrice double
);

create table Portfolio(
portfolioId int not null primary key auto_increment,
portfolioCode varchar(255) not null unique key,
personId int not null,
brokerid int not null,
beneficiaryId int,
foreign key (personId) references Person (personId) on delete cascade,
foreign key (brokerId) references Person (personId) on delete cascade,
foreign key (beneficiaryId) references Person (personId) on delete cascade
);

create table PortfolioAsset (
portfolioAssetId int not null primary key auto_increment,
portfolioId int not null,
assetId int not null,
value double not null,
foreign key (portfolioId) references Portfolio (portfolioId) on delete cascade,
foreign key (assetId) references Asset (assetId) on delete cascade
);

insert into Country (country)values
("USA");

insert into State (state , countryId) values
("NE", (select countryId from Country where country = "USA")),
("IL", (select countryId from Country where country = "USA")),
("NY", (select countryId from Country where country = "USA"));

insert into Address (street, city, zipCode, stateId) values
("9903 Jenifer Streets", "Fremont", "68025", (select stateId from State where state = "NE")),
("1 School Road" , "Chicago", "60613", (select stateId from State where state = "IL")),
("8 Ronald Regan Hill" , "Omaha", "68116", (select stateId from State where state = "NE")),
("1 Independence Plaza" , "New York", "10004", (select stateId from State where state = "NY")),
("64516 Golf View Drive" , "Omaha", "68116", (select stateId from State where state = "NE" ));

insert into Person (firstName, lastName, personCode, brokerType, brokerSection, addressId) values
("Salvatore" , "Cordova", "007Y", "E", "sec1230", (select addressId from Address where street = "9903 Jenifer Streets")),
("Adora" , "Sickling", "UIMG", null, null, (select addressId from Address where street = "1 School Road")),
("Dionis" , "Handscomb", "YMCA", null, null, (select addressId from Address where street = "8 Ronald Regan Hill")),
("Lillian" , "De Angelis", "PK9B", "J", "sec290", (select addressId from Address where street = "1 Independence Plaza")),
("Quinn" , "Carress", "R555RD", null, null, (select addressId from Address where street = "64516 Golf View Drive"));

insert into Email (email, personId) values
("cordova0@zdnet.com", (select personId from Person where personCode = "007Y")), 
("asickling1@a8.net", (select personId from Person where personCode = "UIMG")), 
("ohyeah@ohyea.com", (select personId from Person where personCode = "UIMG")), 
("ldeangelis3@weebly.com", (select personId from Person where personCode = "PK9B")), 
("qcarress4@toplist.cz", (select personId from Person where personCode = "R555RD")), 
("whatever@whatever.com", (select personId from Person where personCode = "R555RD"));

insert into Portfolio (personId, brokerId, beneficiaryId, portfolioCode ) values
((select personId from Person where personCode = "YMCA"), (select personId from Person where personCode = "007Y"), (select personId from Person where personCode = "UIMG"), "PT001"),
((select personId from Person where personCode = "UIMG"), (select personId from Person where personCode = "PK9B"), null, "PT002"),
((select personId from Person where personCode = "R555RD"), (select personId from Person where personCode = "PK9B"), null, "PT003"); 

insert into Asset (assetName, assetType, assetCode, apr, quarterlydividend ,
					baseReturn ,omegaMeasure ,totalValue ,stockSymbol ,sharePrice ) values
("Money Market Savings", "D", "BX001-23", 13.05, null, null, null, null, null, null),
("Savings Account", "D", "CCBB00", 0.24, null, null, null, null, null, null),
("4-year CD", "D", "99800", 4.35, null, null, null, null, null, null),
("3-year Rollover CD", "D", "apple", 2.35, null, null, null, null, null, null),
("2-year Rollover CD", "D", "dell22", 2.75, null, null, null, null, null, null),		
("chrome Inc.", "S", "google", null, 0.0, 5.6, -0.05, null, "alpabte", 84.708),
("Diageo PLC", "S", "dadooe90", null, 12.00, 3.2, 0.01, null, "DEO", 12.75),
("acccv", "S", "324yoo", null, 24.50, 4.3, -0.07, null, "abc", 42.80),
("Many Midget Manufacturers Man.", "P", "AME21", null, 1000, 2 , -0.15, 4333, null, null);

insert PortfolioAsset (portfolioId, assetId, value) values
((select portfolioId from Portfolio where portfolioCode = "PT001"), 
	(select assetId from Asset where assetCode = "BX001-23"), 100),
    
((select portfolioId from Portfolio where portfolioCode = "PT002"), 
	(select assetId from Asset where assetCode = "CCBB00"), 200),
((select portfolioId from Portfolio where portfolioCode = "PT002"), 
	(select assetId from Asset where assetCode = "99800"), 300),
((select portfolioId from Portfolio where portfolioCode = "PT002"), 
	(select assetId from Asset where assetCode = "apple"), 400),
((select portfolioId from Portfolio where portfolioCode = "PT002"), 
	(select assetId from Asset where assetCode = "dell22"), 500),
    
((select portfolioId from Portfolio where portfolioCode = "PT003"), 
	(select assetId from Asset where assetCode = "google"), 111),
((select portfolioId from Portfolio where portfolioCode = "PT003"), 
	(select assetId from Asset where assetCode = "dadooe90"), 222),
((select portfolioId from Portfolio where portfolioCode = "PT003"), 
	(select assetId from Asset where assetCode = "324yoo"), 333),
((select portfolioId from Portfolio where portfolioCode = "PT003"), 
	(select assetId from Asset where assetCode = "AME21"), 44);