package dev.eastar.coroutine.demo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_modify, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        return when (id) {
            R.id.done -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/djrain/coroutine_demo"))).let { true }
            else -> super.onOptionsItemSelected(item)
        }
    }
}