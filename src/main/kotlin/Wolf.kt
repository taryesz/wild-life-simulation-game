class Wolf(game: Game) : Animal() {

    init {
        this.icon = "w"
        this.game = game
        this.setInitialPosition()
    }

}