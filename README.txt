Hello!

You should insert your Api Key in the app build.grade, under defaultConfig, inside
this line:

buildConfigField("String", "MyMovieDbApiKey", "\"INSERT API KEY HERE\"")

Regards!