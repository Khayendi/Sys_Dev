package com.code.tusome.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.code.tusome.R
import com.code.tusome.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_container) as NavHostFragment
        val navController = navHostFragment.navController
        Handler().postDelayed({
            if (FirebaseAuth.getInstance().uid != null) {
                /**
                 * This is an example of internal service using navigation components - Explicit intent
                 */
                navController.navigate(R.id.action_splashFragment_to_homeActivity)
                finish()
            } else {
                /**
                 * This is an example of internal service using navigation components - Explicit intent
                 */
                navController.navigate(R.id.action_splashFragment_to_authFragment)
            }
        }, 2000)

    }
}