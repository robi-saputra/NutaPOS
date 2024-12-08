package robi.saputra.nutapos.ui.portrait

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import robi.saputra.nutapos.BaseFragment
import robi.saputra.nutapos.R
import robi.saputra.nutapos.databinding.FragmentDaftarUangMasukPortraitBinding
import robi.saputra.nutapos.models.FinanceIn
import robi.saputra.nutapos.ui.FinanceViewModel
import robi.saputra.nutapos.utils.dateTimeToDate
import robi.saputra.nutapos.utils.decodeBase64ToBitmap
import java.util.Calendar

class FragmentDaftarUangMasuk: BaseFragment<FragmentDaftarUangMasukPortraitBinding>() {
    private val viewModel by viewModel<FinanceViewModel>()
    lateinit var adapter: GroupedFinanceAdapter
    private var startDate: String? = null
    private var formatedStartDate: String = ""
    private var endDate: String? = null
    private var formatedEndDate: String = ""

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDaftarUangMasukPortraitBinding {
        return FragmentDaftarUangMasukPortraitBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = GroupedFinanceAdapter()
        adapter.adapterCallback = object : GroupedFinanceAdapter.AdapterCallBack {
            override fun onEdit(result: FinanceIn) {
                val bundle = Bundle().apply {
                    putParcelable("financeIn", result)
                }
                findNavController().navigate(R.id.fragmentEditUangMasuk, bundle)
            }

            override fun onDelete(result: FinanceIn) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Warning")
                    .setMessage("Yakin akan menghapus data transaksi")
                    .setPositiveButton("OK") { dialog, _ -> viewModel.deleteTransaction(result) }
                    .setIcon(R.drawable.ic_warning)
                    .show()
            }

            override fun onViewImage(result: FinanceIn) {
                showPreviewDialog(requireContext(), result.image)
            }
        }

        binding.rvPortraitContent.adapter = adapter

        viewModel.groupedTransactions.observe(viewLifecycleOwner) { groupedData ->
            adapter.setFinanceData(groupedData)
        }
        viewModel.loadTransactions()

        binding.btnPortraitBuatTransaksi.setOnClickListener {
            findNavController().navigate(R.id.fragmentInsertUangMasuk)
        }
        binding.btnPortraitDateRange.setOnClickListener { showDateRangePicker() }
    }

    private fun showDateRangePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val startDatePicker = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            Log.d(TAG, "$selectedDay $selectedMonth, $selectedYear")
            startDate = "${selectedDay}-${selectedMonth + 1}-$selectedYear"
            Log.d(TAG, startDate.toString())
            showEndDatePicker(selectedYear, selectedMonth, selectedDay)
        }, year, month, day)

        startDatePicker.show()
    }

    private fun showEndDatePicker(startYear: Int, startMonth: Int, startDay: Int) {
        val endDatePicker = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            Log.d(TAG, "$selectedDay $selectedMonth, $selectedYear")
            endDate = "${selectedDay}-${selectedMonth + 1}-$selectedYear"
            Log.d(TAG, endDate.toString())
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
            binding.tvPortraitDateRange.text = "${startDate!!.dateTimeToDate("d-M-yyyy", "dd MMM yyyy")} - ${endDate!!.dateTimeToDate("d-M-yyyy", "dd MMM yyyy")}"
            formatedStartDate = startDate!!.dateTimeToDate("d-M-yyyy", "MM-dd-yyyy")
            formatedEndDate = endDate!!.dateTimeToDate("d-M-yyyy", "MM-dd-yyyy")

            viewModel.loadRangeTransaction(formatedStartDate, formatedEndDate)
        }
    }

    private fun showPreviewDialog(context: Context, base64String: String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_preview_image, null)
        val btnClose = dialogView.findViewById<Button>(R.id.btn_close)
        val imageView = dialogView.findViewById<ImageView>(R.id.view_image)
        val dialog = MaterialAlertDialogBuilder(context)
            .setView(dialogView)
            .setCancelable(true)  // Allow dialog to be dismissed by tapping outside
            .create()
        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        val bitmap = base64String.decodeBase64ToBitmap()
        if (bitmap != null) {
            Glide.with(context)
                .load(bitmap)
                .into(imageView)
        } else {
            Log.e("ImagePreview", "Failed to decode base64 string into Bitmap.")
        }
        dialog.show()
    }
}