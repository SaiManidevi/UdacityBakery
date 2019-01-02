# UdacityBakery
An Android App that displays recipes by [Udacity’s](https://in.udacity.com/) resident baker-in-chief, Miriam.
The app allows the user to select a recipe and see video-guided steps for how to complete it.
## Overview
The app named as - UdacityBakery displays a grid of desserts. The following are the *features of this app* :

* Fetches data from the Internet using a link provided by Udacity (link removed in code for privacy); 
given JSON file has mixed data (some steps may have videos/only images/none)
* Displays videos for most of the recipe steps (default image if video not available)
* Uses Fragments to create a responsive design that **works on both phones and tablets**.
* Consists of a custom **widget** where users can add a selected dessert's ingredients on the home screen.
* Handles error cases in Android.

## Libraries Used
* **ExoPlayer** to handle video data
* **Volley** to make Network calls easier & faster
* **GSON** for parsing JSON
* **Picasso** for Image Caching
* **Espresso** for performing UI tests for the app

## Screenshots
![image](https://user-images.githubusercontent.com/39236351/50595135-2db9ac80-0ec5-11e9-89a3-c898ebce7ea2.png)
![image](https://user-images.githubusercontent.com/39236351/50595192-5e99e180-0ec5-11e9-8ec6-1f3154acde56.png)
![image](https://user-images.githubusercontent.com/39236351/50594486-cf8bca00-0ec2-11e9-8ee1-4c512337be4d.png)
![image](https://user-images.githubusercontent.com/39236351/50594423-8b98c500-0ec2-11e9-9d8a-2daaf3967704.png)
![image](https://user-images.githubusercontent.com/39236351/50593470-70c45180-0ebe-11e9-8337-79e444beedd7.png)
![image](https://user-images.githubusercontent.com/39236351/50593509-9c473c00-0ebe-11e9-8175-43ae9d3e3191.png)
![image](https://user-images.githubusercontent.com/39236351/50594154-6a83a480-0ec1-11e9-8479-8f361a4fe1b8.png)
