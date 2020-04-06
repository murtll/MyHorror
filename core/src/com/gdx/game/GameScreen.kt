package com.gdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.gdx.game.сharacters.FireSkull
import com.gdx.game.сharacters.GameCharacter
import com.gdx.game.сharacters.Hero
import com.gdx.game.сharacters.Ghost
import java.io.File

class GameScreen(val batch: SpriteBatch) {

    val map = Map()
    val hero = Hero(this)
    val itemsEmitter = ItemsEmitter(this)
    val monstersEmitter = MonstersEmitter(this)
    private val spikeEmitter = SpikeEmitter(this)

    private val textFont = BitmapFont(Gdx.files.internal("fonts/fontText60.fnt"))
    private val fontC30 = BitmapFont(Gdx.files.internal("fonts/fontC30.fnt"))
    private val themeSound = Gdx.audio.newMusic(Gdx.files.internal("sounds/Ruined City Theme.mp3"))
    private var bigMsg = ""
    private var msgTimer = 0F
    private var msgPositionY = 250F
    private var paused = false
    private val stage = Stage()
    private var waveCounter = 0
    private var loadingTimer = 1F
    private var gate = Gate(this, 500F, 500F)
    private var level = 1
    private var levelUpped = false
    private val maps = listOf(File("earth/map-source.txt"),
            File("earth/map-source-2.txt"),
            File("earth/map-source-3.txt"),
            File("earth/map-source-4.txt"),
            File("earth/map-source-5.txt"),
            File("earth/map-source-5.txt"))
    private val gatePlacer = listOf(Vector2(500F, 500F),
            Vector2(430F, 100F),
            Vector2(250F, 400F),
            Vector2(700F, 50F),
            Vector2(140F, 400F),
            Vector2(540F, 400F))

    /**
     * CREATE
     */

    fun create() {
        map.createFromFile(maps[level - 1])
        gate = Gate(this, gatePlacer[level - 1].x, gatePlacer[level - 1].y)

        val skin = Skin().apply { add("pauseButton", Texture("buttons/pause-button.png")) }
        val pauseButton = ImageButton(skin.getDrawable("pauseButton")).apply {
            addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    paused = !paused
                    if (themeSound.isPlaying) {
                        themeSound.pause()
                    } else {
                        themeSound.play()
                    }
                }
            })

            setPosition(370F, 480F)
        }
        stage.addActor(pauseButton)
        Gdx.input.inputProcessor = stage

        spikeEmitter.setup(map)

        (0..1).forEach {
            monstersEmitter.generateMonster((100..700).random().toFloat(), (100..440).random().toFloat(), GameCharacter.Race.GHOST)
            monstersEmitter.generateMonster((200..300).random().toFloat(), (100..440).random().toFloat(), GameCharacter.Race.SKELETON)
        }
        themeSound.run {
            isLooping = true
            volume = 0.1f
            play()
        }
    }

    /**
     * RENDER
     */

    fun render() {
        val deltaTime = Gdx.graphics.deltaTime
        if (loadingTimer <= 0) {
            Gdx.gl.glClearColor(0f, 0.4f, 0.2f, 1f)
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
            batch.begin()
            map.render(batch)
            spikeEmitter.render(batch)
            gate.render(batch)
            try {
                batch.draw(TextureRegion(Texture("earth/gates2.png")).split(64, 64)[0][2], gatePlacer[level - 2].x - 32, gatePlacer[level - 2].y - 32)
            } catch (e: ArrayIndexOutOfBoundsException) {
                batch.draw(TextureRegion(Texture("earth/gates2.png")).split(64, 64)[0][2], 93F, 60F)
            }
            monstersEmitter.render(batch)
            itemsEmitter.render(batch)
            if (hero.isAlive()) {
                hero.render(batch)
                hero.renderUI(batch, fontC30)
            }
            if (msgTimer >= 0) {
                textFont.draw(batch, bigMsg, 0F, msgPositionY, 800F, 1, false)
                msgPositionY += deltaTime * 30
                msgTimer -= deltaTime
            }
            batch.end()
            stage.draw()
            update(deltaTime)
        } else {
            loadingTimer -= deltaTime
            Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
            batch.begin()
            textFont.draw(batch, "LOADING", 0F, 350F, 800F, 1, false)
            batch.end()
        }
    }

    /**
     * UPDATE
     */

    private fun update(deltaTime: Float) {

        if (!paused) {

            itemsEmitter.items.forEach {
                val distance = hero.position.dst(it.position)
                if (it.isActive()) {
                    if (distance < 20) hero.useItem(it)
                }
            }

            if (!hero.isAlive()) {
                bigMsg = "YOU DIED"
                msgTimer = 10f
            }

            /*if (level == 5) {
                bigMsg = "DEMON FIGHT"
                msgTimer = 10f
                monstersEmitter.monsters.forEach {
                    it.deactivate()
                }
            }*/
            if (levelUpped) {
                gate.activate()
            } else if (monstersEmitter.monsters.none { it.isActive() }) {
                if (level != 5) {
                    if (waveCounter < (1..1).random()) {
                        waveCounter++
                        for (i in 0..3) {
                            val race = when ((0..5).random()) {
                                in 0..3 -> GameCharacter.Race.GHOST
                                else -> GameCharacter.Race.FIRE_SKULL
                            }
                            monstersEmitter.generateMonster((100..700).random().toFloat(), (100..440).random().toFloat(), race)
                        }
                    } else {
                        levelUpped = true
                    }
                } else {
                    levelUpped = true
                }
            }

            itemsEmitter.update(deltaTime)
            monstersEmitter.update(deltaTime)
            spikeEmitter.update(deltaTime, map)
            gate.update(deltaTime)
            if (hero.isAlive()) hero.update(deltaTime)
        }
    }

    fun loadNewLevel() {
        level++
        if (level == 6) {
            map.changeTextures(2)
        }
        loadingTimer = 1F
        waveCounter = 0
        levelUpped = false
        bigMsg = "NEXT LEVEL"
        hero.maxHP += 10
        hero.healthPoints += if (hero.healthPoints < hero.maxHP) (10..20).random() else 0
        msgPositionY = 300F
        msgTimer = 3f
        itemsEmitter.items.forEach {
            it.deactivate()
        }
        spikeEmitter.clear()
        map.createFromFile(maps[level - 1])
        spikeEmitter.setup(map)
        gate = Gate(this, gatePlacer[level - 1].x, gatePlacer[level - 1].y)
        hero.position.set(gatePlacer[level - 2].x, gatePlacer[level - 2].y)

        if (level != 5) {
            for (i in 0..level + 5) {
                val race = when ((0..8).random()) {
                    in 0..4 -> GameCharacter.Race.SKELETON
                    in 5..7 -> GameCharacter.Race.GHOST
                    else -> GameCharacter.Race.FIRE_SKULL
                }
                if ((0..8).random() in 0..6) monstersEmitter.generateMonster((100..700).random().toFloat(), (100..440).random().toFloat(), race)
            }
        } else {
            monstersEmitter.generateMonster((100..700).random().toFloat(), (100..440).random().toFloat(), GameCharacter.Race.BIG_DEMON)
        }
    }
}