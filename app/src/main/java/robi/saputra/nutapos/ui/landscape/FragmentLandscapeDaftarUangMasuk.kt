package robi.saputra.nutapos.ui.landscape

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import robi.saputra.nutapos.BaseFragment
import robi.saputra.nutapos.R
import robi.saputra.nutapos.databinding.FragmentDaftarUangMasukLandscapeBinding
import robi.saputra.nutapos.ui.FinanceViewModel
import robi.saputra.nutapos.utils.dateTimeToDate
import java.util.Calendar

class FragmentLandscapeDaftarUangMasuk: BaseFragment<FragmentDaftarUangMasukLandscapeBinding>() {
    private val viewModel by viewModel<FinanceViewModel>()
    lateinit var adapter: LandscapeGroupedFinanceAdapter
    private var startDate: String? = null
    private var formatedStartDate: String = ""
    private var endDate: String? = null
    private var formatedEndDate: String = ""

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDaftarUangMasukLandscapeBinding {
        return FragmentDaftarUangMasukLandscapeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        adapter = LandscapeGroupedFinanceAdapter()
        binding.rvLandscapeContent.adapter = adapter

        binding.btnLandscapeBuatTransaksi.setOnClickListener {
            findNavController().navigate(R.id.fragmentInsertUangMasuk)
        }
        binding.btnLandscapeDateRange.setOnClickListener { showDateRangePicker() }

        viewModel.loadTransactions()
    }

    private fun initViewModel() {
        viewModel.groupedTransactions.observe(viewLifecycleOwner) { groupedData ->
            adapter.setFinanceData(groupedData)
        }
        viewModel.deleteTransactions.observe(viewLifecycleOwner) {
            if (!formatedStartDate.isNullOrEmpty() && !formatedEndDate.isNullOrEmpty()) {
                viewModel.loadRangeTransaction(formatedStartDate, formatedEndDate)
            } else {
                viewModel.loadTransactions()
            }
        }
    }

    private fun showDateRangePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val startDatePicker = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            startDate = "${selectedDay}-${selectedMonth + 1}-$selectedYear"
            showEndDatePicker(selectedYear, selectedMonth, selectedDay)
        }, year, month, day)

        startDatePicker.show()
    }

    private fun showEndDatePicker(startYear: Int, startMonth: Int, startDay: Int) {
        val endDatePicker = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            endDate = "${selectedDay}-${selectedMonth + 1}-$selectedYear"
            updateDateRangeText()
        }, startYear, startMonth, startDay)
        val calendar = Calendar.getInstance()
        calendar.set(startYear, startMonth, startDay)
        endDatePicker.datePicker.minDate = calendar.timeInMillis

        endDatePicker.show()
    }

    @SuppressLint("SetTextI18n")
    private fun updateDateRangeText() {
        if (startDate != null && endDate != null) {
            binding.tvLandscapeDateRange.text = "${startDate!!.dateTimeToDate("M-dd-yyyy", "dd MMM yyyy")} - ${endDate!!.dateTimeToDate("M-dd-yyyy", "dd MMM yyyy")}"
            formatedStartDate = startDate!!.dateTimeToDate("d-M-yyyy", "MM-dd-yyyy")
            formatedEndDate = endDate!!.dateTimeToDate("d-M-yyyy", "MM-dd-yyyy")

            viewModel.loadRangeTransaction(formatedStartDate, formatedEndDate)
        }
    }
}