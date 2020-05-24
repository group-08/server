[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=group-08_server&metric=alert_status)](https://sonarcloud.io/dashboard?id=group-08_server)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=group-08_server&metric=bugs)](https://sonarcloud.io/dashboard?id=group-08_server)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=group-08_server&metric=coverage)](https://sonarcloud.io/dashboard?id=group-08_server)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=group-08_server&metric=security_rating)](https://sonarcloud.io/dashboard?id=group-08_server)


# Dog

## Introduction

The aim of the project was to get a running digital version of the game Dog. Furthermore we wanted to include some features that are only possible in an online game. Therfore we implemented Robo players (bots), which can play on their own. This makes it possible for friends to play, even if they are less than four players. We also wanted some extra features that can only be possible in a digital game, so we included a feature that changes the fields on the board, depending on the weather in various cities.<br>
In addition, the goal was to also create a game, that is visually appealing and fun to play.

## Technologies

- IntelliJ: For the writing of the code an the debugging
- WebStorm: For JavaScript
- Heroku: For the deployment of the server
- GitHub: For the version control and the exchanging of code
- Jira: For the planning of the tasks
- Ms Teams: For the communication
- Sonarcloud: For the testing of our project and the security checks

## High Level Components

### GameService

https://github.com/group-08/server/blob/master/src/main/java/ch/uzh/ifi/seal/soprafs20/service/GameService.java
The most important component in the server part is the GameService. It is responsible for the gameflow and the redirecting to other classes

### BoardService

https://github.com/group-08/server/blob/master/src/main/java/ch/uzh/ifi/seal/soprafs20/service/BoardService.java
The board service is responsible for the movement over the board and also for the calculation of the possible fields. The graph function which connects the fields is also in the boardservice. The graph is responsible for the correct calculation of the possible fields.

### GameController

https://github.com/group-08/server/blob/master/src/main/java/ch/uzh/ifi/seal/soprafs20/controller/LobbyController.java
The GameController handles all the calls that are received from the frontend, and redirects them. This class is the one that is active while the game is actually played.

### Game

https://github.com/group-08/server/blob/master/src/main/java/ch/uzh/ifi/seal/soprafs20/game/Game.java
The Game class is an entity in the database. It includes the main information about the game and is the most complex entity in the database, because it is connected to a lot of other classes.

## Launch & Deployment:

Download your IDE of choice: (e.g., [Eclipse](http://www.eclipse.org/downloads/), [IntelliJ](https://www.jetbrains.com/idea/download/)) and make sure Java 13 is installed on your system. **Important:** A smooth run of the application is only guaranteed on the browser GoogleChrome.

1. File -> Open... -> SoPra Server Template
2. Accept to import the project as a `gradle project`

To build right click the `build.gradle` file and choose `Run Build`

### Building with Gradle

You can use the local Gradle Wrapper to build the application.

Plattform-Prefix:

-   MAC OS X: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

More Information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/).

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```

### Test

```bash
./gradlew test
```

### Development Mode

You can start the backend in development mode, this will automatically trigger a new build and reload the application
once the content of a file has been changed and you save the file.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`



## Road Map

### AI
One feature that is already in the project, but needs to be improved is the Robo Player logic. The Robos can play, but there playstyle is essentially random. There is a lot space for improvement in how the Robos choose their figures and cards. One more complex logic for the Robos is also the exchange of cards. Which card should they exchange and how should they play, depending on the card that they have played.

### Additional weather fields
Another feature would also be the extension of the weatherfield logic. There is a lot of room to implement new weatherstates and therefore also new weatherfields with different behaviour. For example a weatherfield when there is thunder at a location. Furthermore also the distribution of the weatherfield can be improved, so that they are not always at the same place, but can occure randomly on the field.

### Additonal game modes for 6 or 8 people
One more interesting feature would be a new board for more players. So if there are more than 4 people that want to play together, they can choose a bigger board. The board shouldn't include more than 8 people, because otherwise the waiting time is too long for a single player. 

## Authors and acknowledgement

**Oliver Kamer** - *Team Lead and Back-/Frontend* - [olikami](https://github.com/orgs/group-08/people/olikami)<br>
**Felix Hoffmann** - *Frontend* - [Felixuss](https://github.com/orgs/group-08/people/Felixuss)<br>
**Nick Kipfer** - *Backend* - [GiantDwarf42](https://github.com/orgs/group-08/people/GiantDwarf42)<br>
**Philip Flury** - *Backend and Frontend* - [TheRobihno70](https://github.com/orgs/group-08/people/TheRobihno70)<br>
**Flurin Knellwolf** - *Backend* - [saegge123](https://github.com/orgs/group-08/people/saegge123)<br>
**Alex Scheitlin** - *Support and Advice* - [alexscheitlin](https://github.com/orgs/group-08/people/alexscheitlin)<br>

We would especially like to thank Alex, for his support and good advice during our whole project time!

## License

GNU General Public License v3.0 Full License

