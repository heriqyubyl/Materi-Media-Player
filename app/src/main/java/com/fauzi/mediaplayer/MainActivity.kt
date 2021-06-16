package com.fauzi.mediaplayer

import android.annotation.SuppressLint
import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.SeekBar
import androidx.core.graphics.toColor
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPutar : MediaPlayer
    private var waktuTotal: Int = 0

    @Suppress("RedundantSamConstructor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mediaPutar = MediaPlayer.create(this, R.raw.play2)
        mediaPutar.isLooping = true
        mediaPutar.setVolume(0.5f, 0.5f)
        waktuTotal = mediaPutar.duration

        suara_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser){
                    val jumlahVolume = progress / 100.0f
                    mediaPutar.setVolume(jumlahVolume, jumlahVolume)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        position_seekbar_music.max = waktuTotal
        position_seekbar_music.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser){
                    mediaPutar.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        Thread(Runnable {
            while (mediaPutar != null){
                try {
                    val pesan = Message()
                    pesan.what = mediaPutar.currentPosition
                    penanganan.sendMessage(pesan)
                    Thread.sleep(1000)
                }catch (e: InterruptedException){
                }
            }
        }).start()
    }

    private var penanganan = @SuppressLint("HandlerLeak")
    object : Handler() {
        @SuppressLint("SetTextI18n")
        override fun handleMessage(pesan: Message) {
            super.handleMessage(pesan)

            val posisiSaatIni = pesan.what
            position_seekbar_music.progress = posisiSaatIni

            val perkiraanWaktu =  buatLabelWaktu(posisiSaatIni)
            label_perkiraan_waktu.text = perkiraanWaktu

            val pengingatWaktu = buatLabelWaktu(waktuTotal - posisiSaatIni)
            label_pengingat_waktu.text = "-$pengingatWaktu"
        }
    }

    private fun buatLabelWaktu(waktu: Int): String {

        var labelWaktu = ""
        val menit = waktu / 1000 / 60
        val detik = waktu / 1000 % 60

        labelWaktu = "$menit: "
        if (detik < 10 ) labelWaktu += "0"
        labelWaktu += detik

        return labelWaktu
    }

    fun playBtnClick(view: View) {
        if (mediaPutar.isPlaying) {
            mediaPutar.pause()
            btn_play_pause.setIconResource(R.drawable.ic_baseline_play_arrow_24)
        } else {
            mediaPutar.start()
//            btn_play_pause.setBackgroundResource(R.drawable.ic_baseline_pause_24)
            btn_play_pause.setIconResource(R.drawable.ic_baseline_pause_24)

        }
    }
}