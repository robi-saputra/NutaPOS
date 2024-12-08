package robi.saputra.nutapos

import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import robi.saputra.nutapos.databinding.ActivityMainBinding
import robi.saputra.nutapos.ui.SplashScreenFragment
import robi.saputra.nutapos.ui.landscape.FragmentLandscapeDaftarUangMasuk
import robi.saputra.nutapos.ui.portrait.FragmentDaftarUangMasuk
import robi.saputra.nutapos.utils.arePermissionsGranted
import robi.saputra.nutapos.utils.isCameraPermissionGranted
import robi.saputra.nutapos.utils.isStoragePermissionGranted
import robi.saputra.nutapos.utils.requestCameraPermission
import robi.saputra.nutapos.utils.requestStoragePermission

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private lateinit var navController : NavController
    lateinit var graph: NavGraph
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val CAMERA_PERMISSION_REQUEST_CODE = 1001
    private val STORAGE_PERMISSION_REQUEST_CODE = 1002

    override fun initBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigation()

        while (!this.arePermissionsGranted()) {
            if (!this.isCameraPermissionGranted()) {
                this.requestCameraPermission()
                break
            }
            if (!this.isStoragePermissionGranted()) {
                this.requestStoragePermission()
                break
            }
            if (this.isCameraPermissionGranted() && this.isStoragePermissionGranted()) {
                break
            }
        }
    }

    fun navigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        graph = inflater.inflate(R.navigation.nav_graph)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            graph.setStartDestination(R.id.fragmentLandscapeDaftarUangMasuk)
        } else {
            graph.setStartDestination(R.id.fragmentDaftarUangMasuk)
        }

        navController = navHostFragment.navController
        navController.setGraph(graph, intent.extras)
    }

    override fun onOrientationChanged(orientation: Int) {
        super.onOrientationChanged(orientation)
        val fragment = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            R.id.fragmentLandscapeDaftarUangMasuk
        } else {
            R.id.fragmentDaftarUangMasuk
        }
        navController.navigate(fragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}