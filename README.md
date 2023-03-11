# Mob_HSAP

The Hybrid Search/Analytical Processing for Multiple Aspect Trajectory (Mob_HSAP) is a hybrid approach to searching and analyzing semantic trajectories


# Scripts

The database scripts are in MOBHSAP\scripts

* Tripbuilder dataset
```sh
MOBHSAP/scripts/scriptsTripbuilder.zip
```

* Foursquare dataset (sample)
```sh
MOBHSAP/scripts/sampl_input_foursquare.zip
```

* DDL Datawarehouse
```sh
MOBHSAP/scripts/DW/DW.sql
```

* Regexlookbehind function
```sh
MOBHSAP/scripts/DW/regexlookbehind_function.sql
```

## How load dataset
Config the input.properties file. Example:
```sh
input_class=mobhsap.foursquare.FoursquareInput
aspectDao_class=mobhsap.foursquare.FoursquareAspectDAO
separator=;
```
* Run the Main_Input class
```sh
mvn compile exec:java -Dexec.mainClass="mobhsap.Main_Input"
```

# Run tripBuilder and foursqaure search test
Queries for Tripbuilder text database
```sh
mvn compile exec:java -Dexec.mainClass="mobhsap.tripbuilder.search.test.QueriesTripBuilderMain"
```
Queries for Foursquare text database 
```sh
mobhsap.foursquare.search.test.QueriesFoursquareMain
mobhsap.foursquare.search.test.NewQueriesFoursquareMain
```

# Run Foursquare analytic query test
```sh
mvn compile exec:java -Dexec.mainClass="mobhsap.foursquare.analytics.test.QueryATrDWMainTest"
```
