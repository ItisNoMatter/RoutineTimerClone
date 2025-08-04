package jp.itIsNoMatter.routineTimerClone.ui.navigation

sealed interface NavEvent {
    data class NavigateTo(val route: Route) : NavEvent

    data object NavigateBack : NavEvent
}
