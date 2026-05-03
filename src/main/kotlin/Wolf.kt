class Wolf(game: Game) : Animal() {

    init {
        this.power = 9
        this.initiative = 5
        this.game = game
        this.spawn()
        this.icon = "w"
    }

    override fun reproduce(): Wolf {
        return Wolf(this.game!!)
    }

}