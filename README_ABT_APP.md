# ShowCaseAndroidApp #

This application gets a list of devices and the android versions and displays them accordingly.
It allows users to be able to add new android versions to the list and also add new devices with a limitation to not being able to add images to the devices.

It has the following features:
- Add a device
- Add an android version
- Delete a device
- Delete an android version
- Get a list of devices and android versions
- View device details

Pull to refresh allows new data to get synced with the existing data. There is a database storage that allows data to be fetched from it with the capability of syncing only the latest entries using pull to refresh

# How to get setup #

Android Studio v2.0 Preview 4 has been used for this application, however, you can use any earlier version of Android Studio. JDK 1.8 and JDK 1.7 can be used.
- Install JDK and any version of Android Studio
- Clone this repository or else download the application by Selecting import from VCS (GitHub being an option)
- Build the project
- You will find the apk in the folder /app/build/outputs/apk/

# Libraries used #
The following libraries have been used in this application
- AppCompat v7:23.1.1
- GSON
- Squareup OK HTTP
- Retrofit v2.0.0-beta2
- Retrofit GSON Converter
- MaterialEditText
- SwipeLayout
- Android Support v4
- FloatingActionButton
- CardView