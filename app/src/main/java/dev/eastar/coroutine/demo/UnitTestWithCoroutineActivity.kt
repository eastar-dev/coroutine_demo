package dev.eastar.coroutine.demo

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders

class UnitTestWithCoroutineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(Button(this).apply {
            setOnClickListener { load() }
            text = "unit test with coroutines"
        })
    }


    fun load() {
        val vm = ViewModelProviders.of(this).get(UnitTestWithCoroutineViewModel::class.java)
    }

}

