# Brändi Dog

## Introduction

The aim of the project was to get a running digital version of the game Dog. Furthermore we wanted to include some features that are only possible in an online game. Therfeore we implemented Robo players, which can play on their own. This makes it possible for friends to play, even if they are less than four players. We also wanted some extra features that can only be possible in a digital game, so we included a feature that changes the fields on the board, depending on the weather in various cities.<br>
In addition, the goal was to also create a game, that is visually appealing and easy to play.

## Technologies

- For the writing of the code an the debugging IntelliJ
- For the deployment of the server Heroku was used
- For the version control and the exchanging of code Github was used
- For the planning of the tasks Jira was used
- For the communication, MS Teams was mostly used
- Sonarcloud

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





## Road Map

### Better AI
One feature that is already in the project, but needs to be improved is the Robo Player logic. The Robos can play, but there playstyle is very simple. There is a lot space for improvement in how the Robos choose their figures and cards. One more complex logic for the Robos is also the exchange of cards. Which card should they exchange and how should they play, depending on the card that they have played.

### Additional weather fields
Another feature would also be the extension of the weatherfield logic. There is a lot of room to implement new weatherstates and therefore also new weatherfields with different behaviour. For example a weatherfield when there is thunder at a location. Furthermore also the distribution of the weatherfield can be improved, so that they are not always at the same place, but can occure randomly on the field.

### Additonal boards for 6 or 8 people
One more interesting feature would be a new board for more players. So if there are more than 4 people that want to play together, they can choose a bigger board. The board shouldn't include more than 8 people, because otherwise the waiting time is too long for a single player. 

## Authors and acknowledgement

**Oliver Kamer** - *Backend and Frontend* [olikami](https://github.com/orgs/group-08/people/olikami)
**Felix Hoffmann** - *Backend and Frontend* [Felixuss](https://github.com/orgs/group-08/people/Felixuss)
**Nick Kipfer** - *Backend and Frontend* [GiantDwarf42](https://github.com/orgs/group-08/people/GiantDwarf42)
**Philip Flury** - *Backend and Frontend* [TheRobihno70](https://github.com/orgs/group-08/people/TheRobihno70)
**Flurin Knellwolf** - *Backend and Frontend* [saegge123](https://github.com/orgs/group-08/people/saegge123)
**Alex Scheitlin** - *Support and Advice* [alexscheitlin](https://github.com/orgs/group-08/people/alexscheitlin)

We would especially like to thank Alex, for his support and good advice during our whole project time!



