package com.yourdomain.todomanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.yourdomain.todomanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.todoListFragment, R.id.loginFragment)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Show/hide FAB based on current destination
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.fab.visibility = when (destination.id) {
                R.id.todoListFragment -> android.view.View.VISIBLE
                else -> android.view.View.GONE
            }
        }

        // Set up FAB click listener
        binding.fab.setOnClickListener {
            navController.navigate(R.id.action_todoListFragment_to_addTodoFragment)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
} 