package com.gdx.game

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.gdx.game.сharacters.*

class MonstersEmitter (val gameScreen: GameScreen) {

    val monsters = mutableListOf<GameCharacter>().apply {
        addAll(List(10){ FireSkull(gameScreen) })
        addAll(List(20){ Ghost(gameScreen) })
        addAll(List(20){ Skeleton(gameScreen) })
        add(BigDemon(gameScreen))
    }
    fun render(batch: SpriteBatch) {
        monsters.forEach {
            if (it.isActive()) {
                it.render(batch)
            }
        }
    }

    fun generateMonster(x: Float, y: Float, type: GameCharacter.Race) {
        val position = Vector2(x, y)

        for (i in monsters.indices) {
            val it = monsters[i]
            if (!(it.isActive()) && it.race == type) {
                it.activate()
                break
            }
        }
    }

    fun update(deltaTime: Float) {
        monsters.sortBy { -it.position.y }

        for (i in 0 until monsters.size) {
            val it = monsters[i]
            if (it.isActive()) it.update(deltaTime)
            if (!(it.isAlive()) && it.isActive()) {
                it.deathSounds.random().play(0.7F)
                gameScreen.itemsEmitter.generateRandItem(it.position.x, it.position.y, it.chance)
//                gameScreen.hero.killEnemy(it)
                it.deactivate()
            }
        }
    }

}
