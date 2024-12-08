package robi.saputra.nutapos.ui.portrait

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import robi.saputra.nutapos.databinding.ItemPortraitParentBinding
import robi.saputra.nutapos.models.FinanceGroup
import robi.saputra.nutapos.models.FinanceIn
import robi.saputra.nutapos.utils.dateTimeToDate
import robi.saputra.nutapos.utils.formatToRupiah
import java.util.Objects

class GroupedFinanceAdapter: RecyclerView.Adapter<GroupedFinanceAdapter.ViewHolder>() {
    private var financeList: List<FinanceGroup> = emptyList()
    lateinit var adapterCallback: AdapterCallBack

    interface AdapterCallBack {
        fun onEdit(result: FinanceIn)
        fun onDelete(result: FinanceIn)
        fun onViewImage(result: FinanceIn)
    }

    fun setFinanceData(data: List<FinanceGroup>) {
        financeList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPortraitParentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(financeList[position])
        holder.subAdapter.adapterCallback = object : FinanceAdapter.AdapterCallBack {
            override fun onEdit(result: FinanceIn) {
                adapterCallback.onEdit(result)
            }

            override fun onDelete(result: FinanceIn) {
                adapterCallback.onDelete(result)
            }

            override fun onViewImage(result: FinanceIn) {
                adapterCallback.onViewImage(result)
            }
        }
    }

    override fun getItemCount(): Int = financeList.size

    class ViewHolder(private val binding: ItemPortraitParentBinding) : RecyclerView.ViewHolder(binding.root) {
        val subAdapter = FinanceAdapter()

        @SuppressLint("SetTextI18n")
        fun bind(group: FinanceGroup) {
            binding.tvDate.text = group.date.dateTimeToDate("dd-MM-yyyy", "dd-MMM-yyyy")
            binding.tvTotalMasuk.text = group.totalAmount.formatToRupiah()

            // Setup sub RecyclerView
            binding.rvPortraitUangMasuk.apply {
                adapter = subAdapter
            }
            subAdapter.setTransactionData(group.transactions)
        }
    }
}