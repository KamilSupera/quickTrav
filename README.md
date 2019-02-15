# quickTrav

## Firebase config

### Generating google-service.json
To generate `google-service.json` go to firebase console `https://console.firebase.google.com/u/0/`.
Find your project and click on it or create new one. Then go to project settings and if you have created app download
`google-service.json`. If you don't create new firebase android app and continue with instructions
you see there to obtain this file. When you have config file move it to `app` folder in your application
module.


### Generating Firebase Admin SDK file
To generate Admin SDK config file go to: `https://console.firebase.google.com/u/0/project/_/settings/serviceaccounts/adminsdk`
and click on your project. Then generate this file with instructions from Google.
