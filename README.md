# arya-interpreter-android

## Prerequisites

### Android SDK

- Download [the SDK](https://developer.android.com/sdk/installing/index.html?pkg=tools) and extract it.
- `$ANDROID_HOME` environment variable must be configured to point to the installation directory of the Android SDK. This can be achieved with `export ANDROID_HOME=YOUR_INSTALLATION_DIR/android-sdk-linux`
- Execute `./$ANDROID_HOME/tools/android sdk` to launch the Android SDK Manager.
- As a minimum setup, you should at least download the latest (highest version) tools under the Tools directory (Android SDK Tools, Android SDK Platform-tools, Android SDK Build-tools), and also latest SDK Platform under the first Android X.X (highest version) folder.

## Building From Source

To be able to install necessary libraries to build Android interpreter, a helper project named **Maven Android SDK Deployer** is required. It basically publishes Android dependencies to the local Maven repository under the groupId `com.google.android`, as a result, maven gains access to these libraries as it compiles the interpreter. More info about the project can be found [here](https://github.com/simpligility/maven-android-sdk-deployer).

- [Download](https://github.com/simpligility/maven-android-sdk-deployer) the Maven Android SDK Deployer and extract it.
- Run the command `mvn install` in the root folder of the SDK Deployer. To install only a certain SDK level (for example SDK 6.0), use `mvn install -P 6.0`
- After that, you can build the interpreter running the command `mvn clean install -DskipTests`.

## Running the Application

- Go to $ANDROID_HOME/tools directory
- Run `./android avd` to create or edit an Android virtual device, then start it.

> API level of the created virtual device must be compatible with the interpreter. Currently, supported API level is 22.

- Run the following command to deploy and run the interpreter on the virtual device. `mvn install android:deploy android:run`. More info about the maven android commands can be found [here](http://simpligility.github.io/android-maven-plugin/plugin-info.html).

## Setting Up Development Environment (Android Studio)

- [Download](https://maven.apache.org/download.cgi) and install Git and Maven if they are not already installed. (Maven version must be at least 3.1.1!).
- [Download](http://developer.android.com/sdk/index.html#Other) and extract latest version of the Android Studio.
- [Download](https://github.com/simpligility/maven-android-sdk-deployer) and extract Maven Android SDK Deployer.
- Make sure you set environment variables for **JAVA_HOME**, **ANDROID_HOME** and **MAVEN_HOME** correctly.
- Run `mvn install` in the root folder of Maven Android SDK Deployer to generate Android SDK jars. If it fails, try to install missing Android packages via Android SDK Manager, or try to install specific API level by running `mvn install -P 6.0`.
- Run `mvn clean install -DskipTests` in the root folder of Arya.
- Open Android Studio and import **arya-interpreter-android** as existing Maven project.
- Open File > Project Structure > Modules and set src.main.java as java source directory by right clicking and selecting Sources. Apply changes.
- Open File > Project Structure > Facets and set Manifest file path. (Gradle and Maven uses different manifest paths and default path points to the Gradle path!). Apply changes.
- Open Run > Edit Configurations and add new configuration by clicking '+'. In this configuration, set module as arya-interpreter-android and choose an appropriate emulator with API level 22. (Create an AVD first if there is not any!). Apply changes.
- At the same page, under 'Before Launch' section, remove any Gradle build conf and add new Maven goal. In this goal, set command line as `install android:deploy android:run`. (This maven goal will be used by Android Studio before running application!). Apply changes.

> Do not try to use Eclipse for Android development, as its Android plugins no longer supported and maintained by Google!