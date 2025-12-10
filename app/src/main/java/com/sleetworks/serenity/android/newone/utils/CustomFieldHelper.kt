package com.sleetworks.serenity.android.newone.utils

import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield.SubListItem

object CustomFieldHelper {
    private fun getSelectionTitleRecursive(
        parent: SubListItem,
        selectedSubList: MutableList<Int?>
    ): String {
        val selectedLabels: MutableList<String?> = java.util.ArrayList<String?>()

        val isParentSelected = selectedSubList.contains(parent.id.toInt())
        val hasChildren = !parent.subList.isEmpty()

        if (hasChildren) {
            if (isParentSelected) {
                return parent.label + " (all)"
            }

            for (child in parent.subList) {
                val childTitle = getSelectionTitleRecursive(child, selectedSubList)
                if (!childTitle.isEmpty()) {
                    selectedLabels.add(childTitle)
                }
            }

            if (!selectedLabels.isEmpty()) {
                return java.lang.String.join(", ", selectedLabels)
            }
        } else {
            if (isParentSelected) {
                selectedLabels.add(parent.label)
            }

            if (!selectedLabels.isEmpty()) {
                return java.lang.String.join(", ", selectedLabels)
            }
        }

        return ""
    }

    fun createSelectedItemsTitle(
        selectedSubList: List<Int?>,
        parentList: List<SubListItem>
    ): String {
        val result = StringBuilder()

        for (parent in parentList) {
            val title: String =
                getSelectionTitleRecursive(
                    parent,
                    selectedSubList as MutableList<Int?>
                )
            if (!title.isEmpty()) {
                result.append(title).append(", ")
            }
        }

        // Remove trailing comma + space
        if (result.isNotEmpty()) {
            result.setLength(result.length - 2)
        }

        return result.toString()
    }

    fun filterSelectedItems(
        selectedItems: List<Int?>,
        fullSubList: List<SubListItem>
    ): MutableList<Int?> {
        val result: MutableList<Int?> = java.util.ArrayList<Int?>()

        for (node in fullSubList) {
            val filtered: MutableList<Int?> =
                filterNode(
                    selectedItems,
                    node
                )
            result.addAll(filtered)
        }

        return result
    }
    private fun filterNode(selectedItems: List<Int?>, node: SubListItem): MutableList<Int?> {
        val children: MutableList<SubListItem>? = node.subList as MutableList<SubListItem>?
        val filteredIds: MutableList<Int?> = ArrayList<Int?>()
        val nodeId = node.id.toInt()

        // If no children, check if this node is selected
        if (children == null || children.isEmpty()) {
            if (selectedItems.contains(nodeId)) {
                filteredIds.add(nodeId)
            }
            return filteredIds
        }

        val selectedChildIds: MutableList<Int?> = ArrayList<Int?>()
        var allChildrenSelected = true

        for (child in children) {
            val childResult = filterNode(selectedItems, child)
            selectedChildIds.addAll(childResult)

            // Check if this child was selected fully
            val childId = child.id.toInt()
            if (!selectedItems.contains(childId) || !childResult.contains(childId)) {
                allChildrenSelected = false
            }
        }

        val isParentSelected = selectedItems.contains(nodeId)

        if (isParentSelected && allChildrenSelected) {
            // All children are selected => return only parent ID
            filteredIds.add(nodeId)
        } else {
            filteredIds.addAll(selectedChildIds)
            // Optionally include parent if selected but not all children
            if (isParentSelected) {
                filteredIds.add(nodeId)
            }
        }

        return filteredIds
    }
}