package dev.eastar.coroutine.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import dev.eastar.coroutine.demo.databinding.AppMainBinding

class AppMain : AppCompatActivity() {
    private lateinit var vm: AppMainViewModel
    private lateinit var bb: AppMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProviders.of(this).get(AppMainViewModel::class.java)
        bb = DataBindingUtil.setContentView<AppMainBinding>(this, R.layout.app_main)
            .apply {
                lifecycleOwner = this@AppMain
                viewModel = vm
            }

    }
}