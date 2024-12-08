package robi.saputra.nutapos.repository

import robi.saputra.nutapos.data.FinanceInDao
import robi.saputra.nutapos.models.FinanceIn
import robi.saputra.nutapos.utils.dateTimeToMilliseconds

class FinanceRepositoryImpl(private val dao: FinanceInDao) : FinanceRepository {
    override fun getAllTransaction(): List<FinanceIn> {
        return dao.getListFinanceIn()
    }

    override fun getRangeTransaction(dateFrom: String, dateTo: String): List<FinanceIn> {
        return dao.getRangeOfDate(dateFrom.dateTimeToMilliseconds("MM-dd-yyyy"), dateTo.dateTimeToMilliseconds("MM-dd-yyyy"))
    }

    override fun insertTransaction(financeIn: FinanceIn): Boolean {
        return try {
            dao.insertFinanceIn(financeIn)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun updateTransaction(financeIn: FinanceIn): Boolean {
        return try {
            dao.updateFinanceIn(financeIn)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun deleteTransaction(financeIn: FinanceIn): Boolean {
        return try {
            dao.deleteFinanceIn(financeIn)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}