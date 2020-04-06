package com.gdx.game.сharacters

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.gdx.game.GameScreen
import com.gdx.game.Weapon

class Ghost(private val gameScreen: GameScreen) : GameCharacter(gameScreen) {
    private val textureIdle = TextureRegion(Texture("all/ghost/ghost-idle.png")).split(64, 80).first()
    private val textureAttack = TextureRegion(Texture("all/ghost/ghost-shriek.png")).split(64, 80).first()

    override var race = Race.GHOST
    override var texture = textureIdle.first()
    override var chance = 2
    override var speed = 50f
    override var maxHP = 25f
    override var healthPoints = maxHP
    override var weapon = Weapon.MAGIC
    override val attackSounds = setOf(Gdx.audio.newSound(Gdx.files.internal("sounds/voices/ghost-attack.mp3")))
    override val deathSounds = setOf(Gdx.audio.newSound(Gdx.files.internal("sounds/voices/zombieDeath1.wav")),
            Gdx.audio.newSound(Gdx.files.internal("sounds/voices/zombieDeath2.wav")),
            Gdx.audio.newSound(Gdx.files.internal("sounds/voices/zombieDeath4.wav")))
    override val hurtSounds = setOf(Gdx.audio.newSound(Gdx.files.internal("sounds/voices/zombieYell10.wav")),
            Gdx.audio.newSound(Gdx.files.internal("sounds/voices/zombieYell1.wav")))

    override val eyeSight = 200.0f
    private var timer = 0F

    override fun update(deltaTime: Float) {

        gameTimer += deltaTime

        damageEffectTimer -= deltaTime
        if (damageEffectTimer < 0) damageEffectTimer = 0f

        val distance = if (gameScreen.hero.isAlive()) gameScreen.hero.position.dst(position) else Float.MAX_VALUE

        if (distance < eyeSight) {
            if (distance < weapon.attackRadius) {
                    if (gameScreen.hero.speed >= 90F )gameScreen.hero.speed -= 40
                    direction.set(0F, 0F)

                attackTimer += deltaTime
                if (attackTimer >= weapon.attackPeriod) {
                    attackTimer = 0f
                    gameScreen.hero.getDamage(weapon.damage, 0..2)
                    if((0..2).random() == 2) attackSounds.random().play(0.2F)
//                    if ((0..2).random() == 2) gameScreen.hero.hurtSounds.random().play(1F)
                }

                if (gameScreen.hero.position.x > position.x){
                    val index = ((gameTimer / secondsPerFrame) % textureAttack.size).toInt()
                    texture = textureAttack[index].flipped()
                } else {
                    val index = ((gameTimer / secondsPerFrame) % textureAttack.size).toInt()
                    texture = textureAttack[index]
                }

            } else {

                if (gameScreen.hero.speed <= 90F ) gameScreen.hero.speed += 40

                direction.set(gameScreen.hero.position).sub(position).nor()

                if (direction.x > 0){
                    val index = ((gameTimer / secondsPerFrame) % textureIdle.size).toInt()
                    texture = textureIdle[index].flipped()
                } else {
                    val index = ((gameTimer / secondsPerFrame) % textureIdle.size).toInt()
                    texture = textureIdle[index]
                }
            }

        } else {
            timer -= deltaTime

            if (timer <= 0) {
                timer = MathUtils.random(3F, 6F)
                direction.set(MathUtils.random(-1F, 1F), MathUtils.random(-1F, 1F)).nor()
            }

            if (direction.x > 0){
                val index = ((gameTimer / secondsPerFrame) % textureIdle.size).toInt()
                texture = textureIdle[index].flipped()
            } else {
                val index = ((gameTimer / secondsPerFrame) % textureIdle.size).toInt()
                texture = textureIdle[index]
            }

        }

        move(deltaTime)

    }

    override fun deactivate() {
        super.deactivate()

        if (gameScreen.hero.speed <= 90F ) gameScreen.hero.speed += 40
    }
}