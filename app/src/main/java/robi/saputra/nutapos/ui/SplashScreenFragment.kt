package robi.saputra.nutapos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel
import robi.saputra.nutapos.BaseFragment
import robi.saputra.nutapos.databinding.FragmentSplashBinding

class SplashScreenFragment: BaseFragment<FragmentSplashBinding>() {
    private val viewModel by viewModel<FinanceViewModel>()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSplashBinding {
        return FragmentSplashBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}