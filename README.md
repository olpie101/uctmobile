# README #

####NB: This repo has not been updated in over 3 months. Some features my break####
####NB:Clone the 'Jammies' branch for latest build. Use Android Studio or just sideload APK found within app/build/outputs####

An Android UCT App which allows the user to view the University's News, Jammie Shuttle Timetable and in allow undergrads to plan out their degree.

### What works? ###

* News
* Jammie Shuttle Timetables - requires that local app engine instance is running and that the 'setupEnv' API has been called. Use app engine API explorer to initiate the call. 

### Know Bugs? ###

* If a news article is touched and the article is not present in any other search RSS feeds, it shall not open.
* Tapping 'Public Holiday' in the Jammie Shuttle selection process result in the app crashing. (fixing)
* When Jammie Timetable is loaded the stops sometimes appear in the wrong order. (fixing)