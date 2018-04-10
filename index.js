import {
  Platform
} from 'react-native';
import IOSRNToast from './index.ios.js';
import {AndroidRNToast, AndroidRNDocScanner} from './index.android.js';

let RNToast = (Platform.OS === 'android' ? AndroidRNToast : IOSRNToast);
let RNDocScanner = (Platform.OS === 'android' ? AndroidRNDocScanner : IOSRNToast);
export {RNToast, RNDocScanner};
