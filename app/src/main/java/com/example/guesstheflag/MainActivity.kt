package com.example.guesstheflag

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.example.guesstheflag.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding
    private lateinit var navController: NavController

    val myScope = CoroutineScope(Dispatchers.IO)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        navController= Navigation.findNavController(this,R.id.nav_host_fragment)
    /*    val appBarConfig = AppBarConfiguration(topLevelDestinationIds = setOf(
            R.id.home,
            R.id.list,
            R.id.about
        ))

        setupActionBarWithNavController(navController, appBarConfig)*/
         _binding.bottomNavigationView.setupWithNavController(navController)



        val database : AppDatabase by lazy { Room.databaseBuilder(this, AppDatabase::class.java,"my-database")
            .build()
        }

       /* myScope.launch {
            withContext(Dispatchers.IO){
                 database.userScoreDao().insertUserScore(UserScore(0,"test",0))
            }
        }*/
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

}