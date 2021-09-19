# WeatherApp

Weather app get open api from https://openweathermap.org/

## Summary
1. This app use [MVVM Pattern](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel) which has been recommended by google on [component architecture](https://developer.android.com/jetpack/guide?gclid=Cj0KCQjwv5uKBhD6ARIsAGv9a-zdJX3_u4zRglZeRo_N9JHx-E6QxVt3jX0qZC3kE_7Cio6jX5jGmd4aAj9MEALw_wcB&gclsrc=aw.ds)
2. Conccurency with [kotlin coroutines](https://kotlinlang.org/docs/coroutines-overview.html) and Feature coroutines [Kotlin Flow](https://kotlinlang.org/docs/flow.html) for return multiple asynchronously computed values
3. Use [Google Map API](https://developers.google.com/maps/documentation/?_ga=2.171120864.1997251329.1632068810-1553905324.1630597270) for get weather by location
4. Use Feature [view model](https://developer.android.com/topic/libraries/architecture/viewmodel?hl=id), [view binding](https://developer.android.com/topic/libraries/view-binding) and [live data](https://developer.android.com/topic/libraries/architecture/livedata?hl=id)

## Other Dependency and Android Jetpack:
* [Room](https://developer.android.com/training/data-storage/room) the abstraction database local SQLite
* [Data store](https://developer.android.com/topic/libraries/architecture/datastore?hl=id) for storing key-value pairs or objects
* [Dagger2](https://dagger.dev/) dependency injection framework

