package com.gdx.game.сharacters

import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.gdx.game.GameScreen
import com.gdx.game.Item
import com.gdx.game.Weapon

abstract class GameCharacter(private val gameScreen: GameScreen) {

    enum class Race (val index: Int) {
        SKELETON(31), GHOST(11), FIRE_SKULL(0), BIG_DEMON(51)
    }

//    private val hpBar = Texture("all/hero/hp-bar.png")
    open var active = false
    open var damageEffectTimer = 0f
    open var attackTimer = 0.0f
    open var direction = Vector2(0F, 0F)
    open var chance = 1
    open val eyeSight = 200.0f
    open var directionWas = Vector2(direction)
    abstract var race: Race
    abstract var texture: TextureRegion
    open var position = randomPosition()
    abstract var speed: Float
    abstract var maxHP: Float
    abstract var healthPoints: Float
    abstract var weapon: Weapon
    abstract val deathSounds: Set<Sound>
    abstract val attackSounds: Set<Sound>
    abstract val hurtSounds: Set<Sound>

    open var gameTimer = 0F
    open val secondsPerFrame = 0.15F

//    private val hpBuilder = StringBuilder("")

    abstract fun update(deltaTime: Float)

    open fun render(batch: SpriteBatch) {
        if (damageEffectTimer > 0) batch.color = Color(1f, 0 - damageEffectTimer, 0 - damageEffectTimer, 1.0f)

        batch.draw(texture, position.x - texture.regionWidth / 2, position.y - texture.regionHeight / 2)
//        HP bar drawing (optional)
        /*        batch.color = Color(0.1f, 0.1f, 0.0f, 1.0f)
        batch.draw(hpBar, position.x - texture.regionWidth / 2 - 1, position.y + 60 - texture.regionHeight / 2 - 1, 37F, 10F)
        batch.color = Color(0.5f, 0.0f, 0.0f, 1.0f)
        batch.draw(hpBar, position.x - texture.regionWidth / 2, position.y + 60 - texture.regionHeight / 2, 0F, 0F, (healthPoints * 35) / maxHP, 8F, 1F, 1F, 0F, 0, 0, 35, 8, false, false)
        hpBuilder.run {
            setLength(0)
            append(healthPoints.toInt())
        }

        fontC20.draw(batch, hpBuilder, position.x - texture.regionWidth / 2, position.y + 75 - texture.regionHeight / 2, 35F, 1, false)*/
        batch.color = Color(1.0f, 1.0f, 1.0f, 1.0f)

    }

    fun getDamage(amount: Float, counter: IntRange) {
        healthPoints -= amount

        damageEffectTimer += 0.3f
        if (damageEffectTimer > 1) damageEffectTimer = 1f

        if (counter.random() == 2)hurtSounds.random().play(1F)

//        Play hurt animation
    }

    fun isAlive() = healthPoints > 0

    private fun randomPosition(): Vector2 {
        var firstPosition = Vector2(MathUtils.random(100F, 700F), MathUtils.random(100F, 440F))

        while (!gameScreen.map.isReachable(firstPosition)) {
            firstPosition = Vector2(MathUtils.random(100F, 700F), MathUtils.random(100F, 440F))
        }
        return firstPosition
    }

    fun TextureRegion.flipped(x: Boolean = true, y: Boolean = false): TextureRegion {
        val tmpRegion = TextureRegion(this)
        tmpRegion.flip(x, y)
        return tmpRegion
    }

    fun move(deltaTime: Float) {
        val tmp = Vector2(position)
        if (gameScreen.map.isReachable(tmp.set(position).mulAdd(direction, speed * deltaTime))) {
            position.set(tmp)
        } else if (gameScreen.map.isReachable(tmp.set(position).mulAdd(direction, speed * deltaTime).set(tmp.x, position.y))) {
            position.set(tmp.x, position.y)
        } else if (gameScreen.map.isReachable(tmp.set(position).mulAdd(direction, speed * deltaTime).set(position.x, tmp.y))) {
            position.set(position.x, tmp.y)
        } else if (gameScreen.map.isReachable(tmp.set(position).mulAdd(direction, speed * deltaTime).set(position.x, -tmp.y))) {
            position.set(position.x, -tmp.y)
        } else if (gameScreen.map.isReachable(tmp.set(position).mulAdd(direction, speed * deltaTime).set(-tmp.x, position.y))) {
            position.set(-tmp.x, position.y)
        }
    }

    fun isActive() = active

    open fun deactivate() {
        active = false
    }

    fun activate(newPosition: Vector2 = randomPosition()) {
        position.set(newPosition)
        healthPoints = maxHP
        active = true
    }
}