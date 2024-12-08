package robi.saputra.nutapos.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "finance_in")
data class FinanceIn(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Int,
    @ColumnInfo("date")
    var date: Long,
    @ColumnInfo("amount")
    val amount: Int,
    @ColumnInfo("insertTo")
    val insertTo: String,
    @ColumnInfo("insertFrom")
    val insertFrom: String,
    @ColumnInfo("desc")
    val desc: String,
    @ColumnInfo("type")
    val type: String,
    @ColumnInfo("image")
    val image: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readLong(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
    ){}

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeLong(date)
        parcel.writeInt(amount)
        parcel.writeString(insertTo)
        parcel.writeString(insertFrom)
        parcel.writeString(desc)
        parcel.writeString(type)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FinanceIn> {
        override fun createFromParcel(parcel: Parcel): FinanceIn {
            return FinanceIn(parcel)
        }

        override fun newArray(size: Int): Array<FinanceIn?> {
            return arrayOfNulls(size)
        }
    }
}