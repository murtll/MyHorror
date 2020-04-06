package com.gdx.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2

class Gate (private val gameScreen: GameScreen, x: Float, y: Float) {
    private val textures = TextureRegion(Texture("earth/gates2.png")).split(64, 64)[0]
    private var texture = textures[1]
    private val wall = textures[0]

    private val position = Vector2(x, y)
    private var active = false

    fun activate() {
        active = true
    }


    fun update(deltaTime: Float) {
        /**/
            if (active) {
                texture = textures[2]
                if (gameScreen.hero.position.x in position.x - 20..position.x + 32 && gameScreen.hero.position.y in position.y - 20..position.y + 32) {
                    gameScreen.loadNewLevel()
                }
            }
    }

    fun render(batch: SpriteBatch) {
        batch.draw(texture, position.x - texture.regionWidth / 2, position.y - texture.regionHeight / 2)
        batch.draw(wall, position.x - texture.regionWidth / 2 - 32, position.y - texture.regionHeight / 2)
        batch.draw(wall, position.x - texture.regionWidth / 2 - 32, position.y - texture.regionHeight / 2)
        batch.draw(wall, position.x - texture.regionWidth / 2 - 32, position.y - texture.regionHeight / 2 + 32)
        batch.draw(wall, position.x - texture.regionWidth / 2, position.y - texture.regionHeight / 2 + 32)
        batch.draw(wall, position.x - texture.regionWidth / 2 + 32, position.y - texture.regionHeight / 2 + 32)
        batch.draw(wall, position.x - texture.regionWidth / 2 + 32, position.y - texture.regionHeight / 2)
    }
}