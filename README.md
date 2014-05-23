Soundscape-Android-Project
==========================

Music player created for May 2014

This application was created by Ross Owen as a project for a class at Penn State.

The assets and bin folders were unable to be uploaded, but also unnecessary. 

I DO NOT OWN THE RIGHTS TO ANY OF THESE SONGS.
ALL IMAGES AND MUSIC BELONG TO THEIR RESPECTIVE OWNERS.

***IMPORTANT NOTE***
The application will not work properly as it is.
It is dependent on having a servlet set up for a remote database running at a specific computer on a specific port.
Therefore this application is available only for demonstrative purposes.
********************

MainActivity:
-Displays a ListView of song information from an SQLite database that is accessed remotely.
-On clicking one of these items, we go to a SongActivity.

SongActivity:
-Depending on the item that was selected in the MainActivity, begin playing a corresponding song and display information about the song.
-A rewind button that stops the song and begins playing it again from the beginning.
-A pause button that, if playing, pauses the song.
-A play button that, if paused, begins playing the song at where it was paused.

SettingsActivity:
-Uses a SharedPreferences object to persist user preferences (background color).
