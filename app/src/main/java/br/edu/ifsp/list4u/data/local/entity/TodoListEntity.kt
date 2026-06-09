import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "todo_list")
data class TodoList(
    @PrimaryKey val id: Int,
    @ColumnInfo(name= "name") val name: String
)