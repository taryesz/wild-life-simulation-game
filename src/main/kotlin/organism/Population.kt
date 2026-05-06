package organism

import game.Game
import organism.animal.Antelope
import organism.animal.CyberSheep
import organism.animal.Fox
import organism.animal.Sheep
import organism.animal.Turtle
import organism.animal.Wolf
import organism.plant.Belladonna
import organism.plant.Grass
import organism.plant.Guarana
import organism.plant.Heracleum
import organism.plant.SowThistles
import kotlin.random.Random
import kotlin.reflect.KClass

class Population(private val game: Game) {

    private val organisms: MutableList<Organism> = mutableListOf()
    private val organismsToAdd: MutableList<Organism> = mutableListOf()
    private var organismsToRemove: MutableList<Organism> = mutableListOf()
    private var collidingOrganisms: MutableList<Organism> = mutableListOf()

    private companion object {
        const val MAX_SPECIFIC_ORGANISM_COUNT = 4
        const val MIN_SPECIFIC_ORGANISM_COUNT = 2
    }

    fun spawn() {

        for (organismType in Organisms.entries) {

            // Generate a random number of each organism.
            repeat(Random.nextInt(MIN_SPECIFIC_ORGANISM_COUNT, MAX_SPECIFIC_ORGANISM_COUNT)) {
                when (organismType) {

                    // Animals
                    Organisms.WOLF -> this.organisms.add(Wolf(this.game))
                    Organisms.SHEEP -> this.organisms.add(Sheep(this.game))
                    Organisms.FOX -> this.organisms.add(Fox(this.game))
                    Organisms.TURTLE -> this.organisms.add(Turtle(this.game))
                    Organisms.ANTELOPE -> this.organisms.add(Antelope(this.game))
                    Organisms.CYBER_SHEEP -> this.organisms.add(CyberSheep(this.game))

                    // Plants
                    Organisms.GRASS -> this.organisms.add(Grass(this.game))
                    Organisms.SOW_THISTLES -> this.organisms.add(SowThistles(this.game))
                    Organisms.GUARANA -> this.organisms.add(Guarana(this.game))
                    Organisms.BELLADONNA -> this.organisms.add(Belladonna(this.game))
                    Organisms.HERACLEUM -> this.organisms.add(Heracleum(this.game))

                }
            }

        }

    }

    fun add(organism: Organism) {
        this.organismsToAdd.add(organism)
    }

    fun addCollisionRecord(organism: Organism) {
        this.collidingOrganisms.add(organism)
    }

    fun remove(organism: Organism) {
        this.organismsToRemove.add(organism)
    }

    fun live() {
        this.organisms.sortDescending()           // The first organisms to make a move are the ones with the highest "initiative" property - sort accordingly.
        this.organisms.forEach { it.act() }       // Each organism moves (and optionally does something else).
        this.organisms.forEach { it.collide() }   // The game checks the collisions among the organisms and solves them.
        this.update()                             // Actually change the organisms' properties after each iteration.
    }

    private fun update() {

        this.organisms.addAll(this.organismsToAdd)           // Add every new "baby organism".
        this.organismsToAdd.clear()
        this.organisms.removeAll(this.organismsToRemove)     // Remove every killed organism.
        this.organismsToRemove.clear()

        // This map stores the collision data for each organism - we need to clear it after each iteration.
        this.collidingOrganisms.clear()

    }

    fun find(x: Int, y: Int): Organism? {
        return this.organisms.find { it.coordinates.x == x && it.coordinates.y == y }
    }

    fun containsCollisionRecord(organism: Organism): Organism? {
        return this.collidingOrganisms.find { it == organism }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Organism> getAllOrganisms(type: KClass<T>): List<T> {
        return this.organisms.filter { type.isInstance(it) } as List<T>
    }

}