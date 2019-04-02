Progress Report 5

Derek Worley - Generation of NLP parse tree. 

#What I accomplished:
Made a NFLinfo class that holds the info TJ will need. It has a test method that will fill in its values with the expected data in case the NLP fails.
Started converting external libraries used from jars to a maven pom file.
Updated program to no have a large main method, broke it into smaller classes.

##What I want to accomplish:
Get my program in the pipeline with the weather query, get it to work with maven. Fix the java out of memory heap stack error that’s causing the NLP to be crippled. 

###What I didn't get done:
Didn’t get my program to be in the pipeline because of the heap error.

James Setola - Voice to Text and Database Maintenance

#What I accomplished:
Decided that the Google Speech to Text API was no longer a good idea to be used for the project due to cost and other issues.
Researched and implemented an open source speech to text software developed by Carnegie Mellon University.
Was able to set up the software to listen and translate our 10 queries in real time.

##What I want to accomplish for next week:
Implement a “wake word” to our project so that the program does not begin to listen until the wake word is said.
Save logs of all queries asked to our device.
Begin working with Derek to pass my output to his input for his NLP.
 
###What I didn't get done:
Was unable to continue work with the database and front end web app. I have decided that these tasks can wait until my piece of the pipeline is completed and I can successfully pass my output to the next piece of the pipeline.

Jackson Spector - Raw Data analysis and assemblage of voice response.

#What I accomplished:
Wrote java code including a method that takes in a JSON object from TJ, parses it and outputs speech based on the query. Works for about 5 queries so far.

##What I hope to accomplish:
Finish all 10 queries and complete the entire pipeline.

###What I did not accomplish:
I have not tried using my methods in the pipeline and still haven’t completed all of the 10 queries.

TJ Fogarty - NLP tree into Query

#What I accomplished:
Completed the overall structure of my codebase. Finished the weather function. Current operational functions are Weather, Time, Random Number, and Coin Flip. All return JSON objects which i’ve coordinated with Jackson for his canned response. Also, I started a documentation format so that others have a way to view my output format as well as how to run my part of the program

##what I hope to accomplish this week:
work with Derek to standardize my input. Will work on a seamless transition from nlp input to json output. If I have more time, I will work to do SQL. I hope to be able to finish the pipeline this week

###what i did not accomplish:
Have not yet implemented anything to do with querying our databases. Don’t have a great idea as to exactly what i’m getting from derek