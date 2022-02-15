package uz.gita.mynotesapplication

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

            window.statusBarColor = ContextCompat.getColor(
                this,
                R.color.colorBackground
            ); //status bar or the time bar at the top
        }
        val navHost = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        val graph = navHost.navController.navInflater.inflate(R.navigation.nav)
        navHost.navController.graph = graph
    }

}