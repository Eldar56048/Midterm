package com.example.myapplication

class Music {
    var musicName: String  = "Music Title"
    var artist: String = "Artist"
    var song = R.raw.music
    var image = R.drawable.cover
    var txtFile = " "

    constructor(musicName:String, image: Int, song: Int, artist: String, txtFile: String ){
        this.musicName = musicName
        this.artist = artist
        this.image = image
        this.song = song
        this.txtFile = txtFile
    }

}