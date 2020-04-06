package com.gdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener

class MyGame : ApplicationAdapter() {

    private lateinit var batch: SpriteBatch
    private lateinit var gameScreen: GameScreen
    private lateinit var stage: Stage
    private var started = false

    private lateinit var textFont: BitmapFont
    private var y = 300F

    override fun create() {
        batch = SpriteBatch()
        gameScreen = GameScreen(batch)
        stage = Stage()
        textFont = BitmapFont(Gdx.files.internal("fonts/fontText60.fnt"))

        gameScreen.create()
    }

    override fun render() {
        if (Gdx.input.justTouched()) started = true
        if (!started) {
            Gdx.gl.glClearColor(0.3f, 0.1f, 0.1f, 1f)
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
            if (y < 350 && y >= 300) {
                y += Gdx.graphics.deltaTime * 15
            } else {
                y -= Gdx.graphics.deltaTime * 10
            }
            batch.begin()
            textFont.draw(batch, "CLICK TO START", 200F, y)
            batch.end()
        }
        else {
            stage.clear()
            gameScreen.render()
        }
    }

    override fun dispose() {
        batch.dispose()
    }
}