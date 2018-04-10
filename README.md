optional 

`react-native-git-upgrade`

or delete node_modules folder

`react-native upgrade`
    
```
create-react-native-app rndocscannerExample
cd rndocscannerExample
yarn add https://avwave@bitbucket.org/avwave/rn-doc-scanner-android.git
yarn eject
```

select React Native: first option

```
react-native link
```

open `android/build.gradle` 

inside allprojects->repositories

```
allprojects {
    repositories {
        ...
        mavenCentral()
        maven {
            url 'https://maven.google.com'
        }
        maven {
            url "http://dl.bintray.com/steveliles/maven"
        }
        maven {
            url "https://jitpack.io"
        }
    }
}
```

open
`android/app/src/main/AndroidManifest.xml`

inside manifest tag
```
<manifest ...
    xmlns:tools="http://schemas.android.com/tools"
>
```
inside application tag
```
<application
    ...
    tools:replace="android:allowBackup"
>
```

open `android/app/build.gradle`

change `compileSdkVersion xx`
to 

```
compileSdkVersion 26
```

run `react-native run-android --deviceId xxxx`

where `xxxx` is from `adb devices`