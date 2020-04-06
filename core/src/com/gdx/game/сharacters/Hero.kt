package com.gdx.game.сharacters

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.gdx.game.GameScreen
import com.gdx.game.Item
import com.gdx.game.Weapon

class Hero(private val gameScreen: GameScreen) : GameCharacter(gameScreen) {
    private val fontC20 = BitmapFont(Gdx.files.internal("fonts/fontC20.fnt"))


    override var race = Race.BIG_DEMON
    private var textureIdle = TextureRegion(Texture("all/hero/gothic-hero-idle.png")).split(38, 48).first()
    private var textureAttack = TextureRegion(Texture("all/hero/gothic-hero-attack.png")).split(96, 48).first()
    private var textureRun = TextureRegion(Texture("all/hero/gothic-hero-run.png")).split(66, 48).first()

    private val coinTexture = TextureRegion(Texture("all/loot/items.png")).split(33, 28)[0][0]
    private val healTexture = TextureRegion(Texture("all/loot/items.png")).split(33, 28)[0][3]

    override var active = true
    override var texture = textureIdle.first()
    override var position = Vector2(100F, 100F)
    override var speed = 80.0f
    override var maxHP = 100.0f
    override var healthPoints = maxHP
    override var weapon = Weapon.SWORD
    override val attackSounds = setOf(Gdx.audio.newSound(Gdx.files.internal("sounds/hits/sword1.mp3")),
            Gdx.audio.newSound(Gdx.files.internal("sounds/hits/sword2.wav")),
            Gdx.audio.newSound(Gdx.files.internal("sounds/hits/sword3.mp3")))
    override val deathSounds = setOf(Gdx.audio.newSound(Gdx.files.internal("sounds/voices/humanDeath1.wav")),
            Gdx.audio.newSound(Gdx.files.internal("sounds/voices/humanDeath2.wav")))
    override val hurtSounds = setOf(Gdx.audio.newSound(Gdx.files.internal("sounds/voices/humanYell5.wav")),
            Gdx.audio.newSound(Gdx.files.internal("sounds/voices/humanYell1.wav")))
    override val secondsPerFrame = 0.09F

    private var coins = 0

    override fun update(deltaTime: Float) {

        textureRun = weapon.textureRun
        textureIdle = weapon.textureIdle
        textureAttack = weapon.textureAttack

        gameTimer += deltaTime

        damageEffectTimer -= deltaTime
        if (damageEffectTimer < 0) damageEffectTimer = 0f

        var minDistance = Float.MAX_VALUE
        var nearestMonster: GameCharacter? = null

        gameScreen.monstersEmitter.monsters.filter { it !is Hero && it.isActive()}.forEach {
            val distance = it.position.dst(position)
            if (distance < minDistance) {
                minDistance = distance
                nearestMonster = it
            }
        }
        if (nearestMonster != null && minDistance < weapon.attackRadius) {
            attackTimer += deltaTime
            if (attackTimer >= weapon.attackPeriod) {
                attackTimer = 0F
                nearestMonster!!.getDamage(weapon.damage, 0..3)
                attackSounds.random().play(2f)
//                if ((0..8).random() == 4) nearestMonster!!.hurtSounds.random().play(1f)
            }
            if (nearestMonster!!.position.x > position.x) {
                val index = ((gameTimer / secondsPerFrame) % textureAttack.size).toInt()
                texture = textureAttack[index]
            } else {
                val index = ((gameTimer / secondsPerFrame) % textureAttack.size).toInt()
                texture = textureAttack[index].flipped()
            }
        }


        if (!(Gdx.input.isKeyPressed(Input.Keys.W) ||
                        Gdx.input.isKeyPressed(Input.Keys.A) ||
                        Gdx.input.isKeyPressed(Input.Keys.S) ||
                        Gdx.input.isKeyPressed(Input.Keys.D) ||
                        minDistance < weapon.attackRadius)) {
            if (directionWas.x >= 0) {
                val index = ((gameTimer / secondsPerFrame) % textureIdle.size).toInt()
                texture = textureIdle[index]
            } else {
                val index = ((gameTimer / secondsPerFrame) % textureIdle.size).toInt()
                texture = textureIdle[index].flipped()
            }
        }

        direction.set(0F, 0F)

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            direction.y = 1F
            directionWas.y = 1F

            val index = ((gameTimer / secondsPerFrame) % textureRun.size).toInt()
            texture = textureRun[index]
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            direction.x = 1F
            directionWas.x = 1F

            val index = ((gameTimer / secondsPerFrame) % textureRun.size).toInt()
            texture = textureRun[index]
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            direction.y = -1F
            directionWas.y = -1F

            val index = ((gameTimer / secondsPerFrame) % textureRun.size).toInt()
            texture = textureRun[index]
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            direction.x = -1F
            directionWas.x = -1F

            val index = ((gameTimer / secondsPerFrame) % textureRun.size).toInt()
            texture = textureRun[index].flipped()
        }

        move(deltaTime)

    }

    fun useItem(item: Item) {
        when (item.type) {
            Item.Type.COIN -> {
                val newCoins = (1..3).random()
                coins += newCoins
                item.deactivate()

            }
            Item.Type.COIN_STACK -> {
                coins += (3..5).random()
                item.deactivate()
            }
            Item.Type.DOUBLE_COIN_STACK -> {
                coins += (4..7).random()
                item.deactivate()
            }
            Item.Type.HEAL_POTION -> {
                if (healthPoints < maxHP) {
                    healthPoints += (10..30).random()
                    if (healthPoints > maxHP) healthPoints = maxHP
                    item.deactivate()
                }
            }
            Item.Type.SPEED_POTION -> {
                speed += 10F
                item.deactivate()
            }
            Item.Type.MACHETE, Item.Type.SWORD, Item.Type.ALEBARD -> {
                gameScreen.batch.begin()
                fontC20.draw(gameScreen.batch, "E - take ${item.type.name.toLowerCase()}", position.x - texture.regionWidth / 2, position.y + 60 - texture.regionHeight / 2, 60F, 1, false)
                gameScreen.batch.end()
                if (/*weapon.name != item.type.name && */Gdx.input.isKeyPressed(Input.Keys.E)) {
                    gameScreen.itemsEmitter.generateItem(position.x + 30 * directionWas.x, position.y, Item.Type.valueOf(weapon.name))
                    weapon = Weapon.valueOf(item.type.name)
                    item.deactivate()
                }
            }
        }
    }


    fun renderUI(batch: SpriteBatch, fontC30: BitmapFont) {
        batch.draw(coinTexture, -10F, 460F, 100F, 80F)
        fontC30.draw(batch, coins.toString(), 60F, 517F)
        batch.draw(healTexture, 8F, 430F, 70F, 57F)
        fontC30.draw(batch, healthPoints.toInt().toString(), 60F, 475F)
    }
}
