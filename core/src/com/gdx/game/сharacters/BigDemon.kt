package com.gdx.game.сharacters

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.gdx.game.GameScreen
import com.gdx.game.Weapon

class BigDemon(private val gameScreen: GameScreen) : GameCharacter(gameScreen) {
    private val textureIdle = TextureRegion(Texture("all/demon/demon-idle.png")).split(160, 144).first()
    private val textureAttack = TextureRegion(Texture("all/demon/demon-attack.png")).split(240, 192).first()

    override var position = Vector2(384F, 320F)
    override var race = Race.BIG_DEMON
    override var texture = textureIdle.first()
    override var chance = 9
    override var speed = 60f
    override var maxHP = 100f
    override var healthPoints = maxHP
    override var weapon = Weapon.FIRE
    override val attackSounds = setOf(Gdx.audio.newSound(Gdx.files.internal("sounds/voices/skull-attack1.mp3")),
            Gdx.audio.newSound(Gdx.files.internal("sounds/voices/skull-attack2.mp3")))
    override val deathSounds = setOf(Gdx.audio.newSound(Gdx.files.internal("sounds/voices/skull-death.mp3")))
    override val hurtSounds = setOf(Gdx.audio.newSound(Gdx.files.internal("sounds/voices/zombieYell10.wav")),
            Gdx.audio.newSound(Gdx.files.internal("sounds/voices/zombieYell1.wav")))

    override val eyeSight = 250.0f
    private var timer = 0F

    override fun update(deltaTime: Float) {
        gameTimer += deltaTime
        speed = 60F

        damageEffectTimer -= deltaTime
        if (damageEffectTimer < 0) damageEffectTimer = 0f

        val distance = if (gameScreen.hero.isAlive()) gameScreen.hero.position.dst(position) else Float.MAX_VALUE

        if (distance < eyeSight) {
            speed += 50
            if (distance < weapon.attackRadius) {

                direction.set(0F, 0F)
                attackTimer += deltaTime
                if (attackTimer >= weapon.attackPeriod) {
                    attackTimer = 0f
                    gameScreen.hero.getDamage(weapon.damage, 0..2)
                    if((0..2).random() == 2) attackSounds.random().play(0.3F)
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
                direction.set(gameScreen.hero.position).sub(position).nor();

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