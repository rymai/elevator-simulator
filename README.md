# elevator-simulator

This is a Java project I made in 2008 during my _Computer Science Applied to Business Management_ Master at the University of Nice Sophia-Antipolis.

The goal was to create several elevator strategies that could be hot-loaded in this Swing application and run simulation with these different behaviors.

## Usage

Compile the project to `<jar_file>.jar` and run it with `java -jar <jar_file>.jar`.

Note: Be sure to put the elevator strategies `.class` inside the `plugins/strategies/elevators/` folder (see "Elevator strategies" below).

## Strategies

### Elevator strategies

There is a collection of elevator strategies in `src/strategies/elevators/` that can be hot-loaded as plugins.

To have access to these plugins at runtime, compile the `src/strategies/elevators/*.java` files and move the `.class` files into `plugins/strategies/elevators/` (you can reload the plugin list with the _Actions / Recharger les comportements_ menu item when the applicaiton is launched).


## Développement de plugins

### Méthodes à implémenter

Pour réaliser de nouveaux plugins, il faut ouvrir le projet “elevator-plugins”, dupliquer un des plugins existants dans le package src/strategies/elevators, puis modifier les méthodes qui le compose.

#### `public abstract void acts()`

 Cette méthode est le coeur du fonctionnement d’une stratégie d’ascenseur. C’est dans cette méthode que
tous les tests pour savoir si l’ascenseur doit s’arrêter, repartir etc ... se font.

#### `public boolean takePassenger(Passenger passenger)`

Cette méthode est appelé seulement si le passager `passenger` vient de monter dans l’ascenseur. Vous pouvez par exemple augmentez le temps d’arrêt nécesssaire dans cette méthode grâce à `elevator.incrementStopTime(5*passenger.getPersonCount());`.

#### `public void releasePassenger(Passenger passenger)

 Cette méthode est appelé seulement si le passager `passenger` vient de sortir de l’ascenseur. Vous pouvez par exemple augmentez le temps d’arrêt nécesssaire dans cette méthode grâce à `elevator.incrementStopTime(5*passenger.getPersonCount());`.

#### `public void leaveThisFloor()`

 Cette méthode est appelé à la fin de la méthode `leaveThisFloor()` de Elevator. Cette méthode est appelé
pour indiquer à l’ascenseur graphique de repartir.

### Quelques conseils pour le développement

Il est conseiller de s’inspirer des comportements déjà implémenté pour créer de nouveaux comportements.

**Quelques méthodes intéressantes de `Elevator` :**

- La méthode setMoving(false) permet de dire à l’ascenseur de rester immobile.
- La méthode releaseAllArrivedPassengers() permet de laisser sortir tous les passagers de l’ascenseur qui sont arrivés à leur étage de destination.
- La méthode leaveThisFloor() permet de notifier l’ascenseur graphique que l’ascenseur veut repartir. Programmation Avancée - 

Il existe beaucoup d’autres méthodes, pour récupérer des listes de passagers qui attendant, qui sont arrivés, des listes d’étages avec des passagers qui attendant... Voir la Javadoc pour plus de précisions
Importation d’un nouveau comportement
Un fois le comportement terminé, compilez-le, puis placer le `.class` dans le dossier `plugins/strategies/` elevators du projet principal. Le comportement apparaît dans le menu “Choisir un comportement” après un clic sur le choix “Recharger les comportements” dans le menu “Actions” de la fenêtre de configuration de l’application (ou après un redémarrage de l’application bien sûr).

## Limitations et réalisations du projet par rapport au sujet

Nous avons répondu à toutes les demandes obligatoires. En voici la liste :

- Pouvoir facilement paramétrer une simulation. - Pouvoir facilement lancer / relancer une simulation. - Pouvoir intégrer de nouveaux programmes d’ascenseurs “à la volée” (plug-in)
- Pouvoir connaître le temps d’attente des personnes (moyenne par ascenseur et totale)
- Gérer les groupes de personnes qui ne veulent pas se séparer - Gérer l’humeur des personnes - Accélérer ou ralentir la simulation (min. 1 image/seconde, max. 50 images/seconde)

Nous avons également développé d’autres améliorations :

- Pouvoir connaître le temps de voyage des personnes (moyenne par ascenseur et totale).
- Gestion de la charge maximale d’un ascenseur (au delà du poinds maximum, l’ascenseur est bloqué).
- Gestion d’un poids d’alerte (les passagers sont avertis que l’ascenseur risque d’être bloqué si il essaye de rentrer dedans).
- Gestion d’une contenance physique maximum pour chaque ascenseur : un ascenseur ne peut pas accepter plus de personnes que son maximum physique autorisé.
- Gestion du QI des personnes : possibilités d’effectuer différentes actions suivant le QI. Cet attribut pour- rait être massivement exploité par l’utilisation du design-pattern Strategy pour créer différents compor- tement de passagers.
- Gestion du sexe des personnes : Même remarque que pour le QI.