package robi.saputra.nutapos.repository

import robi.saputra.nutapos.models.FinanceIn

class FinanceUseCase(private val repository: FinanceRepository) {
    fun execute(startDate: String, endDate: String): List<FinanceIn> {
        return repository.getRangeTransaction(startDate, endDate)
    }
}