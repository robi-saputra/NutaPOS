package robi.saputra.nutapos.models

data class FinanceGroup (
    val date: String,
    val totalAmount: Int,
    val transactions: List<FinanceIn>
)