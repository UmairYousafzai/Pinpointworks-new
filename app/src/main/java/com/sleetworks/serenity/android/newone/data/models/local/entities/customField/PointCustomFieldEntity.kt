package com.sleetworks.serenity.android.newone.data.models.local.entities.customField

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield.SubListOfTotal

@Entity(
    tableName = "point_custom_fields",
    foreignKeys = [ForeignKey(
        entity = PointEntity::class,
        parentColumns = ["id"],
        childColumns = ["point_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["custom_field_template_id", "point_id"])]
)
data class PointCustomFieldEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val localID: Int,
    @ColumnInfo(name = "point_id")
    val pointID: String,
    val value: String,
    @ColumnInfo(name = "custom_field_template_id")
    val customFieldTemplateId: String,
    val type: String,
    val label: String,
    val currency: String? = null,
    @ColumnInfo(name = "currency_code")
    val currencyCode: String? = null,
    @ColumnInfo(name = "currency_symbol")
    val currencySymbol: String? = null,
    val unit: String? = null,
    @ColumnInfo(name = "decimal_places")
    val decimalPlaces: Int? = null,
    @ColumnInfo(name = "show_commas")
    val showCommas: Boolean? = null,
    @ColumnInfo(name = "show_hours_only")
    val showHoursOnly: Boolean? = null,
    @ColumnInfo(name = "id_of_chosen_element")
    val idOfChosenElement: String? = null,
    @ColumnInfo(name = "selected_item_ids")
    val selectedItemIds: List<String>? = null,
    @ColumnInfo(name = "sub_values")
    val subValues: List<SubListOfTotal>? = null

)