package com.gdx.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

enum class Weapon (val attackRadius: Float,
                   val attackPeriod: Float,
                   val damage: Float,
                   val textureAttack: Array<TextureRegion> = TextureRegion(Texture("all/hero/gothic-hero-attack.png")).split(96, 48).first(),
                   val textureRun: Array<TextureRegion> = TextureRegion(Texture("all/hero/gothic-hero-run.png")).split(66, 48).first(),
                   val textureIdle: Array<TextureRegion> = TextureRegion(Texture("all/hero/gothic-hero-idle.png")).split(38, 48).first()) {
    FIRE(50f, 0.5f, 2.5f),
    HANDS(40f, 1f, 5f),
    MAGIC(70f, 0.5f, 1.5f),
    SWORD(50F, 0.8F, 11F),
    MACHETE(50F, 1F, 15F,
            textureAttack = TextureRegion(Texture("all/hero/gothic-hero-attack-machete.png")).split(96, 48).first(),
            textureIdle = TextureRegion(Texture("all/hero/gothic-hero-idle-machete.png")).split(38, 48).first(),
            textureRun = TextureRegion(Texture("all/hero/gothic-hero-run-machete.png")).split(66, 48).first()),
    ALEBARD(70F, 1.5F, 25F,
            textureAttack = TextureRegion(Texture("all/hero/gothic-hero-attack-alebard.png")).split(108, 48).first(),
            textureIdle = TextureRegion(Texture("all/hero/gothic-hero-idle-alebard.png")).split(54, 48).first(),
            textureRun = TextureRegion(Texture("all/hero/gothic-hero-run-alebard.png")).split(66, 48).first())
}