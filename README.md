## rn-doc-scanner-android

### Installation 🚀
Optional

`react-native-git-upgrade`

or delete node_modules folder

`react-native upgrade`
    
```bash
create-react-native-app rndocscannerExample
cd rndocscannerExample
yarn add https://github.com/Diastorm/rn-doc-scanner-android.git
yarn eject
```

select React Native: first option

```bash
react-native link
```

open `android/build.gradle` 

inside allprojects->repositories

```java
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

### Usage 💃
#### RNDocScanner
| Property | Type | Parameters | Description |
|-----------------|----------|----------|--------------------------------------------|
| `getDocumentCrop` | `Function` | `(disableAutoFocus: Bool)` | If disableAutoFocus equal true, then it will launch cam and you take the picture, otherwise it will autoFocus and take the picture automatically. After that it will lead to the view which enable you to crop the picture. |

#### Example
```javascript
import { RNDocScanner } from 'rn-doc-scanner'
import { Text, TouchableOpacity, Platform } from 'react-native'

const ScanButton = (props) => {
   const onPressScan = async () => {
    if (Platform.OS === 'android') {
        try {
            const image = await RNDocScanner.getDocumentCrop(true)
            console.log(image)
            } catch (err) {
            console.log(err)
            }
        }
    }

  return (
        <TouchableOpacity onPress={onPressScan}>
          <Text>Scan</Text>
        </TouchableOpacity>
    )
}

export default ScanButton

```
