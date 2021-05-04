package com.example.myapplication

import android.content.res.Resources
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.text.method.ScrollingMovementMethod
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader


var play_btn: ImageButton? = null
var previous_btn: ImageButton? = null
var next_btn: ImageButton? = null
var seekBar: SeekBar? = null
var musicTitle: TextView? = null
var artist: TextView? = null
var image: ImageView? = null
var musicText: TextView? = null
var musics = mutableListOf<Music>(
    Music("Its Ok", R.drawable.its_ok, R.raw.its_ok, "Tom Rosenthal", "its_ok_text"),
    Music("First Music", R.drawable.cover, R.raw.music, "Default Artist", "default_music")
)

class MainActivity : AppCompatActivity() {
    lateinit var runnable: Runnable
    private var handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        play_btn = findViewById(R.id.play_btn)
        next_btn = findViewById(R.id.next_btn)
        previous_btn = findViewById(R.id.previous_btn)
        musicTitle = findViewById(R.id.music_title)
        artist = findViewById(R.id.artist)
        image = findViewById(R.id.music_image)
        musicText = findViewById(R.id.music_text)
        musicText!!.movementMethod = ScrollingMovementMethod()
        var mediaPlayer = MediaPlayer.create(this, musics[0].song)
        var index = 0
        image!!.setImageResource(musics[0].image)
        musicTitle!!.text = musics[0].musicName
        artist!!.text = musics[0].artist
        seekBar = findViewById(R.id.seekbar)
        seekBar!!.progress = 0
        seekBar!!.max = mediaPlayer.duration
        musicText!!.text = readTextFile(musics[0])
        next_btn?.setOnClickListener{
            if(index < musics.size -1) {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                    mediaPlayer.release()
                    play_btn!!.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                }
                index += 1
                mediaPlayer = MediaPlayer.create(this, musics[index].song)
                seekBar!!.progress = 0
                seekBar!!.max = mediaPlayer.duration
                image!!.setImageResource(musics[index].image)
                musicTitle!!.text = musics[index].musicName
                artist!!.text = musics[index].artist
                musicText!!.text = readTextFile(musics[index])
            }
        }

        previous_btn?.setOnClickListener{
            if(index>0){
                if(mediaPlayer.isPlaying){
                    mediaPlayer.stop()
                    mediaPlayer.release()
                    play_btn!!.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                }
                index-=1
                mediaPlayer = MediaPlayer.create(this, musics[index].song)
                seekBar!!.progress = 0
                seekBar!!.max = mediaPlayer.duration
                image!!.setImageResource(musics[index].image)
                musicTitle!!.text = musics[index].musicName
                artist!!.text = musics[index].artist
                musicText!!.text = readTextFile(musics[index])
            }
        }

        play_btn?.setOnClickListener{
            if(!mediaPlayer.isPlaying){
                mediaPlayer.start()
                play_btn!!.setImageResource(R.drawable.ic_baseline_pause_24)
            } else {
                mediaPlayer.pause()
                play_btn!!.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            }
        }

        seekBar?.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, pos: Int, changed: Boolean) {
                if (changed) {
                   mediaPlayer.seekTo(pos)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        runnable = Runnable {
            seekBar!!.progress = mediaPlayer.currentPosition
            handler.postDelayed(runnable,1000)
        }

        handler.postDelayed(runnable,1000)

        mediaPlayer.setOnCompletionListener {
            play_btn!!.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            if(index< musics.size-1){
                next_btn!!.performClick()
                next_btn!!.isPressed = true
                next_btn!!.invalidate()
                next_btn!!.isPressed = false
                next_btn!!.invalidate()
            } else {
                previous_btn!!.performClick()
                previous_btn!!.isPressed = true
                previous_btn!!.invalidate()
                previous_btn!!.isPressed = false
                previous_btn!!.invalidate()
            }
            seekBar!!.progress = 0
        }
    }

    fun readTextFile (music: Music):String {
        var string: String? = ""
        val stringBuilder = StringBuilder()
        val res: Resources = resources
        val text: Int = res.getIdentifier(music.txtFile, "raw", packageName)
        val `is`: InputStream = this.resources.openRawResource(text)
        val reader = BufferedReader(InputStreamReader(`is`))
        while (true) {
            try {
                if (reader.readLine().also { string = it } == null) break
            } catch (e: IOException) {
                e.printStackTrace()
            }
            stringBuilder.append(string).append("\n")
        }
        `is`.close()
        println(stringBuilder.toString())
        return stringBuilder.toString()
    }
}