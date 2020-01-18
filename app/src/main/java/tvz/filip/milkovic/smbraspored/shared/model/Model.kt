package tvz.filip.milkovic.smbraspored.shared.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.dbflow5.annotation.Column
import com.dbflow5.annotation.ColumnIgnore
import com.dbflow5.annotation.PrimaryKey
import com.dbflow5.annotation.Table
import kotlinx.android.parcel.Parcelize
import tvz.filip.milkovic.smbraspored.database.AppDatabase

@Keep
@Parcelize
object Model : Parcelable {

    @Keep
    @Parcelize
    @Table(database = AppDatabase::class)
    data class BusLine(
        @PrimaryKey var id: Long = 0,
        @Column var name: String = "",
        @Column var code: String = "",
        @ColumnIgnore var departures: List<Departure>? = null
    ) : Parcelable

    @Keep
    @Parcelize
    @Table(database = AppDatabase::class)
    data class Departure(
        @PrimaryKey var id: Long = 0,
        @Column var departureTime: String = "",
        @Column var typeOfDay: TypeOfDay = TypeOfDay.WEEK_DAY
    ) : Parcelable

}