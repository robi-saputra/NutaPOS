package robi.saputra.nutapos.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel
import robi.saputra.nutapos.BaseActivity
import robi.saputra.nutapos.MainActivity
import robi.saputra.nutapos.R
import robi.saputra.nutapos.databinding.ActivitySplashBinding
import robi.saputra.nutapos.utils.arePermissionsGranted
import robi.saputra.nutapos.utils.isCameraPermissionGranted
import robi.saputra.nutapos.utils.isStoragePermissionGranted
import robi.saputra.nutapos.utils.requestCameraPermission
import robi.saputra.nutapos.utils.requestStoragePermission

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    private val viewModel by viewModel<FinanceViewModel>()

    override fun initBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        val intent = Intent(this, MainActivity::class.java)
        viewModel.isDatabaseEmpty.observe(this) { isEmpty ->
            Log.d(TAG, "isEmpty")
            if (isEmpty) {
                viewModel.insertDummyData() {
                    Log.d(TAG, "Dummy")
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(intent)
                        finish()
                    }, 3000)
                }
            } else {
                startActivity(intent)
            }
        }

        viewModel.isDatabaseEmpty()

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
}