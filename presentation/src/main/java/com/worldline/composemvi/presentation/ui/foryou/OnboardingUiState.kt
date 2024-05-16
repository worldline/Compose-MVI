package com.worldline.composemvi.presentation.ui.foryou

import com.worldline.composemvi.domain.model.FollowableTopic

/**
 * A sealed hierarchy describing the onboarding state for the for you screen.
 */
sealed interface OnboardingUiState {
    /**
     * The onboarding state is loading.
     */
    data object Loading : OnboardingUiState

    /**
     * The onboarding state was unable to load.
     */
    data object LoadFailed : OnboardingUiState

    /**
     * There is no onboarding state.
     */
    data object NotShown : OnboardingUiState

    /**
     * There is a onboarding state, with the given lists of topics.
     */
    data class Shown(
        val topics: List<FollowableTopic>,
    ) : OnboardingUiState {
        /**
         * True if the onboarding can be dismissed.
         */
        val isDismissable: Boolean get() = topics.any { it.isFollowed }
    }
}
