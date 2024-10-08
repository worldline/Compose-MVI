package com.worldline.composemvi.presentation.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import com.worldline.composemvi.presentation.ui.theme.ComposeMVITheme
import com.worldline.composemvi.presentation.utils.LocalLoading
import com.worldline.composemvi.presentation.utils.fadePlaceholder

@Composable
fun SingleTopicButton(
    name: String,
    topicId: String,
    imageUrl: String,
    isSelected: Boolean,
    onClick: (String, Boolean) -> Unit,
) = trace("SingleTopicButton") {
    val loading = LocalLoading.current

    Surface(
        modifier = Modifier
            .width(312.dp)
            .heightIn(min = 56.dp),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        color = MaterialTheme.colorScheme.surface,
        selected = isSelected,
        onClick = {
            onClick(topicId, !isSelected)
        },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 12.dp, end = 8.dp),
        ) {
            TopicIcon(
                imageUrl = imageUrl,
            )
            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .weight(1f)
                    .fadePlaceholder(LocalLoading.current),
                color = MaterialTheme.colorScheme.onSurface,
            )
            NiaIconToggleButton(
                checked = isSelected,
                onCheckedChange = { checked ->
                    if (!loading) {
                        onClick(topicId, checked)
                    }
                },
                icon = {
                    Icon(
                        imageVector = NiaIcons.Add,
                        contentDescription = name,
                    )
                },
                checkedIcon = {
                    Icon(
                        imageVector = NiaIcons.Check,
                        contentDescription = name,
                    )
                },
            )
        }
    }
}

@Preview
@Composable
fun SingleTopicButtonPreview() {
    ComposeMVITheme {
        SingleTopicButton(
            name = "Topic",
            topicId = "topicId",
            imageUrl = "https://example.com/image.jpg",
            isSelected = false,
            onClick = { _, _ -> },
        )
    }
}
