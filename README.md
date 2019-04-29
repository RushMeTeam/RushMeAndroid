# RushMeAndroid
[RushMe](https://github.com/RushMeTeam) is a platform to make Greek Life more accessible. As traditional fraternity rush campaigns leverage technology, physical calendars are succeeded by digital counterparts. By remaining open source, free, and school independent, RushMe has been developed with a strong Greek and student life in mind.

Users of the iOS and Android Apps can see fraternity profiles, get directions to houses, and access organized event calendars. Moreover, the platform allows user to subscribe to events to receive notifications and favorite fraternities to receive unobtrustive daily digests during rush. An aggregate of all calendars, profile photos for each fraternity, and a location-based view of fraternities provides the student body with relevant information, fostering a campus' knowledge, interest, and participation in their Greek Life.

RushMe has been implemented successfully at Rensselaer Polytechnic Institute. The platform can easily be implemented elsewhere, requiring only fraternity participation, at no cost--now, or in the future. Feel free to contact us if your school is interested.

# Screenshots
<img src="https://github.com/RushMeTeam/RushMeAndroid/blob/master/screenshots/Nexus5X_CalendarDetails.png" width="200"> <img src="https://github.com/RushMeTeam/RushMeAndroid/blob/master/screenshots/Nexus5X_FraternityDetails.png" width="200">

# Supported Platforms
RushMe Android supports Android 4.4 (API level 19) and beyond, and is tested on the Nexus 5.

Reccomended Android 9.0 (API level 28) and beyond for maximum functionality.

# Classes 
|Class Name|Path|Description|
|:-:|:-:|:-:|
|ActionLogging.java|RushMeAndroid\app\src\main\java\com\example\prests1\rushmeandroid|Checks if a fraternity was selected and checks if the app is in the foreground or background.|
|Campus.java|RushMeAndroid\app\src\main\java\com\example\prests1\rushmeandroid|Creates the Campus object which holds all events and fraternities.|
|CustomSpan.java|RushMeAndroid\app\src\main\java\com\example\prests1\rushmeandroid|Initializes daily number of events text.|
|EventRecyclerViewAdapter.java|RushMeAndroid\app\src\main\java\com\example\prests1\rushmeandroid|Checks for fraternities that are selected and stores events.|
|Fraternity.java|RushMeAndroid\app\src\main\java\com\example\prests1\rushmeandroid|Creates Fraternity Object, Event Object, and other related functions.|
|FraternityDetail.java|RushMeAndroid\app\src\main\java\com\example\prests1\rushmeandroid|Creates the page that lists fraternity details.|
|MainActivity.java|RushMeAndroid\app\src\main\java\com\example\prests1\rushmeandroid|The starting activity that executes most of the necessary code.|
|eventDecorator.java|RushMeAndroid\app\src\main\java\com\example\prests1\rushmeandroid|Displays the number of events for specific dates.|
|SettingsActivity.java|RushMeAndroid\app\src\main\java\com\example\prests1\rushmeandroid|Creates the settings page and displays Version/Build numbers.|
|SplashActivity.java|RushMeAndroid\app\src\main\java\com\example\prests1\rushmeandroid|Creates and animates the Splash Screen.|
|ExampleInstrumentedTest.java|RushMeAndroid\app\src\androidTest\java\com\example\prests1\rushmeandroid|Instrumented test, which will execute on an Android device.|
|ExampleUnitTest.java|RushMeAndroid\app\src\test\java\com\example\prests1\rushmeandroid|Example local unit test, which will execute on the development machine (host).|

# To-Do
Settings Page
- Data Analytics
- Shuffle Fraternities
- Display past events

List of Fraternities

Ability to favorite Fraternites

Searchbar for Fraternity names

Map View in Fraternity details

Tablet Support

# Contribute to RushMe
The team welcomes contributions to RushMe. To contribute fork the repository, make (and document!) some changes and submit a pull request for us to look over. Approved changes will be included in the next release.

# Contact Us
Regarding development or privacy concerns, [Adam Kuniholm](kuniha@rpi.edu).
To learn more about RPI's Interfraternity Council, [the Vice President of RPI IFC](ifc.rpi.recruitment@gmail.com)

