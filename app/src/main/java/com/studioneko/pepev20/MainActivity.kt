package com.studioneko.pepev20

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.GridLayout
import android.widget.ImageView
import androidx.core.view.children
import com.studioneko.pepev20.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var imageIds = arrayOf<Int>(R.mipmap.anime, R.mipmap.ghibli1, R.mipmap.howl, R.mipmap.kiki, R.mipmap.mononoke, R.mipmap.ponyo, R.mipmap.sootball, R.mipmap.sophie, R.mipmap.spiritedaway2, R.mipmap.totoro, R.mipmap.totoro2, R.mipmap.wind)
    private var rndCheck = arrayOf<Int>(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var imageSrc = arrayOf<Int>(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var score = 0

    private var firstImageId : Int = -1
    private var secondImageId : Int = -1
    private lateinit var firstImageView : ImageView
    private lateinit var secondImageView : ImageView
    private var isChecking = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        resetBtn.setOnClickListener(){Reset()}

        for (i in 0..23){
            setItemImage(i)
        }

        board.children.forEachIndexed{ index, view ->
            view as ImageView
            view.setOnClickListener {
                revealImage(view)
            }
        }

    }

    private fun Reset(){
        firstImageId = -1
        secondImageId = -1
        score = 0
        scoreText.text = ("$score/12")
        for(i in 0..11){
            rndCheck[i] = 0
        }

        for(i in 0..23){
            imageSrc[i] = 0
        }

        board.removeAllViews()

        for (i in 0..23){
            setItemImage(i)
        }

        board.children.forEachIndexed{ index, view ->
            view as ImageView
            view.setOnClickListener {
                revealImage(view)
            }
        }
    }

    private fun GameLoop(){
        if(firstImageId != -1 && secondImageId != -1){
            if(imageSrc[firstImageId] != imageSrc[secondImageId]){
                unrevealImage()
            } else {
                firstImageId = -1
                secondImageId = -1
                firstImageView.setOnClickListener {  }
                secondImageView.setOnClickListener {  }
                isChecking = false
                score++
                scoreText.text = ("$score/12")
            }
        }
    }

    private fun unrevealImage(){
        Handler(Looper.getMainLooper()).postDelayed({
            firstImageView.setImageResource(R.mipmap.iconghibli)
            secondImageView.setImageResource(R.mipmap.iconghibli)
            firstImageId = -1
            secondImageId = -1
            isChecking = false
        }, 500)
    }

    private fun revealImage(item : ImageView){
        if(!isChecking) {
            val a = imageSrc[item.id]
            item.setImageResource(imageIds[a])
            if (firstImageId == -1) {
                firstImageId = item.id
                firstImageView = item
            } else if (firstImageId != item.id) {
                secondImageId = item.id
                secondImageView = item
                isChecking = true
                GameLoop()
            }
        }
    }

    private fun setItemImage(_index: Int){
        var done = true
        for(i in rndCheck){
            if(done)
            done = i == 2
        }

        if(!done) {
            val rnd = Random.nextInt(0, 12)

            if (rndCheck[rnd] != 2) {
                rndCheck[rnd]++

                val item = ImageView(this, null)
                val params = GridLayout.LayoutParams();
                params.width = GridLayout.LayoutParams.WRAP_CONTENT
                params.height = GridLayout.LayoutParams.WRAP_CONTENT
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                params.setMargins(10,10,10,10)
                item.layoutParams = params
                item.id = _index
                item.setImageResource(R.mipmap.iconghibli)

                board.addView(item)

                imageSrc[_index] = rnd
            }
            else
                setItemImage(_index)
        }
    }
}