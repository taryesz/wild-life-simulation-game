
# Wild Life Simulation Game

This project is a simple console game that simulates wild life.

## Usage

Firstly, type the board size when prompted.

Then, the game will inform you what organisms (and how many of them) were spawned and where.

Next, you will see the game board with all the organisms on it. Each organism has its own ASCII representation:

### Animals

> w `Wolf`

> s `Sheep`

> f `Fox`

> t `Turtle`

> a `Antelope`

> c `Cyber Sheep`

### Plants

> G `Grass`

> S `Sow Thistles`

> U `Guarana`

> B `Belladonna`

> H `Heracleum`

Next, the game will prompt you to type either `q` (to quit) or `n` (to perform next game iteration).

If you provide an invalid input, the game will ask you to try again.

By typing `n`, all animals make a move sequentially. The order depends on each animal's `initiative` property - those with the highest one make their move as the first ones, then those with the lower `initiative` property, and so on.

The game then checks for collisions. If any detected, the game checks both organisms types: if the organisms are of the same type, they multiply. However, if they are of different types, they start fighting.

An organism with a higher `power` property wins and eliminates its colliding organism. If the organisms have the same `power` property, the one that moved most recently wins.

There are exceptions, though. Some organisms have their special abilities:

### Animals

> The Fox never moves to a dangerous spot where there is an organism with a higher "power" property.

> The Turtle repels any attacks of those organisms whose "power" property is less than 5. It also moves rarely.

> The Antelope has a survival chance of 50% if being attacked by a stronger organism. It also can move farther.

> The Cyber Sheep has one goal: eliminate every single Heracleum plant in the game. It always moves towards it. If there are no Heracleum plants, it behaves like a normal Sheep (although it has one little secret...).

### Plants

> The Sow Thistles has a chance of multiplying itself.

> The Guarana increases any organism's "power" times 3 if eaten by this organism.

> The Belladonna kills every organism regardless of its "power" property.

> The Heracleum kills every organism, except for the Cyber Sheep. It also kills organisms that are nearby.

Finally, the game will prompt you again to type either `q` (to quit) or `n` (to perform next game iteration). The flow described above repeats if `n` is typed. 

Have fun!