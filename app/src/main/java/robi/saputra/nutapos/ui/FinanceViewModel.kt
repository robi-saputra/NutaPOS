package robi.saputra.nutapos.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import robi.saputra.nutapos.models.FinanceGroup
import robi.saputra.nutapos.models.FinanceIn
import robi.saputra.nutapos.repository.FinanceRepository
import robi.saputra.nutapos.utils.extractDate
import robi.saputra.nutapos.utils.millisecondsToDateTime

class FinanceViewModel(private val repository: FinanceRepository): ViewModel() {
    private val _groupedTransactions = MutableLiveData<List<FinanceGroup>>()
    val groupedTransactions: LiveData<List<FinanceGroup>> get() = _groupedTransactions

    private val _insertTransactions = MutableLiveData<Boolean>()
    val insertTransactions: LiveData<Boolean> get() = _insertTransactions

    private val _updateTransactions = MutableLiveData<Boolean>()
    val updateTransactions: LiveData<Boolean> get() = _updateTransactions

    private val _deleteTransactions = MutableLiveData<Boolean>()
    val deleteTransactions: LiveData<Boolean> get() = _deleteTransactions

    private val _isDatabaseEmpty = MutableLiveData<Boolean>()
    val isDatabaseEmpty: LiveData<Boolean> get() = _isDatabaseEmpty

    fun isDatabaseEmpty() {
        viewModelScope.launch(Dispatchers.IO) {
            val isEmpty = repository.getAllTransaction().isEmpty()
            withContext(Dispatchers.Main) {
                _isDatabaseEmpty.postValue(isEmpty)
            }
        }
    }

    fun loadTransactions() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.getAllTransaction()
            val groupedData = data
                .groupBy { it.date.millisecondsToDateTime().extractDate() }
                .map { entry ->
                    FinanceGroup(
                        date = entry.key,
                        totalAmount = entry.value.sumOf { it.amount },
                        transactions = entry.value
                    )
                }
            withContext(Dispatchers.Main) {
                _groupedTransactions.postValue(groupedData)
            }
        }
    }

    fun loadRangeTransaction(startDate: String, endDate: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.getRangeTransaction(startDate, endDate)
            val groupedData = data
                .groupBy { it.date.millisecondsToDateTime().extractDate() }
                .map { entry ->
                    FinanceGroup(
                        date = entry.key,
                        totalAmount = entry.value.sumOf { it.amount },
                        transactions = entry.value
                    )
                }
            withContext(Dispatchers.Main) {
                _groupedTransactions.postValue(groupedData)
            }
        }
    }

    fun insertTransaction(financeIn: FinanceIn) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.insertTransaction(financeIn)
            _insertTransactions.postValue(result)
        }
    }

    fun updateTransaction(financeIn: FinanceIn) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.updateTransaction(financeIn)
            _insertTransactions.postValue(result)
        }
    }

    fun deleteTransaction(financeIn: FinanceIn) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.deleteTransaction(financeIn)
            _insertTransactions.postValue(result)
        }
    }

    fun insertDummyData(callback: () -> Unit) {
        val dummyData = listOf(
            FinanceIn(
                id = 0,
                date = 1652341620000,
                amount = 1000,
                insertTo = "Kasir Peringkat 1",
                insertFrom = "Bos",
                desc = "Dummy Transaction",
                type = "Debit",
                image = ""
            ),
            FinanceIn(
                id = 0,
                date = 1652345520000,
                amount = 1000,
                insertTo = "Kasir Peringkat 1",
                insertFrom = "Bos",
                desc = "Dummy Transaction",
                type = "Debit",
                image = ""
            ),
            FinanceIn(
                id = 0,
                date = 1654949520000,
                amount = 1000,
                insertTo = "Kasir Peringkat 1",
                insertFrom = "Bos",
                desc = "Dummy Transaction",
                type = "Debit",
                image = ""
            ),
            FinanceIn(
                id = 0,
                date = 1655013900000,
                amount = 1000,
                insertTo = "Kasir Peringkat 1",
                insertFrom = "Bos",
                desc = "Dummy Transaction",
                type = "Debit",
                image = ""
            )
        )
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dummyData.forEach{
                    repository.insertTransaction(it)
                }
                withContext(Dispatchers.Main) {
                    callback()
                }
            } catch (e: Exception) {
                println( "Error inserting dummy data")
            }
        }
    }
}