package com.gdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2

class Spike (private val gameScreen: GameScreen) {
    private val textures = TextureRegion(Texture("earth/spike-animation.png")).split(21, 21)[0]
    private var texture = textures[0]
    private var timer = 0f
    private val secondsPerFrame = 0.2f
    private val sound = Gdx.audio.newSound(Gdx.files.internal("sounds/hits/sword3.mp3"))

    private val position = Vector2(0F, 0F)
    private var active = false
    var ready = false

    fun setup(newPosition: Vector2) {
        position.set(newPosition)
        ready = true
        timer = 0f
    }

    fun update(deltaTime: Float, map: Map) {
        if (gameScreen.hero.position.x in position.x..position.x + 32 && gameScreen.hero.position.y in position.y..position.y + 45 && ready) active = true

        if (active && gameScreen.hero.isAlive()) {
            if (timer == 0F) {
                sound.play(0.1F)
                gameScreen.hero.getDamage((3..4).random().toFloat(), 2..2)
            }
            timer += deltaTime
            val index = ((timer / secondsPerFrame) % textures.size).toInt()
            texture = textures[index]
        }
        if (timer >= 0.5) {
            active = false
            ready = false
            timer = 0f
            map.data[(position.x / 32).toInt()][(position.y / 32).toInt()] = '0'
        }
    }

    fun render(batch: SpriteBatch) {
        if (active && gameScreen.hero.isAlive()) batch.draw(texture, position.x + texture.regionWidth / 2  - 5, position.y + texture.regionHeight / 2)
    }

    fun deactivate() {
        active = false
        ready = false
    }
}