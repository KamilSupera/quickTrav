# quickTrav

## Firebase config

### Generating google-service.json
To generate `google-service.json` go to firebase console `https://console.firebase.google.com/u/0/`.
Find your project and click on it or create new one. Then go to project settings and if you have created app download
`google-service.json`. If you don't create new firebase android app and continue with instructions
you see there to obtain this file. When you have config file move it to `app` folder in your application
module.

## Google Map key
To make Google Map work you have to obtain API key from Google and 
insert it into gradle.properties file in `~/.gradle` directory like:
```
QuickTravel_MapKey="Your_key"
```

