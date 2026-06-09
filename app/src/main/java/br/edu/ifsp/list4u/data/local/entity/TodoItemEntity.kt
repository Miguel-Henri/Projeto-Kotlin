import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "todo_item",)
data class TodoItem(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean?,
    @ColumnInfo(name = "list_id") val listId: Int
)