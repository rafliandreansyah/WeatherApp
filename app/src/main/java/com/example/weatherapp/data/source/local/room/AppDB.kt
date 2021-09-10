package com.example.weatherapp.data.source.local.room

//@Database(entities =[City::class, Current::class, Daily::class, Weather::class] , version = 1, exportSchema = false)
//abstract class AppDB: RoomDatabase() {
//
//    abstract fun weatherDao(): WeatherDao
//
//    companion object{
//        @Volatile
//        private var instance: AppDB? = null
//
//        fun getInstance(context: Context): AppDB =
//            instance ?: synchronized(this) {
//                instance ?: Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDB::class.java, "weather.db"
//                ).build().apply { instance = this }
//            }
//    }
//
//}