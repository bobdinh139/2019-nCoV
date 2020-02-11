# 2019-nCoV

jff project to track total cases of 2019-nCoV virus using ~~[people.cn](http://health.people.com.cn/GB/26466/431463/431576/index.html) website.~~ [ecdc.europa.eu](https://www.ecdc.europa.eu/en/geographical-distribution-2019-ncov-cases) website.

~~This isn't update real-time. The data is on Jan 28, 2020 as the website data has not been updated. I am working on a real-time update version which requires "reverse engineering" website lol.~~

[Report](https://docs.google.com/spreadsheets/d/1Oy1698DsQMpfFc1v8mmHq_1QKkcUtrB9ZS5fiDHAl_M/edit?usp=sharing)

### Usage

```java
GetUpdate.getDateUpdated()

```

returns a ```String```; gets the date that the data is last updated.

```java
GetUpdate.GetAllCountries()

```

returns an ```ArrayList<String>```; gets all infected countries.

```java
GetUpdate.GetAllInfections()

```

returns an ```ArrayList<Integer>```; gets all numbers of infections.

```java
GetUpdate.getAllCases()

```

returns an ```int```; gets total infections.

```java
GetUpdate.getAllCasesAlt()

```

returns an ```int```; gets total infections. Use this when ```getAllCases()``` does not work. But this needs to be called after ```GetAllInfections()``` is called; otherwise,```GetAllInfections()``` and ```GetAllCountries()``` will return nothing.

```java
GetUpdate.getAllDeaths()

```

returns an ```int```; gets total deaths.

```java
GetUpdate.getChinaDeaths()

```

returns an ```int```; gets total deaths in China.

**Important:**

```GetUpdate.GetAllCountries()``` and ```GetUpdate.GetAllInfections()``` are parallel arrays, with that said, index of one is the index of another.

The [ecdc.europa.eu](https://www.ecdc.europa.eu/en/geographical-distribution-2019-ncov-cases) website keeps getting update which broke the program frequently, wait a little bit for a fix.

China recently update their "suspected and confirmed case" program which if a patient has 2019-nCoV but does not show symptoms, it does not count as confirmed case. Hence, dramatically decrease the number of confirmed cases.
