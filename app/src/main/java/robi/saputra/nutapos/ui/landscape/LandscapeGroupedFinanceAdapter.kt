package robi.saputra.nutapos.ui.landscape

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import robi.saputra.nutapos.databinding.ItemLandscapeParentBinding
import robi.saputra.nutapos.models.FinanceGroup
import robi.saputra.nutapos.models.FinanceIn
import robi.saputra.nutapos.utils.dateTimeToDate
import robi.saputra.nutapos.utils.dateTimeToTime
import robi.saputra.nutapos.utils.formatToRupiah

class LandscapeGroupedFinanceAdapter: RecyclerView.Adapter<LandscapeGroupedFinanceAdapter.ViewHolder>() {
    private var financeList: List<FinanceGroup> = emptyList()

    fun setFinanceData(data: List<FinanceGroup>) {
        financeList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLandscapeParentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(financeList[position])
    }

    override fun getItemCount(): Int = financeList.size

    class ViewHolder(private val binding: ItemLandscapeParentBinding) : RecyclerView.ViewHolder(binding.root) {

        private val subAdapter = LandscapeFinanceAdapter()
        @SuppressLint("SetTextI18n")
        fun bind(group: FinanceGroup) {
            binding.tvDateLabel.text = group.date.dateTimeToDate("dd-MM-yyyy", "dd-MMM-yyyy")
            binding.tvTotalValue.text = group.totalAmount.formatToRupiah()

            // Setup sub RecyclerView
            binding.rvLandscapeUangMasuk.apply {
                adapter = subAdapter
            }
            subAdapter.setTransactionData(group.transactions)
        }
    }
}