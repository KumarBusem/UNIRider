package com.uni.data.models

data class Settings(
        val latestVersion: String? = null,
        val latestVersionLink: String? = null,
        val underMaintenance: Boolean? = null) {

    companion object {

        const val LATEST_VERSION: String = "latest_version"
        const val LATEST_VERSION_LINK: String = "latestVersionLink"
        const val UNDER_MAINTENANCE: String = "under_maintenance"
        const val BLOCKED_VERSIONS: String = "blocked_versions"
    }
}
