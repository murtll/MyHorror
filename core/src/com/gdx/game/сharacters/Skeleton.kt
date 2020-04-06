package com.gdx.game.сharacters

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.gdx.game.GameScreen
import com.gdx.game.Weapon

class Skeleton(private val gameScreen: GameScreen) : GameCharacter(gameScreen) {
    private val textureIdle = TextureRegion(Texture("all/skeleton-clothed/skeleton-clothed.png")).split(38, 45).first()
    private val textureAttack = TextureRegion(Texture("all/skeleton-clothed/skeleton-clothed.png")).split(38, 45).first()

    override var race = Race.SKELETON
    override var texture = textureIdle.first()
    override var chance = 1
    override var speed = 20f
    override var maxHP = 15f
    override var healthPoints = maxHP
    override var weapon = Weapon.HANDS
    override val attackSounds = setOf(Gdx.audio.newSound(Gdx.files.internal("sounds/voices/zombie10.wav")))
    override val deathSounds = setOf(Gdx.audio.newSound(Gdx.files.internal("sounds/voices/zombieDeath1.wav")),
            Gdx.audio.newSound(Gdx.files.internal("sounds/voices/zombieDeath2.wav")),
            Gdx.audio.newSound(Gdx.files.internal("sounds/voices/zombieDeath4.wav")))
    override val hurtSounds = setOf(Gdx.audio.newSound(Gdx.files.internal("sounds/voices/zombieYell10.wav")),
            Gdx.audio.newSound(Gdx.files.internal("sounds/voices/zombieYell1.wav")))

    override val eyeSight = 70.0f
    private var timer = 0F

    override fun update(deltaTime: Float) {
        gameTimer += deltaTime

        damageEffectTimer -= deltaTime
        if (damageEffectTimer < 0) damageEffectTimer = 0f

        val distance = if (gameScreen.hero.isAlive()) gameScreen.hero.position.dst(position) else Float.MAX_VALUE

        if (distance < eyeSight) {

            if (distance < weapon.attackRadius) {
                direction.set(gameScreen.hero.position).sub(position.x - 10, position.y - 10).nor()
                attackTimer += deltaTime
                if (attackTimer >= weapon.attackPeriod) {
                    attackTimer = 0f
                    gameScreen.hero.getDamage(weapon.damage, 0..2)
                    attackSounds.random().play(2F)
                }

                if (gameScreen.hero.position.x > position.x){
                    val index = ((gameTimer / secondsPerFrame) % textureAttack.size).toInt()
                    texture = textureAttack[index].flipped()
                } else {
                    val index = ((gameTimer / secondsPerFrame) % textureAttack.size).toInt()
                    texture = textureAttack[index]
                }

            } else {
                direction.set(gameScreen.hero.position).sub(Vector2(position).sub(direction)).nor()
                if(direction.x < 0) {
                    val index = ((gameTimer / secondsPerFrame) % textureIdle.size).toInt()
                    texture = textureIdle[index]
                } else {
                    val index = ((gameTimer / secondsPerFrame) % textureIdle.size).toInt()
                    texture = textureIdle[index].flipped()
                }
            }

        } else {
            timer -= deltaTime

            if (timer <= 0) {
                timer = MathUtils.random(5F, 8F)
                direction.set(MathUtils.random(-1F, 1F), MathUtils.random(-1F, 1F)).nor()
            }

            if(direction.x < 0) {
                val index = ((gameTimer / secondsPerFrame) % textureIdle.size).toInt()
                texture = textureIdle[index]
            } else {
                val index = ((gameTimer / secondsPerFrame) % textureIdle.size).toInt()
                texture = textureIdle[index].flipped()
            }

        }

        move(deltaTime)

    }
}