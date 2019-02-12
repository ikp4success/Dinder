
**Authors** - Immanuel George, Graham Sullivan , David Jones

**Independent Project** - Dinder

**Class** - CIS 470

**Cleveland State University**

**App Description**
Dinder app, is an app that pulls restaurant around your location and displays it to the user. 
Pictures of dishes or Restaurant photo are shown as a series of cards.
Dinder is inspired from the Tinder app which uses card containers to hold pictures of people who are willing to mingle, e.t.c Users are able to dislike or like pictures of dishes or restaurant, just like Tinder. 

**Technical Description**
We used third party libraries in this project which is listed as one of the resources below. The model and view folder hold most of the code that came from an outside source. In the view folder there is multiple custom adapter class which are responsible for the card creation and interaction. This project uses the navigation fragment built by android, so if you swipe right you will other options, like favorites, comunity and the default option which is home. Our code consists of multiple layout xmls, GlobalVariables class, Home class, Likes class, and the RestAdapter class.

**Home.java**
This is where we utilized the swipeable cards library, card containers adapters, universal image loader library, yelp Api, and json.  The loadResultsFromYelpAndToCard() method kicks off the progression async task method. This was necessary, to allow the thread to wait as some background work is done which involves contact yelp api and waiting for data. This data is parsed to get the needful information and then fed to the loadCards() method which is responsible for fixing up the images on the cards itself. The universal image loader helped to speed up some image background work. it does its own image Multithreaded image loading. This library is awesome, especially if you have so many images you want to display and you not sure of the size of the image origin. There was also some work using the android location, which uses the location manager class. This class provides access to the system location services. These services allow applications to obtain periodic updates of the device's geographical location, or to fire an application-specified Intent when the device enters the proximity of a given geographical location. onRefresh() method was also used for the pulldown to refresh, which reloads new cards onto the card container. This is still very experimental and should be used with caution.

**Likes.java**
The like fragment supposed to get all the saved liked from user. We ran into a difficult situation whereby it saves the amount of user likes but just one image/information. So for example if i swipe like seven times, I will get a pizza restaurant seven times, instead of seven different restaurant i chose. Graham was able to create another way to save up the likes by clicking a menu within the home fragments which link you back to the Likes fragment page. This class uses a custom list adapter that is responsible for loading the data into the list view.

**GlobalVariables.java**
	This class is used to locally save which restaurants the user has liked. It currently saves the name and address which are used to load the Likes fragment. This class extends Application so the data within it will be saved to the application context and can be retrieved by any fragment or activity.

**RestAdapter.java**
	This class extends a String ArrayAdapter and is passed the restaurant titles and addresses. It displays the titles in a list and uses the address to create a google maps button. When the maps button is pressed the google maps application will open loaded with the restaurant address, so the user can instantly navigate.



**External Libraries**

Swipeable cards is a native library for Android that provide a Tinder card like effect. A card can be constructed using an image and displayed with animation effects, dismiss-to-like and dismiss-to-unlike, and use different sorting mechanisms.
The library is compatible for Android versions 3.0 (API Level 11) and upwards.
Author/Source: https://github.com/kikoso/Swipeable-Cards

Universal Image Loader aims to provide a powerful, flexible and highly customizable instrument for image loading, caching and displaying. It provides a lot of configuration options and good control over the image loading and caching process.
Multi-thread image loading (async or sync)
Wide customization of Ima
ge-Loader's configuration (thread executors, downloader, decoder, memory and disk cache, display image options, etc.)
Many customization options for every display image call (stub images, caching switch, decoding options, Bitmap processing and displaying, etc.)
Image caching in memory and/or on disk (device's file system or SD card)
Listening loading process (including downloading progress)

Author/Source: https://github.com/nostra13/Android-Universal-Image-Loader

JSON (JavaScript Object Notation) is a lightweight data-interchange format. It is easy for humans to read and write. It is easy for machines to parse and generate. It is based on a subset of the JavaScript Programming Language, Standard ECMA-262 3rd Edition - December 1999. JSON is a text format that is completely language independent but uses conventions that are familiar to programmers of the C-family of languages, including C, C++, C#, Java, JavaScript, Perl, Python, and many others. These properties make JSON an ideal data-interchange language.



Yelp Api has a broad database, which we used currently for this app. Yelp Api v. 2.0 boast a lot of features.
Find up to 40 best results for a geographically-oriented search
Sort results by the best match for the query, highest ratings, or distance
Limit results to those businesses offering a Yelp Deal, and display information about the deal like the title, savings, and purchase URL
Identify and display whether a business has been claimed on Yelp.com
Author/Source: https://www.yelp.com/developers/documentation/v2/overview

**Screenshots**

![scsht_1](https://github.com/ikp4success/Dinder/blob/master/ScreenShot/scsht_1.png)
![scsht_2](https://github.com/ikp4success/Dinder/blob/master/ScreenShot/scsht_2.png)
![scsht_3](https://github.com/ikp4success/Dinder/blob/master/ScreenShot/scsht_3.png)
![scsht_4](https://github.com/ikp4success/Dinder/blob/master/ScreenShot/scsht_4.png)
