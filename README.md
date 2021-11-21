# Satellite Tracker

### Satellite tracking simulation app for Android. Developed with Kotlin.
In the application, there are two screens, the screens where you list the satellites and see the satellite details.

Developed by applying the **MVI architecture**. Thus, as can be seen in the picture below, a circular flow was achieved.

### Libraries used:
- [Koin](https://insert-koin.io/) for dependency injection
- [Ktor](https://ktor.io/) for REST api
- [SQLDelight](https://cashapp.github.io/sqldelight/) for database
- [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html), [Flow](https://kotlinlang.org/docs/flow.html) for asynchronous operations
- [Navigation Component](https://developer.android.com/guide/navigation/navigation-getting-started) for app navigation
- [Pull-To-Refresh](https://developer.android.com/jetpack/androidx/releases/swiperefreshlayout) to make a request by scrolling down the list


###  Introduction
The general approach is to fetch data from the database if there is data in the database, otherwise request it from the server. If you want to get up-to-date data from the server, force reload can be done with **pull to refresh**. Likewise, on the detail screen, if there is data in the database, it is retrieved from there, otherwise it is requested from the server and the database is updated.



### Server Side
Developed a simple service with express on server side to simulate data. You can read the instructions below to use the service API.

**Get satellites**
> Endpoint: */satellite/list*
> Example: *http://46.101.172.63:3000/satellite/list*

**Get a particular satellite detail**
> Endpoint: */satellite/:satelliteId*
> Example: *http://46.101.172.63:3000/satellite/1*

**Update satellite name and status**
> Endpoint: */satellite/:satelliteId/edit?name=:name&active=:active*
> Example: *http://46.101.172.63:3000/satellite/1/edit?name=Satellite-1&active=true*

**Get a particular satellite positions**
> Endpoint: */satellite/:satelliteId/position/list*
> Example: *http://46.101.172.63:3000/satellite/:1/position/list*

**Add position for a specific satellite**
> Endpoint: */satellite/:satelliteId/position/add?posx=:posX&posy=:posY*
> Example: *http://46.101.172.63:3000/satellite/1/position/add?posx=0.83453&posy=0.90988*


![mvi](https://s3.ap-south-1.amazonaws.com/mindorks-server-uploads/mvi_cyclic-49d9f8c2d3fe26b7.png)

