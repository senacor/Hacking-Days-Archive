/**
 * Created with IntelliJ IDEA.
 * User: bbathow
 * Date: 16.06.12
 * Time: 11:58
 * To change this template use File | Settings | File Templates.
 */
// If you want to prevent dragging, uncomment this section
/*
 function preventBehavior(e)
 {
 e.preventDefault();
 };
 document.addEventListener("touchmove", preventBehavior, false);
 */

/* If you are supporting your own protocol, the var invokeString will contain any arguments to the app launch.
 see http://iphonedevelopertips.com/cocoa/launching-your-own-application-via-a-custom-url-scheme.html
 for more details -jm */
/*
 function handleOpenURL(url)
 {
 // TODO: do something with the url passed in.
 }

 */
function onBodyLoad()
{
  document.addEventListener("deviceready", onDeviceReady, false);
}


/* When this function is called, Cordova has been initialized and is ready to roll */
/* If you are supporting your own protocol, the var invokeString will contain any arguments to the app launch.
 see http://iphonedevelopertips.com/cocoa/launching-your-own-application-via-a-custom-url-scheme.html
 for more details -jm */
function onDeviceReady()
{
  // do your thing!
  // navigator.notification.alert("Cordova is working")

  var element = document.getElementById('devicePropertiesSuccess');

  element.innerHTML = 'Device Name: '     + device.name     + '<br />' +
      'Device PhoneGap: ' + device.phonegap + '<br />' +
      'Device Platform: ' + device.platform + '<br />' +
      'Device UUID: '     + device.uuid     + '<br />' +
      'Device Version: '  + device.version  + '<br />';

  $('#devsucess').show();
  $('#devinfo').hide();
  $('#deverror').hide();
  $('#file_input').hide();
  $('#cmdCapturePhoto').show();
  $('#cmdCapturePhotoWithEdit').show();

  phonegapHackingdays = true;
  pictureSource=navigator.camera.PictureSourceType;
  destinationType=navigator.camera.DestinationType;

}

function pageRefresh() {
  location.reload()
}


// Called when a photo is successfully retrieved
//
function onPhotoDataSuccess(imageData) {
  // Uncomment to view the base64 encoded image data
  console.log(imageData);

  // Get image handle
  //
  var smallImage = document.getElementById('smallImage');
  var smallImageDiv = document.getElementById('smallImageDiv');

  // Unhide image elements
  //
  smallImage.style.display = 'block';
  smallImageDiv.style.display = 'block';

  // Show the captured photo
  // The inline CSS rules are used to resize the image
  //
  smallImage.src = "data:image/jpeg;base64," + imageData;

}

// Called when a photo is successfully retrieved
//
function onPhotoURISuccess(imageURI) {
  // Uncomment to view the image file URI
  // console.log(imageURI);

  // Get image handle
  //
  var largeImage = document.getElementById('largeImage');

  // Unhide image elements
  //
  largeImage.style.display = 'block';

  // Show the captured photo
  // The inline CSS rules are used to resize the image
  //
  largeImage.src = imageURI;
}

// A button will call this function
//
function capturePhoto() {
  // Take picture using device camera and retrieve image as base64-encoded string
  navigator.camera.getPicture(onPhotoDataSuccess, onFail, { quality: 50,
    destinationType: destinationType.DATA_URL });
}

// A button will call this function
//
function capturePhotoEdit() {
  // Take picture using device camera, allow edit, and retrieve image as base64-encoded string
  navigator.camera.getPicture(onPhotoDataSuccess, onFail, { quality: 20, allowEdit: true,
    destinationType: destinationType.DATA_URL });
}

// A button will call this function
//
function getPhoto(source) {
  // Retrieve image file location from specified source
  navigator.camera.getPicture(onPhotoURISuccess, onFail, { quality: 50,
    destinationType: destinationType.FILE_URI,
    sourceType: source });
}

// Called if something bad happens.
//
function onFail(message) {
  alert('Failed because: ' + message);
}
