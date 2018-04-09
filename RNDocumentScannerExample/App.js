import React, { Component } from 'react';
import { Image, Text, Button, StyleSheet, View, NativeModules } from 'react-native';
import {RNDocScanner} from 'rn-doc-scanner';

export default class App extends Component {
  state = {
    imageContentUri: ''
  }
  async onButtonPressAuto() {
    console.log(RNDocScanner)
    try {
      let res = await RNDocScanner.getDocumentCrop(false);
      console.log('pressauto', res);
      this.setState({imageContentUri: res});
    } catch(e) {
      console.log(e);
    }
  };

  async onButtonPressManual() {
    try {
      let res = await RNDocScanner.getDocumentCrop(true);
      console.log('pressmanual', res);
      this.setState({imageContentUri: res});
    } catch(e) {
      console.log(e);
    }
  };

  render() {
    return (
      <View style={styles.container}>
      <Image
        style={{
          width: 200,
          height: 200,
          resizeMode: Image.resizeMode.contain,
        }}
        source={{
          uri: this.state.imageContentUri
        }}
      />
        <Button title='Autocrop' onPress={this.onButtonPressAuto.bind(this)} />
        <Button title='Manual' onPress={this.onButtonPressManual.bind(this)} />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});
