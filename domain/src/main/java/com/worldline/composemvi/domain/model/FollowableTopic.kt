package com.worldline.composemvi.domain.model

/**
 * A [topic] with the additional information for whether or not it is followed.
 */
// TODO consider changing to UserTopic and flattening
data class FollowableTopic(
    val topic: Topic,
    val isFollowed: Boolean,
) {
    companion object {
        fun fake(): FollowableTopic {
            return FollowableTopic(
                topic = Topic.fake(),
                isFollowed = false,
            )
        }
    }
}

fun List<Topic>.mapToFollowableTopic(userData: UserData): List<FollowableTopic> = map { topic ->
    FollowableTopic(
        topic = topic,
        isFollowed = topic.id in userData.followedTopics,
    )
}
